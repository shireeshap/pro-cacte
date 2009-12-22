package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class SearchStudyController.
 *
 * @author Vinay Kumar
 * @since Oct 17, 2008
 */
public class SearchStudyController extends AbstractController {


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("study/searchStudy");
        ListValues listValues = new ListValues();

        modelAndView.addObject("searchCriteria", listValues.getStudySearchType());

        return modelAndView;


    }

    /**
     * Instantiates a new search study controller.
     */
    public SearchStudyController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }
}