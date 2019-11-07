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
    private EventHandler eventHandler;

    public Processor(String url, Set<String> urls, EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        stop = false;
        this.url = url;
        this.urls = urls;
        eventHandler.onThreadCreated();
        this.start();
    }

    @Override
    public void run() {
        try {
            sleep((long) (Math.random() * 3000));
            Document doc = Jsoup.connect(url).maxBodySize(0).get();
            Elements elements = doc.getElementsByAttributeValueContaining("abs:href", url);
            for (Element element : elements) {
                synchronized (Processor.class) {
                    while (pause) {
                        Processor.class.wait();
                    }
                    if (stop) {
                        break;
                    }
                }
                String addedElement = element.attr("abs:href");
                if (!urls.contains(addedElement)) {
                    urls.add(addedElement);
                    if (addedElement.endsWith("/")) {
                        createNewThread(addedElement);
                    }
                }
            }
            eventHandler.onThreadFinished();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            eventHandler.onThreadFinished();
        }
    }

    private void createNewThread(String url) {
        new Processor(url, urls, eventHandler);
    }

    public static boolean isPaused() {
        return pause;
    }

    public static void finish() {
        stop = true;
        synchronized (Processor.class) {
            if (pause) {
                pause = false;
                Processor.class.notifyAll();
            }
        }
    }

    public static void proceed() {
        synchronized (Processor.class) {
            pause = false;
            Processor.class.notifyAll();
        }
    }

    public synchronized static void pause() {
        pause = true;
    }

    interface EventHandler {
        void onThreadCreated();

        void onThreadFinished();
    }
}