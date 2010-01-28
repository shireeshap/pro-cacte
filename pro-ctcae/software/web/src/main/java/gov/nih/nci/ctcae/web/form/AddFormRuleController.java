package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.core.domain.rules.CRFNotificationRule;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

//
/**
 * @author Harsh Agarwal
 */
public class AddFormRuleController extends AbstractController {

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/formRule");
        CreateFormCommand command = ControllersUtils.getFormCommand(request);
//        String isSite = request.getParameter("isSite");
//        String override = "N";
//        if ("true".equals(isSite)) {
//            override = "Y";
//        }
        List<String> notifications = new ArrayList<String>();
        notifications.add("PrimaryNurse");
        notifications.add("SiteCRA");
        notifications.add("PrimaryPhysician");
        notifications.add("LeadCRA");

        CRFNotificationRule crfNotificationRule = command.addRuleToCrf();
        modelAndView.addObject("ruleIndex", command.getCrf().getCrfNotificationRules().size() - 1);
        modelAndView.addObject("rule", crfNotificationRule.getNotificationRule());
        modelAndView.addObject("crfSymptoms", ListValues.getSymptomsForCRF(command.getCrf()));
        modelAndView.addObject("notifications", ListValues.getNotificationOptions());


        return modelAndView;
    }


}