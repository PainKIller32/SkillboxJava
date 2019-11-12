import org.junit.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankTest {
    private final int NUM_OF_USERS = 10;
    private final int NUM_OF_THREADS = 100;
    private final int NUM_OF_TRANSACTION = 10_000;
    private final int MONEY_IN_ACCOUNT = 100_000;
    private final int START_ACC_NUM_COUNTER = 1_000_000;
    private final Random random = new Random();

    @Test
    public void transactionTest() {
        Bank bank = new Bank();

        for (int i = 0; i < NUM_OF_USERS; i++) {
            bank.registerAccount(Integer.toString(i), MONEY_IN_ACCOUNT);
        }

        ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            service.submit(() -> {
                for (int j = 0; j < NUM_OF_TRANSACTION; j++) {
                    try {
                        bank.transfer(
                                Integer.toString(random.nextInt(NUM_OF_USERS) + START_ACC_NUM_COUNTER),
                                Integer.toString(random.nextInt(NUM_OF_USERS) + START_ACC_NUM_COUNTER),
                                random.nextInt(MONEY_IN_ACCOUNT)
                        );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        service.shutdown();

        for (; ; ) {
            if (service.isShutdown()) {
                break;
            }
        }

        long totalMoneyAfterTransaction = 0;
        for (int i = 1; i <= NUM_OF_USERS; i++) {
            totalMoneyAfterTransaction += bank.getBalance(Integer.toString(i + START_ACC_NUM_COUNTER));
        }

        long totalMoney = NUM_OF_USERS * MONEY_IN_ACCOUNT;
        Assert.assertEquals(totalMoney, totalMoneyAfterTransaction);
    }
}
