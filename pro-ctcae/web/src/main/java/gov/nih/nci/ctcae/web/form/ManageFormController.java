package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class ManageFormController.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class ManageFormController extends AbstractController {

    private StudyRepository studyRepository;

    /**
     * Instantiates a new manage form controller.
     */
    public ManageFormController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }

    /* (non-Javadoc)
      * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
      */
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {


        ModelAndView modelAndView = new ModelAndView("form/manageForm");
        String studyId = request.getParameter("studyId");
        if (!StringUtils.isBlank(studyId)) {
            Study study = studyRepository.findById(Integer.parseInt(studyId));
            if (study != null) {
                modelAndView.getModel().put("study", study);
            }
        }
        return modelAndView;
    }

    /**
     * Sets the finder repository.
     *
     * @param StudyRepository the new finder repository
     */
    @Required

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}
