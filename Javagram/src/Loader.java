import org.drinkless.tdlib.TdApi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Loader {
    private static Form form;
    private static TelegramController telegramController;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame();
        form = new Form();
        frame.setUndecorated(true);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.addMouseMotionListener(new MouseMotionListener() {
            int x;
            int y;

            @Override
            public void mouseDragged(MouseEvent e) {
                frame.setLocation(frame.getX() + e.getX() - x, frame.getY() + e.getY() - y);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        frame.setContentPane(form.getRootPanel());
        frame.setVisible(true);

        telegramController = new TelegramController();

        telegramController.onContactAdded(() -> SwingUtilities.invokeLater(() -> form.showMain()));
        telegramController.onExit(() -> form.closeApplication());
        telegramController.onContactDeleted((id) -> form.deleteContact(id));
        telegramController.onError((text) -> form.showWarningMessage(text));
        telegramController.onWaitRegistration(() -> SwingUtilities.invokeLater(() -> form.showRegistration()));
        telegramController.onWaitPhoneNumber(() -> SwingUtilities.invokeLater(() -> form.showEnterPhoneNumber()));
        telegramController.onNewMessage((id, message) -> SwingUtilities.invokeLater(() -> addMessageToForm(id, message, -1)));
        telegramController.onUserStatusUpdate((id, online) -> form.getContact(id).setStatus(online));
        telegramController.onMessageRequestReturned((id, message) -> SwingUtilities.invokeLater(() -> addMessageToForm(id, message, 1)));
        telegramController.onLastMessageUpdate((id, text, date, out) -> {
            form.getContact(id).setLastMessage(text, out);
            form.getContact(id).setLastMessageTime(Updater.updateSendTime(date));
        });
        telegramController.onWaitCode((phoneNumber) -> SwingUtilities.invokeLater(() -> {
            form.setAccountPhoneNumber(phoneNumber);
            form.showEnterSMSCode();
        }));
        telegramController.onAuthorizationStateReady(() -> {
            SwingUtilities.invokeLater(() -> form.showMain());
            new Updater(form.getList());
        });
        telegramController.onChatRequestReturned((user) -> {
            form.addContact(user.id, user.firstName + " " + user.lastName, user.phoneNumber, null, telegramController.isContactOnline(user.id));
        });
        telegramController.onUserUpdate((users) -> {
            for (TdApi.User user : users.values()) {
                if (!user.isContact) {
                    form.setAccountName(user.firstName);
                    form.setAccountSurname(user.lastName);
                    form.setAccountPhoneNumber(user.phoneNumber);
                } else {
                    form.updateContact(user.id, user.firstName);
                }
            }
        });

        form.onClose(() -> telegramController.close());
        form.onLogOut(() -> telegramController.logOut());
        form.onMinimize(() -> frame.setState(Frame.ICONIFIED));
        form.onDeleteContact(id -> telegramController.deleteContact(id));
        form.onSearch((query) -> telegramController.searchContact(query));
        form.onAddContact(number -> telegramController.addContact(number));
        form.onEnterSMSCode((smsCode) -> telegramController.sendSMSCode(smsCode));
        form.onSendMessage((id, text) -> telegramController.sendMessage(id, text));
        form.onEditContact((id, name) -> telegramController.editContact(id, name));
        form.onEnterNumber((phoneNumber) -> telegramController.sendPhoneNumber(phoneNumber));
        form.onSaveSetting((name, surname) -> telegramController.updateAccount(name, surname));
        form.onRegistrationComplete((name, surname) -> telegramController.registerUser(name, surname));
    }

    private synchronized static void addMessageToForm(Integer id, TdApi.Message message, int index) {
        boolean out = message.senderUserId != id;
        TdApi.MessageText messageText = (TdApi.MessageText) message.content;
        String text = messageText.text.text;
        form.getContact(id).getDialog().addMessage(new Message(text, out, message.date, Updater.updateSendTime(message.date)), index);
    }
}