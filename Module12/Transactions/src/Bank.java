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

    private synchronized boolean isFraud(Account fromAccountNum, Account toAccountNum, long amount)
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
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Account fromAccount = findAccountByNum(fromAccountNum);
        Account toAccount = findAccountByNum(toAccountNum);
        if (fromAccountNum.compareTo(toAccountNum) > 0) {
            synchronized (fromAccount.getLock()) {
                synchronized (toAccount.getLock()) {
                    doTransaction(fromAccount, toAccount, amount);
                }
            }
        } else {
            synchronized (toAccount.getLock()) {
                synchronized (fromAccount.getLock()) {
                    doTransaction(fromAccount, toAccount, amount);
                }
            }
        }
    }

    private void doTransaction(Account fromAccount, Account toAccount, long amount) throws InterruptedException {
        if (!fromAccount.isBlocked() && !toAccount.isBlocked() && fromAccount.getBalance() >= amount) {
            fromAccount.debit(amount);
            toAccount.credit(amount);
            if (amount > 50000 && isFraud(fromAccount, toAccount, amount)) {
                fromAccount.blocking();
                toAccount.blocking();
            }
        }
    }

    /**
     * TODO: ����������� �����. ���������� ������� �� �����.
     */
    public long getBalance(String accountNum) {
        return findAccountByNum(accountNum).getBalance();
    }


    private Account findAccountByNum(String accountNum) {
        Account foundAccount = null;
        for (Account account : accounts.values()) {
            if (account.getAccNumber().equals(accountNum)) {
                foundAccount = account;
                break;
            }
        }
        return foundAccount;
    }

    private Account findAccountByName(String name) {
        return accounts.get(name);
    }
}
