import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {
    private ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private long accNumberCount = 1_000_000;

    public void registerAccount(String name, long money) {
        Account account = new Account(money, nextAccNumber());
        accounts.put(name, account);
    }

    private synchronized String nextAccNumber() {
        accNumberCount++;
        return Long.toString(accNumberCount);
    }

    private synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: ����������� �����. ����� ��������� ������ ����� �������.
     * ���� ����� ���������� > 50000, �� ����� ���������� ����������,
     * ��� ������������ �� �������� ������ ������������ � ����������
     * ����� isFraud. ���� ������������ true, �� �������� ����������
     * ������ (��� � �� ���� ����������)
     */
    public synchronized void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Account fromAccount = findAccount(fromAccountNum);
        Account toAccount = findAccount(toAccountNum);

        if (!fromAccount.isBlocked() && !toAccount.isBlocked() && fromAccount.getBalance() >= amount) {
            fromAccount.debit(amount);
            toAccount.credit(amount);
            if (amount > 50000 && isFraud(fromAccountNum, toAccountNum, amount)) {
                fromAccount.blocking();
                toAccount.blocking();
            }
        }
    }

    /**
     * TODO: ����������� �����. ���������� ������� �� �����.
     */
    public long getBalance(String accountNum) {
        return findAccount(accountNum).getBalance();
    }


    private Account findAccount(String accountNum) {
        Collection<Account> accountSet = accounts.values();
        Account foundAccount = null;
        for (Account account : accountSet) {
            if (account.getAccNumber().equals(accountNum)) {
                foundAccount = account;
                break;
            }
        }
        return foundAccount;
    }
}