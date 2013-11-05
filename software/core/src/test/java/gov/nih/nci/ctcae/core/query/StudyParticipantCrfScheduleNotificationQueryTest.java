package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import junit.framework.TestCase;

/**
 * @author  Suneel Allareddy
 * @since Jan 10, 2010
 */
public class StudyParticipantCrfScheduleNotificationQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        StudyParticipantCrfScheduleNotificationQuery query = new StudyParticipantCrfScheduleNotificationQuery();
        assertEquals("wrong parsing for constructor",
                "SELECT spcsn from StudyParticipantCrfScheduleNotification spcsn order by spcsn.creationDate", query
                        .getQueryString());

    }

    public void testFilterByStatus() throws Exception {
        StudyParticipantCrfScheduleNotificationQuery query = new StudyParticipantCrfScheduleNotificationQuery();
        query.filterByStatus(CrfStatus.COMPLETED);
        assertEquals(
                "SELECT spcsn from StudyParticipantCrfScheduleNotification spcsn WHERE spcsn.status =:status order by spcsn.creationDate",
                query.getQueryString());
        assertEquals("wrong number of parameters", query.getParameterMap().size(), 1);        
        assertEquals("wrong parameter value", query.getParameterMap().get("status"),
                CrfStatus.COMPLETED);


    }
    
}