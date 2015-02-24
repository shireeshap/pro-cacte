package gov.nih.nci.ctcae.web.form;

import java.util.ArrayList;
import java.util.List;
import gov.nih.nci.ctcae.core.domain.CRFCycleDefinition;
import gov.nih.nci.ctcae.core.domain.FormArmSchedule;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Amey
 * Invoked via Ajax call from calendar_template.jsp.
 * After deleting any of crfCycleDefinitions associated with a Crf, this class is used to reload crfCycleDefinition section of the jsp, thus reflecting  
 * delete operation (display the available set of crfCycleDefinitions and omit the deleted cycleDefinition and handle their naming accordingly.)
 */
public class ReloadFormScheduleCycleController extends AbstractController {
	
	private static String TRUE = "true"; 	

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
    	ModelAndView modelAndView;
        CreateFormCommand command = ControllersUtils.getFormCommand(request);
        String paramIndex = request.getParameter("indexToRelod");
        int index = Integer.parseInt(paramIndex);
        List<CRFCycleDefinition> cycleDefinitionList = new ArrayList<CRFCycleDefinition>();
        List<Integer> list = new ArrayList<Integer>();
        String reloadHiddenInputs = request.getParameter("reloadHiddenInputs");
        
        if(TRUE.equals(reloadHiddenInputs)){
        	 modelAndView = new ModelAndView("form/ajax/reloadHiddenInputsDefinition");
        }else{
        	 modelAndView = new ModelAndView("form/ajax/reloadFormScheduleCycleDefinition");
        }
        FormArmSchedule formArmSchedule = command.getSelectedFormArmSchedule();
        int i = 0;
        for(CRFCycleDefinition crfCycleDefinition : formArmSchedule.getCrfCycleDefinitions()){
        	if(i >= index){
        		list.add(i);
        		cycleDefinitionList.add(crfCycleDefinition);
        	}
        	i++;
        }
        modelAndView.addObject("crfCycleDefinitionList", cycleDefinitionList);
        modelAndView.addObject("indices",list);
        modelAndView.addObject("cyclelengthunits", ListValues.getCalendarRepetitionUnits());
        modelAndView.addObject("cyclelengthunits", ListValues.getCalendarRepetitionUnits());
        modelAndView.addObject("cycleplannedrepetitions", ListValues.getCyclePlannedRepetitions());
        return modelAndView;
    }
}