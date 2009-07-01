package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * User: Harsh
 * Date: Jun 11, 2009
 * Time: 3:09:54 PM
 */
public class ParticipantScheduleTest extends TestDataManager {

    ParticipantSchedule ps;
    StudyParticipantCrf spc;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        ps = new ParticipantSchedule();
        spc = ParticipantTestHelper.getDefaultParticipant().getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
        ps.setStudyParticipantCrf(spc);

    }

    public void testGetCurrentMonthSchedules() {
        List<StudyParticipantCrfSchedule> participantCrfSchedules = ps.getCurrentMonthSchedules();
        assertTrue(participantCrfSchedules.size() > 0);
    }

    public void testRemoveSchedule() throws ParseException {
        int a = spc.getStudyParticipantCrfSchedules().size();
        Date startDate = spc.getStudyParticipantCrfSchedules().get(0).getStartDate();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(startDate.getTime());
        ps.removeSchedule(c);
        assertEquals(a - 1, spc.getStudyParticipantCrfSchedules().size());

        ps.removeAllSchedules();
        assertEquals(0, spc.getStudyParticipantCrfSchedules().size());

    }

    public void testMoveSchedule() throws ParseException {

        ArrayList<Date> ld = new ArrayList<Date>();
        for (StudyParticipantCrfSchedule a : spc.getStudyParticipantCrfSchedules()) {
            ld.add(a.getStartDate());
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        ps.moveAllSchedules(2);
        int i = 0;
        for (StudyParticipantCrfSchedule a : spc.getStudyParticipantCrfSchedules()) {
            assertEquals(DateUtils.addDaysToDate(ld.get(i), 2), a.getStartDate());
            i++;
        }

        cal.setTime(DateUtils.addDaysToDate(cal.getTime(), 3));
        i = 0;
        ps.moveFutureSchedules(cal, 2);
        for (StudyParticipantCrfSchedule a : spc.getStudyParticipantCrfSchedules()) {
            if (i == 0) {
                assertEquals(DateUtils.addDaysToDate(ld.get(i), 2), a.getStartDate());
            } else {
                assertEquals(DateUtils.addDaysToDate(ld.get(i), 4), a.getStartDate());
            }
            i++;
        }
        int a = spc.getStudyParticipantCrfSchedules().size() ;
        cal.setTime(DateUtils.addDaysToDate(cal.getTime(), 10));
        ps.deleteFutureSchedules(cal);
        assertTrue(a> spc.getStudyParticipantCrfSchedules().size());

    }
}
