import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Loader {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        String pathWithThread1 = "res/numbers part1.txt";
        String pathWithThread2 = "res/numbers part2.txt";
        int startRegionThread1 = 0;
        int startRegionThread2 = 50;

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new MyRunnable(pathWithThread1, startRegionThread1));
        service.submit(new MyRunnable(pathWithThread2, startRegionThread2));

        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);

        System.out.println((System.currentTimeMillis() - start) + " ms");
    }

    public static class MyRunnable implements Runnable {
        private String path;
        private int startRegion;
        private PrintWriter writer;

        public MyRunnable(String path, int startRegion) {
            this.path = path;
            this.startRegion = startRegion;
        }

        @Override
        public void run() {
            try {
                writer = new PrintWriter(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            StringBuilder builder = new StringBuilder();
            int bufferSize = 1_000_000;

            char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
            for (int number = 1; number < 1000; number++) {
                for (int regionCode = (1 + startRegion); regionCode < (50 + startRegion); regionCode++) {
                    for (char firstLetter : letters) {
                        for (char secondLetter : letters) {
                            for (char thirdLetter : letters) {
                                if (builder.length() > bufferSize) {
                                    writer.write(builder.toString());
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
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        }
    }
}
