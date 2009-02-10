package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaffRole;
import gov.nih.nci.ctcae.web.ListValues;
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
public class AddClinicalStaffCompoentController extends AbstractController {

    public static final String SITE_COMPONENT_TYPE = "site";
    public static final String ROLE_COMPONENT_TYPE = "role";
    public static final String COMPONENT_TYPE = "componentTyep";
    protected final String SITE_CLINICAL_STAFF_INDEX = "siteClinicalStaffIndex";

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = null;
        ClinicalStaffCommand clinicalStaffCommand = ClinicalStaffControllerUtils.getClinicalStaffCommand(request);
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();

        String componentTyep = request.getParameter(COMPONENT_TYPE);
        if (StringUtils.equals(componentTyep, SITE_COMPONENT_TYPE)) {
            SiteClinicalStaff siteClinicalStaff = new SiteClinicalStaff();
            clinicalStaff.addSiteClinicalStaff(siteClinicalStaff);

            int index = clinicalStaff.getSiteClinicalStaffs().size() - 1;

            modelAndView = new ModelAndView("clinicalStaff/siteSection");
            modelAndView.addObject("index", index);
        } else if (StringUtils.equals(componentTyep, ROLE_COMPONENT_TYPE)) {

            Integer siteClinicalStaffIndex = ServletRequestUtils.getRequiredIntParameter(request, SITE_CLINICAL_STAFF_INDEX);
            SiteClinicalStaff siteClinicalStaff = clinicalStaff.getSiteClinicalStaffs().get(siteClinicalStaffIndex);

            SiteClinicalStaffRole siteClinicalStaffRole = new SiteClinicalStaffRole();
            siteClinicalStaff.addSiteClinicalStaffRole(siteClinicalStaffRole);
            int index = siteClinicalStaff.getSiteClinicalStaffRoles().size() - 1;

            modelAndView = new ModelAndView("clinicalStaff/siteRoleSection");
            modelAndView.addObject("siteClinicalStaffRole", siteClinicalStaffRole);
            modelAndView.addObject("index", index);
            modelAndView.addObject("siteClinicalStaffIndex", siteClinicalStaffIndex);

            modelAndView.addObject("roles", ListValues.getRolesType());
            modelAndView.addObject("roleStatus", ListValues.getRoleStatusType());
        }

        return modelAndView;
    }

    public AddClinicalStaffCompoentController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }
}
