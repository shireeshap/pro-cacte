package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateStudyController extends CtcAeSimpleFormController {

    private StudyRepository studyRepository;

    public CreateStudyController() {
        setCommandClass(StudyCommand.class);
        setCommandName("studyCommand");
        setFormView("study/createStudy");
        setSuccessView("study/confirmStudy");
        setBindOnNewForm(true);
        setSessionForm(true);
    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {

        StudyCommand studyCommand = (StudyCommand) oCommand;
        Study study = studyCommand.getStudy();

        //remove the study sites;
        studyCommand.removeStudySites();
        study = studyRepository.save(study);
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        modelAndView.addObject("studyCommand", study);
        return modelAndView;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        return new StudyCommand();


    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    ////// CONFIGURATION


}