package gov.nih.nci.ctcae.core.query;

/**
 * User: Mehul Gulati
 * Date: Oct 15, 2008
 */
public class ClinicalStaffQuery extends AbstractQuery {

    private static String queryString = "SELECT i from ClinicalStaff i order by i.id";

    private static String FIRST_NAME = "firstName";

    private static String LAST_NAME = "lastName";

    private static String NCI_IDENTIFIER = "nciIdentifier";


    public ClinicalStaffQuery() {

        super(queryString);
        }

     public void filterByClinicalStaffFirstName(final String firstName) {
        String searchString = "%" + firstName.toLowerCase() + "%";
        andWhere("lower(i.firstName) LIKE :" + FIRST_NAME);
        setParameter(FIRST_NAME, searchString);
  }

     public void filterByClinicalStaffLastName(final String lastName) {
           String searchString = "%" + lastName.toLowerCase() + "%";
           andWhere("lower(i.lastName) LIKE :" + LAST_NAME);
           setParameter(LAST_NAME, searchString);
     }
  /*
    public void filterByNciIdentifier(final String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("lower(clinicalstaff.nciIdentifier) LIKE :%s", NCI_IDENTIFIER));
        setParameter(NCI_IDENTIFIER, searchString);

    }
    */

    public void filterByNciIdentifier(final String nciIdentifier){
        String searchString = "%" + nciIdentifier.toLowerCase() + "%";
        andWhere("lower(i.nciIdentifier) LIKE :" + NCI_IDENTIFIER);
        setParameter(NCI_IDENTIFIER, searchString);
    }
}
