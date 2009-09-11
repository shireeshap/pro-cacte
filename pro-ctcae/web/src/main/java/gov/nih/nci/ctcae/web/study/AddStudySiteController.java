package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class AddStudySiteController.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class AddStudySiteController extends AbstractController {

    private StudyRepository studyRepository;

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        StudyCommand studyCommand = ControllersUtils.getStudyCommand(request);

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            Integer siteIndexToRemove = Integer.parseInt(request.getParameter("siteIndexToRemove"));
            studyCommand.getSiteIndexesToRemove().add(siteIndexToRemove);
            return null;
        }
        ModelAndView modelAndView = new ModelAndView("study/ajax/oneStudySiteSection");
        Study study = studyCommand.getStudy();
        studyRepository.addStudySite(study);

        int index = study.getStudySites().size() - 1;

        modelAndView.addObject("index", index);

        return modelAndView;


    }


    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    /**
     * Instantiates a new adds the study site controller.
     */
    public AddStudySiteController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

}