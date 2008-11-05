package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.AbstractTabbedFlowFormController;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.ControllerTools;
import gov.nih.nci.ctcae.web.editor.RepositoryBasedEditor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateFormController<C extends CreateFormCommand> extends AbstractTabbedFlowFormController<CreateFormCommand> {

    private StudyRepository studyRepository;
    protected FinderRepository finderRepository;

    protected ControllerTools controllerTools;

    public CreateFormController() {
        setCommandClass(CreateFormCommand.class);
        Flow<CreateFormCommand> flow = new Flow<CreateFormCommand>("Enter Form");
        layoutTabs(flow);
        setFlowFactory(new StaticFlowFactory<CreateFormCommand>(flow));
        setAllowDirtyBack(false);
        setAllowDirtyForward(false);
        setSessionForm(true);

    }

    private void layoutTabs(Flow<CreateFormCommand> flow) {
        flow.addTab(new SelectStudyForFormTab());
        flow.addTab(new FormDetailsTab());
        flow.addTab(new ReviewFormTab());

    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        super.initBinder(request, binder);
        binder.registerCustomEditor(Date.class, controllerTools.getDateEditor(true));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        RepositoryBasedEditor studyEditor = new RepositoryBasedEditor(finderRepository, Study.class);
        binder.registerCustomEditor(Study.class, studyEditor);


    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CreateFormCommand command = (CreateFormCommand) ControllersUtils.getFormCommand(request, this);
        if (command == null) {
            command = new CreateFormCommand();

        }
////        else {
////            request.setAttribute("flashMessage", "You were already updating one form. Do you want to  resume it or discard it.");
////        }
        //remove this line
        command = new CreateFormCommand();
        if (!StringUtils.isBlank(request.getParameter("studyId"))) {
            command.getStudyCrf().setStudy(studyRepository.findById(Integer.parseInt(request.getParameter("studyId"))));
        }
        return command;


    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = new ModelAndView("");
        return modelAndView;


    }

    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    @Required

    public void setControllerTools(ControllerTools controllerTools) {
        this.controllerTools = controllerTools;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}
