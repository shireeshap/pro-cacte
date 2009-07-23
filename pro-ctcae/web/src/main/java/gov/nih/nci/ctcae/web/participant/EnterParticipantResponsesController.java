package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Mehul Gulati
 * Date: Jun 24, 2009
 */
public class EnterParticipantResponsesController extends CtcAeSimpleFormController {

    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    public EnterParticipantResponsesController() {
        super();
        setCommandClass(StudyParticipantCrfSchedule.class);
        setFormView("participant/inputParticipantResponses");
        setSuccessView("/participant/inputParticipantResponses");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        if (!StringUtils.isBlank(id)) {
        studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(Integer.parseInt(id));
//            HashMap<ProCtcTerm, ArrayList<StudyParticipantCrfItem>> symptomMap = studyParticipantCrfSchedule.getSymptomItems();
        }
        return studyParticipantCrfSchedule;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = (StudyParticipantCrfSchedule) oCommand;
        studyParticipantCrfSchedule.setStatus(CrfStatus.COMPLETED);
        studyParticipantCrfScheduleRepository.save(studyParticipantCrfSchedule);
        ModelAndView modelAndView = new ModelAndView(new RedirectView("enterResponses?id=" + studyParticipantCrfSchedule.getId()));
        modelAndView.addObject("successMessage","true");
        return modelAndView;
    }



    @Required
    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}
