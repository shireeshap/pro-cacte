package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import java.util.Date;
import java.util.List;


/**
 * @author AmeyS
 * Class FormSubmissionHelper.
 * Used to present next available survey (if any) for a participant to answer, after a participant completes a survey.
 * Is used by ParticipantInboxController.
 */
public class FormSubmissionHelper{
	
	/* Check whether a survey is InProgess or Scheduled and crf is not hiden and survey is not pastDue and 
     * survey is available to start today. i.e it is ready for the system to be administered.
     */
	 public static boolean isSurveyReadyToBeAdministed(StudyParticipantCrfSchedule spcrfs, String timeZoneId){
	    	return (!isCrfHidden(spcrfs) && !isPastDueDate(spcrfs, timeZoneId));
	 }

	 //Skip this check to accommodate PRKC-2357 
	 public static boolean isStatusInProgressOrScheduled(StudyParticipantCrfSchedule spcrfs){
	    	return (spcrfs.getStatus().equals(CrfStatus.INPROGRESS) || spcrfs.getStatus().equals(CrfStatus.SCHEDULED));
	 }
	    
     public static boolean isCrfHidden(StudyParticipantCrfSchedule spcrfs){
    	return spcrfs.getStudyParticipantCrf().getCrf().isHidden();
     }
    
     public static boolean isPastDueDate(StudyParticipantCrfSchedule spcrfs, String timeZoneId){
    	Date today = DateUtils.getDateOnlyInTimeZone(new Date(), timeZoneId);
    	// Not of (today >= startDate && today <= dueDate)
    	return !((DateUtils.compareDate(today, spcrfs.getStartDate()) >= 0) && 
    			(DateUtils.compareDate(today, spcrfs.getDueDate()) <= 0));
     }
     
     public static Integer getNextAvailableSurvey(List<StudyParticipantCrfSchedule> spcrfsList, String timeZoneId){
     	Integer id = null;
     	for(StudyParticipantCrfSchedule spcrfs : spcrfsList){
 			if(isSurveyReadyToBeAdministed(spcrfs, timeZoneId)){
 				id = spcrfs.getId();
 				break;
 			}
 		}
     	return id;
     }
}
