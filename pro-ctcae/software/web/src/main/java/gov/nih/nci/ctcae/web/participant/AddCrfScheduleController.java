package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

//

/**
 * The Class AddCrfScheduleController.
 *
 * @author Harsh Agarwal
 * @created Nov 6, 2008
 * Controller class called via Ajax. Used to add / delete schedule based on the date parameter
 */
public class AddCrfScheduleController extends AbstractController {

    GenericRepository genericRepository;

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ParticipantCommand participantCommand = ParticipantControllerUtils.getParticipantCommand(request);
        participantCommand.lazyInitializeAssignment(genericRepository, false);
        Integer index = Integer.parseInt(request.getParameter("index"));
        String action = request.getParameter("action");
        String date = request.getParameter("date");
        String offHoldDate = request.getParameter("offHoldDate");
        String fids = request.getParameter("fids");
        String[] strings;
        List formIds = new ArrayList();
        if (fids != null) {
            strings = fids.split(",");
            formIds = Arrays.asList(strings);
        }

        ParticipantSchedule participantSchedule = participantCommand.getParticipantSchedules().get(index);
        StudyParticipantAssignment studyParticipantAssignment = participantCommand.getSelectedStudyParticipantAssignment();

        Calendar c = new GregorianCalendar();


        if ("delall".equals(action)) {
            participantSchedule.removeAllSchedules(formIds);
        }
        c.setTime(participantSchedule.getProCtcAECalendar().getTime());

        if ("moveall".equals(action)) {
            String strNewdate = date.substring(0, date.indexOf(","));
            Date newDate = DateUtils.parseDate(strNewdate);
            int olddate = Integer.parseInt(date.substring(date.indexOf(",") + 1));
            c.set(Calendar.DATE, olddate);
            participantSchedule.moveAllSchedules(DateUtils.daysBetweenDates(newDate, c.getTime()), formIds);
        }

        if ("moveallfuture".equals(action)) {
            String strNewdate = date.substring(0, date.indexOf(","));
            Date newDate = DateUtils.parseDate(strNewdate);
            int olddate = Integer.parseInt(date.substring(date.indexOf(",") + 1));
            c.set(Calendar.DATE, olddate);
            participantSchedule.moveFutureSchedules(c, DateUtils.daysBetweenDates(newDate, c.getTime()), formIds);
        }
        if ("delallfuture".equals(action)) {
            c.set(Calendar.DATE, Integer.parseInt(date));
            participantSchedule.deleteFutureSchedules(c, formIds);
        }

        if ("add,del".equals(action)) {
            LinkedHashMap<String, List<String>> resultMap = new LinkedHashMap<String, List<String>>();
            ModelAndView mv = new ModelAndView("participant/moveSuccessForm");

            String strNewdate = date.substring(0, date.indexOf(","));
            Date newDate = DateUtils.parseDate(strNewdate);
            String olddate = date.substring(date.indexOf(",") + 1);

            c.set(Calendar.DATE, Integer.parseInt(olddate));

            Calendar newCalendar = new GregorianCalendar();
            newCalendar.setTime(newDate);
            participantSchedule.updateSchedule(c, newCalendar, formIds, resultMap);
            participantCommand.lazyInitializeAssignment(genericRepository, true);
            mv.addObject("day", request.getParameter("date"));
            mv.addObject("index", request.getParameter("index"));
            mv.addObject("resultMap", resultMap);
            return mv;
        }

        if ("onhold".equals(action)) {
            studyParticipantAssignment.putOnHold(DateUtils.parseDate(date));
            studyParticipantAssignment.setStatus(RoleStatus.ONHOLD);
        }

        if ("offhold".equals(action)) {
            int cycle = ServletRequestUtils.getIntParameter(request, "cycle", 0);
            int day = ServletRequestUtils.getIntParameter(request, "day", 0);
            studyParticipantAssignment.setOffHoldTreatmentDate(DateUtils.parseDate(offHoldDate));
            studyParticipantAssignment.setStatus(RoleStatus.ACTIVE);
            for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
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
                            if (studyParticipantCrfSchedule.getStartDate().getTime() >= DateUtils.parseDate(offHoldDate).getTime()) {
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
        }

        if ("add".equals(action)) {
            c.set(Calendar.DATE, Integer.parseInt(date));
            Calendar dueCalendar = (Calendar) c.clone();
            dueCalendar.add(Calendar.DATE, 1);
            participantSchedule.createSchedule(c, null, -1, -1, formIds, false);
            participantCommand.lazyInitializeAssignment(genericRepository, true);
        }
        if ("del".equals(action)) {
            c.set(Calendar.DATE, Integer.parseInt(date));
            participantSchedule.removeSchedule(c, formIds);
        }

        participantCommand.lazyInitializeAssignment(genericRepository, false);
        return new ModelAndView("participant/confirmMove");
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


    /**
     * Instantiates a new adds the crf schedule controller.
     */
    public AddCrfScheduleController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}