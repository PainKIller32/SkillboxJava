import javax.swing.*;
import java.text.DecimalFormat;
import java.util.TimerTask;

public class Processor extends TimerTask {
    private JLabel hours;
    private JLabel minutes;
    private JLabel seconds;
    private JLabel milliSeconds;
    private JPanel time;
    private long millisec;

    public Processor(Form form) {
        this.hours = form.getHours();
        this.minutes = form.getMinutes();
        this.seconds = form.getSeconds();
        this.milliSeconds = form.getMilliSeconds();
        this.time = form.getTime();
        this.millisec = form.getMillisec();
    }

    @Override
    public void run() {
        DecimalFormat millisecFormat = new DecimalFormat("000");
        DecimalFormat secMinHourFormat = new DecimalFormat("00");
        millisec++;
        milliSeconds.setText(millisecFormat.format((float) millisec % 1000));
        seconds.setText(secMinHourFormat.format(millisec / 1000));
        minutes.setText(secMinHourFormat.format(millisec / 60000) + ":");
        hours.setText(secMinHourFormat.format(millisec / 3600000) + ":");
        time.repaint();
    }

    public long getMillisec() {
        return millisec;
    }
}
