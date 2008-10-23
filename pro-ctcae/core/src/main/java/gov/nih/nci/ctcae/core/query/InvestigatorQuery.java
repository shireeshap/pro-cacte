package gov.nih.nci.ctcae.core.query;

/**
 * Created by IntelliJ IDEA.
 * User: Mehul Gulati
 * Date: Oct 15, 2008
 * Time: 6:57:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvestigatorQuery extends AbstractQuery {

    private static String queryString = "SELECT i from Investigator i order by i.id";

    private static String FIRST_NAME = "firstName";

    private static String LAST_NAME = "lastName";

    private static String NCI_IDENTIFIER = "nciIdentifier";


    public InvestigatorQuery() {

        super(queryString);
        }

     public void filterByInvestigatorFirstName(final String firstName) {
        String searchString = "%" + firstName.toLowerCase() + "%";
        andWhere("lower(i.firstName) LIKE :" + FIRST_NAME);
        setParameter(FIRST_NAME, searchString);
  }

     public void filterByInvestigatorLastName(final String lastName) {
           String searchString = "%" + lastName.toLowerCase() + "%";
           andWhere("lower(i.lastName) LIKE :" + LAST_NAME);
           setParameter(LAST_NAME, searchString);
     }
  /*
    public void filterByNciIdentifier(final String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("lower(investigator.nciIdentifier) LIKE :%s", NCI_IDENTIFIER));
        setParameter(NCI_IDENTIFIER, searchString);

    }
    */

    public void filterByNciIdentifier(final String nciIdentifier){
        String searchString = "%" + nciIdentifier.toLowerCase() + "%";
        andWhere("lower(i.nciIdentifier) LIKE :" + NCI_IDENTIFIER);
        setParameter(NCI_IDENTIFIER, searchString);
    }
}
