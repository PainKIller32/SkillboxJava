import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Handler extends DefaultHandler {
    private SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private HashMap<Integer, WorkTime> voteStationWorkTimes = new HashMap<>();
    private DBHandler dbHandler;

    public Handler(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("voter")) {
            String name = attributes.getValue("name");
            String birthDay = attributes.getValue("birthDay");
            try {
                dbHandler.countVoter(name, birthDay);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (qName.equals("visit")) {
            Integer station = Integer.parseInt(attributes.getValue("station"));
            Date time = null;
            try {
                time = visitDateFormat.parse(attributes.getValue("time"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            WorkTime workTime = voteStationWorkTimes.get(station);
            if (workTime == null) {
                workTime = new WorkTime();
                voteStationWorkTimes.put(station, workTime);
            }
            workTime.addVisitTime(time);
        }
    }

    @Override
    public void endDocument() {
        try {
            dbHandler.flushBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printResults() {
        System.out.println("Voting station work times: ");
        for (Integer votingStation : voteStationWorkTimes.keySet()) {
            System.out.println("\t" + votingStation + " - " + voteStationWorkTimes.get(votingStation));
        }

        System.out.println("Duplicated voters: ");
        try {
            dbHandler.printVoterCounts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
