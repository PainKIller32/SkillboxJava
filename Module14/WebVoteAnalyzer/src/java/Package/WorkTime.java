package Package;

import java.util.Date;
import java.util.TreeSet;

public class WorkTime {
    private TreeSet<TimePeriod> periods;

    public WorkTime() {
        periods = new TreeSet<>();
    }

    public void addVisitTime(Date visitTime) {
        TimePeriod newPeriod = new TimePeriod(visitTime, visitTime);
        for (TimePeriod period : periods) {
            if (period.compareTo(newPeriod) == 0) {
                period.appendTime(visitTime);
                return;
            }
        }
        periods.add(newPeriod);
    }

    public String toString() {
        StringBuilder line = new StringBuilder();
        for (TimePeriod period : periods) {
            if (line.length() > 0) {
                line.append(", ");
            }
            line.append(period);
        }
        return line.toString();
    }

    public TreeSet<TimePeriod> getPeriods() {
        return periods;
    }
}
