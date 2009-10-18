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
        Date startDate = null;
        for (StudyParticipantCrfSchedule spcs : spc.getStudyParticipantCrfSchedules()) {
            if (spcs.getStatus().equals(CrfStatus.SCHEDULED)) {
                startDate = spcs.getStartDate();
                break;
            }
        }
        Calendar c = Calendar.getInstance();
        assert startDate != null;
        c.setTimeInMillis(startDate.getTime());
        ps.removeSchedule(c);
        assertEquals(a - 1, spc.getStudyParticipantCrfSchedules().size());
        int b = spc.getStudyParticipantCrfSchedulesByStatus(CrfStatus.SCHEDULED).size();
        ps.removeAllSchedules();
        assertEquals(a - 1 - b, spc.getStudyParticipantCrfSchedules().size());

    }

//    public void testMoveSchedule() throws ParseException {
//
//        ArrayList<Date> ld = new ArrayList<Date>();
//        for (StudyParticipantCrfSchedule a : spc.getStudyParticipantCrfSchedules()) {
//            ld.add(a.getStartDate());
//        }
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(new Date().getTime());
//        ps.moveAllSchedules(2);
//        int i = 0;
//        for (StudyParticipantCrfSchedule a : spc.getStudyParticipantCrfSchedules()) {
//            if (a.getStatus().equals(CrfStatus.SCHEDULED)) {
//                assertEquals(DateUtils.addDaysToDate(ld.get(i), 2), a.getStartDate());
//            }
//            i++;
//        }
//
//        cal.setTime(DateUtils.addDaysToDate(cal.getTime(), 3));
//        i = 0;
//        ps.moveFutureSchedules(cal, 2);
//        for (StudyParticipantCrfSchedule a : spc.getStudyParticipantCrfSchedules()) {
//            if (a.getStatus().equals(CrfStatus.SCHEDULED)) {
//                if (i == 0) {
//                    assertEquals(DateUtils.addDaysToDate(ld.get(i), 2), a.getStartDate());
//                } else {
//                    assertEquals(DateUtils.addDaysToDate(ld.get(i), 4), a.getStartDate());
//                }
//            }
//            i++;
//        }
//
//        int allStatus = spc.getStudyParticipantCrfSchedules().size();
//        Date d = null;
//        boolean nextDate = false;
//        int b = 0;
//
//        for (StudyParticipantCrfSchedule a : spc.getStudyParticipantCrfSchedules()) {
//            if (a.getStatus().equals(CrfStatus.SCHEDULED)) {
//                if (!nextDate) {
//                    nextDate = true;
//                } else {
//                    if (d == null) {
//                        d = a.getStartDate();
//                    }
//                    b++;
//                }
//            }
//        }
//        cal.setTime(d);
//        ps.deleteFutureSchedules(cal);
//        assertEquals(allStatus - b, spc.getStudyParticipantCrfSchedules().size());
//
//    }
}
