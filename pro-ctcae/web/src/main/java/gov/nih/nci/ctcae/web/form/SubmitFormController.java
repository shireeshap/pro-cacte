package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class SubmitFormController extends CtcAeSimpleFormController {


    public SubmitFormController() {
        setCommandClass(SubmitFormCommand.class);
        setCommandName("submitFormCommand");
        setFormView("form/submitForm");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        return modelAndView;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String studyParticipantCrfId = request.getParameter("temp");
        StudyParticipantCrf studyParticipantCrf = (StudyParticipantCrf) finderRepository.findById(StudyParticipantCrf.class, Integer.parseInt(studyParticipantCrfId));
        SubmitFormCommand submitFormCommand = new SubmitFormCommand();
        submitFormCommand.setStudyParticipantCrf(studyParticipantCrf);
        return submitFormCommand;
    }
}