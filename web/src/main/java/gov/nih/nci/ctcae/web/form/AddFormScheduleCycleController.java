package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFCycleDefinition;
import gov.nih.nci.ctcae.core.domain.FormArmSchedule;
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

        FormArmSchedule formArmSchedule = command.getSelectedFormArmSchedule();
        CRFCycleDefinition crfCycleDefinition = new CRFCycleDefinition();
        crfCycleDefinition.setOrder(formArmSchedule.getCrfCycleDefinitions().size() - 1);
        formArmSchedule.addCrfCycleDefinition(crfCycleDefinition);
        modelAndView.addObject("cycleDefinitionIndex", formArmSchedule.getCrfCycleDefinitions().size() - 1);
        modelAndView.addObject("cyclelengthunits", ListValues.getCalendarRepetitionUnits());
        modelAndView.addObject("cyclelengthunits", ListValues.getCalendarRepetitionUnits());
        modelAndView.addObject("cycleplannedrepetitions", ListValues.getCyclePlannedRepetitions());
        return modelAndView;
    }
}