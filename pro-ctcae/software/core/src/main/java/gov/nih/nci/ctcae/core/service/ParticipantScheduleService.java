package gov.nih.nci.ctcae.core.service;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcAECalendar;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 * User: Amey
 * Date: Dec 11, 2012
 */

/*
 * ParticipantScheduleService class.
 * create studyParticipantCrfSchedules tuples and only save the corresponding studyParticipantCrf's to avoid overhead of time and improve performance.
 * (is used as an alternative for createSchedule method of ParticipantSchedule.java for better time performance)
*/

public class ParticipantScheduleService {
 
	StudyParticipantCrfRepository studyParticipantCrfRepository;
	
	public void createAndSaveSchedules(Calendar c, Date dueDate, int cycleNumber, int cycleDay, List<String> formIds, boolean baseline, boolean armChange,ParticipantSchedule participantSchedule){
		
		List<StudyParticipantCrf> studyParticipantCrfs = participantSchedule.getStudyParticipantCrfs();
		if (c != null) {
           
            for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
            	if(createStudyParticipantCrfSchedules(c, dueDate, cycleNumber, cycleDay, formIds, baseline, armChange, studyParticipantCrf, participantSchedule)){
            		studyParticipantCrfRepository.save(studyParticipantCrf);
            	}
            }
		}
	}
	
	
	public void updateAndSaveSchedule(Calendar oldCalendar, Calendar newCalendar, List<String> formIds, LinkedHashMap<String, List<String>> resultMap, ParticipantSchedule participantSchedule) throws ParseException{
	
		List<String> updatedForms = new ArrayList<String>();
		List<String> completedForms = new ArrayList<String>();
		List<StudyParticipantCrf> studyParticipantCrfs = participantSchedule.getStudyParticipantCrfs();
		
	     StudyParticipantCrfSchedule save = null;
	     if (newCalendar != null) {
	
	         StudyParticipantCrfSchedule schToUpdate = null;
	         for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
	        	 if(updateSchedule(oldCalendar, newCalendar, formIds, updatedForms, completedForms, studyParticipantCrf)){
	        		 studyParticipantCrfRepository.save(studyParticipantCrf);
	        	 }
	        	 
	         }
	     }
	     resultMap.put("successForms", updatedForms);
	     resultMap.put("failedForms", completedForms);
	}


public boolean updateSchedule(Calendar oldCalendar, Calendar newCalendar, List<String> formIds, List<String> updatedForms,List<String> completedForms, StudyParticipantCrf studyParticipantCrf) throws ParseException{
    int alreadyExistsCount = 0;
    boolean alreadyExists = false;
    boolean alreadyPresentNewDate = false;
    boolean saveSchedules;
    StudyParticipantCrfSchedule save = null;
    StudyParticipantCrfSchedule schToUpdate = null;
    Date today = ProCtcAECalendar.getCalendarForDate(new Date()).getTime();
    
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
            		 save= studyParticipantCrfSchedule;
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
        //checking for if same form is present in current and moving date or not
        //if moving date has same form then do not process that record.
        if (alreadyExists && !alreadyPresentNewDate) {
            int dateOffsetBetweenStartAndDueDate = DateUtils.daysBetweenDates(schToUpdate.getDueDate(), schToUpdate.getStartDate());
            int dateOffsetBetweenOldAndNewStartDates = DateUtils.daysBetweenDates(newCalendar.getTime(), schToUpdate.getStartDate());
            schToUpdate.setStartDate(newCalendar.getTime());
            //update ivrsSchedules
            schToUpdate.updateIvrsSchedules(studyParticipantCrf, dateOffsetBetweenOldAndNewStartDates);

            Calendar dueCalendar = (Calendar) newCalendar.clone();
            dueCalendar.add(Calendar.DATE, dateOffsetBetweenStartAndDueDate);
            schToUpdate.setDueDate(dueCalendar.getTime());
            if (today.after(schToUpdate.getDueDate())) {
                schToUpdate.setStatus(CrfStatus.PASTDUE);
            } else {
                if (schToUpdate.getStatus().equals(CrfStatus.PASTDUE)) {
                    schToUpdate.setStatus(CrfStatus.SCHEDULED);
                }
            }

            updatedForms.add(studyParticipantCrf.getCrf().getTitle());
            return true;
            
        } else {
            completedForms.add(studyParticipantCrf.getCrf().getTitle());
        }
    }
    if(completedForms.size()>0 && save !=null)
    	save.setStatus(CrfStatus.NOTAPPLICABLE);
    
    return false;
}

	
public boolean createStudyParticipantCrfSchedules(Calendar c, Date dueDate, int cycleNumber, int cycleDay, List<String> formIds, boolean baseline, boolean armChange, StudyParticipantCrf studyParticipantCrf, ParticipantSchedule participantSchedule){
		 SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
         Date today = ProCtcAECalendar.getCalendarForDate(new Date()).getTime();
		 boolean alreadyExists = false;
				 
        if (formIds == null || (formIds != null && formIds.contains(studyParticipantCrf.getCrf().getId().toString()))) {
            StudyParticipantAssignment spa = studyParticipantCrf.getStudyParticipantAssignment();
            List<StudyParticipantCrfSchedule> spcsList = participantSchedule.getPreviousSchedules(spa, studyParticipantCrf);
            if (c.getTime().equals(studyParticipantCrf.getCrf().getEffectiveStartDate()) || c.getTime().after(studyParticipantCrf.getCrf().getEffectiveStartDate())) {
                if (studyParticipantCrf.getCrf().getParentCrf() != null && studyParticipantCrf.getCrf().getParentCrf().getStudyParticipantCrfs() != null) {
                    if (spcsList != null && spcsList.size() > 0) {
                        for (StudyParticipantCrfSchedule spcs : spcsList) {
                            if (sdf.format(spcs.getStartDate()).equals(sdf.format(c.getTime())) && (spcs.getStatus().equals(CrfStatus.INPROGRESS) || spcs.getStatus().equals(CrfStatus.COMPLETED) || spcs.getStatus().equals(CrfStatus.PASTDUE))) {
                                if (spcs.getStudyParticipantCrf().getCrf().equals(studyParticipantCrf.getCrf()) || spcs.getStudyParticipantCrf().getCrf().checkForParent(studyParticipantCrf.getCrf())) {
                                    alreadyExists = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (armChange) {
                    for (StudyParticipantCrf oldStudyParticipantCrf : spa.getStudyParticipantCrfs()) {
                        if (oldStudyParticipantCrf.getId() != null && oldStudyParticipantCrf.getCrf().equals(studyParticipantCrf.getCrf())) {
                            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : oldStudyParticipantCrf.getStudyParticipantCrfSchedules()) {
                                if (sdf.format(studyParticipantCrfSchedule.getStartDate()).equals(sdf.format(c.getTime())) && (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.INPROGRESS) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.PASTDUE))) {
                                    alreadyExists = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!alreadyExists && spa.getArm().equals(studyParticipantCrf.getArm())) {
                    StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
                    studyParticipantCrf.addStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
                    if (baseline) {
                        studyParticipantCrfSchedule.setStartDate(studyParticipantCrf.getStartDate());
                    } else {
                        studyParticipantCrfSchedule.setStartDate(c.getTime());
                    }
                    //check if due date is not fixed, then check the values from arm schedules
                    Date dueDateNew = dueDate;
                    if (dueDate == null) {
                        dueDateNew = participantSchedule.getDueDateForFormSchedule(c, studyParticipantCrf);
                    }
                    studyParticipantCrfSchedule.setDueDate(dueDateNew);

                    if (today.after(dueDateNew) && !today.equals(dueDateNew)) {
                        studyParticipantCrfSchedule.setStatus(CrfStatus.NOTAPPLICABLE);
                    }
                    
                    if(spa.getStatus() != null && spa.getStatus().equals(RoleStatus.ONHOLD)){
                    	studyParticipantCrfSchedule.setStatus(CrfStatus.ONHOLD);
                    }
                    if (cycleNumber != -1) {
                        studyParticipantCrfSchedule.setCycleNumber(cycleNumber);
                        studyParticipantCrfSchedule.setCycleDay(cycleDay);
                    }
                    if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                        studyParticipantCrfSchedule.setHoliday(true);
                    }
                    if (studyParticipantCrf.getCrf().getCreateBaseline() && cycleDay == 1 && cycleNumber == 1) {
                        studyParticipantCrfSchedule.setBaseline(true);
                    }
                    if (participantSchedule.isSpCrfScheduleAvailable(studyParticipantCrfSchedule)) {
                        //call getter on schedule for available forms
                        studyParticipantCrfSchedule.getStudyParticipantCrfItems();
                    }
                    participantSchedule.addIvrsSchedules(studyParticipantCrfSchedule, studyParticipantCrf);
                    
                    return true;
                }
                else
                	return false;

            }
        }
        return false;
	}

@Required
public void setStudyParticipantCrfRepository(StudyParticipantCrfRepository studyParticipantCrfRepository) {
    this.studyParticipantCrfRepository = studyParticipantCrfRepository;
}
}
