package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

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
        String status = ServletRequestUtils.getStringParameter(request, "status");
        if (RoleStatus.ACTIVE.getDisplayName().equals(status)) {
            studyOrganizationClinicalStaff.setRoleStatus(RoleStatus.IN_ACTIVE);
        }
        if (RoleStatus.IN_ACTIVE.getDisplayName().equals(status)) {
            studyOrganizationClinicalStaff.setRoleStatus(RoleStatus.ACTIVE);
        }
        studyOrganizationClinicalStaff = studyOrganizationClinicalStaffRepository.save(studyOrganizationClinicalStaff);
        RedirectView redirectView = new RedirectView("editStudy?tab=0&studyId=" + studyOrganizationClinicalStaff.getStudyOrganization().getStudy().getId());
        return new ModelAndView(redirectView);
    }

    @Required
    public void setStudyOrganizationClinicalStaffRepository(StudyOrganizationClinicalStaffRepository studyOrganizationClinicalStaffRepository) {
        this.studyOrganizationClinicalStaffRepository = studyOrganizationClinicalStaffRepository;
    }
}