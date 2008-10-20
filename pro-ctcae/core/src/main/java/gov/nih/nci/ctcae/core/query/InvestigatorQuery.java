package gov.nih.nci.ctcae.core.query;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: Oct 15, 2008
 * Time: 6:57:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvestigatorQuery extends AbstractQuery {

    private static String queryString = "SELECT i from Investigator i order by i.id";

    private static String FIRST_NAME = "firstName";

    private static String LAST_NAME = "lastName";


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
}
