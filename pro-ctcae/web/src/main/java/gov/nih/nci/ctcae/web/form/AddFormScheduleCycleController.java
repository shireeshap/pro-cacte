package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFCycleDefinition;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * @author Harsh Agarwal
 */
public class AddFormScheduleCycleController extends AbstractController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/formScheduleCycleDefinition");
        CreateFormCommand command = ControllersUtils.getFormCommand(request);
        CRFCycleDefinition crfCycleDefinition = new CRFCycleDefinition();
        crfCycleDefinition.setOrder(command.getCrf().getCrfCycleDefinitions().size() - 1);
        command.getCrf().addCrfCycleDefinition(crfCycleDefinition);
        modelAndView.addObject("cycleDefinitionIndex", command.getCrf().getCrfCycleDefinitions().size() - 1);
        modelAndView.addObject("cyclelengthunits", ListValues.getCalendarRepetitionUnits());

        return modelAndView;
    }
}