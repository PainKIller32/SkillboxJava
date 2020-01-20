package Package;

import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.HashMap;

public class Loader {
    public HashMap<Integer, WorkTime> parseFile() throws Exception {
        try (InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("data-0.2M.xml")) {
            Handler handler = new Handler();
            if (resourceAsStream != null) {
                SAXParserFactory.newInstance().newSAXParser().parse(resourceAsStream, handler);
            }
            return handler.getResults();
        }
    }
}