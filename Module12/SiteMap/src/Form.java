import javax.swing.*;
import java.awt.event.ActionListener;

public class Form {
    private JPanel rootPanel;
    private JPanel buttons;
    private JButton startButton;
    private JButton stopButton;
    private JTextArea textAreaInfo;
    private JPanel fields;
    private JTextField enterURLTextField;
    private JTextField savePathTextField;
    private JButton viewButton;
    private JLabel labelURL;
    private JLabel labelSave;
    private JPanel labels;

    public Form() {
        viewButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                savePathTextField.setText(fileChooser.getSelectedFile().getPath());
            }
        });
    }

    public void addStartActionListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    public void addStopActionListener(ActionListener listener) {
        stopButton.addActionListener(listener);
    }

    public void setTextOnStartButton(String text) {
        startButton.setText(text);
    }

    public void disabledTextFields() {
        enterURLTextField.setEnabled(false);
        savePathTextField.setEnabled(false);
    }

    public void enabledTextFields() {
        enterURLTextField.setEnabled(true);
        savePathTextField.setEnabled(true);
    }

    public void disabledViewButton() {
        viewButton.setEnabled(false);
    }

    public void enabledViewButton() {
        viewButton.setEnabled(true);
    }

    public String getTextOnStartButton() {
        return startButton.getText();
    }

    public void showWarningMassage() {
        JOptionPane.showMessageDialog(rootPanel, "Заполните все поля!", "", JOptionPane.WARNING_MESSAGE);
    }

    public void showCompleteMassage() {
        JOptionPane.showMessageDialog(rootPanel, "Готово!", "", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setInfoTextArea(long time, Integer pageCount) {
        long minute = time / 60000;
        long seconds = time / 1000;
        textAreaInfo.setText("Пройдено страниц: " + pageCount + "\nВремя: " + minute + " минут(а) и " + seconds + " секунд(а)");
        textAreaInfo.repaint();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public String getSavePath() {
        return savePathTextField.getText();
    }

    public String getUrl() {
        return enterURLTextField.getText();
    }
}
