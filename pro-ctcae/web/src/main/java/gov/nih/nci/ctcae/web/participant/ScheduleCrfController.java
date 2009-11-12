package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.form.CtcAeSecuredTabbedFlowController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class ScheduleCrfController.
 *
 * @author Harsh Agarwal
 * @since Nov 5, 2008
 */
public class ScheduleCrfController<C extends StudyParticipantCommand> extends CtcAeSecuredTabbedFlowController<StudyParticipantCommand> {

    /**
     * The study participant assignment repository.
     */
    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    /**
     * Instantiates a new schedule crf controller.
     */
    public ScheduleCrfController() {
        super();
        setCommandClass(StudyParticipantCommand.class);
        Flow<StudyParticipantCommand> flow = new Flow<StudyParticipantCommand>("Schedule Crf");
        layoutTabs(flow);
        setFlowFactory(new StaticFlowFactory<StudyParticipantCommand>(flow));
        setAllowDirtyBack(false);
        setAllowDirtyForward(false);
        setSessionForm(true);

    }

    /**
     * Layout tabs.
     *
     * @param flow the flow
     */
    private void layoutTabs(Flow<StudyParticipantCommand> flow) {
        flow.addTab(new SelectStudyParticipantTab());
        flow.addTab(new ScheduleCrfTab());
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        StudyParticipantCommand studyParticipantCommand = new StudyParticipantCommand();
        if (!StringUtils.isBlank(request.getParameter("sid"))) {
            StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(Integer.parseInt(request.getParameter("sid")));
            if (studyParticipantCrfSchedule != null) {
                StudyParticipantAssignment studyParticipantAssignment = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment();
                studyParticipantCommand.setStudy(studyParticipantAssignment.getStudySite().getStudy());
                studyParticipantCommand.setParticipant(studyParticipantAssignment.getParticipant());
                studyParticipantCommand.setStudyParticipantAssignment(studyParticipantAssignment);
            }
        }
        if (!StringUtils.isBlank(request.getParameter("pId")) && studyParticipantCommand.getParticipant() == null) {
            Participant participant = participantRepository.findById(Integer.valueOf(request.getParameter("pId")));
            studyParticipantCommand.setParticipant(participant);
            studyParticipantCommand.setStudy(participant.getStudyParticipantAssignments().get(0).getStudySite().getStudy());
        }

        return studyParticipantCommand;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        StudyParticipantCommand studyParticipantCommand = (StudyParticipantCommand) command;

        studyParticipantAssignmentRepository.save(studyParticipantCommand.getStudyParticipantAssignment());

        StudyParticipantAssignment studyParticipantAssignment = studyParticipantAssignmentRepository.findById(studyParticipantCommand.getStudyParticipantAssignment().getId());
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
            studyParticipantCrf.getStudyParticipantCrfSchedules();
        }
        studyParticipantCommand.setStudyParticipantAssignment(studyParticipantAssignment);

        ModelAndView modelAndView = new ModelAndView("participant/confirmschedule", errors.getModel());
        return modelAndView;
    }

    /**
     * Sets the study participant assignment repository.
     *
     * @param studyParticipantAssignmentRepository
     *         the new study participant assignment repository
     */
    @Required
    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }

    @Required
    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }

    @Override
    protected int getInitialPage(HttpServletRequest request, Object command) {
        StudyParticipantCommand studyParticipantCommand = (StudyParticipantCommand) command;
        if (studyParticipantCommand.getStudy() != null && studyParticipantCommand.getParticipant() != null) {
            return 1;
        }
        return super.getInitialPage(request, command);
    }
}
