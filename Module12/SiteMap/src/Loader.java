import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class Loader {
    private static Set<String> urls;
    private static Form form;
    private static long start = 0;
    private static long end = 0;

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

            Processor processor = new Processor();

            form.onStart(() -> {
                if (processor.isPaused()) {
                    processor.proceed();
                } else {
                    urls.clear();
                    start = System.currentTimeMillis();
                    processor.start(form.getUrl(), urls, 100);
                }
            });

            processor.onParsingFinished(() -> {
                writeInFile(form.getPath());
                end = System.currentTimeMillis() - start;
                start = 0;
                SwingUtilities.invokeLater(() -> form.parsingFinished(end, urls.size()));
            });

            form.onStop(() -> {
                if (start != 0) {
                    processor.shutdown();
                }
            });

            form.onPause(processor::pause);
        });
    }

    private static void writeInFile(String savePath) {
        try (FileWriter fw = new FileWriter(savePath)) {
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
}
