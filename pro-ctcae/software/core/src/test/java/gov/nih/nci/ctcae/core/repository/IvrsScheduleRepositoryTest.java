package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.IvrsScheduleQuery;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Vinay Gangoli
 */
public class IvrsScheduleRepositoryTest extends TestDataManager {

	private void addStudyParticipantAssignment(Study study){
		Participant participant = Fixture.createParticipantWithStudyAssignment("John", "Doe", "identifier", study.getStudySites().get(0));
        StudyParticipantAssignment studyParticipantAssignment = participant.getStudyParticipantAssignments().get(0);
        CRF crf =    studyParticipantAssignment.getStudyParticipantCrfs().get(0).getCrf();
        crf.setStudy(study);
        crfRepository.save(studyParticipantAssignment.getStudyParticipantCrfs().get(0).getCrf());
		participantRepository.save(participant);


	//	StudyParticipantAssignment spa= studyParticipantAssignmentRepository.save(participant.getStudyParticipantAssignments().get(0));
		study.getArms().get(0).getStudyParticipantAssignments().add(participant.getStudyParticipantAssignments().get(0));
	}
	
    private StudyParticipantAssignment getStudyPartcipantAssignment(){
	    Study study = StudyTestHelper.getDefaultStudy();
	    addStudyParticipantAssignment(study);
	    return study.getArms().get(0).getStudyParticipantAssignments().get(0); 
    }

    private StudyParticipantCrfSchedule getStudyParticipantCrfSchedule(StudyParticipantAssignment assignment2) {
	    StudyParticipantCrf spc = assignment2.getStudyParticipantCrfs().get(0);	
	    return spc.getStudyParticipantCrfSchedules().get(0);
	}
    
    public void testSaveIvrsSchedule() {
    	//create ivrsSchedule
    	Date now = Calendar.getInstance().getTime();
    	IvrsSchedule ivrsSchedule = new IvrsSchedule();
    	ivrsSchedule.setCallCount(2);
    	ivrsSchedule.setCallStatus(IvrsCallStatus.COMPLETED);
    	ivrsSchedule.setNextCallTime(now);
    	ivrsSchedule.setPreferredCallTime(now);
    	ivrsSchedule.setRetryPeriod(5);
    	
    	StudyParticipantAssignment assignment = getStudyPartcipantAssignment();
    	ivrsSchedule.setStudyParticipantAssignment(assignment);
    	ivrsSchedule.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule(assignment));
    	
    	//save it
    	ivrsSchedule = ivrsScheduleRepository.save(ivrsSchedule);
        assertNotNull("IvrsSchedule was not saved to the database.", ivrsSchedule.getId());
    }
    
    public void testUpdateIvrsSchedule() {
    	//create ivrsSchedule
    	Date now = Calendar.getInstance().getTime();
    	IvrsSchedule ivrsSchedule = new IvrsSchedule();
    	ivrsSchedule.setCallCount(2);
    	ivrsSchedule.setCallStatus(IvrsCallStatus.COMPLETED);
    	ivrsSchedule.setNextCallTime(now);
    	ivrsSchedule.setPreferredCallTime(now);
    	ivrsSchedule.setRetryPeriod(5);
    	
    	StudyParticipantAssignment assignment = getStudyPartcipantAssignment();
    	ivrsSchedule.setStudyParticipantAssignment(assignment);
    	ivrsSchedule.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule(assignment));
    	
    	//save it
    	ivrsSchedule = ivrsScheduleRepository.save(ivrsSchedule);
        assertNotNull("IvrsSchedule was not saved to the database.", ivrsSchedule.getId());
        assertEquals(ivrsSchedule.getCallStatus(), IvrsCallStatus.COMPLETED);
        
        //now update it
        ivrsSchedule.setCallStatus(IvrsCallStatus.CANCELLED);
        ivrsSchedule = ivrsScheduleRepository.save(ivrsSchedule);
        assertEquals(ivrsSchedule.getCallStatus(), IvrsCallStatus.CANCELLED);
    }

	public void testFind() {
    	//create ivrsSchedule
		Date now = Calendar.getInstance().getTime();
		IvrsSchedule ivrsSchedule = new IvrsSchedule();
    	ivrsSchedule.setCallCount(2);
    	ivrsSchedule.setCallStatus(IvrsCallStatus.COMPLETED);
    	ivrsSchedule.setNextCallTime(now);
    	ivrsSchedule.setPreferredCallTime(now);
    	ivrsSchedule.setRetryPeriod(5);
    	
    	StudyParticipantAssignment assignment = getStudyPartcipantAssignment();
    	ivrsSchedule.setStudyParticipantAssignment(assignment);
    	ivrsSchedule.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule(assignment));
    	
    	ivrsSchedule = ivrsScheduleRepository.save(ivrsSchedule);
        assertNotNull("IvrsSchedule was not saved to the database.", ivrsSchedule.getId());
        
        //find it by filtering using StudyParticipantAssignment.
		IvrsScheduleQuery ivrsScheduleQuery = new IvrsScheduleQuery();
		ivrsScheduleQuery.filterByStudyParticipantAssignment(assignment.getId());
		IvrsSchedule searchResultIvrsSchedule = ivrsScheduleRepository.find(ivrsScheduleQuery).get(0);
		assertNotNull(searchResultIvrsSchedule);
		assertEquals(searchResultIvrsSchedule.getCallStatus(), ivrsSchedule.getCallStatus());
    }
	
	public void testFindByTimeAndStatus() {
    	//create ivrsSchedule
		Date startDate = Calendar.getInstance().getTime();
		Date now = Calendar.getInstance().getTime();
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.MINUTE, 5);
		IvrsSchedule ivrsSchedule = new IvrsSchedule();
    	ivrsSchedule.setCallCount(2);
    	ivrsSchedule.setCallStatus(IvrsCallStatus.PENDING);
    	ivrsSchedule.setNextCallTime(now);
    	ivrsSchedule.setPreferredCallTime(now);
    	ivrsSchedule.setRetryPeriod(5);
    	
    	StudyParticipantAssignment assignment = getStudyPartcipantAssignment();
    	ivrsSchedule.setStudyParticipantAssignment(assignment);
    	ivrsSchedule.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule(assignment));
    	
    	ivrsSchedule = ivrsScheduleRepository.save(ivrsSchedule);
        assertNotNull("IvrsSchedule was not saved to the database.", ivrsSchedule.getId());
        
        //find it by filtering using StudyParticipantAssignment.
		IvrsScheduleQuery ivrsScheduleQuery = new IvrsScheduleQuery();
		ivrsScheduleQuery.filterByStatus(IvrsCallStatus.PENDING);
		ivrsScheduleQuery.filterByDate(startDate, endDate.getTime());
		IvrsSchedule searchResultIvrsSchedule = ivrsScheduleRepository.find(ivrsScheduleQuery).get(0);
		assertNotNull(searchResultIvrsSchedule);
		assertEquals(searchResultIvrsSchedule.getCallStatus(), ivrsSchedule.getCallStatus());
    }


	public void testFindById() {
    	//create ivrsSchedule
		Date now = Calendar.getInstance().getTime();
		IvrsSchedule ivrsSchedule = new IvrsSchedule();
    	ivrsSchedule.setCallCount(2);
    	ivrsSchedule.setCallStatus(IvrsCallStatus.COMPLETED);
    	ivrsSchedule.setNextCallTime(now);
    	ivrsSchedule.setPreferredCallTime(now);
    	ivrsSchedule.setRetryPeriod(5);
    	
    	StudyParticipantAssignment assignment = getStudyPartcipantAssignment();
    	ivrsSchedule.setStudyParticipantAssignment(assignment);
    	ivrsSchedule.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule(assignment));
    	
    	ivrsSchedule = ivrsScheduleRepository.save(ivrsSchedule);
        assertNotNull("IvrsSchedule was not saved to the database.", ivrsSchedule.getId());
        Integer id = ivrsSchedule.getId();
        
        //find it by id
		IvrsSchedule searchResultIvrsSchedule = ivrsScheduleRepository.findById(id);
		assertNotNull(searchResultIvrsSchedule);
		assertEquals(searchResultIvrsSchedule.getCallStatus(), ivrsSchedule.getCallStatus());
    }
	
    public void testDelete() {
    	//create ivrsSchedule
		Date now = Calendar.getInstance().getTime();
		IvrsSchedule ivrsSchedule = new IvrsSchedule();
		ivrsSchedule.setCallCount(2);
		ivrsSchedule.setCallStatus(IvrsCallStatus.COMPLETED);
		ivrsSchedule.setNextCallTime(now);
		ivrsSchedule.setPreferredCallTime(now);
		ivrsSchedule.setRetryPeriod(5);
		
		StudyParticipantAssignment assignment = getStudyPartcipantAssignment();
		ivrsSchedule.setStudyParticipantAssignment(assignment);
		ivrsSchedule.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule(assignment));
		
		ivrsSchedule = ivrsScheduleRepository.save(ivrsSchedule);
		//Note that its necessary to flush the save before executing the delete operation
		genericRepository.flush();
		Integer id = ivrsSchedule.getId();
		IvrsSchedule searchResultIvrsSchedule = ivrsScheduleRepository.findById(id);
	    assertNotNull("IvrsSchedule was not saved to the database.", id);
	    
	    
	    //delete the created schedule
	    ivrsScheduleRepository.delete(searchResultIvrsSchedule);
	    IvrsSchedule finalSearchResultIvrsSchedule = ivrsScheduleRepository.findById(id);
		assertNull(finalSearchResultIvrsSchedule);
	    
    }
    
    public void testUpdatesToStudyParticipantAssignment(){
    	//create ivrsSchedule
    	Date now = Calendar.getInstance().getTime();
    	IvrsSchedule ivrsSchedule = new IvrsSchedule();
    	ivrsSchedule.setCallCount(2);
    	ivrsSchedule.setCallStatus(IvrsCallStatus.COMPLETED);
    	ivrsSchedule.setNextCallTime(now);
    	ivrsSchedule.setPreferredCallTime(now);
    	ivrsSchedule.setRetryPeriod(5);
    	
    	StudyParticipantAssignment assignment = getStudyPartcipantAssignment();
    	ivrsSchedule.setStudyParticipantAssignment(assignment);
    	ivrsSchedule.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule(assignment));
    	
    	//save it
    	ivrsSchedule = ivrsScheduleRepository.save(ivrsSchedule);
        assertNotNull("IvrsSchedule was not saved to the database.", ivrsSchedule.getId());
    	
    	
    }
    
    
  
    
//TOOD: this test case is yet to be implemented	
//  public IvrsSchedule testFindSingle(IvrsScheduleQuery ivrsScheduleQuery) {
//  	return ivrsScheduleRepository.findSingle(ivrsScheduleQuery);
//  }

    
    
}




