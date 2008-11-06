package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.query.StudyParticipantAssignmentQuery;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfRepository;
import gov.nih.nci.ctcae.web.form.CtcAeTabbedFlowController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @crated Nov 5, 2008
 */
public class ScheduleCrfController<C extends StudyParticipantCommand> extends CtcAeTabbedFlowController<StudyParticipantCommand> {

    private StudyParticipantCrfRepository studyParticipantCrfRepository;


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
        return new StudyParticipantCommand();
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        StudyParticipantCommand studyParticipantCommand = (StudyParticipantCommand) command;

        StudyParticipantAssignmentQuery query = new StudyParticipantAssignmentQuery();
        query.filterByParticipantId(studyParticipantCommand.getParticipant().getId());
        query.filterByStudyId(studyParticipantCommand.getStudy().getId());
        List<StudyParticipantAssignment> persistables = (List<StudyParticipantAssignment>) finderRepository.find(query);

        for (StudyParticipantCrf studyParticipantCrf : studyParticipantCommand.getStudyParticipants()) {
            studyParticipantCrf.setStudyParticipantAssignment(persistables.get(0));
            studyParticipantCrfRepository.save(studyParticipantCrf);
        }

        ModelAndView modelAndView = new ModelAndView("");
        return modelAndView;
    }

    @Required
    public void setStudyParticipantCrfRepository(StudyParticipantCrfRepository studyParticipantCrfRepository) {
        this.studyParticipantCrfRepository = studyParticipantCrfRepository;
    }

}
