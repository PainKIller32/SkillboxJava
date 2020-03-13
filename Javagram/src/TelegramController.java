import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TelegramController {
    private boolean quit = false;
    private int deletedContactId;
    private Action onWaitPhoneNumber;
    private Action onWaitRegistration;
    private Action onExit;
    private Consumer<Integer> onContactDeleted;
    private Consumer<String> onError;
    private Consumer<ConcurrentMap<Integer, TdApi.User>> onUsersUpdate;
    private Action onContactAdded;
    private ThreeConsumer onLastMessageUpdate;
    private Consumer<String> onWaitCode;
    private Consumer<TdApi.User> onChatRequestReturned;
    private Action onAuthorizationStateReady;
    private BiConsumer<Integer, TdApi.Message> onNewMessage;
    private BiConsumer<Integer, Boolean> onUserStatusUpdate;
    private BiConsumer<Integer, TdApi.Message> onMessageRequestReturned;

    private Client client;
    private ConcurrentMap<Long, Integer> chatList;
    private ConcurrentMap<Integer, TdApi.User> users;
    private TdApi.AuthorizationState authorizationState;

    static {
        System.loadLibrary("zlib1");
        System.loadLibrary("tdjni");
    }

    public TelegramController() {
        client = Client.create(new UpdatesHandler(), null, null);
        users = new ConcurrentHashMap<>();
        chatList = new ConcurrentHashMap<>();
    }

    private void onAuthorizationStateUpdate(TdApi.AuthorizationState authorizationState) {
        if (authorizationState != null) {
            this.authorizationState = authorizationState;
        }
        switch (this.authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                TdApi.TdlibParameters parameters = new TdApi.TdlibParameters();
                parameters.useTestDc = true;
                parameters.apiId = 615381;
                parameters.apiHash = "1299ea11aa7f63c004936e40856ca6cb";
                parameters.useSecretChats = false;
                parameters.useMessageDatabase = false;
                parameters.databaseDirectory = "database";
                parameters.systemLanguageCode = "ru";
                parameters.deviceModel = "Desktop";
                parameters.systemVersion = "Unknown";
                parameters.applicationVersion = "1.0";
                parameters.enableStorageOptimizer = true;
                client.send(new TdApi.SetTdlibParameters(parameters), new AuthorizationRequestHandler());
            }
            case TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> client.send(new TdApi.CheckDatabaseEncryptionKey(), new AuthorizationRequestHandler());
            case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> onWaitPhoneNumber.accept();
            case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> onWaitCode.accept(((TdApi.AuthorizationStateWaitCode) this.authorizationState).codeInfo.phoneNumber);
            case TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR -> onWaitRegistration.accept();
            case TdApi.AuthorizationStateReady.CONSTRUCTOR -> {
                client.send(new TdApi.GetContacts(), null);
                client.send(new TdApi.GetChats(new TdApi.ChatListMain(), Long.MAX_VALUE, Long.MAX_VALUE, 10000), null);
                onAuthorizationStateReady.accept();
            }
            case TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR -> users = new ConcurrentHashMap<>();
            case TdApi.AuthorizationStateClosed.CONSTRUCTOR -> {
                if (!quit) {
                    client = Client.create(new UpdatesHandler(), null, null);
                } else {
                    onExit.accept();
                }
            }
        }
    }

    private class AuthorizationRequestHandler implements Client.ResultHandler {
        private AuthorizationRequestHandler() {
        }

        @Override
        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()) {
                case TdApi.Error.CONSTRUCTOR -> onError.accept("Ошибка!");
                case TdApi.Ok.CONSTRUCTOR -> {
                }
            }
        }
    }

    private class RequestHandler implements Client.ResultHandler {
        private RequestHandler() {
        }

        @Override
        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()) {
                case TdApi.Ok.CONSTRUCTOR -> onContactDeleted.accept(deletedContactId);
                case TdApi.ImportedContacts.CONSTRUCTOR -> {
                    TdApi.ImportedContacts importedContacts = (TdApi.ImportedContacts) object;
                    if (importedContacts.userIds[0] > 0) {
                        onContactAdded.accept();
                    } else {
                        onError.accept("Пользователь не найден!");
                    }
                }
                case TdApi.User.CONSTRUCTOR -> {
                    TdApi.User user = (TdApi.User) object;
                    users.put(user.id, user);
                }
                case TdApi.Users.CONSTRUCTOR -> {
                    TdApi.Users users = (TdApi.Users) object;
                    if (users.totalCount > 0) {
                        int contactId = users.userIds[0];
                        client.send(new TdApi.CreatePrivateChat(contactId, false), null);
                    }
                }
                case TdApi.Messages.CONSTRUCTOR -> {
                    TdApi.Messages messages = (TdApi.Messages) object;
                    if (messages.totalCount > 0) {
                        for (TdApi.Message message : messages.messages) {
                            onMessageRequestReturned.accept(chatList.get(message.chatId), message);
                        }
                    }
                }
                case TdApi.Message.CONSTRUCTOR -> {
                    TdApi.Message message = (TdApi.Message) object;
                    onNewMessage.accept(chatList.get(message.chatId), message);
                }
                case TdApi.Error.CONSTRUCTOR -> onError.accept("Ошибка!");
            }
        }
    }

    private class UpdatesHandler implements Client.ResultHandler {
        private UpdatesHandler() {
        }

        @Override
        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()) {
                case TdApi.UpdateAuthorizationState.CONSTRUCTOR -> onAuthorizationStateUpdate(((TdApi.UpdateAuthorizationState) object).authorizationState);
                case TdApi.UpdateUser.CONSTRUCTOR -> {
                    TdApi.UpdateUser updateUser = (TdApi.UpdateUser) object;
                    users.put(updateUser.user.id, updateUser.user);
                    onUsersUpdate.accept(users);
                }
                case TdApi.UpdateUserStatus.CONSTRUCTOR -> {
                    TdApi.UpdateUserStatus updateUserStatus = (TdApi.UpdateUserStatus) object;
                    int userId = updateUserStatus.userId;
                    TdApi.User user = users.get(userId);
                    user.status = updateUserStatus.status;
                    onUserStatusUpdate.accept(userId, isContactOnline(userId));
                }
                case TdApi.UpdateNewChat.CONSTRUCTOR -> {
                    TdApi.UpdateNewChat newChat = (TdApi.UpdateNewChat) object;
                    TdApi.Chat chat = newChat.chat;
                    if (chat.type.getConstructor() == TdApi.ChatTypePrivate.CONSTRUCTOR) {
                        TdApi.ChatTypePrivate chatTypePrivate = (TdApi.ChatTypePrivate) chat.type;
                        chatList.put(chat.id, chatTypePrivate.userId);
                        onChatRequestReturned.accept(users.get(chatTypePrivate.userId));
                        client.send(new TdApi.GetChatHistory(chat.id, 1, -99, 100, false), new RequestHandler());
                    }
                }
                case TdApi.UpdateNewMessage.CONSTRUCTOR -> {
                    TdApi.Message message = (TdApi.Message) object;
                    onNewMessage.accept(chatList.get(message.chatId), message);
                }
                case TdApi.UpdateChatLastMessage.CONSTRUCTOR -> {
                    TdApi.UpdateChatLastMessage chatLastMessage = (TdApi.UpdateChatLastMessage) object;
                    TdApi.MessageText messageText = (TdApi.MessageText) chatLastMessage.lastMessage.content;
                    int userId = chatList.get(chatLastMessage.chatId);
                    String text = messageText.text.text;
                    boolean out = chatLastMessage.lastMessage.senderUserId != userId;
                    onLastMessageUpdate.accept(userId, text, chatLastMessage.lastMessage.date, out);
                }
            }
        }
    }

    public boolean isContactOnline(int id) {
        return users.get(id).status.getConstructor() == TdApi.UserStatusOnline.CONSTRUCTOR;
    }

    public void sendMessage(int userId, String text) {
        TdApi.InputMessageContent content = new TdApi.InputMessageText(new TdApi.FormattedText(text, null), false, true);
        for (long chatId : chatList.keySet()) {
            if (userId == chatList.get(chatId)) {
                client.send(new TdApi.SendMessage(chatId, 0, null, null, content), new RequestHandler());
                break;
            }
        }
    }

    public void searchContact(String query) {
        client.send(new TdApi.SearchContacts(query, 1), new RequestHandler());
    }

    public void sendPhoneNumber(String phoneNumber) {
        client.send(new TdApi.SetAuthenticationPhoneNumber(phoneNumber, null), new AuthorizationRequestHandler());
    }

    public void sendSMSCode(String smsCode) {
        client.send(new TdApi.CheckAuthenticationCode(smsCode), new AuthorizationRequestHandler());
    }

    public void registerUser(String name, String surname) {
        client.send(new TdApi.RegisterUser(name, surname), new AuthorizationRequestHandler());
    }

    public void updateAccount(String name, String surname) {
        client.send(new TdApi.SetName(name, surname), null);
    }

    public void editContact(int id, String name) {
        client.send(new TdApi.ImportContacts(new TdApi.Contact[]{new TdApi.Contact(users.get(id).phoneNumber, name, null, null, id)}), new RequestHandler());
    }

    public void deleteContact(int id) {
        deletedContactId = id;
        client.send(new TdApi.RemoveContacts(new int[]{id}), new RequestHandler());
    }

    public void addContact(String number) {
        client.send(new TdApi.ImportContacts(new TdApi.Contact[]{new TdApi.Contact(number, null, null, null, 0)}), new RequestHandler());
    }

    public void logOut() {
        client.send(new TdApi.LogOut(), null);
    }

    public void close() {
        quit = true;
        client.send(new TdApi.Close(), new AuthorizationRequestHandler());
    }

    public void onChatRequestReturned(Consumer<TdApi.User> onChatRequestReturned) {
        this.onChatRequestReturned = onChatRequestReturned;
    }

    public void onMessageRequestReturned(BiConsumer<Integer, TdApi.Message> onMessageRequestReturned) {
        this.onMessageRequestReturned = onMessageRequestReturned;
    }

    public void onNewMessage(BiConsumer<Integer, TdApi.Message> onNewMessage) {
        this.onNewMessage = onNewMessage;
    }

    public void onLastMessageUpdate(ThreeConsumer onLastMessageUpdate) {
        this.onLastMessageUpdate = onLastMessageUpdate;
    }

    public void onUserStatusUpdate(BiConsumer<Integer, Boolean> onUserStatusUpdate) {
        this.onUserStatusUpdate = onUserStatusUpdate;
    }

    public void onWaitPhoneNumber(Action onWaitPhoneNumber) {
        this.onWaitPhoneNumber = onWaitPhoneNumber;
    }

    public void onWaitCode(Consumer<String> onWaitCode) {
        this.onWaitCode = onWaitCode;
    }

    public void onWaitRegistration(Action onWaitRegistration) {
        this.onWaitRegistration = onWaitRegistration;
    }

    public void onAuthorizationStateReady(Action onAuthorizationStateReady) {
        this.onAuthorizationStateReady = onAuthorizationStateReady;
    }

    public void onExit(Action onExit) {
        this.onExit = onExit;
    }

    public void onUserUpdate(Consumer<ConcurrentMap<Integer, TdApi.User>> onUserUpdate) {
        this.onUsersUpdate = onUserUpdate;
    }

    public void onError(Consumer<String> onError) {
        this.onError = onError;
    }

    public void onContactAdded(Action onContactAdded) {
        this.onContactAdded = onContactAdded;
    }

    public void onContactDeleted(Consumer<Integer> onContactDeleted) {
        this.onContactDeleted = onContactDeleted;
    }

    @FunctionalInterface
    interface Action {
        void accept();
    }

    @FunctionalInterface
    interface ThreeConsumer {
        void accept(int id, String text, int date, boolean out);
    }
}