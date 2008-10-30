package gov.nih.nci.ctcae.core.query;

/**
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
public class OrganizationHavingStudySiteQuery extends AbstractQuery {

    private static String queryString = "SELECT distinct(ss.organization) from StudySite ss order by ss.organization.name";


    public OrganizationHavingStudySiteQuery() {

        super(queryString);
    }


}