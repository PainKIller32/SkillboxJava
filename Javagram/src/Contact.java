import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Contact extends JPanel {
    private JLabel contactName;
    private JLabel lastMassage;
    private JLabel time;
    private JPanel contactPhoto;
    private JPanel contactInfo;
    private boolean online;
    private boolean active;
    private BufferedImage photo;
    private int contactId;
    private String phone;
    private Dialog dialog;

    private Font openSansRegular16;
    private Font openSansRegular10;
    private final static Color whiteBackground = new Color(255, 255, 255);
    private final static Color grayBackground = new Color(230, 230, 230);
    private final static Color colorGrayW = new Color(186, 186, 186);
    private final static Color colorGray = new Color(117, 117, 117);
    private final static Color colorBlue = new Color(0, 179, 230);
    private final static BufferedImage maskGray = readImg("res/mask-gray.png");
    private final static BufferedImage maskGrayOnline = readImg("res/mask-gray-online.png");
    private final static BufferedImage maskWhite = readImg("res/mask-white.png");
    private final static BufferedImage maskWhiteOnline = readImg("res/mask-white-online.png");


    public Contact(int id, String name, String phone, BufferedImage photo, boolean online) {
        this.contactId = id;
        this.photo = photo;
        this.online = online;
        this.phone = phone;
        this.dialog = new Dialog(name);
        setPreferredSize(new Dimension(250, 60));
        setMaximumSize(new Dimension(250, 60));
        setMinimumSize(new Dimension(250, 60));
        setBackground(grayBackground);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentY(Component.CENTER_ALIGNMENT);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setFocusable(true);
        setRequestFocusEnabled(true);
        setVerifyInputWhenFocusTarget(true);

        this.printPhoto(false);

        contactPhoto.setOpaque(false);
        contactPhoto.setSize(70, 60);

        contactName = new JLabel(name);

        try {
            openSansRegular16 = Font.createFont(Font.TRUETYPE_FONT, new File("font/OpenSansRegular.ttf")).deriveFont(16f);
            openSansRegular10 = Font.createFont(Font.TRUETYPE_FONT, new File("font/OpenSansRegular.ttf")).deriveFont(10f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        contactName.setFont(openSansRegular16);

        lastMassage = new JLabel("Привет");

        lastMassage.setFont(openSansRegular10);
        lastMassage.setForeground(colorGray);

        contactInfo = new JPanel();
        contactInfo.setPreferredSize(new Dimension(130, 40));
        contactInfo.setLayout(new BoxLayout(contactInfo, BoxLayout.Y_AXIS));
        contactInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        contactInfo.setAlignmentY(Component.CENTER_ALIGNMENT);
        contactInfo.setOpaque(false);
        contactInfo.add(contactName);
        contactInfo.add(lastMassage);

        time = new JLabel("17.07.15");
        time.setPreferredSize(new Dimension(50, 40));
        time.setFont(openSansRegular10);
        time.setForeground(colorGrayW);

        add(contactPhoto);
        add(contactInfo);
        add(time);
    }

    public Dialog getDialog() {
        return dialog;
    }

    public BufferedImage getPhoto() {
        return photo;
    }

    public String getPhone() {
        return phone;
    }

    public void enabledContact() {
        active = true;
        setBackground(whiteBackground);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, colorBlue));
        remove(contactPhoto);
        printPhoto(true);
        contactPhoto.setOpaque(false);
        add(contactPhoto, 0);
        contactPhoto.setVisible(true);
    }

    public void disableContact() {
        active = false;
        setBackground(grayBackground);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        remove(contactPhoto);
        printPhoto(false);
        contactPhoto.setOpaque(false);
        add(contactPhoto, 0);
    }

    private void printPhoto(boolean focus) {
        contactPhoto = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(photo, 15, 10, null);
                if (online) {
                    if (focus) {
                        g.drawImage(maskWhiteOnline, 15, 10, null);
                    } else {
                        g.drawImage(maskGrayOnline, 15, 10, null);
                    }
                } else {
                    if (focus) {
                        g.drawImage(maskWhite, 15, 10, null);
                    } else {
                        g.drawImage(maskGray, 15, 10, null);
                    }
                }
            }
        };
    }

    private static BufferedImage readImg(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName() {
        return contactName.getText();
    }

    public void setName(String name) {
        contactName.setText(name);
        this.updateUI();
    }

    public int getContactId() {
        return contactId;
    }

    public boolean isActive() {
        return active;
    }

    public void setStatus(boolean online) {
        this.online = online;
    }
}