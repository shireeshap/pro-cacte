package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class ReleaseFormController.
 *
 * @author Vinay Kumar
 * @since Nov 5, 2008
 */
public class ChangeStatusController extends CtcAeSimpleFormController {

    /**
     * The crf repository.
     */
    private StudyOrganizationClinicalStaffRepository studyOrganizationClinicalStaffRepository;
    private ClinicalStaffRepository clinicalStaffRepository;

    /**
     * Instantiates a new release form controller.
     */
    protected ChangeStatusController() {
        super();
        setCommandClass(StudyOrganizationClinicalStaff.class);
        setFormView("study/changeStatus");
        setSessionForm(true);

    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Integer id = ServletRequestUtils.getIntParameter(request, "id");
        StudyOrganizationClinicalStaff studyOrganizationClinicalStaff = studyOrganizationClinicalStaffRepository.findById(id);
        return studyOrganizationClinicalStaff;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        StudyOrganizationClinicalStaff studyOrganizationClinicalStaff = (StudyOrganizationClinicalStaff) command;
        ClinicalStaff clinicalStaff = null;
        String status = ServletRequestUtils.getStringParameter(request, "status");
        if (RoleStatus.ACTIVE.getDisplayName().equals(status)) {
            studyOrganizationClinicalStaff.setRoleStatus(RoleStatus.IN_ACTIVE);
        }
        if (RoleStatus.IN_ACTIVE.getDisplayName().equals(status)) {
            studyOrganizationClinicalStaff.setRoleStatus(RoleStatus.ACTIVE);
            
            Integer id = studyOrganizationClinicalStaff.getOrganizationClinicalStaff().getClinicalStaff().getId();
            clinicalStaff = clinicalStaffRepository.findById(id);
            clinicalStaff.activateClinicalStaff(studyOrganizationClinicalStaff.getRole());
        }
        
        String tabNumber= ServletRequestUtils.getStringParameter(request, "tabNumber");
        
        studyOrganizationClinicalStaff = studyOrganizationClinicalStaffRepository.save(studyOrganizationClinicalStaff);
        if(clinicalStaff != null){
        	clinicalStaffRepository.save(clinicalStaff);
        }
        RedirectView redirectView = new RedirectView("editStudy?tab=" + tabNumber + "&studyId=" +studyOrganizationClinicalStaff.getStudyOrganization().getStudy().getId());
        return new ModelAndView(redirectView);
    }

    @Required
    public void setStudyOrganizationClinicalStaffRepository(StudyOrganizationClinicalStaffRepository studyOrganizationClinicalStaffRepository) {
        this.studyOrganizationClinicalStaffRepository = studyOrganizationClinicalStaffRepository;
    }
    
    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }
}