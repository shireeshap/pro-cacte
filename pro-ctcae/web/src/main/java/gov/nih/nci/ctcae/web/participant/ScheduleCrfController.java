package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.repository.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.web.form.CtcAeSecuredTabbedFlowController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class ScheduleCrfController.
 *
 * @author Harsh Agarwal
 * @crated Nov 5, 2008
 */
public class ScheduleCrfController<C extends StudyParticipantCommand> extends CtcAeSecuredTabbedFlowController<StudyParticipantCommand> {

    /**
     * The study participant assignment repository.
     */
    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;

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
        return studyParticipantCommand;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        StudyParticipantCommand studyParticipantCommand = (StudyParticipantCommand) command;

        studyParticipantCommand.checkRepetition(request);
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

    
}
