package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateFormController<C extends CreateFormCommand> extends CtcAeTabbedFlowController<CreateFormCommand> {

    private CRFRepository crfRepository;

    public CreateFormController() {
        setCommandClass(CreateFormCommand.class);
        Flow<CreateFormCommand> flow = new Flow<CreateFormCommand>("Build Form");
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
        CreateFormCommand createFormCommand = (CreateFormCommand) command;
        crfRepository.save(createFormCommand.getStudyCrf().getCrf());
        ModelAndView modelAndView = new ModelAndView("form/confirmForm", errors.getModel());
        return modelAndView;


    }

    @Required
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

}
