package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.CrfStatus;

import java.util.Date;

//
/**
 * @author mehul
 */

public class StudyParticipantCrfScheduleQuery extends AbstractQuery {

    private static String queryString = "SELECT spcs from StudyParticipantCrfSchedule spcs order by startDate";

    public StudyParticipantCrfScheduleQuery() {
        super(queryString);
    }

    public void filterByStudy(Integer id) {
        andWhere("spcs.studyParticipantCrf.crf.study.id =:studyId");
        setParameter("studyId", id);
    }

    public void filterByCrf(Integer id) {
        andWhere("spcs.studyParticipantCrf.crf.id =:crfId");
        setParameter("crfId", id);
    }

    public void filterByStudySite(Integer id) {
        andWhere("spcs.studyParticipantCrf.studyParticipantAssignment.studySite.id =:studySiteId");
        setParameter("studySiteId", id);
    }

    public void filterByParticipant(Integer id) {
        andWhere("spcs.studyParticipantCrf.studyParticipantAssignment.participant.id =:participantId");
        setParameter("participantId", id);
    }

    public void filterByDate(Date startDate, Date endDate) {
        andWhere("spcs.startDate between :startDate and :endDate");
        setParameter("startDate", startDate);
        setParameter("endDate", endDate);
    }

    public void filterByStatus(CrfStatus status) {
        andWhere("spcs.status =:status");
        setParameter("status", status);
    }
}