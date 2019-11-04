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
    private static Long start;

    public static void main(String[] args) {
        start = System.currentTimeMillis();
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
    }

    public static void searchUrl(String url) {
        Processor processor = new Processor(url, urls);
        processor.start();
    }

    public synchronized static void onThreadCreated() {
        threadRunningCounter++;
        form.setInfoTextArea();
    }

    public synchronized static void onThreadFinished() throws IOException {
        threadRunningCounter--;
        if (threadRunningCounter == 0) {
            FileWriter fw = new FileWriter(form.getSavePath());
            TreeSet<String> printSet = new TreeSet<>(urls);
            for (String strings : printSet) {
                int tab = strings.replaceAll("[^/]", "").length();
                for (int i = 3; i < tab; i++) {
                    fw.write("\t");
                }
                fw.write(strings + "\n");
            }
            fw.flush();
            fw.close();
            form.setInfoTextArea(System.currentTimeMillis() - start);
            form.onFinished();
            urls.clear();
            JOptionPane.showMessageDialog(form.getRootPanel(), "Готово!", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
