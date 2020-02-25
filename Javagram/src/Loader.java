import org.javagram.TelegramApiBridge;
import org.javagram.response.AuthAuthorization;
import org.javagram.response.AuthSentCode;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;
import org.telegram.api.TLImportedContact;
import org.telegram.api.TLInputContact;
import org.telegram.api.contacts.TLImportedContacts;
import org.telegram.api.engine.TelegramApi;
import org.telegram.api.requests.TLRequestContactsImportContacts;
import org.telegram.tl.TLVector;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Loader {
    private static BufferedImage accountPhotoSmall = null;

    private static User user;
    private static AuthSentCode sentCode;
    private static AuthAuthorization logIn;
    private static String smsCode;

    public static void main(String[] args) throws Exception {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame();
        Form form = new Form();
        frame.setContentPane(form.getRootPanel());
        frame.setUndecorated(true);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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

        form.onMinimize(() -> frame.setState(Frame.ICONIFIED));

        TelegramApiBridge bridge = new TelegramApiBridge("149.154.167.40:443", 615381, "1299ea11aa7f63c004936e40856ca6cb");


        Class telegramApiBridgeClass = TelegramApiBridge.class;
        Field apiField = telegramApiBridgeClass.getDeclaredField("api");
        apiField.setAccessible(true);
        TelegramApi telegramApi = (TelegramApi) apiField.get(bridge);

        form.onEnterNumber((number) -> {
            try {
                sentCode = bridge.authSendCode(number);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });

        form.onEnterSMSCode((codeSMS) -> {
            smsCode = codeSMS;
            boolean registered = sentCode.isRegistered();
            try {
                if (registered) {
                    //logIn = bridge.authSignUp(smsCode,"Igor","Melnikov");
                    //logIn = bridge.authSignUp(smsCode,"Ivan","Ivanov");
                    logIn = bridge.authSignIn(smsCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return registered;
        });


        form.onRegistrationComplete((name, surname) -> {
            try {
                logIn = bridge.authSignUp(smsCode, name, surname);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        form.onLogInComplete((list) -> {
            try {
                user = logIn.getUser();
                byte[] userPhoto = user.getPhoto(true);
                if (userPhoto != null) {
                    accountPhotoSmall = ImageIO.read(new ByteArrayInputStream(userPhoto));
                    form.setAccountPhoto(accountPhotoSmall);
                }
                form.setAccountName(user.getFirstName());
                form.setAccountSurname(user.getLastName());
                ArrayList<UserContact> contacts = new ArrayList<>(bridge.contactsGetContacts());
                for (UserContact contact : contacts) {
                    form.addContacts(contact.getId(), contact.toString(), contact.getPhone(),
                            ImageIO.read(new ByteArrayInputStream(contact.getPhoto(true))), contact.isOnline());
                }
                new Updater(list, contacts);
            } catch (IOException e) {
                e.printStackTrace();
            }
            form.addContacts(100, "Иван", "+79552300000", accountPhotoSmall, true); // проба
            form.addContacts(200, "Олег", "85554442323", accountPhotoSmall, false); // проба
        });

        form.onSaveSetting((name, surname) -> {
            try {
                user = bridge.accountUpdateProfile(name, surname);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        form.onAddContact((number, name, surname) -> {
            boolean added = false;
            try {
                TLVector<TLInputContact> v = new TLVector<>();
                v.add(new TLInputContact(0, number, name, surname));
                TLRequestContactsImportContacts ci = new TLRequestContactsImportContacts(v, false);
                TLImportedContacts ic = telegramApi.doRpcCall(ci);
                TLVector<TLImportedContact> listIC = ic.getImported();
                if (!listIC.isEmpty()) {
                    added = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return added;
        });

        form.onDeleteContact((id) -> {
            boolean isDeleteContact = false;
            try {
                isDeleteContact = bridge.contactsDeleteContact(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return isDeleteContact;
        });

        form.onLogOut(() -> {
            boolean logOut = false;
            try {
                logOut = bridge.authLogOut();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return logOut;
        });
    }
}