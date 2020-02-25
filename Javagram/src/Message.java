import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Message extends JPanel {
    private long date;
    private JLabel sendTime;
    private JTextArea messageText;
    private Font openSansRegular16;
    private Font openSansRegular10;
    private final static Color colorGray = new Color(117, 117, 117);
    private final static Color blueBackground = new Color(0, 168, 219);
    private final static Color purpleBackground = new Color(74, 68, 168);
    private final static BufferedImage massageIn = readImg("res/message-in-left.png");
    private final static BufferedImage massageInTop = readImg("res/message-in-top.png");
    private final static BufferedImage massageInBottom = readImg("res/message-in-bottom.png");
    private final static BufferedImage massageOut = readImg("res/message-out-right.png");
    private final static BufferedImage massageOutTop = readImg("res/message-out-top.png");
    private final static BufferedImage massageOutBottom = readImg("res/message-out-bottom.png");

    public Message(String massage, boolean out, long date) {
        this.date = date;
        setMinimumSize(new Dimension(327, 60));
        setMaximumSize(new Dimension(327, 1000));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentY(Component.BOTTOM_ALIGNMENT);
        setOpaque(false);

        try {
            openSansRegular16 = Font.createFont(Font.TRUETYPE_FONT, new File("font/OpenSansRegular.ttf")).deriveFont(16f);
            openSansRegular10 = Font.createFont(Font.TRUETYPE_FONT, new File("font/OpenSansRegular.ttf")).deriveFont(10f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        messageText = new JTextArea(massage);
        messageText.setEditable(false);
        messageText.setFont(openSansRegular16);
        messageText.setForeground(Color.WHITE);

        sendTime = new JLabel();
        sendTime.setAlignmentX(Component.LEFT_ALIGNMENT);
        sendTime.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        sendTime.setFont(openSansRegular10);
        sendTime.setForeground(colorGray);

        if (out) {
            setAlignmentX(Component.RIGHT_ALIGNMENT);
            setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 10));
            messageText.setLineWrap(true);
            messageText.setWrapStyleWord(true);
            messageText.setMinimumSize(new Dimension(307, 10));
            messageText.setMaximumSize(new Dimension(307, 5000));
            messageText.setBackground(purpleBackground);
        } else {
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));
            messageText.setLineWrap(true);
            messageText.setWrapStyleWord(true);
            messageText.setMinimumSize(new Dimension(308, 10));
            messageText.setMaximumSize(new Dimension(308, 5000));
            messageText.setBackground(blueBackground);
        }

        add(messageText);
        add(sendTime);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (messageText.getBackground().equals(purpleBackground)) {
            g.drawImage(massageOut, 310, messageText.getHeight() / 2 + 6, null);
            g.drawImage(massageOutTop, 4, 1, null);
            g.drawImage(massageOutBottom, 4, messageText.getHeight() + 10, null);
        } else {
            g.drawImage(massageIn, 7, messageText.getHeight() / 2 + 6, null);
            g.drawImage(massageInTop, 14, 2, null);
            g.drawImage(massageInBottom, 14, messageText.getHeight() + 10, null);
        }
    }

    public void setSendTime(String sendTime) {
        this.sendTime.setText(sendTime);
    }

    public long getDate() {
        return date;
    }

    private static BufferedImage readImg(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
