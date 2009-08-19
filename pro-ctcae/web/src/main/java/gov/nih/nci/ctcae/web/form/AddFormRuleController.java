package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.core.rules.ProCtcAERule;
import gov.nih.nci.ctcae.core.rules.ProCtcAERulesService;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.semanticbits.rules.brxml.RuleSet;
import com.semanticbits.rules.brxml.Rule;

import java.util.ArrayList;

//
/**
 * @author Harsh Agarwal
 */
public class AddFormRuleController extends AbstractController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/formRule");
        CreateFormCommand command = ControllersUtils.getFormCommand(request);
        RuleSet ruleSet = command.getRuleSet();
        String isSite = request.getParameter("isSite");
        String override = "N";
        if ("true".equals(isSite)) {
            override = "Y";
        }
        Rule rule = ProCtcAERulesService.createRule(ruleSet, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), override);
        ProCtcAERule proCtcAERule = ProCtcAERule.getProCtcAERule(rule);
        command.getFormOrStudySiteRules().add(proCtcAERule);
        modelAndView.addObject("proCtcAERule", proCtcAERule);
        modelAndView.addObject("ruleIndex", command.getFormOrStudySiteRules().size() - 1);
        modelAndView.addObject("isSite", isSite);

        return modelAndView;
    }
}