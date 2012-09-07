package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

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
        String attribute = CreateParticipantController.class.getName() + ".FORM." + "command";
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


}