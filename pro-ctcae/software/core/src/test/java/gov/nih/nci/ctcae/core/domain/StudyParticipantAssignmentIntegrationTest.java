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


}
