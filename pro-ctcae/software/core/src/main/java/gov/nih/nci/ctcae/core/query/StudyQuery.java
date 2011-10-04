package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Study;
import org.apache.commons.lang.StringUtils;

//

/**
 * The Class StudyQuery.
 *
 * @author Vinay Kumar
 * @since Oct 14, 2008
 */
public class StudyQuery extends SecuredQuery<Study> {

    /**
     * The query string.
     */
    private static String queryString = "Select study from Study study order by study.shortTitle ";

    /**
     * The Constant SHORT_TITLE.
     */
    private static final String SHORT_TITLE = "shortTitle";

    /**
     * The Constant LONG_TITLE.
     */
    private static final String LONG_TITLE = "longTitle";

    /**
     * The Constant ASSIGNED_IDENTIFIER.
     */
    private static final String ASSIGNED_IDENTIFIER = "assignedIdentifier";

    /**
     * The Constant PARTICIPANT_ID.
     */
    private static final String PARTICIPANT_ID = "participantId";

    /**
     * The ORGANIZATIO n_ id.
     */
    private static String ORGANIZATION_ID = "organizationId";

    /**
     * The Constant STUDY_SITE.
     */
    private static final String STUDY_SITE = "studySite";
    private static final String LEAD_SITE = "leadSite";

    /**
     * Instantiates a new study query.
     */
    public StudyQuery() {
        super(queryString);


    }

    /**
     * Filter studies with matching text.
     *
     * @param text the text
     */
    public void filterStudiesWithMatchingText(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("(lower(study.shortTitle) LIKE :%s or lower(study.longTitle) LIKE :%s or lower(study.assignedIdentifier) LIKE :%s)"
                , SHORT_TITLE, LONG_TITLE, ASSIGNED_IDENTIFIER));
        setParameter(SHORT_TITLE, searchString);
        setParameter(LONG_TITLE, searchString);
        setParameter(ASSIGNED_IDENTIFIER, searchString);

    }

    /**
     * Filter studies by long title.
     *
     * @param text the text
     */
    public void filterStudiesByLongTitle(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("lower(study.longTitle) LIKE :%s)", LONG_TITLE));
        setParameter(LONG_TITLE, searchString);

    }

    /**
     * Filter studies by assigned identifier.
     *
     * @param text the text
     */
    public void filterStudiesByAssignedIdentifier(String text) {
        String searchString = text != null ? text.toLowerCase() : null;
        searchString = "%"+ searchString + "%";
        andWhere(String.format("lower(study.assignedIdentifier) LIKE :%s)", ASSIGNED_IDENTIFIER));
        setParameter(ASSIGNED_IDENTIFIER, searchString);

    }

    /**
     * Filter by assigned identifier exact match.
     *
     * @param assignedIdentifier the assigned identifier
     */
    public void filterByAssignedIdentifierExactMatch(final String assignedIdentifier) {
        if (!StringUtils.isBlank(assignedIdentifier)) {
            andWhere("lower(study.assignedIdentifier) = :" + ASSIGNED_IDENTIFIER);
            setParameter(ASSIGNED_IDENTIFIER, assignedIdentifier.toLowerCase());
        }
    }

    /**
     * Filter studies by short title.
     *
     * @param text the text
     */
    public void filterStudiesByShortTitle(String text) {
        String searchString = text != null ? text.toLowerCase() : null;
        searchString = "%"+ searchString + "%";

        andWhere(String.format("lower(study.shortTitle) LIKE :%s)", SHORT_TITLE));
        setParameter(SHORT_TITLE, searchString);

    }


    /**
     * Filter studies for study site.
     *
     * @param siteId the site id
     */
    public void filterStudiesForStudySite(Integer siteId) {
        if (siteId != null) {
            leftJoin("study.studyOrganizations as sso");
            andWhere("sso.organization.id = :" + ORGANIZATION_ID);
            andWhere(String.format("(sso.class = :%s or sso.class = :%s )" , STUDY_SITE, LEAD_SITE));
            setParameter(ORGANIZATION_ID, siteId);
            setParameter(STUDY_SITE, "SST");
            setParameter(LEAD_SITE, "LSS");
        }
    }


    /**
     * Filter by participant.
     *
     * @param participantId the participant id
     */
    public void filterByParticipant(Integer participantId) {

        leftJoin("study.studyOrganizations as ss join ss.studyParticipantAssignments as spa join spa.participant as p");
        andWhere("p.id =:" + PARTICIPANT_ID);
        setParameter(PARTICIPANT_ID, participantId);
    }

    public Class<Study> getPersistableClass() {
        return Study.class;


    }

    protected String getObjectIdQueryString() {
        return "study.id";


    }
}
