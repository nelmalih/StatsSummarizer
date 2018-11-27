import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class XMLParserUsingSAX extends XMLParserImp {

    private static XMLParserUsingSAX instance;

    public static XMLParserUsingSAX get() {
        return instance == null ? new XMLParserUsingSAX() : instance;
    }

    @Override
    public boolean buildResultFile() {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        boolean lRet=false;
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            StatsHandler handler = new StatsHandler();
            saxParser.parse(file, handler);

            return writeResultFile();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return lRet;
    }
}
