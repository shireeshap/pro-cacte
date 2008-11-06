package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudyParticipantAssignmentQuery extends AbstractQuery {

    private static String queryString = "SELECT o from StudyParticipantAssignment o";
    private static String STUDY_ID = "studyId";
    private static String PARTICIPANT_ID = "participantId";

    public StudyParticipantAssignmentQuery() {
        super(queryString);
    }


    public void filterByStudyId(final int studyId) {
        leftJoin("o.studySite as ss join ss.study as study");
        andWhere("study.id =:" + STUDY_ID);
        setParameter(STUDY_ID, new Integer(studyId));
    }

    public void filterByParticipantId(final int participantId) {
        andWhere("o.participant.id = :" + PARTICIPANT_ID);
        setParameter(PARTICIPANT_ID, new Integer(participantId));
    }
}