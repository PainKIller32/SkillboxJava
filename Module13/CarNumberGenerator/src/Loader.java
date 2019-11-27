//import java.io.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//public class Loader {
//    private static final int NUM_OF_THREADS = 3;
//    private static final int NUM_OF_REGION = 90;
//
//    public static void main(String[] args) throws Exception {
//        long start = System.currentTimeMillis();
//
//        generateNum(NUM_OF_THREADS);
//
//        System.out.println("Время параллельной генерации номеров для " + NUM_OF_REGION + " регионов в "
//                + NUM_OF_THREADS + " потока(ов): " + (System.currentTimeMillis() - start) + " ms");
//
//        start = System.currentTimeMillis();
//
//        generateNum(1);
//
//        System.out.println("Время последовательной генерации номеров для " + NUM_OF_REGION + " регионов: "
//                + (System.currentTimeMillis() - start) + " ms");
//    }
//
//    private static void generateNum(int numOfThreads) throws InterruptedException {
//        ExecutorService service = Executors.newFixedThreadPool(numOfThreads);
//        int part = NUM_OF_REGION / numOfThreads;
//
//        for (int i = 0; i < numOfThreads; i++) {
//            service.submit(new MyRunnable("res/numbers part" + (i + 1) + ".txt", part, i));
//        }
//
//        service.shutdown();
//        service.awaitTermination(1, TimeUnit.HOURS);
//    }
//
//    public static class MyRunnable implements Runnable {
//        private String path;
//        private int part;
//        private int iteration;
//
//        public MyRunnable(String path, int part, int iteration) {
//            this.path = path;
//            this.part = part;
//            this.iteration = iteration;
//        }
//
//        @Override
//        public void run() {
//            //try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
//            try (FileOutputStream writer = new FileOutputStream(path)) {
//                writer.getChannel().force(true);
//
//                StringBuilder builder = new StringBuilder();
//                int bufferSize = 1_000_000;
//
//                char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
//                for (int number = 1; number < 1000; number++) {
//                    for (int regionCode = (1 + (part * iteration)); regionCode < (part + (part * iteration)); regionCode++) {
//                        for (char firstLetter : letters) {
//                            for (char secondLetter : letters) {
//                                for (char thirdLetter : letters) {
//                                    if (builder.length() > bufferSize) {
//                                        //bufferedWriter.append(builder);
//                                        writer.write(builder.toString().getBytes());
//                                        builder = new StringBuilder();
//                                    }
//
//                                    builder.append(firstLetter);
//                                    if (number < 10) {
//                                        builder.append("00");
//                                    } else if (number < 100) {
//                                        builder.append("0");
//                                    }
//                                    builder.append(number)
//                                            .append(secondLetter)
//                                            .append(thirdLetter);
//                                    if (regionCode < 10) {
//                                        builder.append("0");
//                                    }
//                                    builder.append(regionCode)
//                                            .append("\n");
//
////                                    bufferedWriter.append(firstLetter);
////                                    if (number < 10) {
////                                        bufferedWriter.append("00");
////                                    } else if (number < 100) {
////                                        bufferedWriter.append("0");
////                                    }
////                                    bufferedWriter.append(Integer.toString(number))
////                                            .append(secondLetter)
////                                            .append(thirdLetter);
////                                    if (regionCode < 10) {
////                                        bufferedWriter.append("0");
////                                    }
////                                    bufferedWriter.append(Integer.toString(regionCode))
////                                            .append("\n");
//                                }
//                            }
//                        }
//                    }
//                }
//                //bufferedWriter.append(builder);
//                writer.write(builder.toString().getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.exit(1);
//            }
//        }
//    }
//}