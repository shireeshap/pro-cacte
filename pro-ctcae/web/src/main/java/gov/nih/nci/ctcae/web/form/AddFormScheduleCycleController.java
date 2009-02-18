package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFCycle;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * @author Vinay Kumar
 */
public class AddFormScheduleCycleController extends AbstractController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = null;
        CreateFormCommand command = ControllersUtils.getFormCommand(request);
        modelAndView = new ModelAndView("form/ajax/formScheduleCycle");
        CRFCycle crfCycle = new CRFCycle();
        command.getCrf().addCrfCycle(crfCycle);
        modelAndView.addObject("cycleIndex", command.getCrf().getCrfCycles().size() - 1);
        modelAndView.addObject("cyclelengthunits", ListValues.getCalendarRepetitionUnits());

        return modelAndView;
    }
}