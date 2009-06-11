package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;

import java.util.List;

/**
 * User: Harsh
 * Date: Jun 11, 2009
 * Time: 3:09:54 PM
 */
public class ParticipantScheduleTest extends TestDataManager{

    public void testGetCurrentMonthSchedules(){
        ParticipantSchedule ps = new ParticipantSchedule();
        ps.setStudyParticipantCrf(ParticipantTestHelper.getDefaultParticipant().getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0));
        List<StudyParticipantCrfSchedule> participantCrfSchedules = ps.getCurrentMonthSchedules();
        assertEquals(4, participantCrfSchedules.size());
    }
}
