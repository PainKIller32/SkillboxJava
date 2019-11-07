import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class Loader {
    private static Set<String> urls;
    private static Integer threadRunningCounter = 0;
    private static Form form;
    private static long start = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            urls = ConcurrentHashMap.newKeySet();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame();

            form = new Form();
            frame.setContentPane(form.getRootPanel());

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setTitle("SiteMap");

            frame.setSize(600, 200);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            form.addStartActionListener(e -> {
                if (form.getTextOnStartButton().equals("Pause")) {
                    form.setTextOnStartButton("Start");
                    Processor.pause();
                } else if (form.getUrl().trim().isEmpty() || form.getSavePath().trim().isEmpty()) {
                    form.showWarningMassage();
                } else {
                    form.setTextOnStartButton("Pause");
                    form.disabledTextFields();
                    form.disabledViewButton();
                    if (Processor.isPaused()) {
                        Processor.proceed();
                    } else {
                        start = System.currentTimeMillis();
                        form.setInfoTextArea(0, 0);
                        new Processor(form.getUrl(), urls, new Processor.EventHandler() {
                            @Override
                            public synchronized void onThreadCreated() {
                                threadRunningCounter++;
                            }

                            @Override
                            public synchronized void onThreadFinished() {
                                threadRunningCounter--;
                                if (threadRunningCounter == 0) {
                                    writeInFile();
                                    form.setInfoTextArea(System.currentTimeMillis() - start, urls.size());
                                    setInitialParameters();
                                    form.showCompleteMassage();
                                }
                            }
                        });
                    }
                }
            });

            form.addStopActionListener(e -> {
                if (start != 0) {
                    Processor.finish();
                }
            });
        });
    }

    private static void writeInFile() {
        try (FileWriter fw = new FileWriter(form.getSavePath())) {
            TreeSet<String> printSet = new TreeSet<>(urls);
            for (String strings : printSet) {
                int tab = strings.replaceAll("[^/]", "").length();
                for (int i = 3; i < tab; i++) {
                    fw.write("\t");
                }
                fw.write(strings + "\n");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static void setInitialParameters() {
        form.setTextOnStartButton("Start");
        form.enabledTextFields();
        form.enabledViewButton();
        urls.clear();
        start = 0;
    }
}
