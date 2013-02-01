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


    private StudyParticipantAssignment assignment;

    private Study study;

    private StudySite nciStudySite;
    private Organization nci;

    private Participant participant;

    public void testGetClinicalStaff(){
    	Study study = StudyTestHelper.getDefaultStudy();
    	StudyParticipantAssignment spa = study.getArms().get(0).getStudyParticipantAssignments().get(0);
    	StudyOrganization studyOrganization = spa.getStudySite();
    	StudyOrganizationClinicalStaffQuery query = new StudyOrganizationClinicalStaffQuery();
    	query.filterByStudyOrganization(studyOrganization.getId());
    	
    	List<StudyOrganizationClinicalStaff> socsList = genericRepository.find(query);
    	List<StudyOrganizationClinicalStaff> socsTreatinPhyList = new ArrayList<StudyOrganizationClinicalStaff>();
    	List<StudyOrganizationClinicalStaff> socsResearchNursesList = new ArrayList<StudyOrganizationClinicalStaff>();
    	List<StudyOrganizationClinicalStaff> socsSitePIList = new ArrayList<StudyOrganizationClinicalStaff>();
    	List<StudyOrganizationClinicalStaff> socsSiteCRAList = new ArrayList<StudyOrganizationClinicalStaff>();
    	
    	for(StudyOrganizationClinicalStaff socs : socsList){
    		if(socs.getRole().equals(Role.TREATING_PHYSICIAN)){
    			socsTreatinPhyList.add(socs);
    		}
    		if(socs.getRole().equals(Role.NURSE)){
    			socsResearchNursesList.add(socs);
    		}
    		if(socs.getRole().equals(Role.SITE_PI)){
    			socsSitePIList.add(socs);
    		}
    		if(socs.getRole().equals(Role.SITE_CRA)){
    			socsSiteCRAList.add(socs);
    		}
    	}
    	
    	List<StudyParticipantClinicalStaff> spaTreatinPhyList = spa.getTreatingPhysicians();
    	for(StudyParticipantClinicalStaff spcs : spaTreatinPhyList){
    		assertTrue(socsTreatinPhyList.contains(spcs.getStudyOrganizationClinicalStaff()));
    	}
    	
    	List<StudyParticipantClinicalStaff> spaResearchNurseList = spa.getResearchNurses();
    	for(StudyParticipantClinicalStaff spcs : spaResearchNurseList){
    		assertTrue(socsResearchNursesList.contains(spcs.getStudyOrganizationClinicalStaff()));
    	}
    	
    	List<StudyParticipantClinicalStaff> spaSitePIList = spa.getSitePIs();
    	for(StudyParticipantClinicalStaff spcs : spaSitePIList){
    		assertTrue(socsSitePIList.contains(spcs.getStudyOrganizationClinicalStaff()));
    	}
    	
    	List<StudyParticipantClinicalStaff> spaSiteCRAList = spa.getSiteCRAs();
    	for(StudyParticipantClinicalStaff spcs : spaSiteCRAList){
    		assertTrue(socsSiteCRAList.contains(spcs.getStudyOrganizationClinicalStaff()));
    	}
    	
    }


}
