package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.*;

/**
 * @author Harsh Agarwal
 *         Date: June 10, 2009
 */
public class ParticipantCommonTest extends AbstractWebTestCase {

    public void testParticipantControllerUtils_GetStudyParticipantCommand() {
        StudyParticipantCommand spc = new StudyParticipantCommand();
        String attribute = ScheduleCrfController.class.getName() + ".FORM." + "command";
        request.getSession().setAttribute(attribute, spc);
        StudyParticipantCommand spc1 = ParticipantControllerUtils.getStudyParticipantCommand(request);
        assertEquals(spc, spc1);
    }

    public void testParticipantControllerUtils_GetParticipantCommand() {
        ParticipantCommand pc = new ParticipantCommand();
        String attribute = ParticipantController.class.getName() + ".FORM." + "command";
        request.getSession().setAttribute(attribute, pc);
        ParticipantCommand pc1 = ParticipantControllerUtils.getParticipantCommand(request);
        assertEquals(pc, pc1);
    }

    public void testParticipantReviewTab_GetRequiredPrivilege() {
        ParticipantReviewTab prt = new ParticipantReviewTab();
        assertEquals(Privilege.PRIVILEGE_VIEW_PARTICIPANT, prt.getRequiredPrivilege());
    }

    public void testMonitorFormUtils_GetStartEndDate() throws ParseException {
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.DATE, 1);
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.YEAR, 2009);

        Date[] date = MonitorFormUtils.getStartEndDate("", c.getTime());
        assertEquals(6 * 24 * 60 * 60 * 1000, date[1].getTime() - date[0].getTime());

        date = MonitorFormUtils.getStartEndDate("thisWeek", c.getTime());
        assertTrue(date[1].after(date[0]));

        date = MonitorFormUtils.getStartEndDate("lastWeek", c.getTime());
        assertTrue(date[1].after(date[0]));

        date = MonitorFormUtils.getStartEndDate("thisMonth", c.getTime());
        assertTrue(date[1].after(date[0]));

        date = MonitorFormUtils.getStartEndDate("lastMonth", c.getTime());
        assertTrue(date[1].after(date[0]));
    }

    public void testParticipantRepsonseReport() throws Exception {
        Participant p = ParticipantTestHelper.getDefaultParticipant();
        login(p.getUser().getUsername());
        StudyParticipantCrf spc = p.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
        StudyParticipantCrfSchedule spcs = spc.getStudyParticipantCrfSchedules().get(0);
        spcs.setStatus(CrfStatus.COMPLETED);

        StudyParticipantCrfSchedule spcs1 = spc.getStudyParticipantCrfSchedules().get(1);
        spcs1.setStatus(CrfStatus.COMPLETED);

        ParticipantResponseReportController controller = new ParticipantResponseReportController();
        controller.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
        request.setMethod("GET");
        request.setParameter("id", spcs.getId().toString());

        ModelAndView mv = controller.handleRequest(request, response);

        Map m = mv.getModel();
        TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap = (TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>) m.get("resultsMap");
        List dates = (List<Date>) m.get("dates");

        assertEquals(10, symptomMap.keySet().size());
        assertEquals(spc.getStudyParticipantCrfSchedulesByStatus(CrfStatus.COMPLETED).size(), dates.size());

        assertNotNull(m.get("schedule"));
        assertNotNull(m.get("questionTypes"));

    }


}