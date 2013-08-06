package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.Errors;

/**
 * @author Vinay Gangoli
 */
public class StudyClinicalStaffTab extends SecuredTab<StudyCommand> {
    public StudyClinicalStaffTab() {
        super("study.tab.clinical_staff", "study.tab.clinical_staff", "study/study_clinical_staff");
    }

    private GenericRepository genericRepository;

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
    
    @Override
    public void onDisplay(HttpServletRequest request, StudyCommand command) {
        super.onDisplay(request, command);
    }

    @Override
    public void validate(StudyCommand command, Errors errors) {
    	removeDeletedLCRA(command);
    	removeDeletedODC(command);
    	removeDeletedPI(command);
    }
    
    private void removeDeletedLCRA(StudyCommand command){
        List<StudyOrganizationClinicalStaff> socsToRemove = new ArrayList<StudyOrganizationClinicalStaff>();
        for (Integer index : command.getLCRAIndexesToRemove()) {
        	socsToRemove.add(command.getLeadCRAs().get(index));
        }
        for (StudyOrganizationClinicalStaff socs : socsToRemove) {
        	command.getLeadCRAs().remove(socs);
        	StudyOrganizationClinicalStaff tempSocs = genericRepository.findById(StudyOrganizationClinicalStaff.class, socs.getId());
            if (tempSocs != null) {
            	command.getStudy().getLeadStudySite().getStudyOrganizationClinicalStaffs().remove(tempSocs);
            }
        }
        command.getLCRAIndexesToRemove().clear();
    }
    
    private void removeDeletedODC(StudyCommand command){
        List<StudyOrganizationClinicalStaff> socsToRemove = new ArrayList<StudyOrganizationClinicalStaff>();
        for (Integer index : command.getOdcIndexesToRemove()) {
        	socsToRemove.add(command.getOverallDataCoordinators().get(index));
        }
        for (StudyOrganizationClinicalStaff socs : socsToRemove) {
        	command.getOverallDataCoordinators().remove(socs);
        	StudyOrganizationClinicalStaff tempSocs = genericRepository.findById(StudyOrganizationClinicalStaff.class, socs.getId());
            if (tempSocs != null) {
            	command.getStudy().getDataCoordinatingCenter().getStudyOrganizationClinicalStaffs().remove(tempSocs);
            }
        }
        command.getOdcIndexesToRemove().clear();
    }
    
    private void removeDeletedPI(StudyCommand command){
        List<StudyOrganizationClinicalStaff> socsToRemove = new ArrayList<StudyOrganizationClinicalStaff>();
        for (Integer index : command.getPiIndexesToRemove()) {
        	socsToRemove.add(command.getPrincipalInvestigators().get(index));
        }
        for (StudyOrganizationClinicalStaff socs : socsToRemove) {
        	command.getPrincipalInvestigators().remove(socs);
        	StudyOrganizationClinicalStaff tempSocs = genericRepository.findById(StudyOrganizationClinicalStaff.class, socs.getId());
            if (tempSocs != null) {
            	command.getStudy().getLeadStudySite().getStudyOrganizationClinicalStaffs().remove(tempSocs);
            }
        }
        command.getPiIndexesToRemove().clear();
    }

    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        super.postProcess(request, command, errors);

        try {
            for(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : command.getOverallDataCoordinators()){
            	command.getStudy().getDataCoordinatingCenter().addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
        	}
            if(command.getStudy().getOverallDataCoordinators().size() == 0){
            	errors.reject("error", "Please enter at least one Overall Data Coordinator.");
            }
            
        	for(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : command.getLeadCRAs()){
        		command.getStudy().getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
        	}
        	if(command.getStudy().getLeadCRAs().size() == 0){
        		errors.reject("error", "Please enter at least one Lead CRA.");
        	}
        	
        	for(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : command.getPrincipalInvestigators()){
        		command.getStudy().getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
        	}
        	if(command.getStudy().getPrincipalInvestigators().size() == 0){
        		errors.reject("error", "Please enter at least one Overall PI.");
        	}
        } catch (CtcAeSystemException ex) {
            errors.reject("error", ex.getMessage());
        }
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_ADD_STUDY_CLINICAL_STAFF;
    }
}
