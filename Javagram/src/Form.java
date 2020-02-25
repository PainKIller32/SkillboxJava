import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.*;

public class Form {
    private JPanel RootPanel;
    private JPanel EnterPhoneNumber;
    private JTextField textFieldEnterNumber;
    private JButton nextButtonEnterNum;
    private JPanel Logo;
    private JPanel EnterSMSCode;
    private JTextPane textPaneSMS;
    private JPanel Logo2;
    private JLabel labelTelephoneNumber;
    private JPasswordField textFieldEnterCode;
    private JButton nextButtonEnterSMS;
    private JPanel Main;
    private JButton settingsButton;
    private JLabel accountNameLabel;
    private JPanel Title;
    private JPanel Contacts;
    private JPanel Dialog;
    private JButton addButton;
    private JPanel List;
    private JPanel MainPanel;
    private JButton closeButton;
    private JButton minimizeButton;
    private JPanel Bar;
    private JTextArea textAreaPhone;
    private JPanel Registration;
    private JPanel Logo3;
    private JTextPane textPaneReg;
    private JButton nextButton3;
    private JPanel FIO;
    private JTextField nameRegistrationTextField;
    private JTextField surnameRegistrationTextField;
    private JButton buttonPhoto;
    private JPanel LogoMicro;
    private JTextField searchTextField;
    private JButton contactPhoto;
    private JButton editContactButton;
    private JLabel contactNameLabel;
    private JPanel ContactInformation;
    private JTextArea textFieldEnterMassage;
    private JButton sendButton;
    private JPanel EnterMessage;
    private JPanel Dialogs;
    private JPanel ProfileSetting;
    private JButton saveSettingButton;
    private JLabel labelSetting;
    private JButton settingBackButton;
    private JButton exitButton;
    private JLabel phoneNumberLabel;
    private JTextField nameSettingTextField;
    private JTextField surNameSettingTextField;
    private JPanel SettingPanel;
    private JPanel AddContact;
    private JLabel labelAddContact;
    private JTextArea textAreaAddContact;
    private JTextField enterNumberContactTextField;
    private JButton backButtonAddContact;
    private JButton addContactButton;
    private JPanel EditContactPanel;
    private JButton backButtonEditContact;
    private JButton saveEditContactButton;
    private JTextField textFieldContactName;
    private JButton deleteContactButton;
    private JLabel labelEditContact;
    private JLabel contactPhoneNumberLabel;
    private JLabel accountSurnameLabel;
    private JTextField contactNameTextField;
    private JTextField contactSurnameTextField;
    private JLabel contactNotFound;
    private JLabel invalidPhoneNumber;
    private JLabel invalidSMSCode;

    private Contact activeContact;
    private Action onMinimize;
    private Supplier<Boolean> onLogOut;
    private ThreeFunction onAddContact;
    private Predicate<String> onEnterNumber;
    private Consumer<JPanel> onLogInComplete;
    private Function<String, Boolean> onEnterSMSCode;
    private BiConsumer<String, String> onSaveSetting;
    private Function<Integer, Boolean> onDeleteContact;
    private BiConsumer<String, String> onRegistrationComplete;

    private BufferedImage accountPhoto;
    private BufferedImage buttonBackground;
    private BufferedImage editContactPhoto;
    private Font openSansRegular22;

    public Form() throws IOException, FontFormatException {

        openSansRegular22 = Font.createFont(Font.TRUETYPE_FONT, new File("font/OpenSansRegular.ttf")).deriveFont(22f);
        buttonBackground = readImg("res/button-background.png");

        Color colorRed = new Color(249, 77, 77);
        Color colorBlue = new Color(0, 179, 230);
        Color colorWhite = new Color(230, 230, 230);
        Border bottomLineBorder = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.WHITE);

        textFieldEnterCode.setBorder(bottomLineBorder);
        nameSettingTextField.setBorder(bottomLineBorder);
        surNameSettingTextField.setBorder(bottomLineBorder);
        nameRegistrationTextField.setBorder(bottomLineBorder);
        surnameRegistrationTextField.setBorder(bottomLineBorder);
        searchTextField.setBorder(BorderFactory.createEmptyBorder());
        searchTextField.setBorder(BorderFactory.createEmptyBorder(0, 35, 0, 0));
        exitButton.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, colorBlue));
        textFieldContactName.setBorder(BorderFactory.createEmptyBorder(0, 75, 10, 5));
        textFieldEnterNumber.setBorder(BorderFactory.createCompoundBorder(bottomLineBorder, BorderFactory.createEmptyBorder(0, 40, 0, 0)));
        contactNameTextField.setBorder(BorderFactory.createCompoundBorder(bottomLineBorder, BorderFactory.createEmptyBorder(20, 10, 0, 0)));
        contactSurnameTextField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0), bottomLineBorder));
        enterNumberContactTextField.setBorder(BorderFactory.createCompoundBorder(bottomLineBorder, BorderFactory.createEmptyBorder(0, 30, 0, 0)));
        deleteContactButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 3, 3, 3, colorRed),
                BorderFactory.createEmptyBorder(0, 20, 0, 0))
        );
        textFieldEnterMassage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 0, 0, 0),
                BorderFactory.createLineBorder(colorWhite, 2, true))
        );

        setBoxLayoutY(EnterPhoneNumber);
        setBoxLayoutY(EnterSMSCode);
        setBoxLayoutY(Registration);
        setBoxLayoutY(ProfileSetting);
        setBoxLayoutY(AddContact);
        setBoxLayoutY(EditContactPanel);
        setBoxLayoutY(FIO);
        setBoxLayoutY(List);

        setBoxLayoutX(EnterMessage);
        setBoxLayoutX(ContactInformation);
        ContactInformation.add(Box.createRigidArea(new Dimension(10, 10)), 0);
        ContactInformation.add(Box.createRigidArea(new Dimension(10, 10)), 5);
        setBoxLayoutX(SettingPanel);
        SettingPanel.add(Box.createRigidArea(new Dimension(10, 10)), 3);
        setBoxLayoutX(Title);
        Title.add(Box.createRigidArea(new Dimension(10, 10)), 3);
        Title.add(Box.createRigidArea(new Dimension(10, 10)), 6);

        closeButton.addActionListener(e -> System.exit(1));

        nameRegistrationTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mousePressed(e);
                if (nameRegistrationTextField.getText().equals("Имя")) {
                    nameRegistrationTextField.setText("");
                }
            }
        });
        nameRegistrationTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (nameRegistrationTextField.getText().isEmpty()) {
                    nameRegistrationTextField.setText("Имя");
                }
            }
        });
        surnameRegistrationTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mousePressed(e);
                if (surnameRegistrationTextField.getText().equals("Фамилия")) {
                    surnameRegistrationTextField.setText("");
                }
            }
        });
        surnameRegistrationTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (surnameRegistrationTextField.getText().isEmpty()) {
                    surnameRegistrationTextField.setText("Фамилия");
                }
            }
        });
        searchTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mousePressed(e);
                searchTextField.setText("");
            }
        });
        searchTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setText("Поиск");
                }
            }
        });

        textFieldEnterMassage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (textFieldEnterMassage.getText().equals("Введите сообщение")) {
                    textFieldEnterMassage.setText("");
                }
            }
        });

        backButtonAddContact.addActionListener(e -> {
            enterNumberContactTextField.setText("+7");
            contactNameTextField.setText("");
            contactSurnameTextField.setText("");
            ((CardLayout) MainPanel.getLayout()).show(MainPanel, "Main");
        });

        editContactButton.addActionListener(e -> ((CardLayout) MainPanel.getLayout()).show(MainPanel, "EditContactPanel"));

        backButtonEditContact.addActionListener(e -> ((CardLayout) MainPanel.getLayout()).show(MainPanel, "Main"));

        sendButton.addActionListener(e -> {
            sendMessage();
        });

        textFieldEnterMassage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (textFieldEnterMassage.getText().equals("")) {
                    textFieldEnterMassage.setText("Введите сообщение");
                }
            }
        });

        nextButtonEnterNum.addActionListener(e -> pushPhoneNumber());

        exitButton.addActionListener(e -> {
            if (onLogOut.get()) {
                List.removeAll();
                Dialogs.removeAll();
                invalidPhoneNumber.setVisible(false);
                invalidSMSCode.setVisible(false);
                showEnterPhoneNumber();
            }
        });

        minimizeButton.addActionListener(e -> onMinimize.accept());

        settingBackButton.addActionListener(e -> showMain());

        settingsButton.addActionListener(e -> {
            ((CardLayout) MainPanel.getLayout()).show(MainPanel, "ProfileSetting");
            phoneNumberLabel.setText(textFieldEnterNumber.getText().trim());
            nameSettingTextField.setText(getAccountName());
            surNameSettingTextField.setText(getAccountSurname());
        });

        saveSettingButton.addActionListener(e -> {
            onSaveSetting.accept(nameSettingTextField.getText(), surNameSettingTextField.getText());
            setAccountName(nameSettingTextField.getText());
            setAccountSurname(surNameSettingTextField.getText());
        });

        nextButtonEnterSMS.addActionListener(e -> {
            pushSMSCode();
        });

        addButton.addActionListener(e -> {
            enterNumberContactTextField.setText("+7");
            enterNumberContactTextField.setCaretPosition(enterNumberContactTextField.getText().length());
            contactNameTextField.setText("");
            contactSurnameTextField.setText("");
            contactNotFound.setVisible(false);
            ((CardLayout) MainPanel.getLayout()).show(MainPanel, "AddContact");
        });

        deleteContactButton.addActionListener(e -> {
            if (onDeleteContact.apply(activeContact.getContactId())) {
                List.remove(activeContact);
                showMain();
            }
        });

        saveEditContactButton.addActionListener(e -> {
            activeContact.setName(textFieldContactName.getText());
            //как-то сохранить изменения
        });

        nextButton3.addActionListener(e -> {
            onRegistrationComplete.accept(nameRegistrationTextField.getText(), surnameRegistrationTextField.getText());
            onLogInComplete.accept(List);
            showMain();
        });

        addContactButton.addActionListener(e -> {
            if (!onAddContact.accept(enterNumberContactTextField.getText().replaceAll("[^0-9]+", ""), contactNameTextField.getText(), contactSurnameTextField.getText())) {
                contactNotFound.setVisible(true);
                AddContact.updateUI();
            } else {
                showMain();
            }
        });

        textFieldEnterMassage.addCaretListener(e -> {
            int lineCount = textFieldEnterMassage.getLineCount();
            if (lineCount > 1) {
                if (lineCount > 10) {
                    textFieldEnterMassage.setEditable(false);
                }
                textFieldEnterMassage.setMaximumSize(new Dimension(440, lineCount * 26));
                EnterMessage.setPreferredSize(new Dimension(600, 15 + lineCount * 26));
                Dialog.updateUI();
            } else {
                textFieldEnterMassage.setMaximumSize(new Dimension(440, 45));
                EnterMessage.setPreferredSize(new Dimension(600, 60));
                Dialog.updateUI();
            }
        });

        textFieldEnterMassage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!textFieldEnterMassage.isEditable() && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    textFieldEnterMassage.setEditable(true);
                }
            }
        });

        textFieldEnterNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pushPhoneNumber();
                }
            }
        });

        textFieldEnterCode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pushSMSCode();
                }
            }
        });

        textFieldEnterMassage.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "ctrlEnter");
        javax.swing.Action ctrlEnter = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        };

        textFieldEnterMassage.getActionMap().put("ctrlEnter", ctrlEnter);

    }

    private void createUIComponents() {
        try {
            BufferedImage backIcon = readImg("res/icon-back.png");
            BufferedImage closeIcon = readImg("res/icon-close.png");
            BufferedImage minimizeIcon = readImg("res/icon-hide.png");
            BufferedImage background = readImg("res/background.png");
            BufferedImage logo = readImg("res/logo.png");
            BufferedImage phoneIcon = readImg("res/icon-phone.png");
            BufferedImage logoMini = readImg("res/logo-mini.png");
            BufferedImage lockIcon = readImg("res/icon-lock.png");
            BufferedImage logoMicro = readImg("res/logo-micro.png");
            BufferedImage settingIcon = readImg("res/icon-settings.png");
            BufferedImage maskBlue = readImg("res/mask-blue-mini.png");
            BufferedImage plusIcon = readImg("res/icon-plus.png");
            BufferedImage searchIcon = readImg("res/icon-search.png");
            BufferedImage editIcon = readImg("res/icon-edit.png");
            BufferedImage maskWhiteMini = readImg("res/mask-white-mini.png");
            BufferedImage sendIcon = readImg("res/button-send.png");
            BufferedImage deleteIcon = readImg("res/icon-trash.png");
            BufferedImage maskDarkBig = readImg("res/mask-dark-gray-big.png");

            nextButtonEnterNum = getDrawnButton("ПРОДОЛЖИТЬ");
            nextButtonEnterSMS = getDrawnButton("ПРОДОЛЖИТЬ");
            saveEditContactButton = getDrawnButton("СОХРАНИТЬ");
            nextButton3 = getDrawnButton("ЗАВЕРШИТЬ");
            saveSettingButton = getDrawnButton("СОХРАНИТЬ");
            addContactButton = getDrawnButton("ДОБАВИТЬ");

            closeButton = getDrawnButton(closeIcon, 0, 0);
            minimizeButton = getDrawnButton(minimizeIcon, 0, 0);
            settingsButton = getDrawnButton(settingIcon, 0, 0);
            addButton = getDrawnButton(plusIcon, 0, 0);
            editContactButton = getDrawnButton(editIcon, 0, 0);
            sendButton = getDrawnButton(sendIcon, -5, 0);
            settingBackButton = getDrawnButton(backIcon, 0, 0);
            backButtonAddContact = getDrawnButton(backIcon, 0, 0);
            backButtonEditContact = getDrawnButton(backIcon, 0, 0);
            deleteContactButton = getDrawnButton(deleteIcon, 10, 11);

            Logo = getDrawnPanel(logo, 0, 0);
            Logo2 = getDrawnPanel(logoMini, 0, 0);
            Logo3 = getDrawnPanel(logoMini, 0, 0);
            LogoMicro = getDrawnPanel(logoMicro, 10, 0);
            MainPanel = getDrawnPanel(background, 0, 0);

            textFieldEnterNumber = getDrawnTextField(phoneIcon, 0, 2);
            searchTextField = getDrawnTextField(searchIcon, 10, 10);
            enterNumberContactTextField = getDrawnTextField(phoneIcon, 0, 2);

            buttonPhoto = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(accountPhoto, -10, 0, 50, 30, null); // для пробы!
                    g.drawImage(maskBlue, 0, 0, null);
                }
            };

            contactPhoto = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(maskWhiteMini, 0, 0, null);
                }
            };

            textFieldEnterCode = new JPasswordField() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(lockIcon, 0, 2, null);
                }
            };

            textFieldContactName = new JTextField() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(editContactPhoto, 0, 2, null);
                    g.drawImage(maskDarkBig, 0, 2, null);
                    g.drawLine(75, 50, 400, 50);
                    g.setFont(openSansRegular22);
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JButton getDrawnButton(String text) {
        return new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(buttonBackground, 0, 0, null);
                g.setFont(openSansRegular22);
                g.drawString(text, 97, 38);
            }
        };
    }

    private JButton getDrawnButton(BufferedImage image, int x, int y) {
        return new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, x, y, null);
            }
        };
    }

    private JTextField getDrawnTextField(BufferedImage image, int x, int y) {
        return new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, x, y, null);
            }
        };
    }

    private JPanel getDrawnPanel(BufferedImage image, int x, int y) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, x, y, null);
            }
        };
    }

    private BufferedImage readImg(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    private void setBoxLayoutY(JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    }

    private void setBoxLayoutX(JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    private void sendMessage() {
        if (activeContact != null) {
            if (!textFieldEnterMassage.getText().isEmpty() && !textFieldEnterMassage.getText().equals("Введите сообщение")) {
                int i = 0;
                for (Component comp : Dialogs.getComponents()) {
                    if (comp.isVisible()) {
                        ((Dialog) Dialogs.getComponent(i)).addMessage(new Message(textFieldEnterMassage.getText(), true, System.currentTimeMillis()));
                        textFieldEnterMassage.setText("");
                        break;
                    }
                    i++;
                }
            }
        }
    }

    private void pushPhoneNumber() {
        String phoneNumber = textFieldEnterNumber.getText().replaceAll("[^0-9]+", "");
        if (phoneNumber.isEmpty()) {
            invalidPhoneNumber.setVisible(true);
            EnterPhoneNumber.updateUI();
        } else if (onEnterNumber.test(phoneNumber)) {
            showEnterSMSCode();
        } else {
            invalidPhoneNumber.setVisible(true);
            EnterPhoneNumber.updateUI();
        }
    }

    private void pushSMSCode() {
        String password = String.valueOf(textFieldEnterCode.getPassword());
        if (password.isEmpty()) {
            invalidSMSCode.setVisible(true);
            EnterSMSCode.updateUI();
        } else {
            Boolean status = onEnterSMSCode.apply(password);
            if (status == null) {
                invalidSMSCode.setVisible(true);
                EnterSMSCode.updateUI();
            } else if (status) {
                onLogInComplete.accept(List);
                showMain();
            } else {
                showRegistration();
            }
        }
    }

    public void addContacts(int id, String name, String phone, BufferedImage photo, boolean online) {
        Contact contact = new Contact(id, name, phone, photo, online);
        contact.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!contact.equals(activeContact)) {
                    if (activeContact != null) {
                        activeContact.disableContact();
                    }
                    activeContact = contact;
                    contact.enabledContact();
                    contactNameLabel.setVisible(true);
                    editContactButton.setVisible(true);
                    textFieldEnterMassage.setVisible(true);
                    sendButton.setVisible(true);
                    contactNameLabel.setText(contact.getName());
                    textFieldContactName.setText(contact.getName());
                    editContactPhoto = contact.getPhoto();
                    contactPhoneNumberLabel.setText(contact.getPhone());
                    ((CardLayout) Dialogs.getLayout()).show(Dialogs, contact.getName());
                }
            }
        });
        List.add(contact);
        Dialogs.add(contact.getName(), contact.getDialog());
    }

    public JPanel getRootPanel() {
        showEnterPhoneNumber();
        return RootPanel;
    }

    public void setAccountPhoto(BufferedImage photo) {
        accountPhoto = photo;
    }

    public void setAccountName(String name) {
        accountNameLabel.setText(name);
    }

    public void setAccountSurname(String surname) {
        accountSurnameLabel.setText(surname);
    }

    private String getAccountName() {
        return accountNameLabel.getText();
    }

    private String getAccountSurname() {
        return accountSurnameLabel.getText();
    }

    private void showEnterSMSCode() {
        labelTelephoneNumber.setText(textFieldEnterNumber.getText().trim());
        ((CardLayout) MainPanel.getLayout()).show(MainPanel, "EnterSMSCode");
        textFieldEnterCode.grabFocus();
    }

    private void showMain() {
        accountNameLabel.setPreferredSize(new Dimension(getAccountName().length() * 8, 24));
        accountSurnameLabel.setPreferredSize(new Dimension(getAccountSurname().length() * 8, 24));
        Title.updateUI();
        ((CardLayout) MainPanel.getLayout()).show(MainPanel, "Main");
    }

    private void showRegistration() {
        ((CardLayout) MainPanel.getLayout()).show(MainPanel, "Registration");
    }

    private void showEnterPhoneNumber() {
        textFieldEnterNumber.setCaretPosition(textFieldEnterNumber.getText().length());
        ((CardLayout) MainPanel.getLayout()).show(MainPanel, "EnterPhoneNumber");
    }

    public void onEnterSMSCode(Function<String, Boolean> consumer) {
        this.onEnterSMSCode = consumer;
    }

    public void onEnterNumber(Predicate<String> function) {
        this.onEnterNumber = function;
    }

    public void onLogOut(Supplier<Boolean> supplier) {
        this.onLogOut = supplier;
    }

    public void onMinimize(Action action) {
        this.onMinimize = action;
    }

    public void onSaveSetting(BiConsumer<String, String> biConsumer) {
        this.onSaveSetting = biConsumer;
    }

    public void onAddContact(ThreeFunction function) {
        this.onAddContact = function;
    }

    public void onDeleteContact(Function<Integer, Boolean> function) {
        this.onDeleteContact = function;
    }

    public void onRegistrationComplete(BiConsumer<String, String> biConsumer) {
        this.onRegistrationComplete = biConsumer;
    }

    public void onLogInComplete(Consumer<JPanel> consumer) {
        this.onLogInComplete = consumer;
    }

    @FunctionalInterface
    interface Action {
        void accept();
    }

    @FunctionalInterface
    interface ThreeFunction {
        boolean accept(String number, String name, String surname);
    }
}