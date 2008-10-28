package gov.nih.nci.ctcae.web.study;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class AddStudySiteController extends AbstractController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("study/ajax/oneStudySiteSection");


        StudyCommand studyCommand = StudyControllerUtils.getStudyCommand(request);

        Study study = studyCommand.getStudy();
        StudySite studySite = new StudySite();
        study.addStudySite(studySite);

        int index = study.getStudySites().size() - 1;

        modelAndView.addObject("index", index);

        return modelAndView;


    }


    public AddStudySiteController() {
        setSupportedMethods(new String[]{"GET"});

    }

}