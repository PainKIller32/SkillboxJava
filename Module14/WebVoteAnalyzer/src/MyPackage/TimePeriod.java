package MyPackage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimePeriod implements Comparable<TimePeriod> {
    private Date from;
    private Date to;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
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
        return getDay() + " " + getTime();
    }

    public String getDay() {
        return dateFormat.format(this.from);
    }

    public String getTime() {
        return timeFormat.format(this.from) + " - " + timeFormat.format(this.to);
    }

    @Override
    public int compareTo(TimePeriod period) {
        Date current = new Date(from.getTime() / MILLISECONDS_PER_DAY);
        Date compared = new Date(period.from.getTime() / MILLISECONDS_PER_DAY);
        return current.compareTo(compared);
    }
}
