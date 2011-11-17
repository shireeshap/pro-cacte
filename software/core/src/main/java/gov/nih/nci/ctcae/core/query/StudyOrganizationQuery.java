package gov.nih.nci.ctcae.core.query;

import java.util.ArrayList;
import java.util.List;

//
/**
 * The Class StudyOrganizationQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudyOrganizationQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT o from StudyOrganization o order by o.organization.name";

    /**
     * The STUD y_ id.
     */
    private static String STUDY_ID = "studyId";

    /**
     * The ORGANIZATIO n_ id.
     */
    private static String ORGANIZATION_ID = "organizationId";

    /**
     * The Constant STUDY_SITE.
     */
    private static final String STUDY_SITE = "studySite";
    private static final String ORGANIZATION_NAME = "name";
    /**
     * The NCI code.
     */
    private static String NCI_CODE = "nciInstituteCode";


    /**
     * Instantiates a new study organization query.
     */
    public StudyOrganizationQuery() {

        super(queryString);
    }


    /**
     * Filter by study id.
     *
     * @param studyId the study id
     */
    public void filterByStudyId(final int studyId) {
        andWhere("o.study.id = :" + STUDY_ID);
        setParameter(STUDY_ID, new Integer(studyId));
    }

    /**
     * Filter by study site only.
     */
    public void filterByStudySiteAndLeadSiteOnly() {
        List<String> siteTypes = new ArrayList<String>();
        siteTypes.add("SST");
        siteTypes.add("LSS");
        andWhere("o.class in ( :" + STUDY_SITE + ")");
        setParameterList(STUDY_SITE, siteTypes);

    }

    public void filterByOrganizationName(final String name) {
        String searchString = "%" + name.toLowerCase() + "%";
        andWhere("lower(o.organization.name) LIKE :" + ORGANIZATION_NAME);
        setParameter(ORGANIZATION_NAME, searchString);
    }

    /**
     * Filter by organization id.
     *
     * @param organizationId the organization id
     */
    public void filterByOrganizationId(final int organizationId) {
        andWhere("o.organization.id = :" + ORGANIZATION_ID);
        setParameter(ORGANIZATION_ID, new Integer(organizationId));
    }    

        /**
     * Filter by data coordinating center id.
     *
     * @param organizationId the organization id
     */
    public void filterByDataCoordinatingCenterId(final int organizationId) {
        andWhere("o.organization.id = :" + ORGANIZATION_ID);
        List<String> siteTypes = new ArrayList<String>();
        siteTypes.add("DCC");
        andWhere("o.class in ( :" + STUDY_SITE + ")");
        setParameterList(STUDY_SITE, siteTypes);
        setParameter(ORGANIZATION_ID, new Integer(organizationId));
    }
    /**
     * Filter by organization name or nci institute code.
     *
     * @param text the text
     */
    public void filterByOrganizationNameOrNciInstituteCode(final String text) {
        String searchString = "%" + text.toLowerCase() + "%";
        andWhere(String.format("(lower(o.organization.name) LIKE :%s or lower(o.organization.nciInstituteCode) LIKE :%s)", ORGANIZATION_NAME, NCI_CODE));
        setParameter(ORGANIZATION_NAME, searchString);
        setParameter(NCI_CODE, searchString);
    }
   
   
}

