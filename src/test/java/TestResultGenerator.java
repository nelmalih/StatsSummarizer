import org.junit.Test;
import java.io.File;
import static org.junit.Assert.assertTrue;

public class TestResultGenerator {

    private final static String INPUT_FILE_DIR_PATH = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "main" + File.separator + "resources" +
            File.separator + "inputs" + File.separator + "$.xml";

    private final static String OUTPUT_FILE_DIR_PATH = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "main" + File.separator + "resources" +
            File.separator + "results" + File.separator + "$.xml";

    @Test
    public void testBuildResultFileUsingDOM(){
        XMLParserUsingDOM lXmlParserUsingDOM = XMLParserUsingDOM.get();

        lXmlParserUsingDOM.initInputFile(INPUT_FILE_DIR_PATH.replace("$", "InputSimpleFile"),
                                            OUTPUT_FILE_DIR_PATH.replace("$", "DomOutputSimpleFile"));
        lXmlParserUsingDOM.initDOM();

        assertTrue(lXmlParserUsingDOM.buildResultFile());
    }

    @Test
    public void testbuildResultFileUsingSAX(){
        XMLParserUsingSAX lXMLParserUsingSAX = XMLParserUsingSAX.get();

        lXMLParserUsingSAX.initInputFile(INPUT_FILE_DIR_PATH.replace("$", "InputSimpleFile"),
                OUTPUT_FILE_DIR_PATH.replace("$", "SaxOutputSimpleFile"));
        lXMLParserUsingSAX.initDOM();

        assertTrue(lXMLParserUsingSAX.buildResultFile());
    }

}
