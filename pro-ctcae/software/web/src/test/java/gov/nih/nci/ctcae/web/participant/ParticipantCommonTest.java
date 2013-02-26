package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import static org.easymock.EasyMock.expect;
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


    public void testSetParticipantModesAndReminders(){
    	ParticipantCommand participantCommand = new ParticipantCommand();
    	StudySite studySite = new StudySite();
    	studySite.setId(11);
    	IvrsSchedule ivrsSchedule = new IvrsSchedule();
    	Date today = new Date();
    	ivrsSchedule.setPreferredCallTime(today);
    	ivrsSchedule.setCallStatus(IvrsCallStatus.PENDING);
    	StudyParticipantCrf spcrf = new StudyParticipantCrf();
    	StudyParticipantCrfSchedule spcrfs = new StudyParticipantCrfSchedule();
    	List<IvrsSchedule> ivrsScheduleList = new ArrayList<IvrsSchedule>();
    	ivrsScheduleList.add(ivrsSchedule);
    	spcrfs.setIvrsSchedules(ivrsScheduleList);
    	spcrfs.setStudyParticipantCrf(spcrf);
    	spcrf.addStudyParticipantCrfSchedule(spcrfs);
    	StudyParticipantAssignment spa = new StudyParticipantAssignment();
    	spa.addStudyParticipantCrf(spcrf);
    	participantCommand.setResponseModes(new String[]{"IVRS"});
    	
    	request.getSession().setAttribute("email_" + studySite.getId(), false);
    	request.getSession().setAttribute("call_" + studySite.getId(), false);
    	request.getSession().setAttribute("text_" + studySite.getId(), false);
    	request.getSession().setAttribute("call_hour_" + studySite.getId(), 1);
    	request.getSession().setAttribute("call_hour_" + studySite.getId(), 1);
    	request.setParameter("call_ampm_" + studySite.getId(), "am");
    	request.setParameter("call_timeZone_" + studySite.getId(), "America/Chicago");
    	request.setParameter("home_paper_lang_" + studySite.getId(), "ENGLISH");
    	request.setParameter("home_web_lang_" + studySite.getId(), "ENGLISH");
    	request.setParameter("ivrs_lang_" + studySite.getId(), "ENGLISH");
    	request.setParameter("clinic_paper_lang_" + studySite.getId(), "ENGLISH");
    	request.setParameter("clinic_web_lang_" + studySite.getId(), "ENGLISH");

    	participantCommand.setParticipantModesAndReminders(studySite, spa, request);
    	assertEquals(spa.getCallHour(), Integer.valueOf(1));
    	assertEquals(spa.getCallAmPm(), "am");
    	assertNotSame(spa.getCallMinute(), null);
    	assertEquals(spa.getCallTimeZone(), "America/Chicago" );
    	assertEquals(spa.getHomePaperLanguage(), "ENGLISH");
    	assertEquals(spa.getHomeWebLanguage(), "ENGLISH");
    	assertEquals(spa.getIvrsLanguage(), "ENGLISH");
    	assertEquals(spa.getClinicPaperLanguage(), "ENGLISH");
    	assertEquals(spa.getClinicWebLanguage(), "ENGLISH");
    	
    	StudyParticipantMode spMode = spa.getStudyParticipantModes().get(0);
    	assertEquals(spMode.getMode(), "IVRS/Automated Telephone");
    	assertFalse(spMode.getEmail());
    	assertFalse(spMode.getCall());
    	assertFalse(spMode.getText());
    	
    	request.setParameter("call_minute_" + studySite.getId(), "44");
    	participantCommand.setParticipantModesAndReminders(studySite, spa, request);
    	today.setHours(2);
    	today.setMinutes(5);
    	today.setSeconds(0);
    	assertEquals(spa.getIvrsScheduleList().get(0).getPreferredCallTime().getMinutes(), 44);
    }
}