package gov.nih.nci.ctcae.core.query;

// TODO: Auto-generated Javadoc
/**
 * The Class OrganizationHavingStudySiteQuery.
 * 
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
public class OrganizationHavingStudySiteQuery extends AbstractQuery {

    /** The query string. */
    private static String queryString = "SELECT distinct(ss.organization) from StudySite ss order by ss.organization.name";


    /**
     * Instantiates a new organization having study site query.
     */
    public OrganizationHavingStudySiteQuery() {

        super(queryString);
    }


}