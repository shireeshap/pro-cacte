package gov.nih.nci.ctcae.core.query;

import java.util.List;

import gov.nih.nci.ctcae.core.domain.CrfStatus;

//
/**
 * The Class StudyParticipantAssignmentQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudyParticipantAssignmentQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT o from StudyParticipantAssignment o";
    
    private static String queryStringDistinct = "SELECT distinct o from StudyParticipantAssignment o";
    
    private static String CRF_STATUSES = "crf_statuses";


    /**
     * The STUD y_ id.
     */
    private static String STUDY_ID = "studyId";
    private static String STUDY_SITE_ID = "studySiteId";

    /**
     * The PARTICIPAN t_ id.
     */
    private static String PARTICIPANT_ID = "participantId";
    private static String USER_ID = "userId";

    /**
     * Instantiates a new study participant assignment query.
     */
    public StudyParticipantAssignmentQuery() {
        super(queryString);
    }
    
    /**
     * Instantiates a new study participant assignment query.
     */
    public StudyParticipantAssignmentQuery(boolean isDistinct) {
            super(queryStringDistinct);
    }

    /**
     * Filter based on status of StudyParticipantAssignment
     * @param status
     */
    public void filterByStatus(final String status){
    	andWhere("o.status ='"+ status +"'");
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
    
    public void filterByUserId(Integer userId) {
        andWhere("o.participant.user.id = :" + USER_ID);
        setParameter(USER_ID, userId);
    }
    
    public void filterBySpcrfSchedulesStatus(List<CrfStatus> spCrfScheduleStatusList, int orgId){
    	leftJoin("o.studyParticipantCrfs as spcrf join spcrf.studyParticipantCrfSchedules as spcrfs");
        andWhere("spcrfs.status in (:" + CRF_STATUSES + ")");
        andWhere("o.studySite.id =:" + STUDY_SITE_ID);
        setParameter(STUDY_SITE_ID, new Integer(orgId));
        setParameterList(CRF_STATUSES, spCrfScheduleStatusList);
    }
    
}