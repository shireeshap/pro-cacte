package gov.nih.nci.ctcae.core.rules;

import com.semanticbits.rules.brxml.*;
import com.semanticbits.rules.objectgraph.FactResolver;
import com.semanticbits.rules.utils.RuleUtil;
import com.semanticbits.rules.impl.*;
import gov.nih.nci.ctcae.core.domain.*;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User: Harsh
 * Date: Mar 24, 2009
 * Time: 1:52:31 PM
 */
public class ProCtcAERulesService {


    public RuleAuthoringServiceImpl ruleAuthoringService;
    public RulesEngineServiceImpl rulesEngineService;
    public BusinessRulesExecutionServiceImpl ruleExecutionService;
    public RepositoryServiceImpl repositoryService;

    public RuleSet getRuleSetForCrf(CRF crf, boolean createNewIfNull) {
        String packageName = getPackageNameForCrf(crf);
        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, false);
        if (ruleSet == null) {
            if (createNewIfNull) {
                ruleSet = createRuleSetForCrf(crf, packageName);
            }
        }
        logout();
        return ruleSet;
    }

    public synchronized boolean ruleSetExists(CRF crf) {
        String packageName = getPackageNameForCrf(crf);
        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, false);
        return ruleSet != null;
    }

    public Rule createRule(RuleSet ruleSet, List<String> symptoms, List<String> questiontypes, List<String> operators, List<String> values, List<String> notifications, String override, boolean defaultRule) {
        Rule rule = new Rule();

        MetaData metaData = new MetaData();
        String ruleName = "Rule_" + UUID.randomUUID().toString();
        if (defaultRule) {
            metaData.setName(ruleName + "_default_rule");
        } else {
            metaData.setName(ruleName);
        }
        metaData.setPackageName(ruleSet.getName());
        metaData.setDescription("");
        rule.setMetaData(metaData);
        setRuleProperties(rule, symptoms, questiontypes, operators, values, notifications, override);
        ruleSet.getRule().add(rule);
        ruleAuthoringService.createRule(rule);
        logout();
        return rule;
    }

    public Rule updateRule(String ruleId, List<String> symptoms, List<String> questiontypes, List<String> operators, List<String> values, List<String> notifications, String override) {
        Rule rule = ruleAuthoringService.getRule(ruleId);
        setRuleProperties(rule, symptoms, questiontypes, operators, values, notifications, override);
        ruleAuthoringService.updateRule(rule);
        logout();
        return rule;
    }


    private void setRuleProperties(Rule rule, List<String> symptoms, List<String> questiontypes, List<String> operators, List<String> values, List<String> notifications, String override) {
        RuleAttribute ruleAttribute = new RuleAttribute();
        ruleAttribute.setName("override");
        ruleAttribute.setValue("Y".equals(override) ? "Y" : "N");
        List<RuleAttribute> l = new ArrayList<RuleAttribute>();
        l.add(ruleAttribute);
        rule.setRuleAttribute(l);
        Condition condition = new Condition();
        condition.getColumn().add(getColumnForSymptoms(symptoms));
        condition.getColumn().add(getColumnForFactResolver());
        condition.getColumn().add(getColumnForProCtcAEFactResolver());
        condition.getColumn().add(getColumnForCRF());
        int i = 0;
        for (String questiontype : questiontypes) {
            condition.getColumn().add(getColumnForQuestionType(questiontype, i));
            condition.getColumn().add(getColumnForValidValue(questiontype, operators.get(i), values.get(i), i));
            i++;
        }
        rule.setCondition(condition);
        rule.setAction(notifications);
    }


    public RuleSet getRuleSetForCrfAndSite(CRF crf, StudyOrganization myOrg, boolean createNewIfNull) {
        String packageName = getPackageNameForCrfAndSite(crf, myOrg);
        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, false);
        if (ruleSet == null) {
            RuleSet crfRuleSet = getRuleSetForCrf(crf, false);
            if (createNewIfNull) {
                ruleSet = createRuleSetForCrfAndSite(crf, myOrg, getPackageNameForCrfAndSite(crf, myOrg));
                copyRulesFromCrfRuleSet(crfRuleSet, ruleSet);
            } else {
                return crfRuleSet;
            }
        }
        logout();
        return ruleSet;

    }

    private void copyRulesFromCrfRuleSet(RuleSet crfRuleSet, RuleSet ruleSet) {
        for (Rule rule : crfRuleSet.getRule()) {
            ProCtcAERule proCtcAERule = ProCtcAERule.getProCtcAERule(rule);
            createRule(ruleSet, proCtcAERule.getSymptoms(), proCtcAERule.getQuestiontypes(), proCtcAERule.getOperators(), proCtcAERule.getValues(), proCtcAERule.getNotifications(), proCtcAERule.getOverride(), false);
        }
    }

    public boolean isSiteLevelRuleSet(RuleSet ruleSet) {
        return ruleSet.getName().startsWith(RuleSetType.STUDY_SITE_LEVEL.getPackagePrefix());
    }

    public boolean isFormLevelRuleSet(RuleSet ruleSet) {
        return ruleSet.getName().startsWith(RuleSetType.FORM_LEVEL.getPackagePrefix());
    }

    public List<Object> fireRules(List<Object> inputObjects, String bindURI) {

        List<Object> outputObjects;
        outputObjects = ruleExecutionService.fireRules(bindURI, inputObjects);
        logout();
        return outputObjects;
    }

    public void deployRuleSet(RuleSet ruleSet) throws Exception {
        rulesEngineService.deployRuleSet(ruleSet);
        logout();
    }

    public RuleSet deleteExistingAndGetNewRuleSetForCrf(CRF crf) throws Exception {
        deleteExistingRuleSetForCrf(crf);
        String packageName = getPackageNameForCrf(crf);
        RuleSet ruleSet = createRuleSetForCrf(crf, packageName);
        logout();
        return ruleSet;
    }

    private String getPackageNameForCrf(CRF crf) {
        return RuleUtil.getPackageName(RuleSetType.FORM_LEVEL.getPackagePrefix(), "Study_" + crf.getStudy().getId().toString(), "Form_" + crf.getId());
    }

    private Column getColumnForValidValue(String questionType, String operator, String value, int index) {
        String id = "proCtcValidValue" + index;
        String idQt = "proCtcQuestionType" + index;
        Column column = new Column();

        column.setObjectType(ProCtcValidValue.class.getName());
        column.setIdentifier(id);
        FieldConstraint fieldConstraint = new FieldConstraint();
        fieldConstraint.setFieldName(id);
        LiteralRestriction literalRestriction = new LiteralRestriction();
        literalRestriction.setEvaluator(operator);
        List<String> myValues = new ArrayList<String>();
        myValues.add(value);
        literalRestriction.setValue(myValues);

        column.setExpression("proCtcAEFactResolver.assertFact(crf," + idQt + "," + id + ",\"" + questionType + "\",\"" + operator + "\",\"" + value + "\")");
        fieldConstraint.getLiteralRestriction().add(literalRestriction);
        column.getFieldConstraint().add(fieldConstraint);
        return column;
    }

    private Column getColumnForQuestionType(String questiontype, int index) {
        String id = "proCtcQuestionType" + index;
        Column column = new Column();
        column.setObjectType(ProCtcQuestionType.class.getName());
        column.setIdentifier(id);

        FieldConstraint fieldConstraint = new FieldConstraint();
        fieldConstraint.setFieldName(id);
        LiteralRestriction literalRestriction = new LiteralRestriction();
        literalRestriction.setEvaluator("==");
        List<String> myValues = new ArrayList<String>();
        myValues.add(questiontype);
        literalRestriction.setValue(myValues);
        column.setExpression("true");
        fieldConstraint.getLiteralRestriction().add(literalRestriction);
        column.getFieldConstraint().add(fieldConstraint);
        return column;

    }


    private Column getColumnForSymptoms(List<String> symptoms) {
        Column column = new Column();
        column.setObjectType(ProCtcTerm.class.getName());
        column.setIdentifier("proCtcTerm");
        FieldConstraint fieldConstraint = new FieldConstraint();
        fieldConstraint.setFieldName("term");

        LiteralRestriction literalRestriction = new LiteralRestriction();
        literalRestriction.setEvaluator("==");
        List<String> myValues = new ArrayList<String>();

        for (String symptom : symptoms) {
            myValues.add(symptom);
        }
        literalRestriction.setValue(myValues);
        column.setExpression("factResolver.assertFact(proCtcTerm, null,\"term\", 'runTimeValue', 'runTimeOperator')");
        fieldConstraint.getLiteralRestriction().add(literalRestriction);
        column.getFieldConstraint().add(fieldConstraint);
        return column;
    }

    private Column getColumnForFactResolver() {
        Column column = new Column();
        column.setObjectType(FactResolver.class.getName());
        column.setIdentifier("factResolver");
        return column;
    }

    private Column getColumnForProCtcAEFactResolver() {
        Column column = new Column();
        column.setObjectType(ProCtcAEFactResolver.class.getName());
        column.setIdentifier("proCtcAEFactResolver");
        return column;
    }

    private Column getColumnForCRF() {
        Column column = new Column();
        column.setObjectType(CRF.class.getName());
        column.setIdentifier("crf");
        return column;
    }

    public void deleteExistingRuleSetForCrf(CRF crf) throws Exception {
        RuleSet ruleSet = getRuleSetForCrf(crf, false);
        if (ruleSet != null) {
            rulesEngineService.deleteRuleSet(ruleSet.getName());
        }
        logout();
    }

    private RuleSet createRuleSetForCrf(CRF crf, String packageName) {
        String description = "RuleSet for Study: " + crf.getStudy().getShortTitle() + ", CRF: " + crf.getTitle();
        String subject = "Form Rules||" + crf.getStudy().getShortTitle() + "||" + crf.getTitle();
        RuleSet ruleSet = createRuleSet(packageName, description, subject);
        logout();
        return ruleSet;
    }

    private RuleSet createRuleSetForCrfAndSite(CRF crf, StudyOrganization myOrg, String packageName) {
        String description = "RuleSet for Study: " + crf.getStudy().getShortTitle() + ", CRF: " + crf.getTitle() + ", Study Site: " + myOrg.getDisplayName();
        String subject = "Form Rules||" + crf.getStudy().getShortTitle() + "||" + crf.getTitle() + "||" + myOrg.getDisplayName();
        RuleSet ruleSet = createRuleSet(packageName, description, subject);
        logout();
        return ruleSet;
    }

    private RuleSet createRuleSet(String packageName, String description, String subject) {
        RuleSet ruleSet = new RuleSet();
        ruleSet.setName(packageName);
        ruleSet.setStatus(RuleStatus.DRAFT.getDisplayName());
        ruleSet.setDescription(description);
        ruleSet.setSubject(subject);
        ruleSet.setCoverage("Not Enabled");
        List<String> imports = new ArrayList<String>();
        imports.add("gov.nih.nci.ctcae.core.domain.*");
        ruleSet.setImport(imports);
        ruleAuthoringService.createRuleSet(ruleSet);
        return ruleSet;
    }

    public void deleteExistingRuleSetForCrfAndSite(CRF crf, StudyOrganization myOrg) throws Exception {
        RuleSet ruleSet = getRuleSetForCrfAndSite(crf, myOrg, false);
        if (ruleSet != null) {
            if (isSiteLevelRuleSet(ruleSet)) {
                rulesEngineService.deleteRuleSet(ruleSet.getName());
            }
        }
        logout();
    }


    private String getPackageNameForCrfAndSite(CRF crf, StudyOrganization myOrg) {
        return RuleUtil.getPackageName(RuleSetType.STUDY_SITE_LEVEL.getPackagePrefix(), "Study_" + crf.getStudy().getId().toString(), "Form_" + crf.getId().toString() + ".StudySite_" + myOrg.getId().toString());
    }

    @Required
    public void setRuleAuthoringService(RuleAuthoringServiceImpl ruleAuthoringService) {
        this.ruleAuthoringService = ruleAuthoringService;
    }

    @Required
    public void setRulesEngineService(RulesEngineServiceImpl rulesEngineService) {
        this.rulesEngineService = rulesEngineService;
    }

    @Required
    public void setRuleExecutionService(BusinessRulesExecutionServiceImpl ruleExecutionService) {
        this.ruleExecutionService = ruleExecutionService;
    }

    @Required
    public void setRepositoryService(RepositoryServiceImpl repositoryService) {
        this.repositoryService = repositoryService;
    }

    public void logout() {
        repositoryService.logout();
    }


    public void deleteRule(String ruleId, RuleSet ruleSet) throws Exception {
        rulesEngineService.unDeployRuleSet(ruleSet);
        Rule rule = ruleAuthoringService.getRule(ruleId);
        rulesEngineService.deleteRule(ruleSet.getName(), rule.getMetaData().getName());
        ruleSet.getRule().remove(rule);
        deployRuleSet(ruleSet);
        logout();
    }

    public RuleAuthoringServiceImpl getRuleAuthoringService() {
        return ruleAuthoringService;
    }
}
