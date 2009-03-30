package gov.nih.nci.ctcae.web.rules;

import com.semanticbits.rules.brxml.*;
import com.semanticbits.rules.utils.RuleUtil;
import com.semanticbits.rules.api.RuleAuthoringService;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.ArrayList;

import gov.nih.nci.ctcae.core.domain.*;

/**
 * User: Harsh
 * Date: Mar 24, 2009
 * Time: 1:52:31 PM
 */
public class ProCtcAERulesService {


    static RuleAuthoringService ruleAuthoringService;

    public RuleSet getRuleSetForForm(CRF crf) {
        String packageName = RuleUtil.getPackageName(RuleSetType.FORM_LEVEL.getPackagePrefix(), crf.getStudy().getId().toString(), crf.getId().toString());
        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        if (ruleSet == null) {
            ruleSet = new RuleSet();
            ruleSet.setName(packageName);
            ruleSet.setStatus(RuleStatus.DRAFT.getDisplayName());
            ruleSet.setDescription("RuleSet for Study: " + crf.getStudy().getShortTitle() + ", CRF: " + crf.getTitle());
            ruleSet.setSubject("Form Rules||" + crf.getStudy().getShortTitle() + "||" + crf.getTitle());
            ruleSet.setCoverage("Not Enabled");
            ruleAuthoringService.createRuleSet(ruleSet);
        }
        return ruleSet;
    }


    @Required
    public void setRuleAuthoringService(RuleAuthoringService ruleAuthoringService) {
        this.ruleAuthoringService = ruleAuthoringService;
    }

    public static void createRule(Rule rule) {
        ruleAuthoringService.createRule(rule);
    }


}
