package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.IvrsCallStatus;

import java.util.Date;

/**
 * @author Vinay Gangoli
 */

public class IvrsScheduleQuery extends AbstractQuery {

    private static String queryString = "SELECT ischd from IvrsSchedule ischd order by preferredCallTime";

    public IvrsScheduleQuery() {
        super(queryString);
    }

    public void filterByStudyParticipantAssignment(Integer id) {
        andWhere("ischd.studyParticipantAssignment.id =:spaId");
        setParameter("spaId", id);
    }

    public void filterByStudyParticipantCrfSchedule(Integer id) {
        andWhere("ischd.studyParticipantCrfSchedule.id =:crfId");
        setParameter("crfId", id);
    }

    public void filterByDate(Date startDate, Date endDate) {
        andWhere("ischd.nextCallTime between :startDate and :endDate");
        setParameter("startDate", startDate);
        setParameter("endDate", endDate);
    }

    public void filterByStatus(IvrsCallStatus status) {
        andWhere("ischd.callStatus =:status");
        setParameter("status", status);
    }
}