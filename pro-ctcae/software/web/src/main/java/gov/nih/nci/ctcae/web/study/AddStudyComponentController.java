package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.ListValues;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * @author Vinay Kumar
 */
public class AddStudyComponentController extends AbstractController {


    public static final String COMPONENT_TYPE = "componentType";
    protected final static String STUDY_SITE_ID = "studySiteId";

    protected final static String STUDY_ORGANIZATION_CLINICAL_STAFF = "studyOrganizationClinicalStaff";
    protected final static String ROLE = "role";


//    protected final String SITE_CLINICAL_STAFF_INDEX = "clinicalStaffAssignmentIndex";

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        String action = request.getParameter("action");
        ModelAndView modelAndView = null;

        if ("delete".equals(action)) {
            deleteComponent(request);
        } else {
            modelAndView = addComponent(request);
        }
        return modelAndView;

    }

    private ModelAndView addComponent(HttpServletRequest request) throws ServletRequestBindingException {
        StudyCommand studyCommand = ControllersUtils.getStudyCommand(request);
        request.setAttribute("command", studyCommand);
        String componentType = request.getParameter(COMPONENT_TYPE);

        if (StringUtils.equals(componentType, STUDY_ORGANIZATION_CLINICAL_STAFF)) {
            String roleString = ServletRequestUtils.getRequiredStringParameter(request, ROLE);
            Role role = Role.getByCode(roleString);

            int studySiteId = ServletRequestUtils.getRequiredIntParameter(request, STUDY_SITE_ID);
            StudySite studySite = studyCommand.getStudy().getStudySiteById(studySiteId);

            StudyOrganizationClinicalStaff studyOrganizationClinicalStaff = new StudyOrganizationClinicalStaff();
            studyOrganizationClinicalStaff.setRole(role);
            studyOrganizationClinicalStaff.setStudyOrganization(studySite);

            studyCommand.addStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);

            int studyOrganizationClinicalStaffIndex = studyCommand.getStudyOrganizationClinicalStaffs().size() - 1;
            ModelAndView modelAndView = new ModelAndView("study/ajax/studyOrganizationClinicalStaffSection");
            modelAndView.addObject("roleStatusOptions", ListValues.getRoleStatusType());
            modelAndView.addObject("studyOrganizationClinicalStaff", studyOrganizationClinicalStaff);
            modelAndView.addObject("studyOrganizationClinicalStaffIndex", studyOrganizationClinicalStaffIndex);
            modelAndView.addObject("notifyOptions", ListValues.getNotificationRequired());
            return modelAndView;
        }
        return null;
    }

    private void deleteComponent(HttpServletRequest request) {
        String componentType = request.getParameter(COMPONENT_TYPE);

        if (StringUtils.equals(componentType, STUDY_ORGANIZATION_CLINICAL_STAFF)) {
            String studyOrganizationClinicalStaffIndex = request.getParameter("studyOrganizationClinicalStaffIndex");
            Integer index = Integer.parseInt(studyOrganizationClinicalStaffIndex);
            StudyCommand studyCommand = ControllersUtils.getStudyCommand(request);
            studyCommand.getStudyOrganizationClinicalStaffs().set(index, null);
        }
    }


    public AddStudyComponentController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }
}