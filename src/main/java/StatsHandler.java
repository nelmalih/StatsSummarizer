import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class StatsHandler extends DefaultHandler {

    int lNbOfPerson=0, lNbOfJobless=0, lNbOfActif=0, lNbOfMarried=0, lNbOfSingle=0;
    int lSumAge=0, lAverageAge=0, lNbOfPersonAboveAverageAge=0;
    int lSumSalary=0, lSumSalaryAdjusted=0, lAverageSalary=0, lAverageSalaryAdjusted=0, lTotalOfChildren=0;

    boolean lAgeTAG=false, lJobTAG=false, lMaritalStatusTAG=false;
    boolean lNbOfChildTAG=false, lSalaryTAG=false;

    private StringBuilder data=null;

    @Override
    public void startElement(String aInUri, String aInLocalName, String aInQName, Attributes aInAttributes) throws SAXException {
        if (aInQName.equals(Constants.PERSON_TAG))
            lNbOfPerson++;

        switch (aInQName) {
            case Constants.AGE_TAG:
                lAgeTAG = true;
                break;
            case Constants.JOB_TAG:
                lJobTAG = true;
                break;
            case Constants.MARITAL_STATUS_TAG:
                lMaritalStatusTAG = true;
                break;
            case Constants.NB_OF_CHILDREN_TAG:
                lNbOfChildTAG = true;
                break;
            case Constants.SALARY_TAG:
                lSalaryTAG = true;
                break;
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String aInUri, String aInLocalName, String aInQName) throws SAXException {
        if (lAgeTAG) {
            int lAge = Integer.parseInt(data.toString());
            lSumAge = lSumAge + lAge;
            lAverageAge = lSumAge / lNbOfPerson;
            if (lAge >= lAverageAge)
                lNbOfPersonAboveAverageAge++;
            lAgeTAG = false;
        }

        if (lJobTAG) {
            String lJobTitle = data.toString();
            if (lJobTitle.equals(Constants.JOBLESS_VALUE))
                lNbOfJobless++;
            else
                lNbOfActif++;
            lJobTAG = false;
        }

        if (lMaritalStatusTAG) {
            String lMaritalStatus = data.toString();
            if (lMaritalStatus.equals(Constants.SINGLE_STATUS_VALUE))
                lNbOfSingle++;
            else
                lNbOfMarried++;
            lMaritalStatusTAG = false;
        }

        if (lSalaryTAG) {
            int lSalary = Integer.parseInt(data.toString());
            lSumSalary = lSumSalary + lSalary;
            lAverageSalary = lSumSalary/lNbOfPerson;
            lAverageSalaryAdjusted = lSumSalary/lNbOfActif;
            lSalaryTAG = false;
        }

        if (lNbOfChildTAG) {
            lTotalOfChildren = lTotalOfChildren + Integer.parseInt(data.toString());
            lNbOfChildTAG = false;
        }

        if (aInQName.equals(Constants.PERSONS_TAG)) {
            Stats lStats = new Stats(lNbOfPerson, lNbOfJobless, lNbOfMarried, lNbOfSingle, lAverageAge, lNbOfPersonAboveAverageAge,
                                        lAverageSalary, lAverageSalaryAdjusted, lTotalOfChildren);

            ResultGenerator.setStats(lStats);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }
}
