package MyPackage;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Handler extends DefaultHandler {
    private SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private HashMap<Integer, WorkTime> voteStationWorkTimes = new HashMap<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("visit")) {
            Integer station = Integer.parseInt(attributes.getValue("station"));
            Date time;
            try {
                time = visitDateFormat.parse(attributes.getValue("time"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            WorkTime workTime = voteStationWorkTimes.get(station);
            if (workTime == null) {
                workTime = new WorkTime();
                voteStationWorkTimes.put(station, workTime);
            }
            workTime.addVisitTime(time);
        }
    }

    public HashMap<Integer, WorkTime> getResults() {
        return voteStationWorkTimes;
    }
}
