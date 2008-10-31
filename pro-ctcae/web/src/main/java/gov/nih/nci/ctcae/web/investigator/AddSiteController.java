package gov.nih.nci.ctcae.web.investigator;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.Investigator;
import gov.nih.nci.ctcae.core.domain.SiteInvestigator;

/**
 * @author Mehul Gulati
 * Date: Oct 30, 2008
 */
public class AddSiteController extends AbstractController {

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("investigator/siteSection");

        InvestigatorCommand investigatorCommand = InvestigatorControllerUtils.getInvestigatorCommand(request);

        Investigator investigator = investigatorCommand.getInvestigator();
        SiteInvestigator siteInvestigator = new SiteInvestigator();
        investigator.addSiteInvestigator(siteInvestigator);

        int index = investigator.getSiteInvestigators().size() - 1;

        modelAndView.addObject("index", index);
        return modelAndView;
    }

    public AddSiteController() {
        setSupportedMethods(new String[]{"GET"});
    }
}
