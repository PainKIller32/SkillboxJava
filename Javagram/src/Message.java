import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Message extends JPanel {
    private long date;
    private JPanel panel;
    private JLabel sendTime;
    private JTextArea messageText;
    private Font openSansRegular14;
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

    public Message(String massage, boolean out, long date, String sendDate) {
        this.date = date;
        setMinimumSize(new Dimension(500, 60));
        setMaximumSize(new Dimension(500, 5000));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        try {
            openSansRegular14 = Font.createFont(Font.TRUETYPE_FONT, new File("font/OpenSansRegular.ttf")).deriveFont(14f);
            openSansRegular10 = Font.createFont(Font.TRUETYPE_FONT, new File("font/OpenSansRegular.ttf")).deriveFont(10f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        messageText = new JTextArea(massage);
        messageText.setEditable(false);
        messageText.setFont(openSansRegular14);
        messageText.setForeground(Color.WHITE);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);

        sendTime = new JLabel(sendDate);
        sendTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
        sendTime.setFont(openSansRegular10);
        sendTime.setForeground(colorGray);
        sendTime.setHorizontalAlignment(SwingConstants.CENTER);

        panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 10));
        panel.setMaximumSize(new Dimension(328,5000));
        panel.setMinimumSize(new Dimension(327, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(messageText, BorderLayout.CENTER);
        panel.add(sendTime, BorderLayout.SOUTH);

        if (out) {
            messageText.setMinimumSize(new Dimension(307, 10));
            messageText.setMaximumSize(new Dimension(307, 5000));
            messageText.setBackground(purpleBackground);
            add(Box.createHorizontalStrut(173));
            add(panel);
        } else {
            messageText.setMinimumSize(new Dimension(308, 10));
            messageText.setMaximumSize(new Dimension(308, 5000));
            messageText.setBackground(blueBackground);
            add(panel);
            add(Box.createHorizontalStrut(172));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (messageText.getBackground().equals(purpleBackground)) {
            g.drawImage(massageOut, 490, messageText.getHeight() / 2 + 6, null);
            g.drawImage(massageOutTop, 183, 1, null);
            g.drawImage(massageOutBottom, 183, messageText.getHeight() + 10, null);
        } else {
            g.drawImage(massageIn, 3, messageText.getHeight() / 2 + 6, null);
            g.drawImage(massageInTop, 10, 2, null);
            g.drawImage(massageInBottom, 10, messageText.getHeight() + 10, null);
        }
    }

    public synchronized void setSendTime(String sendTime) {
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
