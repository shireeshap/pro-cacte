package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author mehul gulati
 *         Date: Oct 26, 2010
 */
public class ParticipantOffHoldController extends CtcAeSimpleFormController {

    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    protected ParticipantOffHoldController() {
        super();
        setCommandClass(StudyParticipantCommand.class);
        setFormView("participant/offHoldDate");
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
            studyParticipantCommand.setOffHoldTreatmentDate(c.getTime());
        }
        return studyParticipantCommand;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        String recreateSchedules = request.getParameter("recreate");
        StudyParticipantCommand spCommand = (StudyParticipantCommand) command;
       
        StudyParticipantAssignment studyParticipantAssignment = studyParticipantAssignmentRepository.findById(spCommand.getStudyParticipantAssignment().getId());
        studyParticipantAssignment.setOffHoldTreatmentDate(spCommand.getOffHoldTreatmentDate());
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
            if (recreateSchedules.equals("recreate")) {
                studyParticipantCrf.getStudyParticipantCrfSchedules().clear();
                studyParticipantCrf.setStartDate(studyParticipantAssignment.getOffHoldTreatmentDate());
                studyParticipantCrf.createSchedules();
            }

            else if (recreateSchedules.equals("move")) {
                Long timeDiff = studyParticipantAssignment.getOffHoldTreatmentDate().getTime() - studyParticipantAssignment.getOnHoldTreatmentDate().getTime();
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                    if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.ONHOLD)) {
                        Date newStartDate = new Date(studyParticipantCrfSchedule.getStartDate().getTime() + timeDiff);
                        Date newDueDate = new Date(studyParticipantCrfSchedule.getDueDate().getTime() + timeDiff);
                        studyParticipantCrfSchedule.setStartDate(newStartDate);
                        studyParticipantCrfSchedule.setDueDate(newDueDate);
                        studyParticipantCrfSchedule.setStatus(CrfStatus.SCHEDULED);
//                        studyParticipantCrfScheduleRepository.save(studyParticipantCrfSchedule);
                    }
                }
            }
        }
//        studyParticipantAssignmentRepository.save(studyParticipantAssignment);
                   
        studyParticipantAssignment.setOnHoldTreatmentDate(null);
//        if (recreateSchedules.equals("recreate")) {
//                   for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
//                       for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
//                           if (studyParticipantCrfSchedule.isBaseline()) {
//                               studyParticipantCrf.getStudyParticipantCrfSchedules().remove(studyParticipantCrfSchedule);
//                           }
//                       }
//                   }
//               }
        
//         studyParticipantAssignmentRepository.save(studyParticipantAssignment);
        spCommand.setStudyParticipantAssignment(studyParticipantAssignment);
//        studyParticipantAssignment = studyParticipantAssignmentRepository.findById(savedStudyParticipantAssignment1.getId());

        RedirectView redirectView = new RedirectView("schedulecrf?pId=" + studyParticipantAssignment.getParticipant().getId());
        return new ModelAndView(redirectView);

    }

    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}

