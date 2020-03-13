import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Updater extends Thread {
    private JPanel list;
    private final static int MINUTE_IN_HOUR = 60;

    public Updater(JPanel list) {
        this.list = list;
        this.start();
    }

    @Override
    public void run() {
        for (; ; ) {
            try {
                for (Component component : list.getComponents()) {
                    if (component.isVisible()) {
                        Contact contact = (Contact) component;
                        for (Message message : contact.getDialog().getMessages()) {
                            message.setSendTime(updateSendTime(message.getDate()));
                        }
                    }
                }
                sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized static String updateSendTime(long date) {
        date *= 1000;
        String sendTime = "";
        SimpleDateFormat oldMessages = new SimpleDateFormat("dd.MM.yyyy в HH:mm");
        SimpleDateFormat yesterdayMessages = new SimpleDateFormat("вчера в HH:mm");
        Calendar currentDate = new GregorianCalendar();
        Calendar sendDate = new GregorianCalendar();
        sendDate.setTimeInMillis(date);
        int dayInterval = currentDate.get(Calendar.DAY_OF_YEAR) - sendDate.get(Calendar.DAY_OF_YEAR);

        if (dayInterval < 1) {
            int hourInterval = currentDate.get(Calendar.HOUR_OF_DAY) - sendDate.get(Calendar.HOUR_OF_DAY);
            int minutePassed;
            boolean inHourInterval = true;

            if (hourInterval == 1) {
                minutePassed = MINUTE_IN_HOUR - sendDate.get(Calendar.MINUTE) + currentDate.get(Calendar.MINUTE);
                if (minutePassed > MINUTE_IN_HOUR) {
                    inHourInterval = false;
                }
            }

            if (hourInterval <= 1 && inHourInterval) {
                minutePassed = Math.abs(currentDate.get(Calendar.MINUTE) - sendDate.get(Calendar.MINUTE));
                if (minutePassed != 0) {
                    if (minutePassed > 10 && minutePassed < 20) {
                        sendTime = minutePassed + " минут назад";
                    } else if (minutePassed % 10 == 1) {
                        sendTime = minutePassed + " минуту назад";
                    } else if (minutePassed % 10 > 1 && minutePassed % 10 < 5) {
                        sendTime = minutePassed + " минуты назад";
                    } else {
                        sendTime = minutePassed + " минут назад";
                    }
                }
            } else {
                if (hourInterval > 10 && hourInterval < 20) {
                    sendTime = hourInterval + " часов назад";
                } else if (hourInterval % 10 == 1) {
                    sendTime = hourInterval + " час назад";
                } else if (hourInterval % 10 > 1 && hourInterval % 10 < 5) {
                    sendTime = hourInterval + " часа назад";
                } else {
                    sendTime = hourInterval + " часов назад";
                }
            }
        } else if (dayInterval == 1) {
            sendTime = yesterdayMessages.format(sendDate.getTime());
        } else {
            sendTime = oldMessages.format(sendDate.getTime());
        }
        return sendTime;
    }
}
