package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author mehul gulati
 *         Date: Nov 2, 2010
 */

public class ParticipantOnHoldController extends CtcAeSimpleFormController {

    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    protected ParticipantOnHoldController() {
        super();
        setCommandClass(StudyParticipantCommand.class);
        setFormView("participant/onHoldDate");
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String spaId = request.getParameter("id");
        StudyParticipantAssignment studyParticipantAssignment = studyParticipantAssignmentRepository.findById(Integer.parseInt(spaId));
        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);
        studyParticipantCommand.setStudyParticipantAssignment(studyParticipantAssignment);
        if (request.getParameter("index") != null) {
            ParticipantSchedule participantSchedule = studyParticipantCommand.getParticipantSchedules().get(Integer.parseInt(request.getParameter("index")));
            Calendar c = new GregorianCalendar();
            String date = request.getParameter("date");
            c.setTime(participantSchedule.getProCtcAECalendar().getTime());
            c.set(Calendar.DATE, Integer.parseInt(date));
            studyParticipantCommand.setOnHoldTreatmentDate(c.getTime());
        }
        return studyParticipantCommand;
    }

    @Override
    protected ModelAndView onSubmit
            (HttpServletRequest
                    request, HttpServletResponse
                    response, Object
                    command, BindException
                    errors) throws Exception {
        StudyParticipantCommand spCommand = (StudyParticipantCommand) command;
        StudyParticipantAssignment studyParticipantAssignment = spCommand.getStudyParticipantAssignment();
        studyParticipantAssignment.setOnHoldTreatmentDate(spCommand.getOnHoldTreatmentDate());
        studyParticipantAssignment.setOffHoldTreatmentDate(null);
        studyParticipantAssignmentRepository.save(studyParticipantAssignment);
        studyParticipantAssignment = studyParticipantAssignmentRepository.findById(studyParticipantAssignment.getId());

        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                if (studyParticipantAssignment.getOnHoldTreatmentDate().getTime() <= studyParticipantCrfSchedule.getStartDate().getTime()) {
                    studyParticipantCrfSchedule.setStatus(CrfStatus.ONHOLD);
//                    studyParticipantCrfScheduleRepository.save(studyParticipantCrfSchedule);
                }
            }
        }
        studyParticipantAssignmentRepository.save(studyParticipantAssignment);
        RedirectView redirectView = new RedirectView("schedulecrf?pId=" + studyParticipantAssignment.getParticipant().getId());
        return new ModelAndView(redirectView);
    }

    public StudyParticipantAssignmentRepository getStudyParticipantAssignmentRepository() {
        return studyParticipantAssignmentRepository;
    }

    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }

    public StudyParticipantCrfScheduleRepository getStudyParticipantCrfScheduleRepository() {
        return studyParticipantCrfScheduleRepository;
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}
