package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public class IvrsScheduleTest extends TestCase{
	StudyParticipantAssignment assignment = new StudyParticipantAssignment();
	Study study = new Study();
	StudySite site = new StudySite();
	Date preferredTime;
	Calendar cal = Calendar.getInstance();	
	Date studyParticipantCrfScheduleDate;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		assignment.setCallAmPm("pm");
		assignment.setCallHour(12);
		assignment.setCallTimeZone("America/Chicago");
		assignment.setCallMinute(30);
		study.setCallBackHour(60);
		study.setCallBackFrequency(2);
		site.setStudy(study);
		assignment.setStudySite(site);
	}
	
	public void testGetPreferredTimeInSystemTimeZone() throws Exception {
		
		assignment.setCallHour(12);
		assignment.setCallAmPm("pm");
		assignment.setCallMinute(45);
		studyParticipantCrfScheduleDate = DateUtils.parseDate("12/15/2010");
		preferredTime = new IvrsSchedule().getPreferredTimeInSystemTimeZone(assignment, studyParticipantCrfScheduleDate);
		cal.setTime(preferredTime);
		assertEquals("wrong date",15,cal.get(Calendar.DATE));
		assertEquals("wrong hour",12,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",45,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",1,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",11,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2010,cal.get(Calendar.YEAR));
		
		// test last day of the month
		assignment.setCallHour(12);
		assignment.setCallAmPm("am");
		assignment.setCallMinute(00);
		studyParticipantCrfScheduleDate = DateUtils.parseDate("02/28/2010");
		preferredTime = new IvrsSchedule().getPreferredTimeInSystemTimeZone(assignment, studyParticipantCrfScheduleDate);
		cal.setTime(preferredTime);
		assertEquals("wrong date",28,cal.get(Calendar.DATE));
		assertEquals("wrong hour",0,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",0,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",0,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",1,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2010,cal.get(Calendar.YEAR));
		
		// test Jan 1st 12 pm
		assignment.setCallHour(12);
		assignment.setCallAmPm("pm");
		assignment.setCallMinute(00);
		studyParticipantCrfScheduleDate = DateUtils.parseDate("01/01/2000");
		preferredTime = new IvrsSchedule().getPreferredTimeInSystemTimeZone(assignment, studyParticipantCrfScheduleDate);
		cal.setTime(preferredTime);
		assertEquals("wrong date",1,cal.get(Calendar.DATE));
		assertEquals("wrong hour",12,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",0,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",1,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",0,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2000,cal.get(Calendar.YEAR));
		
		// test leap year
		assignment.setCallHour(11);
		assignment.setCallAmPm("am");
		assignment.setCallMinute(0);
		studyParticipantCrfScheduleDate = DateUtils.parseDate("02/29/2008");
		preferredTime = new IvrsSchedule().getPreferredTimeInSystemTimeZone(assignment, studyParticipantCrfScheduleDate);
		cal.setTime(preferredTime);
		assertEquals("wrong date",29,cal.get(Calendar.DATE));
		assertEquals("wrong hour",11,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",0,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",0,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",1,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2008,cal.get(Calendar.YEAR));
		
	}
	
	public void testConstructor() throws Exception {
		// test eastern time zone
		assignment.setCallHour(12);
		assignment.setCallAmPm("pm");
		assignment.setCallMinute(45);
		studyParticipantCrfScheduleDate = DateUtils.parseDate("12/15/2010");
		assignment.setCallTimeZone("America/New_York");
		
		IvrsSchedule schedule = new IvrsSchedule(assignment, studyParticipantCrfScheduleDate);
		preferredTime = schedule.getPreferredCallTime();
		cal.setTime(preferredTime);
		assertEquals("wrong date",15,cal.get(Calendar.DATE));
		assertEquals("wrong hour",12,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",45,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",1,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",11,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2010,cal.get(Calendar.YEAR));
		
		// test central time zone
		assignment.setCallTimeZone("America/Chicago");
		assignment.setCallMinute(15);
		
		schedule = new IvrsSchedule(assignment, studyParticipantCrfScheduleDate);
		preferredTime = schedule.getPreferredCallTime();
		cal.setTime(preferredTime);
		assertEquals("wrong date",15,cal.get(Calendar.DATE));
		assertEquals("wrong hour",13,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",15,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",1,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",11,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2010,cal.get(Calendar.YEAR));
		
		// test mountain time zone
		assignment.setCallTimeZone("America/Denver");
		assignment.setCallMinute(0);
		schedule = new IvrsSchedule(assignment, studyParticipantCrfScheduleDate);
		preferredTime = schedule.getPreferredCallTime();
		cal.setTime(preferredTime);
		assertEquals("wrong date",15,cal.get(Calendar.DATE));
		assertEquals("wrong hour",14,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",0,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",1,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",11,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2010,cal.get(Calendar.YEAR));
		
		// test pacific time zone
		assignment.setCallTimeZone("America/Los_Angeles");
		assignment.setCallMinute(0);
		assignment.setCallHour(9);
		assignment.setCallAmPm("am");
		
		schedule = new IvrsSchedule(assignment, studyParticipantCrfScheduleDate);
		preferredTime = schedule.getPreferredCallTime();
		cal.setTime(preferredTime);
		assertEquals("wrong date",15,cal.get(Calendar.DATE));
		assertEquals("wrong hour",12,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",0,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",1,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",11,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2010,cal.get(Calendar.YEAR));
		
	}
	
	public void testConstructorCornerCases() throws Exception {
		// test eastern time zone
		assignment.setCallHour(12);
		assignment.setCallAmPm("am");
		assignment.setCallMinute(0);
		studyParticipantCrfScheduleDate = DateUtils.parseDate("12/31/2012");
		assignment.setCallTimeZone("America/New_York");
		
		IvrsSchedule schedule = new IvrsSchedule(assignment, studyParticipantCrfScheduleDate);
		preferredTime = schedule.getPreferredCallTime();
		cal.setTime(preferredTime);
		assertEquals("wrong date",31,cal.get(Calendar.DATE));
		assertEquals("wrong hour",0,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",0,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",0,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",11,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2012,cal.get(Calendar.YEAR));
		
		// test central time zone Dec 31st 2012 11 59 pm
		assignment.setCallHour(11);
		assignment.setCallAmPm("pm");
		assignment.setCallMinute(59);
		assignment.setCallTimeZone("America/Chicago");
		
		schedule = new IvrsSchedule(assignment, studyParticipantCrfScheduleDate);
		preferredTime = schedule.getPreferredCallTime();
		cal.setTime(preferredTime);
		assertEquals("wrong date",1,cal.get(Calendar.DATE));
		assertEquals("wrong hour",0,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",59,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",0,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",0,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2013,cal.get(Calendar.YEAR));
		
		// test mountain time zone
		assignment.setCallTimeZone("America/Denver");
		assignment.setCallHour(10);
		assignment.setCallAmPm("pm");
		assignment.setCallMinute(00);
		schedule = new IvrsSchedule(assignment, studyParticipantCrfScheduleDate);
		preferredTime = schedule.getPreferredCallTime();
		cal.setTime(preferredTime);
		assertEquals("wrong date",1,cal.get(Calendar.DATE));
		assertEquals("wrong hour",0,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",0,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",0,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",0,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2013,cal.get(Calendar.YEAR));
		
		
		// test pacific time zone
		assignment.setCallTimeZone("America/Los_Angeles");
		assignment.setCallHour(9);
		assignment.setCallAmPm("pm");
		assignment.setCallMinute(15);
		schedule = new IvrsSchedule(assignment, studyParticipantCrfScheduleDate);
		preferredTime = schedule.getPreferredCallTime();
		cal.setTime(preferredTime);
		assertEquals("wrong date",1,cal.get(Calendar.DATE));
		assertEquals("wrong hour",0,cal.get(Calendar.HOUR_OF_DAY));
		assertEquals("wrong minute",15,cal.get(Calendar.MINUTE));
		assertEquals("wrong time of the day",0,cal.get(Calendar.AM_PM));
		assertEquals("wrong month",0,cal.get(Calendar.MONTH));
		assertEquals("wrong year",2013,cal.get(Calendar.YEAR));
		
		
	}

}
