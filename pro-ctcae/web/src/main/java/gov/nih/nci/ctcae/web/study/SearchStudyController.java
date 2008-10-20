package gov.nih.nci.ctcae.web.study;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.web.ListValues;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class SearchStudyController extends AbstractController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("study/searchStudy");
        ListValues listValues = new ListValues();

        modelAndView.addObject("searchCriteria", listValues.getStudySearchType());

        return modelAndView;


    }

    public SearchStudyController() {
        setSupportedMethods(new String[]{"GET"});

    }
}