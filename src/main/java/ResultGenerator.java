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
import java.util.HashMap;
import java.util.Map;

public class ResultGenerator {

    private final static String INPUT_FILE_PATH = System.getProperty("user.dir") +
            "\\src\\main\\resources\\inputs\\$.xml";
    private final static String OUTPUT_FILE_PATH = System.getProperty("user.dir") +
            "\\src\\main\\resources\\results\\$.xml";

    static private ResultGenerator instance;

    private static String filePath;
    private static File file;

    private static Document dom;
    private static Stats stats;

    public static void setStats(Stats stats) {
        ResultGenerator.stats = stats;
    }

    private ResultGenerator() {
    }

    public static ResultGenerator get() {
        return instance == null ? new ResultGenerator() : instance;
    }

    public static Document getDom() {
        return dom;
    }

    public static boolean init(String aInInputFileName) {
        filePath = INPUT_FILE_PATH.replace("$", aInInputFileName);
        file = new File(filePath);

        return initDOM();
    }

    private static boolean initDOM() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        boolean lRest = false;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(filePath);
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

    public boolean buildResultFileUsingDOM(String aInOutputFileName) {
        dom.getDocumentElement().normalize();
        NodeList nodeList = dom.getElementsByTagName(Constants.PERSON_TAG);

        Map<String, Integer> lMap = computeIndicators(nodeList);
        stats = new Stats(nodeList.getLength(),
                lMap.get(Constants.JOBLESS_TAG),
                lMap.get(Constants.MARRIED_TAG),
                lMap.get(Constants.SINGLE_TAG),
                lMap.get(Constants.AVERAGE_AGE_TAG),
                lMap.get(Constants.NB_OF_PERSON_ABOVE_AVERAGE_AGE_TAG),
                lMap.get(Constants.AVERAGE_SALARY_TAG),
                lMap.get(Constants.AVERAGE_SALARY_ADJUSTED_TAG),
                lMap.get(Constants.TOTAL_NUMBER_OF_CHILDREN_TAG));

        return writeResultFile(aInOutputFileName);
    }

    public Map<String, Integer> computeIndicators(NodeList aInNodeList) {
        Map<String, Integer> lMap = new HashMap<String, Integer>();

        int lNbOfJobless=0, lNbOfActif=0, lNbOfMarried=0, lNbOfSingle=0;
        int lSumAge=0, lAverageAge=0, lNbOfPersonAboveAverageAge=0;
        int lSumSalary=0, lSumSalaryAdjusted=0, lAverageSalary=0, lAverageSalaryAdjusted=0, lTotalOfChildren=0;

        for (int itr = 1; itr <= aInNodeList.getLength(); itr++) {
            Node node = aInNodeList.item(itr-1);
            Element eElement = (Element) node;
            if (eElement.getElementsByTagName(Constants.JOB_TAG).getLength() != 0
                && eElement.getElementsByTagName(Constants.JOB_TAG).item(0).getTextContent().equals(Constants.JOBLESS_VALUE))
                lNbOfJobless++;
            if (eElement.getElementsByTagName(Constants.MARITAL_STATUS_TAG).getLength() != 0) {
                if (eElement.getElementsByTagName(Constants.MARITAL_STATUS_TAG).item(0).getTextContent().equals(Constants.SINGLE_STATUS_VALUE))
                    lNbOfSingle++;
                else if (eElement.getElementsByTagName(Constants.MARITAL_STATUS_TAG).item(0).getTextContent().equals(Constants.MARRIED_STATUS_VALUE))
                    lNbOfMarried++;
            }
            if (eElement.getElementsByTagName(Constants.AGE_TAG).getLength() != 0) {
                int lValue = Integer.parseInt(eElement.getElementsByTagName(Constants.AGE_TAG).item(0).getTextContent());
                lSumAge = lSumAge + lValue;
                lAverageAge = lSumAge/itr;
                if (lValue > lAverageAge)
                    lNbOfPersonAboveAverageAge++;

            }
            if (eElement.getElementsByTagName(Constants.SALARY_TAG).getLength() != 0) {
                int lValue = Integer.parseInt(eElement.getElementsByTagName(Constants.SALARY_TAG).item(0).getTextContent());
                lSumSalary = lSumSalary + lValue;
                lAverageSalary = lSumSalary/itr;
                if (! eElement.getElementsByTagName(Constants.JOB_TAG).item(0).getTextContent().equals(Constants.JOBLESS_VALUE))
                    lNbOfActif++;
                    lSumSalaryAdjusted = lSumSalaryAdjusted + lValue;
                    lAverageSalaryAdjusted = lSumSalaryAdjusted/lNbOfActif;
            }
            if (eElement.getElementsByTagName(Constants.NB_OF_CHILDREN_TAG).getLength() != 0) {
                int lValue = Integer.parseInt(eElement.getElementsByTagName(Constants.NB_OF_CHILDREN_TAG).item(0).getTextContent());
                lTotalOfChildren = lTotalOfChildren + lValue;
            }
        }

        lMap.put(Constants.JOBLESS_TAG, lNbOfJobless);
        lMap.put(Constants.MARRIED_TAG, lNbOfMarried);
        lMap.put(Constants.SINGLE_TAG, lNbOfSingle);
        lMap.put(Constants.AVERAGE_AGE_TAG, lAverageAge);
        lMap.put(Constants.NB_OF_PERSON_ABOVE_AVERAGE_AGE_TAG, lNbOfPersonAboveAverageAge);
        lMap.put(Constants.AVERAGE_SALARY_TAG, lAverageSalary);
        lMap.put(Constants.AVERAGE_SALARY_ADJUSTED_TAG, lAverageSalaryAdjusted);
        lMap.put(Constants.TOTAL_NUMBER_OF_CHILDREN_TAG, lTotalOfChildren);
        return lMap;
    }

    public boolean buildResultFileUsingSAX(String aInOutputFileName) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        boolean lRet=false;
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            StatsHandler handler = new StatsHandler();
            saxParser.parse(file, handler);

            return writeResultFile(aInOutputFileName);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return lRet;
    }

    private boolean writeResultFile(String aInOutputFileName) {

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
        StreamResult streamResult = new StreamResult(new File(OUTPUT_FILE_PATH.replace("$", aInOutputFileName)));

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

