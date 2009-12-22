package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.core.rules.ProCtcAERule;
import gov.nih.nci.ctcae.core.rules.ProCtcAERulesService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.semanticbits.rules.brxml.RuleSet;
import com.semanticbits.rules.brxml.Rule;

import java.util.ArrayList;
import java.util.List;

//
/**
 * @author Harsh Agarwal
 */
public class AddFormRuleController extends AbstractController {

    private ProCtcAERulesService proCtcAERulesService;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/formRule");
        CreateFormCommand command = ControllersUtils.getFormCommand(request);
        RuleSet ruleSet = command.getRuleSet();
        String isSite = request.getParameter("isSite");
        String override = "N";
        if ("true".equals(isSite)) {
            override = "Y";
        }
        List<String> notifications = new ArrayList<String>();
        notifications.add("PrimaryNurse");
        notifications.add("SiteCRA");
        notifications.add("PrimaryPhysician");
        notifications.add("LeadCRA");

        Rule rule = proCtcAERulesService.createRule(ruleSet, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), notifications, override, false);
        ProCtcAERule proCtcAERule = ProCtcAERule.getProCtcAERule(rule);
        command.getFormOrStudySiteRules().add(proCtcAERule);
        modelAndView.addObject("proCtcAERule", proCtcAERule);
        modelAndView.addObject("ruleIndex", command.getFormOrStudySiteRules().size() - 1);
        modelAndView.addObject("isSite", isSite);
        modelAndView.addObject("notifications", ListValues.getNotificationOptions());
        modelAndView.addObject("crfSymptoms", ListValues.getSymptomsForCRF(command.getCrf()));

        return modelAndView;
    }

    @Required
    public void setProCtcAERulesService(ProCtcAERulesService proCtcAERulesService) {
        this.proCtcAERulesService = proCtcAERulesService;
    }
}