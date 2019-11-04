import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Set;

public class Processor extends Thread {
    private String url;
    private Set<String> urls;
    private static Boolean stop;
    private static Boolean pause = false;

    public Processor(String url, Set<String> urls) {
        stop = false;
        this.url = url;
        this.urls = urls;
        Loader.onThreadCreated();
    }

    @Override
    public void run() {
        try {
            sleep((long) (Math.random() * 10000));
            Document doc = Jsoup.connect(url).maxBodySize(0).get();
            Elements elements = doc.getElementsByAttributeValueContaining("abs:href", url);
            for (Element element : elements) {
                synchronized (Processor.class) {
                    while (pause) Processor.class.wait();
                    if (stop) break;
                }
                String addedElement = element.attr("abs:href");
                if (!urls.contains(addedElement)) {
                    urls.add(addedElement);
                    if (addedElement.endsWith("/"))
                        Loader.searchUrl(addedElement);
                }
            }
            Loader.onThreadFinished();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isPaused() {
        return pause;
    }

    public static void finished() {
        stop = true;
    }

    public static void started() {
        synchronized (Processor.class) {
            pause = false;
            Processor.class.notifyAll();
        }
    }

    public static void paused() {
        pause = true;
    }
}