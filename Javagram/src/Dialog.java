import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Dialog extends JPanel {
    private JPanel panel;
    private JScrollPane scrollPane;

    public Dialog(String name) {
        setName(name);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500,500));
        setOpaque(false);

        panel = new JPanel();
        panel.setMaximumSize(new Dimension(500,5000));
        panel.setMinimumSize(new Dimension(500,500));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.add(Box.createVerticalGlue());
        scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(500,500));
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addMessage(Message message, int index) {
        panel.add(message, index);
        updateUI();
        scrollDown();
    }

    public void scrollDown() {
        SwingUtilities.invokeLater(() -> scrollPane.getViewport().setViewPosition(new Point(0, panel.getHeight())));
    }

    public ArrayList<Message> getMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        for (Component component : panel.getComponents()) {
            if (component.isBackgroundSet()) {
                messages.add((Message) component);
            }
        }
        return messages;
    }
}
