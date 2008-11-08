package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaff;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 *         Date: Oct 30, 2008
 */
public class AddSiteController extends AbstractController {

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

    public AddSiteController() {
        setSupportedMethods(new String[]{"GET"});
    }
}
