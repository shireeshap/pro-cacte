package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.rules.ProCtcAERule;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * @author Harsh Agarwal
 */
public class AddFormRuleController extends AbstractController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/formRule");
        CreateFormCommand command = ControllersUtils.getFormCommand(request);
        ProCtcAERule proCtcAERule = new ProCtcAERule();
        command.getFormOrStudySiteRules().add(proCtcAERule);
        modelAndView.addObject("proCtcAERule", proCtcAERule);
        modelAndView.addObject("ruleIndex", command.getFormOrStudySiteRules().size() - 1);
        modelAndView.addObject("isSite", request.getParameter("isSite"));

        return modelAndView;
    }
}