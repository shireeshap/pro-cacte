package gov.nih.nci.ctcae.core.query;

/**
 * @author Mehul Gulati
 * Date: Oct 24, 2008
 */
public class ResearchStaffQuery extends AbstractQuery {

       private static String queryString = "SELECT r from ResearchStaff r order by r.id";

       private static String FIRST_NAME = "firstName";

       private static String LAST_NAME = "lastName";

       private static String ORGANIZATION = "organization";

      public ResearchStaffQuery() {

        super(queryString);
    }

        public void filterByResearchStaffFirstName(final String firstName) {
           String searchString = "%" + firstName.toLowerCase() + "%";
           andWhere("lower(r.firstName) LIKE :" + FIRST_NAME);
           setParameter(FIRST_NAME, searchString);
     }

        public void filterByResearchStaffLastName(final String lastName) {
           String searchString = "%" + lastName.toLowerCase() + "%";
           andWhere("lower(r.lastName) LIKE :" + LAST_NAME);
           setParameter(LAST_NAME, searchString);
     }

       public void filterByResearchStaffOrganization(final String organization) {
           String searchString = "%" + organization.toLowerCase() + "%";
           andWhere("lower(r.organization) LIKE :" + ORGANIZATION);
           setParameter(ORGANIZATION, searchString);
     }

}
