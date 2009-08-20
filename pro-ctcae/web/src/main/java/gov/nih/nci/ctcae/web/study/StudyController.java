package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.form.CtcAeSecuredTabbedFlowController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @since Mar 2, 2009
 */
public class StudyController extends CtcAeSecuredTabbedFlowController<StudyCommand> {
    /**
     * The study repository.
     */
    protected StudyRepository studyRepository;
    protected GenericRepository genericRepository;
    protected UserRepository userRepository;
    public StudyController() {
        super();
        setCommandClass(StudyCommand.class);
        Flow<StudyCommand> flow = new Flow<StudyCommand>("Enter Study");
        layoutTabs(flow);
        setFlowFactory(new StaticFlowFactory<StudyCommand>(flow));
        setAllowDirtyBack(false);
        setAllowDirtyForward(false);

    }

    protected void layoutTabs(final Flow<StudyCommand> flow) {
        flow.addTab(new StudyDetailsTab());
        flow.addTab(new StudySitesTab());
        flow.addTab(new StudyClinicalStaffTab());
        flow.addTab(new StudySiteClinicalStaffTab());

        flow.addTab(new EmptyStudyTab("study.tab.overview", "study.tab.overview", "study/study_reviewsummary"));

    }


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */

    @Override
    protected ModelAndView processFinish(final HttpServletRequest request,
                                         final HttpServletResponse response, final Object command,
                                         final BindException errors) throws Exception {

        StudyCommand studyCommand = (StudyCommand) command;


        studyCommand.setStudy(studyRepository.save(studyCommand.getStudy()));

        ModelAndView mv = new ModelAndView("study/confirmStudy", errors.getModel());

        return mv;
    }

    @Override
    protected void save(StudyCommand command) {

        command.setStudy(studyRepository.save(command.getStudy()));

        command.updateClinicalStaffs();

    }

    protected String getFormSessionAttributeName() {
        return StudyController.class.getName() + ".FORM." + getCommandName();
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Override
    protected boolean shouldSave(HttpServletRequest request, StudyCommand command, Tab tab) {
        return !(tab instanceof EmptyStudyTab) ? true : false;


    }
    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
