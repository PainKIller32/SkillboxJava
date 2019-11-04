import javax.swing.*;

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
    private Integer pageCount = 0;

    public Form() {
        viewButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION)
                savePathTextField.setText(fileChooser.getSelectedFile().getPath());
        });
        startButton.addActionListener(e -> {
            if (startButton.getText().equals("Pause")) {
                startButton.setText("Start");
                Processor.paused();
            } else if (enterURLTextField.getText().trim().isEmpty() || savePathTextField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(rootPanel, "Заполните все поля!", "", JOptionPane.WARNING_MESSAGE);
            } else {
                startButton.setText("Pause");
                enterURLTextField.setEnabled(false);
                savePathTextField.setEnabled(false);
                viewButton.setEnabled(false);
                if (Processor.isPaused()) Processor.started();
                else Loader.searchUrl(enterURLTextField.getText());
            }
        });
        stopButton.addActionListener(e -> Processor.finished());
    }

    public void setInfoTextArea() {
        pageCount++;
        textAreaInfo.setText("Пройдено страниц: " + pageCount);
        textAreaInfo.repaint();
    }

    public void setInfoTextArea(Long time) {
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

    public void onFinished() {
        startButton.setText("Start");
        enterURLTextField.setEnabled(true);
        savePathTextField.setEnabled(true);
        viewButton.setEnabled(true);
        pageCount = 0;
    }
}
