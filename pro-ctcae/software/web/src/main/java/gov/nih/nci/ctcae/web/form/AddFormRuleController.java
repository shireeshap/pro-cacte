package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.rules.*;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

//
/**
 * @author Harsh Agarwal
 */
public class AddFormRuleController extends AbstractController {

    CRFRepository crfRepository;
    StudyOrganizationRepository studyOrganizationRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        String action = request.getParameter("action");
        if ("addRule".equals(action)) {
            return addRule(request);
        }
        if ("addCondition".equals(action)) {
            return addCondition(request);
        }
        return null;
    }

    private ModelAndView addCondition(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("form/ajax/formRuleCondition");
        CreateFormCommand command = ControllersUtils.getFormCommand(request);
        String ruleId = request.getParameter("ruleId");
        String isSite = request.getParameter("isSite");
        int index = -1;
        NotificationRule notificationRule = null;
        if ("true".equals(isSite)) {
            for (SiteCRFNotificationRule siteCRFNotificationRule : command.getMyOrg().getSiteCRFNotificationRules()) {
                if (siteCRFNotificationRule.getNotificationRule().getId().equals(ruleId)) {
                    notificationRule = siteCRFNotificationRule.getNotificationRule();
                    break;
                }
            }
        } else {
            for (CRFNotificationRule crfNotificationRule : command.getCrf().getCrfNotificationRules()) {
                if (crfNotificationRule.getNotificationRule().getId().intValue() == Integer.parseInt(ruleId)) {
                    notificationRule = crfNotificationRule.getNotificationRule();
                    break;
                }
            }
        }
        NotificationRuleCondition notificationRuleCondition = command.addCondition(notificationRule);
        index = notificationRule.getNotificationRuleConditions().size() - 1;
        
        //If number of deleted conditions(only tr from html is removed at this point; the command object will still have the conditions) equals one less than the size
        //of the conditions Array then this is the first condition. This happens when user deletes all conditions and starts over.
        String dc = request.getParameter("dcStr");
        int sizeOfDeletedConditions = -1;
        if(dc != null){
        	if(dc.trim().equals("")){
        		sizeOfDeletedConditions = 0;
        	} else {
            	sizeOfDeletedConditions = dc.split(",").length;
        	}
        }
        boolean showOr = (sizeOfDeletedConditions == index)?false:true;
        
        modelAndView.addObject("showOr", showOr);
        modelAndView.addObject("ruleId", ruleId);
        modelAndView.addObject("ruleConditionIndex", index);
        modelAndView.addObject("questionTypes", new ArrayList<ProCtcQuestionType>(command.getCrf().getAllQuestionTypes()));
        modelAndView.addObject("condition", notificationRuleCondition);
        modelAndView.addObject("operators", Arrays.asList(NotificationRuleOperator.values()));
        if(notificationRuleCondition.getProCtcQuestionType()!=null){
        modelAndView.addObject("thresholds", Arrays.asList(notificationRuleCondition.getProCtcQuestionType().getValidValues()));
        }
        return modelAndView;
    }

    private ModelAndView addRule(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("form/ajax/formRule");
        CreateFormCommand command = ControllersUtils.getFormCommand(request);
        String isSite = request.getParameter("isSite");
        int index;
        if ("true".equals(isSite)) {
            command.addRuleToSite();
            index = command.getMyOrg().getSiteCRFNotificationRules().size() - 1;
            command.setMyOrg(studyOrganizationRepository.save(command.getMyOrg()));
            modelAndView.addObject("rule", command.getMyOrg().getSiteCRFNotificationRules().get(index).getNotificationRule());
        } else {
            command.addRuleToCrf();
            index = command.getCrf().getCrfNotificationRules().size() - 1;
            //command.setCrf(crfRepository.save(command.getCrf()));
            modelAndView.addObject("rule", command.getCrf().getCrfNotificationRules().get(index).getNotificationRule());
        }
        modelAndView.addObject("ruleIndex", index);
        modelAndView.addObject("crfSymptoms", ListValues.getSymptomsForCRF(command.getCrf()));
        modelAndView.addObject("notifications", ListValues.getNotificationOptions());
        modelAndView.addObject("isSite", isSite);
        modelAndView.addObject("questionTypes", new ArrayList<ProCtcQuestionType>(command.getCrf().getAllQuestionTypes()));
        modelAndView.addObject("operators", Arrays.asList(NotificationRuleOperator.values()));


        return modelAndView;
    }

    @Required
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    @Required
    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }
}