import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoaderCP {
    private static final int NUM_OF_THREADS = 10;
    private static final int NUM_OF_REGION = 90;

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        generateNum(NUM_OF_THREADS);

        System.out.println("Время параллельной генерации номеров для " + NUM_OF_REGION + " регионов в "
                + NUM_OF_THREADS + " потока(ов): " + (System.currentTimeMillis() - start) + " ms");

        start = System.currentTimeMillis();

        generateNum(1);

        System.out.println("Время последовательной генерации номеров для " + NUM_OF_REGION + " регионов: "
                + (System.currentTimeMillis() - start) + " ms");
    }

    private static void generateNum(int numOfThreads) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(numOfThreads);
        int part = NUM_OF_REGION / numOfThreads;

        for (int i = 0; i < numOfThreads; i++) {
            service.submit(new MyRunnable("res/numbers part" + (i + 1) + ".txt", part, i));
        }

        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
    }

    public static class MyRunnable implements Runnable {
        private String path;
        private int part;
        private int iteration;
        private StringBuilder builder = new StringBuilder();
        private StringBuilder buffer = new StringBuilder();
        private StringBuilder writer = new StringBuilder();
        private final Object lock = new Object();

        public MyRunnable(String path, int part, int iteration) {
            this.path = path;
            this.part = part;
            this.iteration = iteration;
        }

        @Override
        public void run() {
            try (FileOutputStream fileWriter = new FileOutputStream(path)) {
                fileWriter.getChannel().force(true);

                int bufferSize = 1_000_000;
                int writeBufferSize = 3_000_000;

                Thread generateThread = new Thread(() -> {
                    char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
                    for (int number = 1; number < 1000; number++) {
                        for (int regionCode = (1 + (part * iteration)); regionCode < (part + (part * iteration)); regionCode++) {
                            for (char firstLetter : letters) {
                                for (char secondLetter : letters) {
                                    for (char thirdLetter : letters) {
                                        if (builder.length() > bufferSize) {
                                            synchronized (lock) {
                                                while (buffer.length() > writeBufferSize) {
                                                    try {
                                                        lock.wait();
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                buffer.append(builder);
                                            }
                                            builder = new StringBuilder();
                                        }
                                        builder.append(firstLetter);
                                        if (number < 10) {
                                            builder.append("00");
                                        } else if (number < 100) {
                                            builder.append("0");
                                        }
                                        builder.append(number)
                                                .append(secondLetter)
                                                .append(thirdLetter);
                                        if (regionCode < 10) {
                                            builder.append("0");
                                        }
                                        builder.append(regionCode)
                                                .append("\n");
                                    }
                                }
                            }
                        }
                    }
                    buffer.append(builder);
                });
                generateThread.start();

                while (generateThread.isAlive()) {
                    if (buffer.length() > bufferSize) {
                        synchronized (lock) {
                            writer = buffer;
                            buffer = new StringBuilder();
                            lock.notify();
                        }
                        fileWriter.write(writer.toString().getBytes());
                        writer = new StringBuilder();
                    }
                }
                generateThread.join();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
