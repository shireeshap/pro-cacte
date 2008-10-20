package gov.nih.nci.ctcae.web.study;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyFundingSponsor;
import gov.nih.nci.ctcae.core.domain.StudyCoordinatingCenter;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateStudyController extends CtcAeSimpleFormController {

    private StudyRepository studyRepository;

    public CreateStudyController() {
        setCommandClass(Study.class);
        setCommandName("studyCommand");
        setFormView("study/createStudy");
        setSuccessView("study/confirmStudy");
        setBindOnNewForm(true);
        setSessionForm(true);
    }



    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {

        Study study = (Study) oCommand;

        study = studyRepository.save(study);
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        modelAndView.addObject("studyCommand", study);
        return modelAndView;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Study study = new Study();
        study.setStudyFundingSponsor(new StudyFundingSponsor());
        study.setStudyCoordinatingCenter(new StudyCoordinatingCenter());
        return study;


    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    ////// CONFIGURATION


}