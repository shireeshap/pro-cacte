package gov.nih.nci.ctcae.core.query;

// TODO: Auto-generated Javadoc
/**
 * The Class StudyParticipantAssignmentQuery.
 * 
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudyParticipantAssignmentQuery extends AbstractQuery {

    /** The query string. */
    private static String queryString = "SELECT o from StudyParticipantAssignment o";
    
    /** The STUD y_ id. */
    private static String STUDY_ID = "studyId";
    
    /** The PARTICIPAN t_ id. */
    private static String PARTICIPANT_ID = "participantId";

    /**
     * Instantiates a new study participant assignment query.
     */
    public StudyParticipantAssignmentQuery() {
        super(queryString);
    }


    /**
     * Filter by study id.
     * 
     * @param studyId the study id
     */
    public void filterByStudyId(final int studyId) {
        leftJoin("o.studySite as ss join ss.study as study");
        andWhere("study.id =:" + STUDY_ID);
        setParameter(STUDY_ID, new Integer(studyId));
    }

    /**
     * Filter by participant id.
     * 
     * @param participantId the participant id
     */
    public void filterByParticipantId(final int participantId) {
        andWhere("o.participant.id = :" + PARTICIPANT_ID);
        setParameter(PARTICIPANT_ID, new Integer(participantId));
    }
}