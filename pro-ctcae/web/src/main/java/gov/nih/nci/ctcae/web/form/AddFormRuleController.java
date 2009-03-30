package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.rules.ProCtcAERulesService;
import gov.nih.nci.ctcae.web.ControllersUtils;
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
        command.incrementRuleSetSize();
        Rule rule = new Rule();
        modelAndView.addObject("rule", rule);
        modelAndView.addObject("ruleIndex", command.getRuleSetSize());
        return modelAndView;
    }
}