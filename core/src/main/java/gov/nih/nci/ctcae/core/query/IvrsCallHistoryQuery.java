package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.IvrsCallStatus;

import java.util.Date;

/**
 * @author Vinay Gangoli
 */

public class IvrsCallHistoryQuery extends AbstractQuery {

    private static String queryString = "SELECT ihist from IvrsCallHistory ihist order by callTime";

    public IvrsCallHistoryQuery() {
        super(queryString);
    }

    public void filterByIvrsSchedule(Integer id) {
        andWhere("ihist.ivrsSchedule.id =:ivrsSchId");
        setParameter("ivrsSchId", id);
    }

    public void filterByDate(Date startDate, Date endDate) {
        andWhere("ihist.callTime between :startDate and :endDate");
        setParameter("startDate", startDate);
        setParameter("endDate", endDate);
    }

    public void filterByStatus(IvrsCallStatus status) {
        andWhere("ihist.callStatus =:status");
        setParameter("status", status);
    }
}