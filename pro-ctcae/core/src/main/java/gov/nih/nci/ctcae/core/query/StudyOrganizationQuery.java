package gov.nih.nci.ctcae.core.query;

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
    public void filterByStudySiteOnly() {
        andWhere("o.class = :" + STUDY_SITE);
        setParameter(STUDY_SITE, "SST");

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
}