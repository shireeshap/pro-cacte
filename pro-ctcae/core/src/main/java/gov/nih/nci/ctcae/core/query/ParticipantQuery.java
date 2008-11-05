package gov.nih.nci.ctcae.core.query;

/**
 * @author mehul
 */

public class ParticipantQuery extends AbstractQuery {

    private static String queryString = "SELECT p from Participant p order by p.id";

    private static String FIRST_NAME = "firstName";

    private static String LAST_NAME = "lastName";

    private static String IDENTIFIER = "assignedIdentifier";
    private static final String STUDY_ID = "studyId";

    public ParticipantQuery() {

        super(queryString);
    }

    public void filterByParticipantFirstName(final String firstName) {
        String searchString = "%" + firstName.toLowerCase() + "%";
        andWhere("lower(p.firstName) LIKE :" + FIRST_NAME);
        setParameter(FIRST_NAME, searchString);
    }

    public void filterByParticipantLastName(final String lastName) {
        String searchString = "%" + lastName.toLowerCase() + "%";
        andWhere("lower(p.lastName) LIKE :" + LAST_NAME);
        setParameter(LAST_NAME, searchString);
    }

    public void filterByParticipantIdentifier(final String identifier) {
        String searchString = "%" + identifier.toLowerCase() + "%";
        andWhere("lower(p.assignedIdentifier) LIKE :" + IDENTIFIER);
        setParameter(IDENTIFIER, searchString);
    }


    public void filterParticipantsWithMatchingText(String text) {

        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("(lower(p.firstName) LIKE :%s or lower(p.lastName) LIKE :%s " +
                "or lower(p.assignedIdentifier) LIKE :%s)", FIRST_NAME, LAST_NAME, IDENTIFIER));
        setParameter(IDENTIFIER, searchString);
        setParameter(FIRST_NAME, searchString);
        setParameter(LAST_NAME, searchString);

    }

    public void filterByStudy(Integer studyId) {
        if (studyId != null) {

            leftJoin("p.studyParticipantAssignments as spa join spa.studySite as ss join ss.study as study");
            andWhere("study.id =:" + STUDY_ID);
            setParameter(STUDY_ID, studyId);
        }
    }

}
