package gov.nih.nci.ctcae.web.investigator;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.repository.InvestigatorRepository;
import gov.nih.nci.ctcae.core.domain.Investigator;

/**
 * @autjor Mehul Gulati
 * Date: Oct 22, 2008
 * Time: 10:21:08 AM
 */
public class SearchInvestigatorController extends AbstractController {

   
    protected ModelAndView handleRequestInternal (final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("investigator/searchInvestigator");

        return modelAndView;
    }
    
}
