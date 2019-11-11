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
    private Action onStart;
    private Action onStop;
    private Action onPause;

    public Form() {
        viewButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                savePathTextField.setText(fileChooser.getSelectedFile().getPath());
            }
        });

        startButton.addActionListener(e -> {
            if (startButton.getText().equals("Pause")) {
                startButton.setText("Start");
                onPause.accept();
            } else if (isFormValid()) {
                textAreaInfo.setText("Пройдено страниц:\nВремя:");
                startButton.setText("Pause");
                enterURLTextField.setEnabled(false);
                savePathTextField.setEnabled(false);
                viewButton.setEnabled(false);
                onStart.accept();
            } else {
                JOptionPane.showMessageDialog(rootPanel, "Заполните все поля!", "", JOptionPane.WARNING_MESSAGE);
            }
        });

        stopButton.addActionListener(e -> onStop.accept());
    }

    private boolean isFormValid() {
        return !enterURLTextField.getText().trim().isEmpty() && !savePathTextField.getText().trim().isEmpty();
    }

    public void onStart(Action onStart) {
        this.onStart = onStart;
    }

    public void onPause(Action onPause) {
        this.onPause = onPause;
    }

    public void onStop(Action onStop) {
        this.onStop = onStop;
    }

    public void parsingFinished(long time, Integer pageCount) {
        long minute = time / 60000;
        long seconds = time / 1000;
        textAreaInfo.setText("Пройдено страниц: " + pageCount + "\nВремя: " + minute + " минут(а) и " + seconds + " секунд(а)");
        textAreaInfo.repaint();
        startButton.setText("Start");
        enterURLTextField.setEnabled(true);
        savePathTextField.setEnabled(true);
        viewButton.setEnabled(true);
        JOptionPane.showMessageDialog(rootPanel, "Готово!", "", JOptionPane.INFORMATION_MESSAGE);
    }

    @FunctionalInterface
    interface Action {
        void accept();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public String getUrl() {
        return enterURLTextField.getText();
    }

    public String getPath() {
        return savePathTextField.getText();
    }
}
