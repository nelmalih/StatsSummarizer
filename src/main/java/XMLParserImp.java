import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public abstract class XMLParserImp implements XMLParser {

    private String INPUT_FILE_PATH;
    private String OUT_FILE_PATH;

    static Document dom;
    static File file;

    static Stats stats;

    public Document getDom() {
        return dom;
    }

    public static Stats getStats() {
        return stats;
    }

    static void setStats(Stats aInStats) {
        stats = aInStats;
    }

    public void initInputFile(String aInInputFilePath, String aInOutputFilePath) {
        INPUT_FILE_PATH = aInInputFilePath;
        OUT_FILE_PATH = aInOutputFilePath;
        file = new File(INPUT_FILE_PATH);
    }

    public boolean initDOM() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        boolean lRest = false;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(file);
            lRest = true;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lRest;
    }

    public boolean buildResultFile() {
        return false;
    }

    protected boolean writeResultFile() {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        boolean lRest = false;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // root element
        Element root = dom.createElement(Constants.STATS_TAG);
        dom.appendChild(root);

        // set attributes
        addAttribute(root, Constants.PERSONS_TAG, stats.getNbOfPerson());
        addAttribute(root, Constants.JOBLESS_TAG, stats.getNbOfJobless());
        addAttribute(root, Constants.MARRIED_TAG, stats.getNbOfMarried());
        addAttribute(root, Constants.SINGLE_TAG, stats.getNbOfSingle());
        addAttribute(root, Constants.AVERAGE_AGE_TAG, stats.getAverageAge());
        addAttribute(root, Constants.NB_OF_PERSON_ABOVE_AVERAGE_AGE_TAG, stats.getNbOfPersonAboveAverageAge());
        addAttribute(root, Constants.AVERAGE_SALARY_TAG, stats.getAverageSalary());
        addAttribute(root, Constants.AVERAGE_SALARY_ADJUSTED_TAG, stats.getAverageSalaryAdjusted());
        addAttribute(root, Constants.TOTAL_NUMBER_OF_CHILDREN_TAG, stats.getTotalNbOfChildren());

        // create the xml file
        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        DOMSource domSource = new DOMSource(dom);
        StreamResult streamResult = new StreamResult(new File(OUT_FILE_PATH));

        try {
            transformer.transform(domSource, streamResult);
            lRest = true;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return lRest;
    }

    private void addAttribute(Element aInElement, String aInName, int aInValue) {
        Element element = dom.createElement(aInName);
        element.appendChild(dom.createTextNode(String.valueOf(aInValue)));
        aInElement.appendChild(element);
    }
}

