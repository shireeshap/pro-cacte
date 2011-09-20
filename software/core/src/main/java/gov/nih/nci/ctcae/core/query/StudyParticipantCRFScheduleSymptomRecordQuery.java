package gov.nih.nci.ctcae.core.query;

import java.util.Date;

/**
 * @author Vinay Gangoli
 */
		
public class StudyParticipantCRFScheduleSymptomRecordQuery extends AbstractQuery {

    private static String queryString = "SELECT spcssr from StudyParticipantCRFScheduleSymptomRecord spcssr order by creationDate";

    public StudyParticipantCRFScheduleSymptomRecordQuery() {
        super(queryString);
    }

    public void filterByStudyParticipantCrfSchedule(Integer id) {
        andWhere("spcssr.studyParticipantCrfSchedule.id =:studyParticipantCrfScheduleId");
        setParameter("studyParticipantCrfScheduleId", id);
    }

    public void filterByCreationDate(Date startDate, Date endDate) {
        andWhere("spcssr.creationDate between :startDate and :endDate");
        setParameter("startDate", startDate);
        setParameter("endDate", endDate);
    }

}