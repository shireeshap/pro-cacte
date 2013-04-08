package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.IvrsCallHistory;
import gov.nih.nci.ctcae.core.domain.IvrsCallStatus;
import gov.nih.nci.ctcae.core.domain.IvrsSchedule;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.OrganizationTestHelper;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.IvrsCallHistoryQuery;
import gov.nih.nci.ctcae.core.query.IvrsScheduleQuery;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vinay Gangoli
 */
public class IvrsCallHistoryRepositoryTest extends TestDataManager {

    private StudyParticipantAssignment getStudyPartcipantAssignment(){
	    Study study = StudyTestHelper.getDefaultStudy();
	    return study.getArms().get(0).getStudyParticipantAssignments().get(0); 
    }

    private StudyParticipantCrfSchedule getStudyParticipantCrfSchedule(StudyParticipantAssignment assignment2) {
	    StudyParticipantCrf spc = assignment2.getStudyParticipantCrfs().get(0);	
	    return spc.getStudyParticipantCrfSchedules().get(0);
	}
    
    public IvrsSchedule getIvrsSchedule() {
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
        return ivrsSchedule;
    }
    
    public void testSaveIvrsCallHistory() {
    	//create ivrsCallHistory
    	Date now = Calendar.getInstance().getTime();
    	IvrsCallHistory ivrsCallHistory = new IvrsCallHistory();
    	ivrsCallHistory.setCallStatus(IvrsCallStatus.COMPLETED);
    	ivrsCallHistory.setCallTime(now);
    	ivrsCallHistory.setIvrsSchedule(getIvrsSchedule());
    	
    	//save it
    	ivrsCallHistory = ivrsCallHistoryRepository.save(ivrsCallHistory);
        assertNotNull("ivrsCallHistory was not saved to the database.", ivrsCallHistory.getId());
        assertNotNull("ivrsSchedule asociation was not saved to the database.", ivrsCallHistory.getIvrsSchedule().getId());
        assertEquals(ivrsCallHistory.getCallStatus(), IvrsCallStatus.COMPLETED);
    }
    
    public void testUpdateIvrsCallHistory() {
    	//create ivrsCallHistory
    	Date now = Calendar.getInstance().getTime();
    	IvrsCallHistory ivrsCallHistory = new IvrsCallHistory();
    	ivrsCallHistory.setCallStatus(IvrsCallStatus.COMPLETED);
    	ivrsCallHistory.setCallTime(now);
    	ivrsCallHistory.setIvrsSchedule(getIvrsSchedule());
    	
    	//save it
    	ivrsCallHistory = ivrsCallHistoryRepository.save(ivrsCallHistory);
        assertNotNull("ivrsCallHistory was not saved to the database.", ivrsCallHistory.getId());
        assertEquals(ivrsCallHistory.getCallStatus(), IvrsCallStatus.COMPLETED);
        
        //now update it
        ivrsCallHistory.setCallStatus(IvrsCallStatus.CANCELLED);
        ivrsCallHistory = ivrsCallHistoryRepository.save(ivrsCallHistory);
        assertEquals(ivrsCallHistory.getCallStatus(), IvrsCallStatus.CANCELLED);
    }

	public void testFind() {
		//create ivrsCallHistory
    	Date now = Calendar.getInstance().getTime();
    	IvrsCallHistory ivrsCallHistory = new IvrsCallHistory();
    	ivrsCallHistory.setCallStatus(IvrsCallStatus.COMPLETED);
    	ivrsCallHistory.setCallTime(now);
    	ivrsCallHistory.setIvrsSchedule(getIvrsSchedule());
    	
    	//save it
    	ivrsCallHistory = ivrsCallHistoryRepository.save(ivrsCallHistory);
        assertNotNull("ivrsCallHistory was not saved to the database.", ivrsCallHistory.getId());
        assertEquals(ivrsCallHistory.getCallStatus(), IvrsCallStatus.COMPLETED);
        Integer ivrsScheduleId = ivrsCallHistory.getIvrsSchedule().getId();
        
        //find it by filtering using StudyParticipantAssignment.
		IvrsCallHistoryQuery ivrsCallHistoryQuery = new IvrsCallHistoryQuery();
		ivrsCallHistoryQuery.filterByIvrsSchedule(ivrsScheduleId);
		IvrsCallHistory searchResultivrsCallHistory = ivrsCallHistoryRepository.find(ivrsCallHistoryQuery).get(0);
		assertNotNull(searchResultivrsCallHistory);
		assertEquals(searchResultivrsCallHistory.getCallStatus(), ivrsCallHistory.getCallStatus());
    }


	public void testFindById() {
		//create ivrsCallHistory
    	Date now = Calendar.getInstance().getTime();
    	IvrsCallHistory ivrsCallHistory = new IvrsCallHistory();
    	ivrsCallHistory.setCallStatus(IvrsCallStatus.COMPLETED);
    	ivrsCallHistory.setCallTime(now);
    	ivrsCallHistory.setIvrsSchedule(getIvrsSchedule());
    	
    	//save it
    	ivrsCallHistory = ivrsCallHistoryRepository.save(ivrsCallHistory);
        assertNotNull("ivrsCallHistory was not saved to the database.", ivrsCallHistory.getId());
        assertEquals(ivrsCallHistory.getCallStatus(), IvrsCallStatus.COMPLETED);
        Integer id = ivrsCallHistory.getId();
        
        //find it by id
		IvrsCallHistory searchResultivrsCallHistory = ivrsCallHistoryRepository.findById(id);
		assertNotNull(searchResultivrsCallHistory);
		assertEquals(searchResultivrsCallHistory.getCallStatus(), ivrsCallHistory.getCallStatus());
    }
    
    public void testDelete() {
		//create ivrsCallHistory
    	Date now = Calendar.getInstance().getTime();
    	IvrsCallHistory ivrsCallHistory = new IvrsCallHistory();
    	ivrsCallHistory.setCallStatus(IvrsCallStatus.COMPLETED);
    	ivrsCallHistory.setCallTime(now);
    	ivrsCallHistory.setIvrsSchedule(getIvrsSchedule());
    	
    	//save it
    	ivrsCallHistory = ivrsCallHistoryRepository.save(ivrsCallHistory);
        assertNotNull("ivrsCallHistory was not saved to the database.", ivrsCallHistory.getId());
        assertEquals(ivrsCallHistory.getCallStatus(), IvrsCallStatus.COMPLETED);
        
		//Note that its necessary to flush the save before executing the delete operation
		genericRepository.flush();
		Integer id = ivrsCallHistory.getId();
		IvrsCallHistory searchResultivrsCallHistory = ivrsCallHistoryRepository.findById(id);
	    assertNotNull("ivrsCallHistory was not saved to the database.", id);
	    
	    
	    //delete the created schedule
	    ivrsCallHistoryRepository.delete(searchResultivrsCallHistory);
	    IvrsCallHistory finalSearchResultivrsCallHistory = ivrsCallHistoryRepository.findById(id);
		assertNull(finalSearchResultivrsCallHistory);
	    
    }
    
	
//TOOD: this test case is yet to be implemented	
//      public ivrsCallHistory testFindSingle(ivrsCallHistoryQuery ivrsCallHistoryQuery) {
//      	return ivrsCallHistoryRepository.findSingle(ivrsCallHistoryQuery);
//      }

}


