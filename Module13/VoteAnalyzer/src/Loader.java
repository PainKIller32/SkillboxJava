import javax.xml.parsers.SAXParserFactory;
import java.io.File;

/**
 * Created by Danya on 24.02.2016.
 */
public class Loader {

    public static void main(String[] args) throws Exception {
        String fileName = "res/data-1572M.xml";

        long start = System.currentTimeMillis();
        parseFile(fileName);
        System.out.println((System.currentTimeMillis() - start) + " ms");
    }

    private static void parseFile(String fileName) throws Exception {
        DBHandler dbHandler = new DBHandler("learn", "root", "mmm333");
        Handler handler = new Handler(dbHandler);
        SAXParserFactory.newInstance().newSAXParser().parse(new File(fileName), handler);
        handler.printResults();
    }
}