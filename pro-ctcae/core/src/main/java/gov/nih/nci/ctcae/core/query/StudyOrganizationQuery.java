package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudyOrganizationQuery extends AbstractQuery {

    private static String queryString = "SELECT o from StudyOrganization o order by o.organization.name";
    private static String STUDY_ID = "studyId";
    private static String ORGANIZATION_ID = "organizationId";
    private static final String STUDY_SITE = "studySite";

    public StudyOrganizationQuery() {

        super(queryString);
    }


    public void filterByStudyId(final int studyId) {
        andWhere("o.study.id = :" + STUDY_ID);
        setParameter(STUDY_ID, new Integer(studyId));
    }

    public void filterByStudySiteOnly() {
        andWhere("o.class = :" + STUDY_SITE);
        setParameter(STUDY_SITE, "SST");

    }

    public void filterByOrganizationId(final int organizationId) {
        andWhere("o.organization.id = :" + ORGANIZATION_ID);
        setParameter(ORGANIZATION_ID, new Integer(organizationId));
    }
}