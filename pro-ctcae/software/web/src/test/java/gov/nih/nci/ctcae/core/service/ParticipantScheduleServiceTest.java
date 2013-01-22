package gov.nih.nci.ctcae.core.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.asteriskjava.util.DateUtil;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;

public class ParticipantScheduleServiceTest extends TestDataManager{
	
	ParticipantScheduleService participantScheduleService = new ParticipantScheduleService();
	StudyParticipantCrf defaultStudyParticippantCrf;
	StudyParticipantCrfSchedule spcrfs;
	StudyParticipantCrfScheduleQuery query = new StudyParticipantCrfScheduleQuery(true);
	ParticipantSchedule participantSchedule = new ParticipantSchedule();
	
	public void testCreateAndSaveSchedules(){
		defaultStudyParticippantCrf = getStudyPartcipantCrf();
		List<String> formIds = new ArrayList<String>();
		formIds.add(defaultStudyParticippantCrf.getCrf().getId().toString());
		Date today = DateUtils.addDaysToDate(defaultStudyParticippantCrf.getCrf().getEffectiveStartDate(), 2); 
		Calendar c = Calendar.getInstance();
		participantSchedule.addStudyParticipantCrf(defaultStudyParticippantCrf);
		participantScheduleService.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
		
		// studyParticipantCrf realease date is prior to form scheduling date. Expect studyParticipantCrfSchedule to be saved in the database.
		Long initialcount = studyParticipantCrfScheduleRepository.findWithCount(query);
		c.setTime(today);
		participantScheduleService.createAndSaveSchedules(c, null, -1, -1, formIds, false, false, participantSchedule);
		Long afterSaveCount = studyParticipantCrfScheduleRepository.findWithCount(query);
		assertEquals(++initialcount, afterSaveCount);

		
		// studyParticipantCrf realease date is greater than to form scheduling date. Expect studyParticipantCrfSchedule not to be saved in the database.
		initialcount = studyParticipantCrfScheduleRepository.findWithCount(query);
		today = DateUtils.addDaysToDate(defaultStudyParticippantCrf.getCrf().getEffectiveStartDate(), -2);
		c.setTime(today);
		participantScheduleService.createAndSaveSchedules(c, null, -1, -1, formIds, false, false, participantSchedule);
		afterSaveCount = studyParticipantCrfScheduleRepository.findWithCount(query);
		assertEquals(initialcount, afterSaveCount);
	}
	
	
	public void testUpdateAndSaveSchedule() throws ParseException{
		defaultStudyParticippantCrf = getStudyPartcipantCrf();
		List<String> formIds = new ArrayList<String>();
		formIds.add(defaultStudyParticippantCrf.getCrf().getId().toString());
		LinkedHashMap<String, List<String>> resultMap = new LinkedHashMap<String, List<String>>();
		Date today = defaultStudyParticippantCrf.getCrf().getEffectiveStartDate(); 
		Calendar c = Calendar.getInstance();
		Calendar newDate = Calendar.getInstance();
		participantSchedule.addStudyParticipantCrf(defaultStudyParticippantCrf);
		participantScheduleService.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
		
		// Create a new schedule on present day.
		c.setTime(today);
		participantScheduleService.createAndSaveSchedules(c, null, -1, -1, formIds, false, false, participantSchedule);
		newDate.setTime(DateUtils.addDaysToDate(today, 6));
		
		// Attempt to update the schedule start date to a further date. Expect the schedule to be updated and saved in database.
		spcrfs = getScheduleToUpdate(c, newDate, formIds, defaultStudyParticippantCrf);
		if(spcrfs != null){
			participantScheduleService.updateAndSaveSchedule(c, newDate, formIds, resultMap, participantSchedule);
			assertTrue((newDate.getTime().equals(studyParticipantCrfScheduleRepository.findById(spcrfs.getId()).getStartDate())));
		}

		// Attempt to update the schedule start date to same dsate as already scheduled. Expect the schedule not to be updated and saved in database.
		spcrfs = participantScheduleService.updateSchedule(newDate, newDate, formIds, new ArrayList<String>(),  new ArrayList<String>(), defaultStudyParticippantCrf);
		assertNull(spcrfs);
	}
	
	private StudyParticipantCrf getStudyPartcipantCrf(){
	    Study study = StudyTestHelper.getDefaultStudy();
	    StudyParticipantAssignment assignment = study.getArms().get(0).getStudyParticipantAssignments().get(0);
	    return assignment.getStudyParticipantCrfs().get(0);	
    }
	
	public StudyParticipantCrfSchedule getScheduleToUpdate(Calendar oldCalendar, Calendar newCalendar, List<String> formIds, StudyParticipantCrf studyParticipantCrf) throws ParseException{
		StudyParticipantCrfSchedule schToUpdate = null;
		boolean alreadyExists = false;
		boolean alreadyPresentNewDate = false;
		   
		    if (formIds.contains(studyParticipantCrf.getCrf().getId().toString())) {
		        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
		            String scheduleDate = DateUtils.format(studyParticipantCrfSchedule.getStartDate());
		            String calendarDate = DateUtils.format(oldCalendar.getTime());
		            String calendarNewDate = DateUtils.format(newCalendar.getTime());
		            if(calendarDate.equals(scheduleDate) && studyParticipantCrfSchedule.getStatus().equals(CrfStatus.NOTAPPLICABLE)){
		            	 DateFormat dateFormat= new SimpleDateFormat("mm/dd/yyyy");
		            	 Date newscheduledDate= dateFormat.parse(calendarNewDate);
		            	 Date oldscheduledDate= dateFormat.parse(scheduleDate);
		            	 if(DateUtils.compareDate(newscheduledDate,oldscheduledDate) > 0){
		            		 studyParticipantCrfSchedule.setStatus(CrfStatus.SCHEDULED);
		                 }
		            }
		            if (calendarDate.equals(scheduleDate) && !studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED)) {
		                schToUpdate = studyParticipantCrfSchedule;
		                alreadyExists = true;
		            }
		            
		            if (calendarNewDate.equals(scheduleDate)) {
		                schToUpdate = studyParticipantCrfSchedule;
		                alreadyPresentNewDate = true;
		            }
		            if (alreadyExists && alreadyPresentNewDate) {
		                break;
		            }
		        }
		  }
		    
		  if (alreadyExists && !alreadyPresentNewDate) {
			  return schToUpdate;
		  }
		  return null;
	}
	
}
