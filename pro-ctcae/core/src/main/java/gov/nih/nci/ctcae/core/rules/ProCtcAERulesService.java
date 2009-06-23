package gov.nih.nci.ctcae.core.rules;

import com.semanticbits.rules.api.BusinessRulesExecutionService;
import com.semanticbits.rules.api.RuleAuthoringService;
import com.semanticbits.rules.api.RulesEngineService;
import com.semanticbits.rules.brxml.*;
import com.semanticbits.rules.objectgraph.FactResolver;
import com.semanticbits.rules.utils.RuleUtil;
import gov.nih.nci.ctcae.core.domain.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Harsh
 * Date: Mar 24, 2009
 * Time: 1:52:31 PM
 */
public class ProCtcAERulesService {


    static RuleAuthoringService ruleAuthoringService;
    static RulesEngineService rulesEngineService;
    static BusinessRulesExecutionService ruleExecutionService;

    public static RuleSet getExistingRuleSetForCrf(CRF crf) {
        String packageName = getPackageNameForCrf(crf);
        return ruleAuthoringService.getRuleSet(packageName, true);
    }


    public static void createRule(Rule rule) {
        ruleAuthoringService.createRule(rule);
    }

    public static Rule createRule(RuleSet ruleSet, String ruleName, List<String> symptoms, List<String> questiontypes, List<String> operators, List<String> values, List<String> notifications, String override) {
        Rule rule = new Rule();

        MetaData metaData = new MetaData();
        metaData.setName(ruleName);
        metaData.setPackageName(ruleSet.getName());
        metaData.setDescription("");
        rule.setMetaData(metaData);

        RuleAttribute ruleAttribute = new RuleAttribute();
        ruleAttribute.setName("override");
        ruleAttribute.setValue(StringUtils.isBlank(override) ? "N" : "Y");
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
        ruleSet.getRule().add(rule);
        createRule(rule);
        return rule;
    }

    public static RuleSet deleteExistingAndGetNewRuleSetForSite(CRF crf, StudyOrganization myOrg) throws Exception {
        deleteExistingRuleSetForCrfAndSite(crf, myOrg);
        String packageName = getPackageNameForCrfAndSite(crf, myOrg);
        return createRuleSetForCrfAndSite(crf, myOrg, packageName);
    }

    public static RuleSet getExistingRuleSetForCrfAndSite(CRF crf, StudyOrganization myOrg) {
        String packageName = getPackageNameForCrfAndSite(crf, myOrg);
        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        if (ruleSet == null) {
            ruleSet = getExistingRuleSetForCrf(crf);
        }
        return ruleSet;

    }

    public static boolean isSiteLevelRuleSet(RuleSet ruleSet) {
        return ruleSet.getName().startsWith(RuleSetType.STUDY_SITE_LEVEL.getPackagePrefix());
    }

    public static List<Object> fireRules(List<Object> inputObjects, String bindURI) {

        List<Object> outputObjects;
        outputObjects = ruleExecutionService.fireRules(bindURI, inputObjects);
        return outputObjects;
    }

    public static void deployRuleSet(RuleSet ruleSet) throws Exception {
        rulesEngineService.deployRuleSet(ruleSet);
    }

    public static RuleSet deleteExistingAndGetNewRuleSetForCrf(CRF crf) throws Exception {
        deleteExistingRuleSetForCrf(crf);
        String packageName = getPackageNameForCrf(crf);
        return createRuleSetForCrf(crf, packageName);
    }

    private static String getPackageNameForCrf(CRF crf) {
        return RuleUtil.getPackageName(RuleSetType.FORM_LEVEL.getPackagePrefix(), "Study_" + crf.getStudy().getId().toString(), "Form_" + crf.getId().toString());
    }

    private static Column getColumnForValidValue(String questionType, String operator, String value, int index) {
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

    private static Column getColumnForQuestionType(String questiontype, int index) {
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


    private static Column getColumnForSymptoms(List<String> symptoms) {
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

    private static Column getColumnForFactResolver() {
        Column column = new Column();
        column.setObjectType(FactResolver.class.getName());
        column.setIdentifier("factResolver");
        return column;
    }

    private static Column getColumnForProCtcAEFactResolver() {
        Column column = new Column();
        column.setObjectType(ProCtcAEFactResolver.class.getName());
        column.setIdentifier("proCtcAEFactResolver");
        return column;
    }

    private static Column getColumnForCRF() {
        Column column = new Column();
        column.setObjectType(CRF.class.getName());
        column.setIdentifier("crf");
        return column;
    }

    private static void deleteExistingRuleSetForCrf(CRF crf) throws Exception {
        RuleSet ruleSet = ProCtcAERulesService.getExistingRuleSetForCrf(crf);
        if (ruleSet != null) {
            rulesEngineService.deleteRuleSet(ruleSet.getName());
        }
    }

    private static RuleSet createRuleSetForCrf(CRF crf, String packageName) {
        RuleSet ruleSet = new RuleSet();
        ruleSet.setName(packageName);
        ruleSet.setStatus(RuleStatus.DRAFT.getDisplayName());
        ruleSet.setDescription("RuleSet for Study: " + crf.getStudy().getShortTitle() + ", CRF: " + crf.getTitle());
        ruleSet.setSubject("Form Rules||" + crf.getStudy().getShortTitle() + "||" + crf.getTitle());
        ruleSet.setCoverage("Not Enabled");
        List<String> imports = new ArrayList<String>();
        imports.add("gov.nih.nci.ctcae.core.domain.*");
        ruleSet.setImport(imports);
        ruleAuthoringService.createRuleSet(ruleSet);
        return ruleSet;
    }

    private static RuleSet createRuleSetForCrfAndSite(CRF crf, StudyOrganization myOrg, String packageName) {
        RuleSet ruleSet = new RuleSet();
        ruleSet.setName(packageName);
        ruleSet.setStatus(RuleStatus.DRAFT.getDisplayName());
        ruleSet.setDescription("RuleSet for Study: " + crf.getStudy().getShortTitle() + ", CRF: " + crf.getTitle() + ", Study Site: " + myOrg.getDisplayName());
        ruleSet.setSubject("Form Rules||" + crf.getStudy().getShortTitle() + "||" + crf.getTitle() + "||" + myOrg.getDisplayName());
        ruleSet.setCoverage("Not Enabled");
        ruleAuthoringService.createRuleSet(ruleSet);
        return ruleSet;
    }

    private static void deleteExistingRuleSetForCrfAndSite(CRF crf, StudyOrganization myOrg) throws Exception {
        RuleSet ruleSet = ProCtcAERulesService.getExistingRuleSetForCrfAndSite(crf, myOrg);
        if (ruleSet != null) {
            if (isSiteLevelRuleSet(ruleSet)) {
                rulesEngineService.deleteRuleSet(ruleSet.getName());
            }
        }
    }


    private static String getPackageNameForCrfAndSite(CRF crf, StudyOrganization myOrg) {
        return RuleUtil.getPackageName(RuleSetType.STUDY_SITE_LEVEL.getPackagePrefix(), "Study_" + crf.getStudy().getId().toString(), "Form_" + crf.getId().toString() + ".StudySite_" + myOrg.getId().toString());
    }

    @Required
    public void setRuleAuthoringService(RuleAuthoringService ruleAuthoringService) {
        ProCtcAERulesService.ruleAuthoringService = ruleAuthoringService;
    }

    @Required
    public void setRulesEngineService(RulesEngineService rulesEngineService) {
        ProCtcAERulesService.rulesEngineService = rulesEngineService;
    }

    @Required
    public void setRuleExecutionService(BusinessRulesExecutionService ruleExecutionService) {
        ProCtcAERulesService.ruleExecutionService = ruleExecutionService;
    }


}
