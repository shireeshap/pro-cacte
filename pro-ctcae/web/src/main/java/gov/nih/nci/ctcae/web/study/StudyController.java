package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.form.CtcAeTabbedFlowController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public abstract class StudyController<C extends StudyCommand> extends CtcAeTabbedFlowController<StudyCommand> {

    private StudyRepository studyRepository;

    public StudyController() {
        setCommandClass(StudyCommand.class);
        Flow<StudyCommand> flow = new Flow<StudyCommand>("Enter Study");
        layoutTabs(flow);
        setFlowFactory(new StaticFlowFactory<StudyCommand>(flow));
        setAllowDirtyBack(false);
        setAllowDirtyForward(false);

    }

    protected abstract void layoutTabs(final Flow<StudyCommand> flow);

    @Override
    protected ModelAndView processFinish(final HttpServletRequest request,
                                         final HttpServletResponse response, final Object command,
                                         final BindException errors) throws Exception {

        StudyCommand studyCommand = (StudyCommand) command;
        // saveResearchStaff the study by calling merge, as the study might be assocated
        // to different copy of same object (eg: Organization, with same id)
        // in different screens (hibernate session)
        studyCommand.setStudy(studyRepository.save(studyCommand.getStudy()));

        ModelAndView mv = new ModelAndView("study/confirmStudy", errors.getModel());

        return mv;
    }


    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }


}