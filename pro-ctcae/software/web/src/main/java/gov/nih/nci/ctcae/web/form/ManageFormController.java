package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.study.StudyAjaxFacade;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//

/**
 * The Class ManageFormController.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class ManageFormController extends AbstractController {

    private StudyRepository studyRepository;
    private CrfAjaxFacade crfAjaxFacade;
    private StudyAjaxFacade studyAjaxFacade;

    /**
     * Instantiates a new manage form controller.
     */
//    public ManageFormController() {
//        super();
//        setSupportedMethods(new String[]{"GET"});
//    }

    /* (non-Javadoc)
      * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
      */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {


        ModelAndView modelAndView = new ModelAndView("form/manageForm");
        String searchString = request.getParameter("searchString");
        modelAndView.addObject("searchString", searchString);
        request.getSession().setAttribute("crfSearchString", searchString);
        return modelAndView;
    }

    /**
     * Sets the finder repository.
     *
     * @param studyRepository the new finder repository
     */
    @Required

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Required
    public void setCrfAjaxFacade(CrfAjaxFacade crfAjaxFacade) {
        this.crfAjaxFacade = crfAjaxFacade;
    }

    @Required
    public void setStudyAjaxFacade(StudyAjaxFacade studyAjaxFacade) {
        this.studyAjaxFacade = studyAjaxFacade;
    }
}
