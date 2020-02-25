import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Dialog extends JPanel {
    private JPanel panel;
    private JScrollPane scrollPane;

    public Dialog(String name) {
        setName(name);
        setLayout(new BorderLayout());
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setOpaque(false);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.add(Box.createVerticalGlue());
        scrollPane = new JScrollPane(panel);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addMessage(Message message) {
        panel.add(message);
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
