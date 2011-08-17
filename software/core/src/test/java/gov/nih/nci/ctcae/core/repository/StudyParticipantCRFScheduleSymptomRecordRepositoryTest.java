package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCRFScheduleSymptomRecord;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.StudyParticipantCRFScheduleSymptomRecordQuery;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Vinay Gangoli
 */
public class StudyParticipantCRFScheduleSymptomRecordRepositoryTest extends TestDataManager {

    private StudyParticipantAssignment getStudyPartcipantAssignment(){
	    Study study = StudyTestHelper.getDefaultStudy();
	    return study.getArms().get(0).getStudyParticipantAssignments().get(0); 
    }

    private StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
    	StudyParticipantAssignment assignment = getStudyPartcipantAssignment();
	    StudyParticipantCrf spc = assignment.getStudyParticipantCrfs().get(0);	
	    return spc.getStudyParticipantCrfSchedules().get(0);
	}
    
    public void testSaveIvrsSchedule() {
    	//create StudyParticipantCRFScheduleSymptomRecord
    	Date now = Calendar.getInstance().getTime();
    	String fileName = "fileName";
    	String filePath = "filePath";
    	
    	StudyParticipantCRFScheduleSymptomRecord spcrfSSR = new StudyParticipantCRFScheduleSymptomRecord();
    	spcrfSSR.setCreationDate(now);
    	spcrfSSR.setFileName(fileName);
    	spcrfSSR.setFilePath(filePath);
    	spcrfSSR.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule());
    	
    	//save it
    	spcrfSSR = studyParticipantCRFScheduleSymptomRecordRepository.save(spcrfSSR);
        assertNotNull("StudyParticipantCRFScheduleSymptomRecord was not saved to the database.", spcrfSSR.getId());
        assertEquals(spcrfSSR.getCreationDate(), now);
        assertEquals(spcrfSSR.getFileName(), fileName);
        assertEquals(spcrfSSR.getFilePath(), filePath);
    }
    
    public void testUpdateIvrsSchedule() {
    	//create StudyParticipantCRFScheduleSymptomRecord
    	Date now = Calendar.getInstance().getTime();
    	StudyParticipantCRFScheduleSymptomRecord spcrfSSR = new StudyParticipantCRFScheduleSymptomRecord();
    	spcrfSSR.setCreationDate(now);
    	spcrfSSR.setFileName("fileName");
    	spcrfSSR.setFilePath("filePath");
    	spcrfSSR.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule());
    	
    	//save it
    	spcrfSSR = studyParticipantCRFScheduleSymptomRecordRepository.save(spcrfSSR);
        assertNotNull("StudyParticipantCRFScheduleSymptomRecord was not saved to the database.", spcrfSSR.getId());
        assertEquals(spcrfSSR.getCreationDate(), now);
        
        //now update it
        spcrfSSR.setFileName("updatedFileName");
        spcrfSSR = studyParticipantCRFScheduleSymptomRecordRepository.save(spcrfSSR);
        assertEquals(spcrfSSR.getFileName(), "updatedFileName");
    }

	public void testFind() {
    	//create StudyParticipantCRFScheduleSymptomRecord
    	Date now = Calendar.getInstance().getTime();
    	StudyParticipantCRFScheduleSymptomRecord spcrfSSR = new StudyParticipantCRFScheduleSymptomRecord();
    	spcrfSSR.setCreationDate(now);
    	spcrfSSR.setFileName("fileName");
    	spcrfSSR.setFilePath("filePath");
    	spcrfSSR.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule());
    	
    	//save it
    	spcrfSSR = studyParticipantCRFScheduleSymptomRecordRepository.save(spcrfSSR);
        assertNotNull("StudyParticipantCRFScheduleSymptomRecord was not saved to the database.", spcrfSSR.getId());
        Integer id = spcrfSSR.getStudyParticipantCrfSchedule().getId();
        
        //find it by filtering using StudyParticipantAssignment.
        StudyParticipantCRFScheduleSymptomRecordQuery ivrsScheduleQuery = new StudyParticipantCRFScheduleSymptomRecordQuery();
		ivrsScheduleQuery.filterByStudyParticipantCrfSchedule(id);
		StudyParticipantCRFScheduleSymptomRecord searchResultIvrsSchedule = studyParticipantCRFScheduleSymptomRecordRepository.find(ivrsScheduleQuery).get(0);
		assertNotNull(searchResultIvrsSchedule);
		assertEquals(searchResultIvrsSchedule.getFilePath(), spcrfSSR.getFilePath());
    }


	public void testFindById() {
    	//create StudyParticipantCRFScheduleSymptomRecord
    	Date now = Calendar.getInstance().getTime();
    	StudyParticipantCRFScheduleSymptomRecord spcrfSSR = new StudyParticipantCRFScheduleSymptomRecord();
    	spcrfSSR.setCreationDate(now);
    	spcrfSSR.setFileName("fileName");
    	spcrfSSR.setFilePath("filePath");
    	spcrfSSR.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule());
    	
    	//save it
    	spcrfSSR = studyParticipantCRFScheduleSymptomRecordRepository.save(spcrfSSR);
        assertNotNull("StudyParticipantCRFScheduleSymptomRecord was not saved to the database.", spcrfSSR.getId());
        Integer id = spcrfSSR.getId();
        
        //find it by id
        StudyParticipantCRFScheduleSymptomRecord searchResultIvrsSchedule = 
        			studyParticipantCRFScheduleSymptomRecordRepository.findById(id);
		assertNotNull(searchResultIvrsSchedule);
		assertEquals(searchResultIvrsSchedule.getFilePath(), spcrfSSR.getFilePath());
    }
	
    public void testDelete() {
    	//create StudyParticipantCRFScheduleSymptomRecord
    	Date now = Calendar.getInstance().getTime();
    	StudyParticipantCRFScheduleSymptomRecord spcrfSSR = new StudyParticipantCRFScheduleSymptomRecord();
    	spcrfSSR.setCreationDate(now);
    	spcrfSSR.setFileName("fileName");
    	spcrfSSR.setFilePath("filePath");
    	spcrfSSR.setStudyParticipantCrfSchedule(getStudyParticipantCrfSchedule());
    	
    	//save it
    	spcrfSSR = studyParticipantCRFScheduleSymptomRecordRepository.save(spcrfSSR);
        assertNotNull("StudyParticipantCRFScheduleSymptomRecord was not saved to the database.", spcrfSSR.getId());
		//Note that its necessary to flush the save before executing the delete operation
		genericRepository.flush();
		Integer id = spcrfSSR.getId();
		StudyParticipantCRFScheduleSymptomRecord searchResultStudyParticipantCRFScheduleSymptomRecord = 
					studyParticipantCRFScheduleSymptomRecordRepository.findById(id);
	    assertNotNull("IvrsSchedule was not saved to the database.", id);
	    
	    
	    //delete the created schedule
	    studyParticipantCRFScheduleSymptomRecordRepository.delete(searchResultStudyParticipantCRFScheduleSymptomRecord);
	    StudyParticipantCRFScheduleSymptomRecord finalSearchResultIvrsSchedule = 
	    			studyParticipantCRFScheduleSymptomRecordRepository.findById(id);
		assertNull(finalSearchResultIvrsSchedule);
	    
    }
    
//TOOD: this test case is yet to be implemented	
//  public IvrsSchedule testFindSingle(IvrsScheduleQuery ivrsScheduleQuery) {
//  	return studyParticipantCRFScheduleSymptomRecordRepository.findSingle(ivrsScheduleQuery);
//  }

}


