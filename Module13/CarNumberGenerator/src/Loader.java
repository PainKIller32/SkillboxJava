import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Loader {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        String pathWithThread1 = "res/numbers part1.txt";
        String pathWithThread2 = "res/numbers part2.txt";
        int startRegionThread1 = 0;
        int startRegionThread2 = 75;

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
        private FileOutputStream writer;

        public MyRunnable(String path, int startRegion) {
            this.path = path;
            this.startRegion = startRegion;
        }

        @Override
        public void run() {
            try {
                writer = new FileOutputStream(path);
                writer.getChannel().force(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            StringBuilder builder = new StringBuilder();
            int bufferSize = 1_000_000;

            char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
            for (int number = 1; number < 1000; number++) {
                for (int regionCode = (1 + startRegion); regionCode < (75 + startRegion); regionCode++) {
                    for (char firstLetter : letters) {
                        for (char secondLetter : letters) {
                            for (char thirdLetter : letters) {
                                if (builder.length() > bufferSize) {
                                    try {
                                        writer.write(builder.toString().getBytes());
                                    } catch (IOException e) {
                                        e.printStackTrace();
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
            try {
                writer.write(builder.toString().getBytes());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
