package gov.nih.nci.ctcae.core.query;

//
/**
 * The Class ParticipantQuery.
 *
 * @author mehul
 */

public class ParticipantQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT p from Participant p order by p.id";

    /**
     * The FIRS t_ name.
     */
    private static String FIRST_NAME = "firstName";

    /**
     * The LAS t_ name.
     */
    private static String LAST_NAME = "lastName";

    /**
     * The IDENTIFIER.
     */
    private static String IDENTIFIER = "assignedIdentifier";

    /**
     * The Constant STUDY_ID.
     */
    private static final String STUDY_ID = "studyId";
    private static final String STUDY_SITE_ID = "studySiteId";
    private static final String USERNAME = "username";
    private static final String STUDY_PARTICIPANT_IDENTIFIER = "studyParticipantIdentifier";

    /**
     * Instantiates a new participant query.
     */
    public ParticipantQuery() {

        super(queryString);
    }

    /**
     * Filter by participant first name.
     *
     * @param firstName the first name
     */
    public void filterByParticipantFirstName(final String firstName) {
        String searchString = firstName.toLowerCase();
        andWhere("lower(p.firstName) LIKE :" + FIRST_NAME);
        setParameter(FIRST_NAME, searchString);
    }

    /**
     * Filter by participant last name.
     *
     * @param lastName the last name
     */
    public void filterByParticipantLastName(final String lastName) {
        String searchString = lastName.toLowerCase();
        andWhere("lower(p.lastName) LIKE :" + LAST_NAME);
        setParameter(LAST_NAME, searchString);
    }

    /**
     * Filter by participant identifier.
     *
     * @param identifier the identifier
     */
    public void filterByParticipantIdentifier(final String identifier) {
        String searchString = identifier.toLowerCase();
        andWhere("lower(p.assignedIdentifier) LIKE :" + IDENTIFIER);
        setParameter(IDENTIFIER, searchString);
    }


    /**
     * Filter participants with matching text.
     *
     * @param text the text
     */
    public void filterParticipantsWithMatchingText(String text) {

        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("(lower(p.firstName) LIKE :%s " +
                "or lower(p.lastName) LIKE :%s " +
                "or lower(p.assignedIdentifier) LIKE :%s " +
                "or p.studyParticipantAssignments.studyParticipantIdentifier LIKE :%s)", FIRST_NAME, LAST_NAME, IDENTIFIER, STUDY_PARTICIPANT_IDENTIFIER));
        setParameter(IDENTIFIER, searchString);
        setParameter(FIRST_NAME, searchString);
        setParameter(LAST_NAME, searchString);
        setParameter(STUDY_PARTICIPANT_IDENTIFIER, searchString);

    }

    /**
     * Filter by study.
     *
     * @param studyId the study id
     */
    public void filterByStudy(Integer studyId) {
        if (studyId != null) {

            leftJoin("p.studyParticipantAssignments as spa join spa.studySite as ss join ss.study as study");
            andWhere("study.id =:" + STUDY_ID);
            setParameter(STUDY_ID, studyId);
        }
    }

    public void filterByStudySite(Integer studySiteId) {
        if (studySiteId != null) {

            leftJoin("p.studyParticipantAssignments as spa join spa.studySite as ss ");
            andWhere("ss.id =:" + STUDY_SITE_ID);
            setParameter(STUDY_SITE_ID, studySiteId);
        }
    }

    public void filterByUsername(String username) {
        if (username != null) {
            andWhere("p.user.username =:" + USERNAME);
            setParameter(USERNAME, username.toLowerCase());
        }
    }

    public void filterByStudyParticipantIdentifier(String spIdentifier) {
        if (spIdentifier != null) {
            leftJoin("p.studyParticipantAssignments as spa");
            andWhere("spa.studyParticipantIdentifier LIKE :" + STUDY_PARTICIPANT_IDENTIFIER);
            setParameter(STUDY_PARTICIPANT_IDENTIFIER, spIdentifier);
        }
    }

}
