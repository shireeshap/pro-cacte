package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.ctcae.core.repository.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.web.form.CtcAeTabbedFlowController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Harsh Agarwal
 * @crated Nov 5, 2008
 */
public class ScheduleCrfController<C extends StudyParticipantCommand> extends CtcAeTabbedFlowController<StudyParticipantCommand> {

    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;

    public ScheduleCrfController() {
        setCommandClass(StudyParticipantCommand.class);
        Flow<StudyParticipantCommand> flow = new Flow<StudyParticipantCommand>("Schedule Crf");
        layoutTabs(flow);
        setFlowFactory(new StaticFlowFactory<StudyParticipantCommand>(flow));
        setAllowDirtyBack(false);
        setAllowDirtyForward(false);
        setSessionForm(true);

    }

    private void layoutTabs(Flow<StudyParticipantCommand> flow) {
        flow.addTab(new SelectStudyParticipantTab());
        flow.addTab(new ScheduleCrfTab());
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        StudyParticipantCommand studyParticipantCommand = new StudyParticipantCommand();
        return studyParticipantCommand;
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        StudyParticipantCommand studyParticipantCommand = (StudyParticipantCommand) command;

        studyParticipantCommand.setFinderRepository(finderRepository);
        //studyParticipantCommand.createSchedules();
        studyParticipantAssignmentRepository.save(studyParticipantCommand.getStudyParticipantAssignment());

        StudyParticipantAssignment studyParticipantAssignment = finderRepository.findById(StudyParticipantAssignment.class, studyParticipantCommand.getStudyParticipantAssignment().getId());
        for(StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()){
            studyParticipantCrf.getStudyParticipantCrfSchedules();
        }
        studyParticipantCommand.setStudyParticipantAssignment(studyParticipantAssignment);

        ModelAndView modelAndView = new ModelAndView("participant/confirmschedule", errors.getModel());
        return modelAndView;
    }

    @Required
    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }
}
