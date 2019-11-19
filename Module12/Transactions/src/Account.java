public class Account {
    private long money;
    private final String accNumber;
    private boolean blocked;
    private final Object lock = new Object();

    public Account(long money, String accNumber) {
        blocked = false;
        this.money = money;
        this.accNumber = accNumber;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void debit(long money) {
        this.money -= money;
    }

    public void credit(long money) {
        this.money += money;
    }

    public long getBalance() {
        synchronized (lock) {
            return money;
        }
    }

    public synchronized boolean isBlocked() {
        return blocked;
    }

    public synchronized void blocking() {
        blocked = true;
    }

    public synchronized void unblocking() {
        blocked = false;
    }

    public synchronized Object getLock() {
        return lock;
    }
}
