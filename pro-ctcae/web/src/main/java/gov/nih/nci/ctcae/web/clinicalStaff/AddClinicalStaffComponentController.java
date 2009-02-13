package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignment;
import gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignmentRole;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.ListValues;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class AddSiteController.
 *
 * @author Mehul Gulati
 *         Date: Oct 30, 2008
 */
public class AddClinicalStaffComponentController extends AbstractController {

    public static final String SITE_COMPONENT_TYPE = "site";
    public static final String ROLE_COMPONENT_TYPE = "role";
    public static final String COMPONENT_TYPE = "componentTyep";
    protected final String CLINICAL_STAFF_ASSIGNMENT_INDEX = "clinicalStaffAssignmentIndex";

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = null;
        ClinicalStaffCommand clinicalStaffCommand = ClinicalStaffControllerUtils.getClinicalStaffCommand(request);
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();

        String componentTyep = request.getParameter(COMPONENT_TYPE);
        if (StringUtils.equals(componentTyep, SITE_COMPONENT_TYPE)) {
            ClinicalStaffAssignment clinicalStaffAssignment = new ClinicalStaffAssignment();
            clinicalStaffAssignment.setDomainObjectClass(Organization.class.getName());
            clinicalStaff.addClinicalStaffAssignment(clinicalStaffAssignment);

            int index = clinicalStaff.getClinicalStaffAssignments().size() - 1;

            modelAndView = new ModelAndView("clinicalStaff/clinicalStaffAssignmentSection");
            modelAndView.addObject("index", index);
            modelAndView.addObject("clinicalStaffAssignment", clinicalStaffAssignment);
        } else if (StringUtils.equals(componentTyep, ROLE_COMPONENT_TYPE)) {

            Integer clinicalStaffAssignmentIndex = ServletRequestUtils.getRequiredIntParameter(request, CLINICAL_STAFF_ASSIGNMENT_INDEX);
            ClinicalStaffAssignment clinicalStaffAssignment = clinicalStaff.getClinicalStaffAssignments().get(clinicalStaffAssignmentIndex);

            ClinicalStaffAssignmentRole clinicalStaffAssignmentRole = new ClinicalStaffAssignmentRole();
            clinicalStaffAssignment.addClinicalStaffAssignmentRole(clinicalStaffAssignmentRole);
            int index = clinicalStaffAssignment.getClinicalStaffAssignmentRoles().size() - 1;

            modelAndView = new ModelAndView("clinicalStaff/clinicalStaffAssignmentRoleSection");
            modelAndView.addObject("clinicalStaffAssignmentRole", clinicalStaffAssignmentRole);
            modelAndView.addObject("index", index);
            modelAndView.addObject("clinicalStaffAssignmentIndex", clinicalStaffAssignmentIndex);

            modelAndView.addObject("siteRoles", ListValues.getSiteRolesType());
            modelAndView.addObject("roleStatus", ListValues.getRoleStatusType());
        }

        return modelAndView;
    }

    public AddClinicalStaffComponentController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }
}
