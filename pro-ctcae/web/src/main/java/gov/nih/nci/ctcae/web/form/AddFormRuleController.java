package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFCycleDefinition;
import gov.nih.nci.ctcae.core.rules.ProCtcAERulesService;
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
        Rule rule = ProCtcAERulesService.addEmptyRule(command.getCrf(), command.getRuleSet());
        modelAndView.addObject("ruleSet", command.getRuleSet());
        modelAndView.addObject("ruleIndex", command.getRuleSet().getRule().size()-1);


        return modelAndView;
    }
}