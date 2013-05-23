package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: Apr 7, 2011
 * Time: 3:32:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddMoreQuestionByParticipantController extends CtcAeSimpleFormController {

    private GenericRepository genericRepository;

    public AddMoreQuestionByParticipantController() {
        super();
        setFormView("form/addMoreQuestionForParticipant");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    	SubmitFormCommand sCommand = (SubmitFormCommand) command;
    	String direction = RequestUtils.getStringParameter(request, "direction");
//    	String direction = request.getParameter("direction");
    	Integer healthAmount = RequestUtils.getIntParameter(request, "healthAmount");
//    	Integer healthAmount = Integer.parseInt(request.getParameter("healthAmount").toString());
    	
    	int pageNumber = sCommand.getNewPageIndex();
    	String[] selectedSymptoms = request.getParameterValues("symptomsByParticipants");
    	sCommand.getSchedule().setHealthAmount(healthAmount);

        if ("continue".equals(direction) && selectedSymptoms != null) {
            sCommand = (SubmitFormCommand) command;
            sCommand.addMoreParticipantAddedQuestions(selectedSymptoms, true);
        } else if(sCommand.getIsEq5dCrf()){
        	sCommand.setSchedule(genericRepository.save(sCommand.getSchedule()));
        }
        	
        //Setting the next or previous page number to be rendered in command object (will be used in SubmitFormController)
        sCommand.setCurrentPageIndex(String.valueOf(pageNumber));
        
        ModelAndView mv = showForm(request, errors, "");
        request.getSession().setAttribute(SubmitFormController.class.getName() + ".FORM." + "command", sCommand);
        request.getSession().setAttribute("id", ((SubmitFormCommand) command).getSchedule().getId());
        mv.setView(new RedirectView("submit"));
        return mv;
    }


    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand)
                request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");
        return submitFormCommand;
    }


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
