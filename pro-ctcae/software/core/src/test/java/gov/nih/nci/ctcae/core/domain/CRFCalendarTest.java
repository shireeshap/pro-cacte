package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

public class CRFCalendarTest extends TestDataManager{

	public void testEqualsAndHashcode(){
		CRF crf = getDefaultCrf();
		FormArmSchedule formArmSchedule = crf.getFormArmSchedules().get(0);
		
		CRF copiedCrf = crf.copy();
		FormArmSchedule copiedFormArmSchedule = copiedCrf.getFormArmSchedules().get(0);
		
		assertTrue(formArmSchedule.getCrfCalendars().get(0).equals(copiedFormArmSchedule.getCrfCalendars().get(0)));
		assertEquals(formArmSchedule.getCrfCalendars().get(0).hashCode(),copiedFormArmSchedule.getCrfCalendars().get(0).hashCode());
		
	}
	
	public void testMakeInvalid(){
		CRF crf = getDefaultCrf();
		CRFCalendar crfCalendar = crf.getFormArmSchedules().get(0).getCrfCalendars().get(0);
		
		CRF copiedCrf = crf.copy();
		CRFCalendar copiedcrfCalendar = copiedCrf.getFormArmSchedules().get(0).getCrfCalendars().get(0);
		
		assertEquals(crfCalendar, copiedcrfCalendar);
		copiedcrfCalendar.makeInvalid();
		assertNotSame(crfCalendar, copiedcrfCalendar);
	}
	
	public CRF getDefaultCrf(){
		Study study = StudyTestHelper.getDefaultStudy();
		return study.getArms().get(0).getStudyParticipantCrfs().get(0).getCrf();
	}
	
}
