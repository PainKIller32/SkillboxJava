import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Danya on 24.02.2016.
 */
public class TimePeriod implements Comparable<TimePeriod> {
    private Date from;
    private Date to;
    private static final long MILLISECONDS_PER_DAY = 86_400_000;

    public TimePeriod(long fromLong, long toLong) throws ParseException {
        SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date from = visitDateFormat.parse(visitDateFormat.format(fromLong));
        Date to = visitDateFormat.parse(visitDateFormat.format(toLong));
        if (from.compareTo(to) != 0) {
            throw new IllegalArgumentException("Dates 'from' and 'to' must be within ONE day!");
        } else {
            this.from = from;
            this.to = to;
        }
    }

    public TimePeriod(Date from, Date to) {
        if (from.compareTo(to) != 0) {
            throw new IllegalArgumentException("Dates 'from' and 'to' must be within ONE day!");
        } else {
            this.from = from;
            this.to = to;
        }
    }

    public void appendTime(Date visitTime) {
        if (visitTime.before(from)) {
            from = visitTime;
        }
        if (visitTime.after(to)) {
            to = visitTime;
        }
    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String from = dateFormat.format(this.from);
        String to = timeFormat.format(this.to);
        return from + "-" + to;
    }

    @Override
    public int compareTo(TimePeriod period) {
        Date current = new Date(from.getTime() / MILLISECONDS_PER_DAY);
        Date compared = new Date(period.from.getTime() / MILLISECONDS_PER_DAY);
        return current.compareTo(compared);
    }
}
