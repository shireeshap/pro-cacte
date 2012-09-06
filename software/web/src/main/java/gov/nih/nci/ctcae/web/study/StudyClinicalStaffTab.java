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
        List<StudyOrganizationClinicalStaff> socsToRemove = new ArrayList<StudyOrganizationClinicalStaff>();
        for (Integer index : command.getCraIndexesToRemove()) {
        	socsToRemove.add(command.getLeadCRAs().get(index));
        }
        for (StudyOrganizationClinicalStaff socs : socsToRemove) {
        	command.getLeadCRAs().remove(socs);
        	StudyOrganizationClinicalStaff tempSocs = genericRepository.findById(StudyOrganizationClinicalStaff.class, socs.getId());
            if (tempSocs != null) {
            	command.getStudy().getLeadStudySite().getStudyOrganizationClinicalStaffs().remove(tempSocs);
            }
        }
        command.getCraIndexesToRemove().clear();
        //TODO: Filter out staff who are being added multiple times. Its Validation for pranksters, but worthwhile.
    }

    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        super.postProcess(request, command, errors);

        try {
            command.getStudy().getDataCoordinatingCenter().addOrUpdateStudyOrganizationClinicalStaff(command.getOverallDataCoordinator());
        } catch (CtcAeSystemException ex) {
            errors.reject("error", ex.getMessage());
        }

        try {

        	for(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : command.getLeadCRAs()){
        		command.getStudy().getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
        	}
        	if(command.getStudy().getLeadCRAs().size() == 0){
        		errors.reject("error", "Please enter at least one Lead CRA.");
    		}
        } catch (CtcAeSystemException ex) {
            errors.reject("error", ex.getMessage());
        }

        try {
            command.getStudy().getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(command.getPrincipalInvestigator());
        } catch (CtcAeSystemException ex) {
            errors.reject("error", ex.getMessage());
        }
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_ADD_STUDY_CLINICAL_STAFF;
    }
}
