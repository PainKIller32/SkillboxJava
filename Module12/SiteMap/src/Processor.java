import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Processor {
    private Set<String> urls;
    private AtomicInteger threadRunningCounter = new AtomicInteger();
    private boolean pause;
    private Finished finish;
    private ExecutorService service;

    public void start(String url, Set<String> urls, int numberOfThreads) {
        pause = false;
        this.urls = urls;
        service = Executors.newFixedThreadPool(numberOfThreads);
        service.submit(createNewThread(url));
    }

    private Runnable createNewThread(String url) {
        return () -> {
            try {
                threadRunningCounter.incrementAndGet();
                System.out.println(Thread.currentThread().getName());
                Thread.sleep((long) (Math.random() * 3000));
                Document doc = Jsoup.connect(url).maxBodySize(0).get();
                Elements elements = doc.getElementsByAttributeValueContaining("abs:href", url);
                for (Element element : elements) {
                    synchronized (this) {
                        while (pause) {
                            this.wait();
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
                threadRunningCounter.decrementAndGet();
                if (threadRunningCounter.get() == 0) {
                    finish.accept();
                    service.shutdown();
                }
            }
        };
    }

    public void onParsingFinished(Finished finished) {
        finish = finished;
    }

    public boolean isPaused() {
        return pause;
    }

    public void shutdown() {
        proceed();
        service.shutdownNow();
    }

    public synchronized void proceed() {
        pause = false;
        this.notifyAll();
    }

    public void pause() {
        pause = true;
    }

    @FunctionalInterface
    interface Finished {
        void accept();
    }
}