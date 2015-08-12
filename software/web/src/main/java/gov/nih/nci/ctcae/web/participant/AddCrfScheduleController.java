package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.IvrsCallStatus;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.service.ParticipantScheduleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

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
    ParticipantScheduleService participantScheduleService;
    private static final String DELETE_ALL_FUTURE = "delallfuture";
    private static final String DELETE_ALL = "delall";
    private static final String MOVE_ALL = "moveall";
    private static final String MOVE_ALL_FUTURE = "moveallfuture";
    private static final String ADD_DEL = "add,del";
    private static final String ON_HOLD = "onhold";
    private static final String OFF_HOLD = "offhold";
    private static final String ADD = "add";
    private static final String DELETE = "del";

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
        Date today = new Date();
        String fids = request.getParameter("fids");
        String participantId = request.getParameter(ParticipantController.PARTICIPANT_ID);
        String[] strings;
        boolean isSave = false;
        List formIds = new ArrayList();
        if (fids != null) {
            strings = fids.split(",");
            formIds = Arrays.asList(strings);
        }

        ParticipantSchedule participantSchedule = participantCommand.getParticipantSchedules().get(index);
        StudyParticipantAssignment studyParticipantAssignment = participantCommand.getSelectedStudyParticipantAssignment();

        Calendar c = new GregorianCalendar();


        if (DELETE_ALL.equals(action)) {
        	 HashSet<StudyParticipantCrf> spcrfList = participantSchedule.removeAllSchedules(formIds);
        	 participantScheduleService.save(spcrfList);
        	 isSave = true;
        }
        c.setTime(participantSchedule.getProCtcAECalendar().getTime());

        if (MOVE_ALL.equals(action)) {
            String strNewdate = date.substring(0, date.indexOf(","));
            Date newDate = DateUtils.parseDate(strNewdate);
            Calendar cNewDate = Calendar.getInstance();
            cNewDate.setTime(newDate);
            int olddate = Integer.parseInt(date.substring(date.indexOf(",") + 1));
            c.set(Calendar.DATE, olddate);
            participantScheduleService.moveAllSchedules(DateUtils.daysBetweenDatesWithRoundOff(cNewDate, c), 
														formIds, participantSchedule.getStudyParticipantCrfs());
            participantCommand.lazyInitializeAssignment(genericRepository, true);
        }

        if (MOVE_ALL_FUTURE.equals(action)) {
            String strNewdate = date.substring(0, date.indexOf(","));
            Date newDate = DateUtils.parseDate(strNewdate);
            Calendar cNewDate = Calendar.getInstance();
            cNewDate.setTime(newDate);
            
            int olddate = Integer.parseInt(date.substring(date.indexOf(",") + 1));
            c.set(Calendar.DATE, olddate);
            
            participantScheduleService.moveFutureSchedules(c, DateUtils.daysBetweenDatesWithRoundOff(cNewDate, c), 
    													   formIds, participantSchedule.getStudyParticipantCrfs());
            participantCommand.lazyInitializeAssignment(genericRepository, true);
        }
        
        if (DELETE_ALL_FUTURE.equals(action)) {
            c.set(Calendar.DATE, Integer.parseInt(date));
            HashSet<StudyParticipantCrf> spcrfList = participantSchedule.deleteFutureSchedules(c, formIds);
        	participantScheduleService.save(spcrfList);
        	isSave = true;
        }

        if (ADD_DEL.equals(action)) {
            LinkedHashMap<String, List<String>> resultMap = new LinkedHashMap<String, List<String>>();
            ModelAndView mv = new ModelAndView("participant/moveSuccessForm");

            String strNewdate = date.substring(0, date.indexOf(","));
            Date newDate = DateUtils.parseDate(strNewdate);
            String olddate = date.substring(date.indexOf(",") + 1);

            c.set(Calendar.DATE, Integer.parseInt(olddate));

            Calendar newCalendar = new GregorianCalendar();
            newCalendar.setTime(newDate);
            //using service layer object to save moved schedules
            participantScheduleService.updateAndSaveSchedule(c, newCalendar, formIds, resultMap, participantSchedule);
            participantCommand.lazyInitializeAssignment(genericRepository, true);
            mv.addObject("day", request.getParameter("date"));
            mv.addObject("index", request.getParameter("index"));
            mv.addObject("resultMap", resultMap);
            mv.addObject("pid", participantId);
            return mv;
        }

        if (ON_HOLD.equals(action)) {
            studyParticipantAssignment.putOnHold(DateUtils.parseDate(date));
           //For PRKC-1867: Updating the status to OnHold only if the onHoldTreatmentDate is equal to todays date.
            if(DateUtils.compareDate(today, studyParticipantAssignment.getOnHoldTreatmentDate()) == 0){
            	studyParticipantAssignment.setStatus(RoleStatus.ONHOLD);
            }
            participantScheduleService.saveStudyParticipantCrfAssignment(genericRepository, studyParticipantAssignment);
            participantCommand.lazyInitializeParticipant(genericRepository);
        }

        if (OFF_HOLD.equals(action)) {
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
							/* PKRC-1876: Survey cancellation criterion should be based on the due date and not on the start
							 * date of the survey (similar change in ParticipantOffHoldController.java) 
							 */
	            				Date dateInParticipantTimeZone = DateUtils.getDateInTimeZone(DateUtils.parseDate(offHoldDate), studyParticipantAssignment.getCallTimeZone());
	            				if (studyParticipantCrfSchedule.getDueDate().getTime() >= dateInParticipantTimeZone.getTime()) {
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
            participantScheduleService.saveStudyParticipantCrfAssignment(genericRepository, studyParticipantAssignment);
            participantCommand.lazyInitializeParticipant(genericRepository);
        }

        if (ADD.equals(action)) {
            c.set(Calendar.DATE, Integer.parseInt(date));
            Calendar dueCalendar = (Calendar) c.clone();
            dueCalendar.add(Calendar.DATE, 1);
            //using service layer object to save newly added schedules
            participantScheduleService.createAndSaveSchedules(c, null, -1, -1, formIds, false, false, participantSchedule);
           // participantSchedule.createSchedule(c, null, -1, -1, formIds, false, false);
            isSave = true;
        }
        if (DELETE.equals(action)) {
            c.set(Calendar.DATE, Integer.parseInt(date));
            HashSet<StudyParticipantCrf> spcrfList = participantSchedule.removeSchedule(c, formIds);
            participantScheduleService.save(spcrfList);
            isSave = true;
        }

        participantCommand.lazyInitializeAssignment(genericRepository, isSave);
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
    
    @Required
    public void setParticipantScheduleService(ParticipantScheduleService participantScheduleService) {
        this.participantScheduleService = participantScheduleService;
    }
}