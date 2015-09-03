package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.List;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;

/**
 * @author Vinay Kumar
 * @since Nov 21, 2008
 */
public class StudyParticipantAssignmentIntegrationTest extends TestDataManager {

    public void testGetClinicalStaff(){
    	Study study = StudyTestHelper.getDefaultStudy();
    	StudyParticipantAssignment spa = study.getArms().get(0).getStudyParticipantAssignments().get(0);
    	StudyOrganization studyOrganization = spa.getStudySite();
    	StudyOrganizationClinicalStaffQuery query = new StudyOrganizationClinicalStaffQuery();
    	query.filterByStudyOrganization(studyOrganization.getId());
    	
    	List<StudyOrganizationClinicalStaff> socsTreatinPhyList = new ArrayList<StudyOrganizationClinicalStaff>();
    	List<StudyOrganizationClinicalStaff> socsResearchNursesList = new ArrayList<StudyOrganizationClinicalStaff>();
    	List<StudyOrganizationClinicalStaff> socsSitePIList = new ArrayList<StudyOrganizationClinicalStaff>();
    	List<StudyOrganizationClinicalStaff> socsSiteCRAList = new ArrayList<StudyOrganizationClinicalStaff>();
    	
    	socsTreatinPhyList.addAll(studyOrganization.getTreatingPhysicians());
    	List<StudyParticipantClinicalStaff> spaTreatinPhyList = spa.getTreatingPhysicians();
    	for(StudyParticipantClinicalStaff spcs : spaTreatinPhyList){
    		assertTrue(socsTreatinPhyList.contains(spcs.getStudyOrganizationClinicalStaff()));
    	}
    	
    	socsResearchNursesList.addAll(studyOrganization.getResearchNurses());
    	List<StudyParticipantClinicalStaff> spaResearchNurseList = spa.getResearchNurses();
    	for(StudyParticipantClinicalStaff spcs : spaResearchNurseList){
    		assertTrue(socsResearchNursesList.contains(spcs.getStudyOrganizationClinicalStaff()));
    	}
    	
    	socsSitePIList.addAll(studyOrganization.getSitePIs());
    	List<StudyParticipantClinicalStaff> spaSitePIList = spa.getSitePIs();
    	for(StudyParticipantClinicalStaff spcs : spaSitePIList){
    		assertTrue(socsSitePIList.contains(spcs.getStudyOrganizationClinicalStaff()));
    	}
    	
    	socsSiteCRAList.addAll(studyOrganization.getSiteCRAs());
    	List<StudyParticipantClinicalStaff> spaSiteCRAList = spa.getSiteCRAs();
    	for(StudyParticipantClinicalStaff spcs : spaSiteCRAList){
    		assertTrue(socsSiteCRAList.contains(spcs.getStudyOrganizationClinicalStaff()));
    	}
    	
    }
    
    public void testRemoveAllSchedules(){
    	StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
    	StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
    	List<Integer> markCompleted = new ArrayList<Integer>();
    	markCompleted.add(4);
    	addStudyParticipantCrfWithDummySchedules(studyParticipantAssignment, studyParticipantCrf, 5, markCompleted);
    	
    	int totalNumberOfSchedules = getAllSchedulesForStudyParticipantAssignment(studyParticipantAssignment).size();
    	// expect total no of surveys to be 5 (4 SCHEDULED and 1 COMPLETED)
    	assertEquals(5, totalNumberOfSchedules);
    	
    	studyParticipantAssignment.removeAllSchedules();
    	totalNumberOfSchedules = getAllSchedulesForStudyParticipantAssignment(studyParticipantAssignment).size();
    	// expect total no of surveys to be 1 (1 COMPLETED)
    	assertEquals(1, totalNumberOfSchedules);
    }
    
    public void testRemoveSpCrfsIfNoCompletedSchedules(){
    	StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
    	StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
    	StudyParticipantCrf otherStudyParticipantCrf = new StudyParticipantCrf();
    	
    	List<Integer> markCompleted = new ArrayList<Integer>();
    	markCompleted.clear();
    	addStudyParticipantCrfWithDummySchedules(studyParticipantAssignment, otherStudyParticipantCrf, 2, markCompleted);
    	markCompleted.add(4);
    	addStudyParticipantCrfWithDummySchedules(studyParticipantAssignment, studyParticipantCrf, 5, markCompleted);
    	
    	
    	int totalNumberOfSchedules = getAllSchedulesForStudyParticipantAssignment(studyParticipantAssignment).size();
    	int totalNumberOfStudyParticipantCrfs = studyParticipantAssignment.getStudyParticipantCrfs().size();
    	// expect total no of surveys to be 5 (4 SCHEDULED and 1 COMPLETED)
    	assertEquals(7, totalNumberOfSchedules);
    	assertEquals(2, totalNumberOfStudyParticipantCrfs);
    	
    	try{
    		studyParticipantAssignment.removeSpCrfsIfNoCompletedSchedules();
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	totalNumberOfSchedules = getAllSchedulesForStudyParticipantAssignment(studyParticipantAssignment).size();
    	totalNumberOfStudyParticipantCrfs = studyParticipantAssignment.getStudyParticipantCrfs().size();
    	// expect total no of surveys to be 1 (1 COMPLETED)
    	assertEquals(1, totalNumberOfSchedules);
    	assertEquals(1, totalNumberOfStudyParticipantCrfs);
    }
    
    private List<StudyParticipantCrfSchedule> getAllSchedulesForStudyParticipantAssignment(StudyParticipantAssignment studyParticipantAssignment){
    	List<StudyParticipantCrfSchedule> allSchedules = new ArrayList<StudyParticipantCrfSchedule>();
    	for(StudyParticipantCrf spcrf: studyParticipantAssignment.getStudyParticipantCrfs()){
    		allSchedules.addAll(spcrf.getStudyParticipantCrfSchedules());
    	}
    	return allSchedules;
    }
    
    private void addStudyParticipantCrfWithDummySchedules(StudyParticipantAssignment studyParticipantAssignment, StudyParticipantCrf studyParticipantCrf,int noOfSchedules, List<Integer> markCompleted){
    	// Create 5 dummy schedules for studyParticipantCrf
    	List<StudyParticipantCrfSchedule> schedules = createDummyStudyParticipantCrfSchedules(noOfSchedules, markCompleted);
    	for(StudyParticipantCrfSchedule spcrfs : schedules){
    		studyParticipantCrf.addStudyParticipantCrfSchedule(spcrfs);
    	}
    	studyParticipantCrf.setCrf(new CRF());
    	studyParticipantCrf.setStudyParticipantAssignment(studyParticipantAssignment);
    	studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);
    }
    
    private List<StudyParticipantCrfSchedule> createDummyStudyParticipantCrfSchedules(int size, List<Integer> markCompleted){
    	List<StudyParticipantCrfSchedule> spcrfsList = new ArrayList<StudyParticipantCrfSchedule>();
    	StudyParticipantCrfSchedule spcrfs;
    	for(int i=0 ;i<size; i++){
    		spcrfs = new StudyParticipantCrfSchedule();
    		if(markCompleted.contains(Integer.valueOf(i))){
    			spcrfs.setStatus(CrfStatus.COMPLETED);
    		} else {
    			spcrfs.setStatus(CrfStatus.SCHEDULED);
    		}
    		spcrfsList.add(spcrfs);
    	}
    	return spcrfsList;
    }
}
