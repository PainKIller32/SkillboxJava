import org.junit.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class BankTest {
    private final int NUM_OF_USERS = 10;
    private final int NUM_OF_THREADS = 10;
    private final int NUM_OF_TRANSACTION = 500;
    private final int MONEY_IN_ACCOUNT = 100000;
    private final int START_ACC_NUM_COUNTER = 1_000_000;
    private final Random random = new Random();
    private final Bank bank = new Bank();
    private AtomicInteger threadCounter = new AtomicInteger();

    @Test
    public void transactionTest() {
        for (int i = 0; i < NUM_OF_USERS; i++) {
            bank.registerAccount(Integer.toString(i), MONEY_IN_ACCOUNT);
        }

        ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            service.submit(() -> {
                threadCounter.incrementAndGet();
                for (int j = 0; j < NUM_OF_TRANSACTION; j++) {
                    System.out.println(j);
                    try {
                        bank.transfer(
                                Integer.toString(random.nextInt(NUM_OF_USERS + 1) + START_ACC_NUM_COUNTER),
                                Integer.toString(random.nextInt(NUM_OF_USERS + 1) + START_ACC_NUM_COUNTER),
                                random.nextInt(MONEY_IN_ACCOUNT)
                        );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        continue;
                    }
                }
                if (threadCounter.decrementAndGet() == 0) {
                    service.shutdown();
                }
            });
        }

        for (; ; ) {
            if (service.isShutdown()) {
                long totalMoneyAfterTransaction = 0;
                for (int i = 1; i <= NUM_OF_USERS; i++) {
                    totalMoneyAfterTransaction += bank.getBalance(Integer.toString(i + START_ACC_NUM_COUNTER));
                }
                long totalMoney = NUM_OF_USERS * MONEY_IN_ACCOUNT;
                Assert.assertEquals(totalMoney, totalMoneyAfterTransaction);
                break;
            }
        }
    }
}
