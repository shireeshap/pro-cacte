package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

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
    	
    	int pageNumber = sCommand.getNewPageIndex();
    	String[] selectedSymptoms = request.getParameterValues("symptomsByParticipants");

        if ("continue".equals(direction) && selectedSymptoms != null) {
            sCommand.addMoreParticipantAddedQuestions(selectedSymptoms, true);
        }
        
        if(sCommand.getIsEq5dCrf()){
        	Integer healthAmount = null;
    		String val = request.getParameter("healthAmount");
    		if(!StringUtils.isEmpty(val)){
    			healthAmount = RequestUtils.getIntParameter(request, "healthAmount");
    		}
        		
        	sCommand.getSchedule().setHealthAmount(healthAmount);
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

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command,
    		BindException e) throws Exception {
    	super.onBindAndValidate(request, command, e);
    	SubmitFormCommand sCommand = (SubmitFormCommand) command;
    	if(sCommand.getIsEq5dCrf()){
    		if("continue".equals(sCommand.getDirection()) && StringUtils.isEmpty(request.getParameter("healthAmount"))){
    			sCommand.setDirection(" ");
            	e.reject("crf.health_score_missing", "Please provide a response to the health score.");
    		}
    	}
    }
    
    
    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
