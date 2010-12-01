package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        StudyParticipantCommand studyParticipantCommand = (StudyParticipantCommand) command;
        StudyParticipantAssignment studyParticipantAssignment = studyParticipantCommand.getStudyParticipantAssignment();
        StudyParticipantCrf studyParticipantCrf = studyParticipantAssignment.getStudyParticipantCrfs().get(0);
        LinkedList<StudyParticipantCrfSchedule> onHoldStudyParticipantCrfSchedules = new LinkedList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
            if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.ONHOLD)) {
                onHoldStudyParticipantCrfSchedules.add(studyParticipantCrfSchedule);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (onHoldStudyParticipantCrfSchedules.size() > 0 && onHoldStudyParticipantCrfSchedules.getFirst().getCycleNumber() != null) {
            map.put("cycle", onHoldStudyParticipantCrfSchedules.getFirst().getCycleNumber());
            map.put("day", onHoldStudyParticipantCrfSchedules.getFirst().getCycleDay());
        }
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        String recreateSchedules = request.getParameter("recreate");
        int cycle = Integer.parseInt(request.getParameter("cycle"));
        int day = Integer.parseInt(request.getParameter("day"));
        StudyParticipantCommand spCommand = (StudyParticipantCommand) command;

        StudyParticipantAssignment studyParticipantAssignment = studyParticipantAssignmentRepository.findById(spCommand.getStudyParticipantAssignment().getId());
        studyParticipantAssignment.setOffHoldTreatmentDate(spCommand.getOffHoldTreatmentDate());
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
            if (recreateSchedules.equals("continue")) {
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                    if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.ONHOLD)) {
                        if (studyParticipantCrfSchedule.getStartDate().getTime() >= spCommand.getOffHoldTreatmentDate().getTime()) {
                            studyParticipantCrfSchedule.setStatus(CrfStatus.SCHEDULED);
                        } else {
                            studyParticipantCrfSchedule.setStatus(CrfStatus.CANCELLED);
                        }
                    }
                }

//                studyParticipantCrf.getStudyParticipantCrfSchedules().clear();
//                studyParticipantCrf.setStartDate(studyParticipantAssignment.getOffHoldTreatmentDate());
//                studyParticipantCrf.createSchedules();
            } else if (recreateSchedules.equals("move")) {
                Long timeDiff = studyParticipantAssignment.getOffHoldTreatmentDate().getTime() - studyParticipantAssignment.getOnHoldTreatmentDate().getTime();
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                    if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.ONHOLD)) {
                        setScheduleDateAndStatus(studyParticipantCrfSchedule, timeDiff);
                    }
                }
            } else if (recreateSchedules.equals("cycle")) {
                Long timeDiff = studyParticipantAssignment.getOffHoldTreatmentDate().getTime() - studyParticipantAssignment.getOnHoldTreatmentDate().getTime();
                LinkedList<StudyParticipantCrfSchedule> offHoldStudyParticipantCrfSchedules = new LinkedList<StudyParticipantCrfSchedule>();
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                    if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.ONHOLD)) {
                        if (studyParticipantCrfSchedule.getCycleNumber() != null) {
                            if (studyParticipantCrfSchedule.getCycleNumber() == cycle && studyParticipantCrfSchedule.getCycleDay() >= day) {
                                offHoldStudyParticipantCrfSchedules.add(studyParticipantCrfSchedule);

//                                setScheduleDateAndStatus(studyParticipantCrfSchedule, timeDiff);
                            } else if (studyParticipantCrfSchedule.getCycleNumber() > cycle) {
                                offHoldStudyParticipantCrfSchedules.add(studyParticipantCrfSchedule);

//                                setScheduleDateAndStatus(studyParticipantCrfSchedule, timeDiff);
                            } else {
                                studyParticipantCrfSchedule.setStatus(CrfStatus.CANCELLED);
                            }
                        } else {
                            setScheduleDateAndStatus(studyParticipantCrfSchedule, timeDiff);
                        }
                    }
                }
                if (offHoldStudyParticipantCrfSchedules.size() > 0) {
                    Long newTimeDiff = studyParticipantAssignment.getOffHoldTreatmentDate().getTime() - offHoldStudyParticipantCrfSchedules.getFirst().getStartDate().getTime();
                    for (StudyParticipantCrfSchedule offHoldStudyParticipantCrfSchedule : offHoldStudyParticipantCrfSchedules) {
                        setScheduleDateAndStatus(offHoldStudyParticipantCrfSchedule, newTimeDiff);
                    }
                }
            }
        }
        studyParticipantAssignment.setOnHoldTreatmentDate(null);

        spCommand.setStudyParticipantAssignment(studyParticipantAssignment);

        RedirectView redirectView = new RedirectView("schedulecrf?pId=" + studyParticipantAssignment.getParticipant().getId());
        return new ModelAndView(redirectView);

    }

    public void setScheduleDateAndStatus(StudyParticipantCrfSchedule studyParticipantCrfSchedule, Long timeDiff) {
        Date newStartDate = new Date(studyParticipantCrfSchedule.getStartDate().getTime() + timeDiff);
        Date newDueDate = new Date(studyParticipantCrfSchedule.getDueDate().getTime() + timeDiff);
        studyParticipantCrfSchedule.setStartDate(newStartDate);
        studyParticipantCrfSchedule.setDueDate(newDueDate);
        studyParticipantCrfSchedule.setStatus(CrfStatus.SCHEDULED);
    }

    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}

