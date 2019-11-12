public class Account {
    private long money;
    private String accNumber;
    private boolean blocked;

    public Account(long money, String accNumber){
        blocked = false;
        this.money = money;
        this.accNumber = accNumber;
    }

    public synchronized void debit(long money) {
        this.money -= money;
    }

    public synchronized void credit(long money) {
        this.money += money;
    }

    public synchronized long getBalance() {
        return money;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public synchronized boolean isBlocked() {
        return blocked;
    }

    public synchronized void blocking(){
        blocked = true;
    }

    public void unblocking(){
        blocked = false;
    }
}
