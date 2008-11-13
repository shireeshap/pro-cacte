package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.repository.JpaGenericRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.beans.factory.annotation.Required;

/**
 * User: Harsh
 * Date: Nov 12, 2008
 * Time: 1:36:54 PM
 */
public class SubmitFormController extends CtcAeSimpleFormController {
    GenericRepository genericRepository;

    public SubmitFormController() {
        setFormView("form/submitForm");
        setSuccessView("form/confirmFormSubmission");
        setCommandClass(SubmitFormCommand.class);
        setSessionForm(true);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand) command;
        boolean displayConfirmation = submitFormCommand.saveResponseAndGetQuestion(finderRepository, genericRepository);
        if (displayConfirmation) {
            return showForm(request, errors, getSuccessView());
        } else {
            return showForm(request, errors, getFormView());
        }
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        String crfScheduleId = request.getParameter("id");
        SubmitFormCommand submitFormCommand = new SubmitFormCommand();

        if (crfScheduleId != null && !("".equals(crfScheduleId))) {
            StudyParticipantCrfSchedule studyParticipantCrfSchedule = finderRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt(crfScheduleId));
            submitFormCommand.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
        }
        return submitFormCommand;
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request,
                                     Object command, BindException errors) throws Exception {
        super.onBindAndValidate(request, command, errors);

        SubmitFormCommand submitFormCommand = (SubmitFormCommand) command;

        if (submitFormCommand.getDirection().equals("continue") || submitFormCommand.getDirection().equals("save")) {
            if (submitFormCommand.getStudyParticipantCrfSchedule().getStudyParticipantCrfItems().get(submitFormCommand.getCurrentIndex()).getProCtcValidValue() == null) {
                errors.reject(
                        "answer", "Please select at least one answer.");
            }
        }
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
