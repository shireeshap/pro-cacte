package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.IvrsCallStatus;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author mehul gulati
 *         Date: Oct 26, 2010
 */
public class ParticipantOffHoldController extends CtcAeSimpleFormController {

    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    protected ParticipantOffHoldController() {
        super();
//        setCommandClass(StudyParticipantCommand.class);
        setFormView("participant/offHoldDate");
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String spaId = request.getParameter("id");
        String flowName = request.getParameter("flow");
        if (StringUtils.equals("participant", flowName)) {
            //create or edit participant flow
            ParticipantCommand command = ParticipantControllerUtils.getParticipantCommand(request);
            command.setOffHoldTreatmentDate(command.getSelectedStudyParticipantAssignment().getOnHoldTreatmentDate());
            return command;
        } else {

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
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        int cycleNumber = 0;
        int dayNumber = 0;
        int cycle = 0;
        int day = 0;
        Date onHoldTreatmentDate = new Date();
        if (command instanceof ParticipantCommand) {
            ParticipantCommand participantCommand = (ParticipantCommand) command;
            StudyParticipantAssignment studyParticipantAssignment = participantCommand.getSelectedStudyParticipantAssignment();
            onHoldTreatmentDate = studyParticipantAssignment.getOnHoldTreatmentDate();
            LinkedList<StudyParticipantCrfSchedule> onHoldStudyParticipantCrfSchedules = new LinkedList<StudyParticipantCrfSchedule>();
            StudyParticipantCrf studyParticipantCrf = studyParticipantAssignment.getStudyParticipantCrfs().get(0);
            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.ONHOLD)) {
                    onHoldStudyParticipantCrfSchedules.add(studyParticipantCrfSchedule);
                }
            }
            if (onHoldStudyParticipantCrfSchedules.size() > 0 && onHoldStudyParticipantCrfSchedules.getFirst().getCycleNumber() != null) {
                cycle = onHoldStudyParticipantCrfSchedules.getFirst().getCycleNumber();
                day = onHoldStudyParticipantCrfSchedules.getFirst().getCycleDay();
            }
        } else if (command instanceof StudyParticipantCommand) {
            StudyParticipantCommand studyParticipantCommand = (StudyParticipantCommand) command;

            StudyParticipantAssignment studyParticipantAssignment = studyParticipantCommand.getStudyParticipantAssignment();
            onHoldTreatmentDate = studyParticipantAssignment.getOnHoldTreatmentDate();
            StudyParticipantCrf studyParticipantCrf = studyParticipantAssignment.getStudyParticipantCrfs().get(0);  //assuming all spCrfs will have same the cycle definition
            LinkedList<StudyParticipantCrfSchedule> onHoldStudyParticipantCrfSchedules = studyParticipantCommand.getOnHoldStudyParticipantCrfSchedules();
            onHoldStudyParticipantCrfSchedules.clear();
            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.ONHOLD)) {
                    onHoldStudyParticipantCrfSchedules.add(studyParticipantCrfSchedule);
                    if (studyParticipantCrfSchedule.getStartDate().getDate() == studyParticipantCommand.getOffHoldTreatmentDate().getDate()) {
                        cycleNumber = studyParticipantCrfSchedule.getCycleNumber();
                        dayNumber = studyParticipantCrfSchedule.getCycleDay();
                    }
                }
            }

            if (onHoldStudyParticipantCrfSchedules.size() > 0 && onHoldStudyParticipantCrfSchedules.getFirst().getCycleNumber() != null) {
                cycle = onHoldStudyParticipantCrfSchedules.getFirst().getCycleNumber();
                day = onHoldStudyParticipantCrfSchedules.getFirst().getCycleDay();
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cycle", cycle);
        map.put("day", day);
        map.put("cycleNumber", cycleNumber);
        map.put("dayNumber", dayNumber);
        map.put("onHoldTreatmentDate", onHoldTreatmentDate);
        return map;
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object o, BindException e) throws Exception {
        int cycle = ServletRequestUtils.getIntParameter(request, "cycle", 0);
        int day = ServletRequestUtils.getIntParameter(request, "day", 0);

        if (o instanceof ParticipantCommand) {
            //BJ : what validation ??
            ParticipantCommand participantCommand=(ParticipantCommand) o;
             Date onHoldDate=participantCommand.getSelectedStudyParticipantAssignment().getOnHoldTreatmentDate();
                if (onHoldDate != null) {
                    if (participantCommand.getOffHoldTreatmentDate().getTime() < onHoldDate.getTime()) {
                        e.reject("participant.offhold_date", "participant.offhold_date");
                    }
                }
        } else if (o instanceof StudyParticipantCommand) {

            StudyParticipantCommand spCommand = (StudyParticipantCommand) o;
            if (spCommand.getOnHoldStudyParticipantCrfSchedules().size() > 0) {
                StudyParticipantCrfSchedule spcSchedule = spCommand.getOnHoldStudyParticipantCrfSchedules().getFirst();
                if (cycle != 0) {
                    if (spcSchedule.getCycleNumber() > cycle || (spcSchedule.getCycleNumber() == cycle && spcSchedule.getCycleDay() > day)) {
                        e.reject("Cycle and Day number should be equal to greater that the held survey cycle and day number.", "ssss");
                    }
                } else {
                    if (spCommand.getOffHoldTreatmentDate().getTime() < spCommand.getStudyParticipantAssignment().getOnHoldTreatmentDate().getTime()) {
                        e.reject("Selected date should be equal to or greater than the survey held from date. Please select another date.", "Selected date should be equal to or greater than the survey held from date. Please select another date.");
                    }
                }
            } else { // off hold date should be greater than on hold date
                Date onHoldDate=spCommand.getStudyParticipantAssignment().getOnHoldTreatmentDate();
                if (onHoldDate != null) {
                    if (spCommand.getOffHoldTreatmentDate().getTime() < onHoldDate.getTime()) {
                        e.reject("participant.offhold_date", "participant.offhold_date");
                    }
                }
            }
        }


        super.onBindAndValidate(request, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
//        String recreateSchedules = request.getParameter("recreate");
//        String recreateSchedules = ServletRequestUtils.getStringParameter(request, "recreate", "nocycle");
        int cycle = ServletRequestUtils.getIntParameter(request, "cycle", 0);
        int day = ServletRequestUtils.getIntParameter(request, "day", 0);
        Date offHoldDate = new Date();
        int studyParticipantId = 0;
        if (command instanceof ParticipantCommand) {
            ParticipantCommand participantCommand = (ParticipantCommand) command;
            offHoldDate = participantCommand.getOffHoldTreatmentDate();
            studyParticipantId = participantCommand.getSelectedStudyParticipantAssignment().getId();

        } else if (command instanceof StudyParticipantCommand) {
            StudyParticipantCommand spCommand = (StudyParticipantCommand) command;
            offHoldDate = spCommand.getOffHoldTreatmentDate();
            studyParticipantId = spCommand.getStudyParticipantAssignment().getId();
        }


        StudyParticipantAssignment studyParticipantAssignment = studyParticipantAssignmentRepository.findById(studyParticipantId);
        studyParticipantAssignment.setOffHoldTreatmentDate(offHoldDate);
        studyParticipantAssignment.setStatus(RoleStatus.ACTIVE);
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {

//            if (recreateSchedules.equals("cycle")) {
            if (cycle != 0 && day != 0) {
                Long timeDiff = studyParticipantAssignment.getOffHoldTreatmentDate().getTime() - studyParticipantAssignment.getOnHoldTreatmentDate().getTime();
                int dateOffset = DateUtils.daysBetweenDates(studyParticipantAssignment.getOffHoldTreatmentDate(), studyParticipantAssignment.getOnHoldTreatmentDate());
                LinkedList<StudyParticipantCrfSchedule> offHoldStudyParticipantCrfSchedules = new LinkedList<StudyParticipantCrfSchedule>();
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                    if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.ONHOLD)) {
                        if (studyParticipantCrfSchedule.getCycleNumber() != null) {
                            if (studyParticipantCrfSchedule.getCycleNumber() == cycle && studyParticipantCrfSchedule.getCycleDay() >= day) {
                                offHoldStudyParticipantCrfSchedules.add(studyParticipantCrfSchedule);
                            } else if (studyParticipantCrfSchedule.getCycleNumber() > cycle) {
                                offHoldStudyParticipantCrfSchedules.add(studyParticipantCrfSchedule);
                            } else {
                                studyParticipantCrfSchedule.setStatus(CrfStatus.CANCELLED);
                                studyParticipantCrfSchedule.updateIvrsSchedulesStatus(IvrsCallStatus.CANCELLED);
                            }
                        } else {
                            setScheduleDateAndStatus(studyParticipantCrfSchedule, timeDiff, dateOffset);
                        }
                    }
                }
                if (offHoldStudyParticipantCrfSchedules.size() > 0) {
                    Long newTimeDiff = studyParticipantAssignment.getOffHoldTreatmentDate().getTime() - offHoldStudyParticipantCrfSchedules.getFirst().getStartDate().getTime();
                    int newDateOffset = DateUtils.daysBetweenDates(studyParticipantAssignment.getOffHoldTreatmentDate(), offHoldStudyParticipantCrfSchedules.getFirst().getStartDate());
                    for (StudyParticipantCrfSchedule offHoldStudyParticipantCrfSchedule : offHoldStudyParticipantCrfSchedules) {
                        setScheduleDateAndStatus(offHoldStudyParticipantCrfSchedule, newTimeDiff, newDateOffset);
                    }
                }
            } else {
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                    if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.ONHOLD)) {
                        if (studyParticipantCrfSchedule.getStartDate().getTime() >= offHoldDate.getTime()) {
                            studyParticipantCrfSchedule.setStatus(CrfStatus.SCHEDULED);
                            studyParticipantCrfSchedule.updateIvrsSchedulesStatus(IvrsCallStatus.PENDING);
                        } else {
                            studyParticipantCrfSchedule.setStatus(CrfStatus.CANCELLED);
                            studyParticipantCrfSchedule.updateIvrsSchedulesStatus(IvrsCallStatus.CANCELLED);
                        }
                    }
                }
            }
        }
        studyParticipantAssignment.setOnHoldTreatmentDate(null);
        if (command instanceof ParticipantCommand) {
            ParticipantCommand participantCommand = (ParticipantCommand) command;

        } else if (command instanceof StudyParticipantCommand) {
            StudyParticipantCommand spCommand = (StudyParticipantCommand) command;
            spCommand.setStudyParticipantAssignment(studyParticipantAssignment);
        }

        ModelAndView mv = new ModelAndView("participant/offHoldRedirect");
        mv.addObject("pId", studyParticipantAssignment.getParticipant().getId());
        return mv;

    }

    public void setScheduleDateAndStatus(StudyParticipantCrfSchedule studyParticipantCrfSchedule, Long timeDiff, int dateOffset) {
        Date newStartDate = new Date(studyParticipantCrfSchedule.getStartDate().getTime() + timeDiff);
        Date newDueDate = new Date(studyParticipantCrfSchedule.getDueDate().getTime() + timeDiff);
        studyParticipantCrfSchedule.setStartDate(newStartDate);
        studyParticipantCrfSchedule.setDueDate(newDueDate);
        studyParticipantCrfSchedule.setStatus(CrfStatus.SCHEDULED);
        //update ivrsSchedules as well
        studyParticipantCrfSchedule.updateIvrsSchedules(studyParticipantCrfSchedule.getStudyParticipantCrf(), dateOffset);
        studyParticipantCrfSchedule.updateIvrsSchedulesStatus(IvrsCallStatus.SCHEDULED);
    }

    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}

