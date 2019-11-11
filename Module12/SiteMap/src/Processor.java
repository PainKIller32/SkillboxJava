import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Processor extends Thread {
    private String url;
    private Set<String> urls;
    private Integer threadRunningCounter;
    private static boolean stop;
    private static boolean pause;
    private static Finished finish;
    private static ExecutorService service;
    private final int NUMBER_OF_THREADS;

    public Processor(String url, Set<String> urls, int numberOfThreads) {
        NUMBER_OF_THREADS = numberOfThreads;
        threadRunningCounter = 0;
        pause = false;
        stop = false;
        this.url = url;
        this.urls = urls;
        this.start();
    }

    @Override
    public void run() {
        service = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        service.submit(createNewThread(url));
    }

    private Runnable createNewThread(String url) {
        return () -> {
            try {
                synchronized (Processor.class) {
                    threadRunningCounter++;
                }
                System.out.println(Thread.currentThread().getName());
                sleep((long) (Math.random() * 2000));
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
                            service.submit(createNewThread(addedElement));
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                synchronized (Processor.class) {
                    threadRunningCounter--;
                }
                if (threadRunningCounter == 0) {
                    finish.accept();
                    service.shutdown();
                }
            }
        };
    }

    public static void onParsingFinished(Finished finished) {
        finish = finished;
    }

    public static boolean isPaused() {
        return pause;
    }

    public static void shutdown() {
        stop = true;
        synchronized (Processor.class) {
            if (pause) {
                pause = false;
                Processor.class.notifyAll();
            }
        }
        service.shutdownNow();
    }

    public static void proceed() {
        synchronized (Processor.class) {
            pause = false;
            Processor.class.notifyAll();
        }
    }

    public static void pause() {
        pause = true;
    }

    @FunctionalInterface
    interface Finished {
        void accept();
    }
}