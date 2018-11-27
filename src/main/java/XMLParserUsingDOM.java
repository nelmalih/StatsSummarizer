import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public class XMLParserUsingDOM extends XMLParserImp {

    private static XMLParserUsingDOM instance;

    public static XMLParserUsingDOM get() {
        return instance == null ? new XMLParserUsingDOM() : instance;
    }

    @Override
    public boolean buildResultFile() {
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

        return writeResultFile();
    }

    private Map<String, Integer> computeIndicators(NodeList aInNodeList) {
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

}
