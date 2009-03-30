package gov.nih.nci.ctcae.web.form;

import edu.nwu.bioinformatics.commons.CollectionUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.web.rules.ProCtcAERulesService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.semanticbits.rules.brxml.*;

//
/**
 * The Class CreateFormCommand.
 *
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateFormCommand implements Serializable {

    /**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(CreateFormCommand.class);

    /**
     * The crf.
     */
    private CRF crf;


    /**
     * The questions ids.
     */
    private String questionsIds;


    /**
     * The crf page numbers.
     */
    private String crfPageNumbers;

    /**
     * The crf page numbers to remove.
     */
    private String crfPageNumberToRemove = "";

    /**
     * The question ids to remove.
     */
    private String questionIdToRemove = "";


    private String crfCycleDefinitionIndexToRemove = "";

    private RuleSet ruleSet;

    private int ruleSetSize = 0;
    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        String title = getCrf().getTitle();
        return !org.apache.commons.lang.StringUtils.isBlank(title) ? title : "Click here to name";
    }


    /**
     * Instantiates a new creates the form command.
     */
    public CreateFormCommand() {
        super();
        crf = new CRF();
        crf.setStatus(CrfStatus.DRAFT);
        crf.setCrfVersion("1.0");

    }

    /**
     * Update crf items.
     *
     * @param proCtcQuestionRepository the pro ctc question repository
     */
    public void updateCrfItems(ProCtcQuestionRepository proCtcQuestionRepository) {


        removeQuestions(proCtcQuestionRepository);

        if (!org.apache.commons.lang.StringUtils.isBlank(crfPageNumberToRemove)) {
            crf.removeCrfPageByPageNumber(Integer.valueOf(crfPageNumberToRemove));
        }

        setQuestionIdToRemove("");
        setCrfPageNumberToRemove("");

        String[] crfPageNumberArray = org.springframework.util.StringUtils.commaDelimitedListToStringArray(crfPageNumbers);

        //now clear the empty pages
        clearEmptyPages();

        //reorder crf page items

        try {
            List<CRFPage> crfPages = crf.getCrfPagesSortedByPageNumber();
            for (int i = 0; i < crfPages.size(); i++) {
                CRFPage crfPage = crfPages.get(i);
                crfPage.setPageNumber(Integer.valueOf(crfPageNumberArray[i]));

            }
        } catch (Exception e) {
            logger.error("error while reordering crf page numbers" + e);
            //FIXME:SAURABH throw this exception
            // throw new CtcAeSystemException(e);
        }

        crf.updatePageNumberOfCrfPages();
        crf.updateCrfPageInstructions();


    }

    /**
     * Removes the questions.
     *
     * @param proCtcQuestionRepository the pro ctc question repository
     */
    private void removeQuestions(ProCtcQuestionRepository proCtcQuestionRepository) {

        if (!org.apache.commons.lang.StringUtils.isBlank(questionIdToRemove)) {
            ProCtcQuestion proCtcQuestion = proCtcQuestionRepository.findById(Integer.valueOf(questionIdToRemove));
            crf.removeCrfPageItemByQuestion(proCtcQuestion);
        } else {
            logger.debug("no question to remove as value ofquestionIdToRemove is either empty or null:" + questionIdToRemove);
        }
    }


    /**
     * Gets the crf.
     *
     * @return the crf
     */
    public CRF getCrf() {
        return crf;
    }

    /**
     * Sets the crf.
     *
     * @param crf the new crf
     */
    public void setCrf(CRF crf) {
        this.crf = crf;
    }


    /**
     * Gets the questions ids.
     *
     * @return the questions ids
     */
    public String getQuestionsIds() {
        return questionsIds;
    }


    /**
     * Sets the questions ids.
     *
     * @param questionsIds the new questions ids
     */
    public void setQuestionsIds(String questionsIds) {
        this.questionsIds = questionsIds;
    }


    /**
     * Gets the crf page numbers.
     *
     * @return the crf page numbers
     */
    public String getCrfPageNumbers() {
        return crfPageNumbers;
    }


    /**
     * Sets the crf page numbers.
     *
     * @param crfPageNumber the new crf page numbers
     */
    public void setCrfPageNumbers(final String crfPageNumber) {
        this.crfPageNumbers = crfPageNumber;
    }

    /**
     * Gets the crf page numbers to remove.
     *
     * @return the crf page numbers to remove
     */
    public String getCrfPageNumberToRemove() {
        return crfPageNumberToRemove;
    }

    /**
     * Sets the crf page numbers to remove.
     *
     * @param crfPageNumberToRemove the new crf page numbers to remove
     */
    public void setCrfPageNumberToRemove(final String crfPageNumberToRemove) {
        this.crfPageNumberToRemove = crfPageNumberToRemove;
    }


    /**
     * Adds the pro ctc term.
     *
     * @param proCtcTerm the pro ctc term
     * @return the object
     */
    public Object addProCtcTerm(ProCtcTerm proCtcTerm) {

        Object object = crf.addProCtcTerm(proCtcTerm);
        return object;


    }

    /**
     * Clear empty pages.
     */
    public void clearEmptyPages() {
        crf.clearEmptyPages();

    }

    /**
     * Gets the question ids to remove.
     *
     * @return the question ids to remove
     */

    public String getQuestionIdToRemove() {
        return questionIdToRemove;
    }

    /**
     * Sets the question ids to remove.
     *
     * @param questionIdToRemove the new question ids to remove
     */
    public void setQuestionIdToRemove(String questionIdToRemove) {
        this.questionIdToRemove = questionIdToRemove;
    }


    /**
     * Returns the selected pro ctc terms
     *
     * @return
     */
    public List<Integer> getSelectedProCtcTerms() {

        List<Integer> selectedProCtcTerms = new ArrayList<Integer>();
        return selectedProCtcTerms;
    }

    public String getCrfCycleDefinitionIndexToRemove() {
        return crfCycleDefinitionIndexToRemove;
    }

    public void setCrfCycleDefinitionIndexToRemove(String crfCycleDefinitionIndexToRemove) {
        this.crfCycleDefinitionIndexToRemove = crfCycleDefinitionIndexToRemove;
    }

    public void createCycles(HttpServletRequest request) {
        int cycleDefinitionIndex = -1;
        for (CRFCycleDefinition crfCycleDefinition : crf.getCrfCycleDefinitions()) {
            cycleDefinitionIndex++;
            crfCycleDefinition.getCrfCycles().clear();
            String repeat = crfCycleDefinition.getRepeatTimes();
            if (org.apache.commons.lang.StringUtils.isNumeric(repeat) || ("-1").equals(repeat)) {
                int repeattimes = Integer.parseInt(repeat);
                boolean isIndefinite = false;
                if (repeattimes == -1) {
                    repeattimes = 100;
                    isIndefinite = true;
                }
                for (int i = 0; i < repeattimes; i++) {
                    CRFCycle crfCycle = new CRFCycle();
                    if (isIndefinite) {
                        crfCycle.setCycleDays(request.getParameter("selecteddays_" + cycleDefinitionIndex + "_" + 0));
                    } else {
                        crfCycle.setCycleDays(request.getParameter("selecteddays_" + cycleDefinitionIndex + "_" + i));
                    }
                    crfCycle.setOrder(i);
                    crfCycleDefinition.addCrfCycle(crfCycle);
                }
            }
        }
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
        ruleSetSize = ruleSet.getRule().size();
    }

    public void processRules(HttpServletRequest request) {
        int ruleIndex = 0;
        String[] rules = (String[]) request.getParameterValues("rule");

        for (String dummyRule : rules) {
            List<String> symptoms = getListForRule(ruleIndex, request, "symptoms");
            List<String> questiontypes = getListForRule(ruleIndex, request, "questiontypes");
            List<String> operators = getListForRule(ruleIndex, request, "operators");
            List<String> values = getListForRule(ruleIndex, request, "values");
            List<String> notifications = getListForRule(ruleIndex, request, "notifications");
            String ruleName = "Rule " + ruleIndex;
            createRules(ruleSet, ruleName, symptoms, questiontypes, operators, values, notifications);
            ruleIndex++;
        }
    }

    public void createRules(RuleSet ruleSet, String ruleName, List<String> symptoms, List<String> questiontypes, List<String> operators, List<String> values, List<String> notifications) {
        for (String symptom : symptoms) {
            Rule rule = new Rule();
            MetaData metaData = new MetaData();
            metaData.setName(ruleName);
            metaData.setPackageName(ruleSet.getName());
            metaData.setDescription("");
            rule.setMetaData(metaData);
            Condition condition = new Condition();
            int j = 0;
            for (String questiontype : questiontypes) {
                Column column = new Column();
                column.setObjectType(ProCtcTerm.class.getName());
                column.setIdentifier(symptom);
                FieldConstraint fieldConstraint = new FieldConstraint();
                fieldConstraint.setFieldName(questiontype);
                LiteralRestriction literalRestriction = new LiteralRestriction();
                literalRestriction.setEvaluator(operators.get(j));
                List<String> myValues = new ArrayList<String>();
                myValues.add(values.get(j));
                literalRestriction.setValue(myValues);
                fieldConstraint.getLiteralRestriction().add(literalRestriction);
                column.getFieldConstraint().add(fieldConstraint);
                condition.getColumn().add(column);
                j++;
            }
            rule.setCondition(condition);
            rule.setAction(notifications);
            ruleSet.getRule().add(rule);
            ProCtcAERulesService.createRule(rule);
        }
    }

    private List<String> getListForRule(int ruleIndex, HttpServletRequest request, String listType) {
        List<String> list = new ArrayList<String>();
        int index = 0;
        while (true) {
            String parameterName = listType + "_" + ruleIndex + "_" + index;
            String parameterValue = request.getParameter(parameterName);
            if (!StringUtils.isBlank(parameterValue)) {
                list.add(parameterValue);
                index++;
            } else {
                return list;
            }
        }
    }

    public void incrementRuleSetSize(){
        ruleSetSize++;
    }

    public int getRuleSetSize() {
        return ruleSetSize;
    }
}
