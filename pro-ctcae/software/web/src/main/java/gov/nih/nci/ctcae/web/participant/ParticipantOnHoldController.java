package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.apache.commons.lang.StringUtils;
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
import static gov.nih.nci.ctcae.core.domain.CrfStatus.*;

/**
 * @author mehul gulati
 *         Date: Nov 2, 2010
 */

public class ParticipantOnHoldController extends CtcAeSimpleFormController {

    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;
    private ParticipantRepository participantRepository;

    protected ParticipantOnHoldController() {
        super();
        setFormView("participant/onHoldDate");
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String spaId = request.getParameter("id");
        String flowName = request.getParameter("flow");


        if(StringUtils.equals("participant", flowName)){
            //create or edit participant flow
           ParticipantCommand command = ParticipantControllerUtils.getParticipantCommand(request);
           StudyParticipantAssignment studyParticipantAssignment = command.getSelectedStudyParticipantAssignment();
           
           return command;
        }else{
            //from schedule crf flow
            StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);
            StudyParticipantAssignment studyParticipantAssignment = studyParticipantAssignmentRepository.findById(Integer.parseInt(spaId));
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

    }

    @Override
    protected ModelAndView onSubmit (HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ModelAndView mv = null;

        if(command instanceof ParticipantCommand){
            ParticipantCommand participantCommand = (ParticipantCommand) command;
            StudyParticipantAssignment studyParticipantAssignment = participantCommand.getSelectedStudyParticipantAssignment();
            studyParticipantAssignment.putOnHold(participantCommand.getOnHoldTreatmentDate());
            studyParticipantAssignment.setStatus(RoleStatus.ONHOLD);
            participantRepository.saveOrUpdate(participantCommand.getParticipant());
            ParticipantControllerUtils.clearParticipantCommand(request);
            RedirectView redirectView = new RedirectView("edit?_target2=2&_page=1&id=" + participantCommand.getParticipant().getId());
            mv = new ModelAndView(redirectView);


        }else if(command instanceof StudyParticipantCommand){
            
            StudyParticipantCommand spCommand = (StudyParticipantCommand) command;
            StudyParticipantAssignment studyParticipantAssignment = spCommand.getStudyParticipantAssignment();
            studyParticipantAssignment = studyParticipantAssignmentRepository.findById(studyParticipantAssignment.getId()); //BJ-avoid reloading
            studyParticipantAssignment.putOnHold(spCommand.getOnHoldTreatmentDate());
            studyParticipantAssignment.setStatus(RoleStatus.ONHOLD);            
            studyParticipantAssignmentRepository.save(studyParticipantAssignment);
            RedirectView redirectView = new RedirectView("schedulecrf?pId=" + studyParticipantAssignment.getParticipant().getId());
            mv = new ModelAndView(redirectView);
        }
        return mv;

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

    public ParticipantRepository getParticipantRepository() {
        return participantRepository;
    }

    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
}
