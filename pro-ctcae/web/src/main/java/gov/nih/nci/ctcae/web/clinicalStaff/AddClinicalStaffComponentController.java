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
    public static final String COMPONENT_TYPE = "componentType";
    protected final String CLINICAL_STAFF_ASSIGNMENT_INDEX = "clinicalStaffAssignmentIndex";

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        String action = request.getParameter("action");
        ClinicalStaffCommand clinicalStaffCommand = ClinicalStaffControllerUtils.getClinicalStaffCommand(request);
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();


        if ("delete".equals(action)) {
            deleteComponent(clinicalStaffCommand, request);
            return null;
        }
        ModelAndView modelAndView = null;

        OrganizationClinicalStaff organizationClinicalStaff = new OrganizationClinicalStaff();
        clinicalStaff.addOrganizationClinicalStaff(organizationClinicalStaff);

        int organizationClinicalStaffIndex = clinicalStaff.getOrganizationClinicalStaffs().size() - 1;

        modelAndView = new ModelAndView("clinicalStaff/organizationClinicalStaffSection");
        modelAndView.addObject("organizationClinicalStaffIndex", organizationClinicalStaffIndex);
        modelAndView.addObject("organizationClinicalStaff", organizationClinicalStaff);

        return modelAndView;
    }

    private void deleteComponent(ClinicalStaffCommand clinicalStaffCommand, HttpServletRequest request) {
        Integer organizationClinicalStaffIndex = Integer.parseInt(request.getParameter("organizationClinicalStaffIndex"));
        clinicalStaffCommand.getIndexesToRemove().add(organizationClinicalStaffIndex);
    }

    public AddClinicalStaffComponentController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }
}
