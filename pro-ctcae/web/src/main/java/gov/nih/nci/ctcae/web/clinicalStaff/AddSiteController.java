package gov.nih.nci.ctcae.web.clinicalStaff;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaff;

/**
 * @author Mehul Gulati
 * Date: Oct 30, 2008
 */
public class AddSiteController extends AbstractController {

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("clinicalStaff/siteSection");

        ClinicalStaffCommand clinicalStaffCommand = ClinicalStaffControllerUtils.getClinicalStaffCommand(request);

        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();
        SiteClinicalStaff siteClinicalStaff = new SiteClinicalStaff();
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff);

        int index = clinicalStaff.getSiteClinicalStaffs().size() - 1;

        modelAndView.addObject("index", index);
        return modelAndView;
    }

    public AddSiteController() {
        setSupportedMethods(new String[]{"GET"});
    }
}
