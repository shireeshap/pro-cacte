package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//
/**
 * @author Vinay Kumar
 */
public class AddStudyComponentController extends AbstractController {


    public static final String SITE_COMPONENT_TYPE = "site";
    public static final String ROLE_COMPONENT_TYPE = "role";

    private ClinicalStaffRepository clinicalStaffRepository;
    public static final String COMPONENT_TYPE = "componentTyep";
    protected final String STUDY_SITE_ID = "studySiteId";
    protected final String CLINICAL_STAFF_ID = "clinicalStaffId";
    protected final String CLINICAL_STAFF_ASSIGNMENT_INDEX = "clinicalStaffAssignmentIndex";


//    protected final String SITE_CLINICAL_STAFF_INDEX = "clinicalStaffAssignmentIndex";

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = null;
        StudyCommand studyCommand = ControllersUtils.getStudyCommand(request);


        //FIXME:SAURABH delete study site will not work in edit mode

        String componentTyep = request.getParameter(COMPONENT_TYPE);

        if (StringUtils.equals(componentTyep, SITE_COMPONENT_TYPE)) {

            Integer studySiteId = ServletRequestUtils.getRequiredIntParameter(request, STUDY_SITE_ID);
            Integer clinicalStaffId = ServletRequestUtils.getRequiredIntParameter(request, CLINICAL_STAFF_ID);

            ClinicalStaff clinicalStaff = clinicalStaffRepository.findById(clinicalStaffId);

            StudySite studySite = studyCommand.getStudy().getStudySiteById(studySiteId);
            Organization organization = studySite.getOrganization();
            Integer organizationId = organization.getId();

            ClinicalStaffAssignment clinicalStaffAssignment = new ClinicalStaffAssignment();
            clinicalStaffAssignment.setDomainObjectClass(StudySite.class.getName());
            clinicalStaffAssignment.setDomainObjectId(studySiteId);
            clinicalStaffAssignment.setDisplayName(organization.getDisplayName());
            clinicalStaffAssignment.setClinicalStaff(clinicalStaff);

            studyCommand.addClinicalStaffAssignment(clinicalStaffAssignment);

            modelAndView = new ModelAndView("study/clinicalStaffAssignmentSection");

            modelAndView.addObject("clinicalStaffAssignment", clinicalStaffAssignment);
            modelAndView.addObject("organizationId", organizationId);

            modelAndView.addObject("clinicalStaffAssignmentIndex", studyCommand.getClinicalStaffAssignments().size() - 1);


        } else if (StringUtils.equals(componentTyep, ROLE_COMPONENT_TYPE)) {


            Integer clinicalStaffAssignmentIndex = ServletRequestUtils.getRequiredIntParameter(request, CLINICAL_STAFF_ASSIGNMENT_INDEX);

            ClinicalStaffAssignment clinicalStaffAssignment = studyCommand.getClinicalStaffAssignments().get(clinicalStaffAssignmentIndex);


            ClinicalStaffAssignmentRole clinicalStaffAssignmentRole = new ClinicalStaffAssignmentRole();
            clinicalStaffAssignment.addClinicalStaffAssignmentRole(clinicalStaffAssignmentRole);

            List<ClinicalStaffAssignmentRole> clinicalStaffAssignmentRoles = clinicalStaffAssignment.getClinicalStaffAssignmentRoles();

            Integer clinicalStaffAssignmentRoleIndex = clinicalStaffAssignmentRoles.size() - 1;

            modelAndView = new ModelAndView("study/clinicalStaffAssignmentRoleSection");
            modelAndView.addObject("clinicalStaffAssignmentRole", clinicalStaffAssignmentRole);
            modelAndView.addObject("clinicalStaffAssignmentRoleIndex", clinicalStaffAssignmentRoleIndex);
            modelAndView.addObject("clinicalStaffAssignmentIndex", clinicalStaffAssignmentIndex);


            modelAndView.addObject("roleStatus", ListValues.getRoleStatusType());


        }
        return modelAndView;
    }

    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

    public AddStudyComponentController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }
}