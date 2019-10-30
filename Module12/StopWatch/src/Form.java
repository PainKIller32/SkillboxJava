import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

/**
 * Created by Danya on 28.10.2015.
 */
public class Form {
    private JPanel rootPanel;
    private JPanel buttons;
    private JButton startButton;
    private JButton StopButton;
    private JLabel Hours;
    private JLabel Minutes;
    private JLabel Seconds;
    private JLabel MilliSeconds;
    private JPanel time;
    private Processor processor;
    private Timer timer;
    private long millisec = 0;

    public Form() {
        Form form = this;
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startButton.getText().equals("Pause!")) {
                    millisec = processor.getMillisec();
                    timer.cancel();
                    startButton.setText("Start!");
                } else {
                    startButton.setText("Pause!");
                    processor = new Processor(form);
                    timer = new Timer();
                    timer.scheduleAtFixedRate(processor, 0, 1);
                }
            }
        });
        StopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.cancel();
                startButton.setText("Start!");
                Hours.setText("00:");
                Minutes.setText("00:");
                Seconds.setText("00");
                MilliSeconds.setText("000");
                millisec = 0;
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public long getMillisec() {
        return millisec;
    }

    public JPanel getTime() {
        return time;
    }

    public JLabel getHours() {
        return Hours;
    }

    public JLabel getMinutes() {
        return Minutes;
    }

    public JLabel getSeconds() {
        return Seconds;
    }

    public JLabel getMilliSeconds() {
        return MilliSeconds;
    }
}
