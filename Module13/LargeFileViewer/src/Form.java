import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileNotFoundException;

public class Form {
    private JPanel rootPanel;
    private JButton openButton;
    private JButton saveButton;
    private JScrollBar scrollBar;
    private JComboBox charsetsBox;
    private JButton closeButton;
    private JTextPane viewPane;
    private JPanel progressBarPanel;
    private Open onOpen;
    private Scroll onScroll;
    private ChangeCharset charset;
    private Edit onEdit;
    private Save onSave;
    private MouseWheelListener mouseWheelListener;
    private DocumentListener documentListener;
    private JProgressBar progressBar;
    private ChangeListener changeListener;
    private final int BUFFER_SIZE = 5_000;
    private final int SCROLL_RATIO = 1_000;

    public Form() {
        mouseWheelListener = e -> scrollBar.setValue(scrollBar.getValue() + e.getWheelRotation());

        changeListener = e -> {
            if (progressBar.getValue() == 100) {
                progressBarPanel.setVisible(false);
                viewPane.setEditable(true);
            }
        };

        documentListener = new DocumentListener() {
            private void run() {
                setText();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                onEdit.accept(e.getOffset(), e.getLength() * -1);
                if (viewPane.getText().length() < (BUFFER_SIZE / 2)) {
                    SwingUtilities.invokeLater(this::run);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onEdit.accept(e.getOffset(), e.getLength());
                if (viewPane.getText().length() < (BUFFER_SIZE / 2)) {
                    SwingUtilities.invokeLater(this::run);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        };

        scrollBar.setUnitIncrement(1);
        scrollBar.setEnabled(false);

        openButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(rootPanel);
            File file = fileChooser.getSelectedFile();
            if (file != null) {
                try {
                    viewPane.setEnabled(true);
                    onOpen.accept(file);
                    scrollBar.setEnabled(true);
                    scrollBar.setMaximum(((int) file.length() + (BUFFER_SIZE * 2)) / SCROLL_RATIO);
                    System.out.println("Файл: " + file.length());
                    System.out.println("Максимум: " + scrollBar.getMaximum());
                    scrollBar.addAdjustmentListener(es -> {
                        setText();
                    });

                    viewPane.addMouseWheelListener(mouseWheelListener);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                setText();
                closeButton.addActionListener(c -> {
                    int choose = JOptionPane.showConfirmDialog(rootPanel, "Сохранить изменения?", "", JOptionPane.YES_NO_OPTION);
                    if (choose != JOptionPane.CLOSED_OPTION) {
                        if (choose == JOptionPane.YES_OPTION) {
                            showProgressBar();
                        }
                        viewPane.setText("");
                        viewPane.setEnabled(false);
                        scrollBar.setEnabled(false);
                        viewPane.removeMouseWheelListener(mouseWheelListener);
                    }
                });
            }
        });
        charsetsBox.addActionListener(e -> {
            charset.accept((String) charsetsBox.getSelectedItem());
            setText();
        });
        saveButton.addActionListener(e -> showProgressBar());
    }

    private void showProgressBar() {
        viewPane.setEditable(false);
        progressBar = onSave.accept();
        progressBar.setPreferredSize(new Dimension(300, 10));
        progressBarPanel.add(progressBar);
        progressBar.addChangeListener(changeListener);
        progressBarPanel.setVisible(true);
    }

    private void setText() {
        viewPane.getDocument().removeDocumentListener(documentListener);
        viewPane.setText(onScroll.accept(scrollBar.getValue() * SCROLL_RATIO, viewPane.getDocument()).toString());
        viewPane.setCaretPosition(0);
        viewPane.repaint();
        viewPane.getDocument().addDocumentListener(documentListener);
    }

    public void onSave(Save onSave) {
        this.onSave = onSave;
    }

    public void onScroll(Scroll onScroll) {
        this.onScroll = onScroll;
    }

    public void onOpen(Open onOpen) {
        this.onOpen = onOpen;
    }

    public void onChangeCharset(ChangeCharset charset) {
        this.charset = charset;
    }

    public void onEdit(Edit onEdit) {
        this.onEdit = onEdit;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    @FunctionalInterface
    interface Open {
        void accept(File path) throws FileNotFoundException;
    }

    @FunctionalInterface
    interface Scroll {
        StringBuilder accept(int position, Document text);
    }

    @FunctionalInterface
    interface ChangeCharset {
        void accept(String charset);
    }

    @FunctionalInterface
    interface Edit {
        void accept(int start, int increment);
    }

    @FunctionalInterface
    interface Save {
        JProgressBar accept();
    }
}
