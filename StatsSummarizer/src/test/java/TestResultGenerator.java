import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class TestResultGenerator {

    @Test
    public void testBuildResultFile(){
        ResultGenerator lResultGenerator = ResultGenerator.get();

        lResultGenerator.init("InputSimpleFile");

        assertTrue(lResultGenerator.buildResultFile("outputFile"));
    }

    @Test
    public void testComputeIndicators(){

        ResultGenerator lResultGenerator = ResultGenerator.get();
        lResultGenerator.init("InputSimpleFile");

        Document document = lResultGenerator.getDom();
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName(Constants.PERSON_TAG);

        Map<String, Integer> lMap = lResultGenerator.computeIndicators(nodeList);

        assertTrue(nodeList.getLength() == 3);
        assertTrue(lMap.get(Constants.JOBLESS_TAG).toString().equals("1"));
        assertTrue(lMap.get(Constants.MARRIED_TAG).toString().equals("1"));
        assertTrue(lMap.get(Constants.SINGLE_TAG).toString().equals("2"));
        assertTrue(lMap.get(Constants.AVERAGE_AGE_TAG).toString().equals("42"));
        assertTrue(lMap.get(Constants.NB_OF_PERSON_ABOVE_AVERAGE_AGE_TAG).toString().equals("1"));
        assertTrue(lMap.get(Constants.AVERAGE_SALARY_TAG).toString().equals("39000"));
        assertTrue(lMap.get(Constants.AVERAGE_SALARY_ADJUSTED_TAG).toString().equals("58500"));
        assertTrue(lMap.get(Constants.TOTAL_NUMBER_OF_CHILDREN_TAG).toString().equals("3"));
    }
}
