package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import org.apache.commons.lang.StringUtils;
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
            OrganizationClinicalStaff organizationClinicalStaff = new OrganizationClinicalStaff();
            clinicalStaff.addOrganizationClinicalStaff(organizationClinicalStaff);

            int organizationClinicalStaffIndex = clinicalStaff.getOrganizationClinicalStaffs().size() - 1;

            modelAndView = new ModelAndView("clinicalStaff/organizationClinicalStaffSection");
            modelAndView.addObject("organizationClinicalStaffIndex", organizationClinicalStaffIndex);
            modelAndView.addObject("organizationClinicalStaff", organizationClinicalStaff);
        }

        return modelAndView;
    }

    public AddClinicalStaffComponentController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }
}
