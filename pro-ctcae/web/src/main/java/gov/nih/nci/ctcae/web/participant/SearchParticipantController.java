package gov.nih.nci.ctcae.web.participant;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.repository.InvestigatorRepository;
import gov.nih.nci.ctcae.core.domain.Investigator;

/**
 * @author Harsh Agarwal
 * Date: Oct 23, 2008
 */
public class SearchParticipantController extends AbstractController {

   
    protected ModelAndView handleRequestInternal (final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/searchParticipant");

        return modelAndView;
    }
    
}
