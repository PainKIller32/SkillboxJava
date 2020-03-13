import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WarningMessage extends JDialog {
    private Font openSansRegular16;

    public WarningMessage(JPanel owner, String text) {
        try {
            openSansRegular16 = Font.createFont(Font.TRUETYPE_FONT, new File("font/OpenSansRegular.ttf")).deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        JLabel label = new JLabel(text);
        label.setFont(openSansRegular16);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(false);
        JButton option = new JButton("OK");
        option.setFont(openSansRegular16);
        option.addActionListener(e -> this.dispose());
        JOptionPane optionPane = new JOptionPane(label, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{option}) {
            private RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            @Override
            public void paintComponents(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHints(hints);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHints(hints);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }
        };
        optionPane.setOpaque(false);
        getRootPane().setOpaque(false);
        getContentPane().setBackground(new Color(0, 0, 0,0));
        setUndecorated(true);
        setBackground(new Color(0, 0, 0,0));
        setLayout(new BorderLayout());
        add(optionPane);
        pack();
        setModal(true);
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}