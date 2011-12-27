package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.CrfStatus;

//

/**
 * @author Suneel Allareddy
 * @since Jan 09, 2011
 */

public class StudyParticipantCrfScheduleNotificationQuery extends AbstractQuery {

    private static String queryString = "SELECT spcsn from StudyParticipantCrfScheduleNotification spcsn order by spcsn.creationDate";

    public StudyParticipantCrfScheduleNotificationQuery() {
        super(queryString);
    }

    public void filterByStatus(CrfStatus status) {
        andWhere("spcsn.status =:status");
        setParameter("status", status);
    }
}