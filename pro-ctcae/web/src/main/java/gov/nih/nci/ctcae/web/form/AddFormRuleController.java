package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.rules.ProCtcAERulesService;
import gov.nih.nci.ctcae.web.rules.ProCtcAERule;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.semanticbits.rules.brxml.Rule;

//
/**
 * @author Harsh Agarwal
 */
public class AddFormRuleController extends AbstractController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/formRule");
        CreateFormCommand command = ControllersUtils.getFormCommand(request);
        ProCtcAERule proCtcAERule = new ProCtcAERule();
        command.getFormRules().add(proCtcAERule);
        modelAndView.addObject("proCtcAERule", proCtcAERule);
        modelAndView.addObject("ruleIndex", command.getFormRules().size() - 1);
        modelAndView.addObject("crfSymptoms", ListValues.getSymptomsForCRF(command.getCrf()));
        modelAndView.addObject("questionTypes", ListValues.getQuestionTypes(command.getCrf()));
        modelAndView.addObject("comparisonOptions", ListValues.getComparisonOptions());
        modelAndView.addObject("comparisonValues", ListValues.getComparisonValues(command.getCrf()));
        modelAndView.addObject("notifications", ListValues.getNotificationOptions());

        return modelAndView;
    }
}