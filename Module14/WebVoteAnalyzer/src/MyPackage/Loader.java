package MyPackage;

import javax.xml.parsers.SAXParserFactory;
import java.util.HashMap;

public class Loader {
    public HashMap<Integer, WorkTime> parseFile() throws Exception {
        Handler handler = new Handler();
        SAXParserFactory.newInstance().newSAXParser().parse(getClass().getResourceAsStream("res/data-0.2M.xml"), handler);
        return handler.getResults();
    }
}