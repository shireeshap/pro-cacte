package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaff;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class AddSiteController.
 * 
 * @author Mehul Gulati
 * Date: Oct 30, 2008
 */
public class AddSiteController extends AbstractController {

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("clinicalStaff/siteSection");

        gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand clinicalStaffCommand = gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffControllerUtils.getClinicalStaffCommand(request);

        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();
        SiteClinicalStaff siteClinicalStaff = new SiteClinicalStaff();
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff);

        int index = clinicalStaff.getSiteClinicalStaffs().size() - 1;

        modelAndView.addObject("index", index);
        return modelAndView;
    }

    /**
     * Instantiates a new adds the site controller.
     */
    public AddSiteController() {
        setSupportedMethods(new String[]{"GET"});
    }
}
