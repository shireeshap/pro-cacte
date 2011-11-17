package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.rules.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//
/**
 * The Class CreateFormCommand.
 *
 * @author Vinay Kumar
 * @author Mehul
 * @since Oct 17, 2008
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

    private String readonlyview = "true";
    private FormArmSchedule selectedFormArmSchedule;
    private FormArmSchedule newSelectedFormArmSchedule;
    private boolean allArms;
    private List<CRFCycleDefinition> invalidCycleDefinitions = new ArrayList<CRFCycleDefinition>();

    private List<FormArmSchedule> copySelectedArmScheduleIds = new ArrayList<FormArmSchedule>();

    public List<FormArmSchedule> getCopySelectedArmScheduleIds() {
        return copySelectedArmScheduleIds;
    }

    public void setCopySelectedArmScheduleIds(List<FormArmSchedule> copySelectedArmScheduleIds) {
        this.copySelectedArmScheduleIds = copySelectedArmScheduleIds;
    }

    public boolean isAllArms() {
        return allArms;
    }

    public void setAllArms(boolean allArms) {
        this.allArms = allArms;
    }

    public boolean getAllArms() {
        return allArms;
    }


    public FormArmSchedule getSelectedFormArmSchedule() {
        return selectedFormArmSchedule;
    }

    public void setSelectedFormArmSchedule(FormArmSchedule selectedFormArmSchedule) {
        this.selectedFormArmSchedule = selectedFormArmSchedule;
    }

    public FormArmSchedule getNewSelectedFormArmSchedule() {
        return newSelectedFormArmSchedule;
    }

    public void setNewSelectedFormArmSchedule(FormArmSchedule newSelectedFormArmSchedule) {
        this.newSelectedFormArmSchedule = newSelectedFormArmSchedule;
    }

    public String getReadonlyview() {
        return readonlyview;
    }

    public void setReadonlyview(String readonlyview) {
        this.readonlyview = readonlyview;
    }

    private StudyOrganization myOrg;

    public StudyOrganization getMyOrg() {
        return myOrg;
    }

    public void setMyOrg(StudyOrganization myOrg) {
        this.myOrg = myOrg;
    }

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
        return crf.addProCtcTerm(proCtcTerm);
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
        return new ArrayList<Integer>();
    }

    public String getCrfCycleDefinitionIndexToRemove() {
        return crfCycleDefinitionIndexToRemove;
    }

    public void setCrfCycleDefinitionIndexToRemove(String crfCycleDefinitionIndexToRemove) {
        this.crfCycleDefinitionIndexToRemove = crfCycleDefinitionIndexToRemove;
    }

    public void createCycles(HttpServletRequest request) {
        int cycleDefinitionIndex = -1;
        for (CRFCycleDefinition crfCycleDefinition : getSelectedFormArmSchedule().getCrfCycleDefinitions()) {
            if (crfCycleDefinition.getCycleLength() == null || StringUtils.isBlank(crfCycleDefinition.getRepeatTimes())) {
                invalidCycleDefinitions.add(crfCycleDefinition);
                continue;
            }
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
                    if (StringUtils.isBlank(crfCycle.getCycleDays())) {
                        crfCycle.setCycleDays(",");
                    }
                    crfCycle.setOrder(i);
                    crfCycleDefinition.addCrfCycle(crfCycle);
                }
            }
        }
    }


    public List<NotificationRule> getFormRules(CRFRepository crfRepository) {
        if (crf.getCrfNotificationRules().size() == 0) {
            addRuleToCrf();
        }
        crf = crfRepository.save(crf);
        return crf.getNotificationRules();
    }

    public List<NotificationRule> getSiteRules(GenericRepository genericRepository) {

        if (myOrg.getSiteCRFNotificationRules().size() == 0) {
            int displayOrder = 1;
            for (CRFNotificationRule crfNotificationRule : crf.getCrfNotificationRules()) {
                SiteCRFNotificationRule siteCRFNotificationRule = new SiteCRFNotificationRule();
                siteCRFNotificationRule.setCrf(crf);
                siteCRFNotificationRule.setNotificationRule(crfNotificationRule.getNotificationRule().getCopy());
                siteCRFNotificationRule.setDisplayOrder(displayOrder);
                myOrg.addSiteCRFNotificationRules(siteCRFNotificationRule);
                displayOrder++;
            }
        }
        myOrg = genericRepository.save(myOrg);
        return myOrg.getNotificationRules();
    }

    public CRFNotificationRule addRuleToCrf() {
        NotificationRule notificationRule = createNotificationRule();
        CRFNotificationRule crfNotificationRule = new CRFNotificationRule();
        crfNotificationRule.setDisplayOrder(crf.getCrfNotificationRules().size());
        crfNotificationRule.setNotificationRule(notificationRule);
        crf.addCrfNotificationRule(crfNotificationRule);
        return crfNotificationRule;
    }

    private NotificationRule createNotificationRule() {
        NotificationRule notificationRule = new NotificationRule();
        notificationRule.setTitle("Rule");
        Set<ProCtcTerm> allProCtcTermsInCrf = crf.getAllProCtcTermsInCrf();
        ArrayList<ProCtcTerm> symptoms = new ArrayList(allProCtcTermsInCrf);
        for (ProCtcTerm proCtcTerm : symptoms) {
            NotificationRuleSymptom notificationRuleSymptom = new NotificationRuleSymptom(proCtcTerm);
            notificationRule.addNotificationRuleSymptom(notificationRuleSymptom);
        }
        List<ProCtcQuestionType> questiontypes = new ArrayList<ProCtcQuestionType>(crf.getAllQuestionTypes());
        for (ProCtcQuestionType questionType : questiontypes) {
            NotificationRuleCondition notificationRuleCondition = new NotificationRuleCondition();
            notificationRuleCondition.setProCtcQuestionType(questionType);
            notificationRuleCondition.setNotificationRuleOperator(NotificationRuleOperator.GREATER_EQUAL);
            notificationRuleCondition.setThreshold(3);
            notificationRule.addNotificationRuleCondition(notificationRuleCondition);
        }

        notificationRule.addNotificationRuleRole(new NotificationRuleRole(Role.NURSE));
        notificationRule.addNotificationRuleRole(new NotificationRuleRole(Role.SITE_CRA));
        notificationRule.addNotificationRuleRole(new NotificationRuleRole(Role.TREATING_PHYSICIAN));
        notificationRule.addNotificationRuleRole(new NotificationRuleRole(Role.LEAD_CRA));
        return notificationRule;
    }


    public void processRulesForSite(HttpServletRequest request, GenericRepository genericRepository) throws Exception {
        List<SiteCRFNotificationRule> siteCRFNotificationRules = new ArrayList<SiteCRFNotificationRule>();
        for (SiteCRFNotificationRule siteCRFNotificationRule : myOrg.getSiteCRFNotificationRules()) {
            siteCRFNotificationRules.add(siteCRFNotificationRule);
        }
        for (SiteCRFNotificationRule siteCRFNotificationRule : siteCRFNotificationRules) {
            myOrg.getSiteCRFNotificationRules().remove(siteCRFNotificationRule);
            genericRepository.delete(siteCRFNotificationRule);
        }
        myOrg = genericRepository.save(myOrg);
        String[] ruleIds = request.getParameterValues("ruleIds");
        String rulesToDelete = "," + request.getParameter("rulesToDelete");
        int displayOrder = 1;
        if (ruleIds != null) {
            for (String ruleId : ruleIds) {
                if (rulesToDelete.indexOf("," + ruleId + ",") == -1) {
                    SiteCRFNotificationRule siteCRFNotificationRule = createSiteCrfNotificationRule(request, ruleId, displayOrder, genericRepository);
                    myOrg.addSiteCRFNotificationRules(siteCRFNotificationRule);
                    displayOrder++;
                }
            }
        }


    }

    public List<CRFCycleDefinition> getInvalidCycleDefinitions() {
        return invalidCycleDefinitions;
    }

    public String getUniqueTitleForCrf() {
        return "Untitled_" + UUID.randomUUID();
    }


    public StudyOrganization getOrganizationForUser(User loggedInUser, List<Role> roles) {
        StudyOrganization myOrg = null;

        List<StudyOrganizationClinicalStaff> roleUsers = new ArrayList<StudyOrganizationClinicalStaff>();
        for (Role role : roles) {
            roleUsers.addAll(getCrf().getStudy().getStudySiteLevelStudyOrganizationClinicalStaffsByRole(role));
        }
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : roleUsers) {
            User user = studyOrganizationClinicalStaff.getOrganizationClinicalStaff().getClinicalStaff().getUser();
            if (user != null) {
                if (user.equals(loggedInUser)) {
                    myOrg = studyOrganizationClinicalStaff.getStudyOrganization();
                }
            }
        }
        return myOrg;
    }

    public void processRulesForForm(HttpServletRequest request, GenericRepository genericRepository) {

        List<CRFNotificationRule> crfNotificationRules = new ArrayList<CRFNotificationRule>();
        for (CRFNotificationRule crfNotificationRule : crf.getCrfNotificationRules()) {
            crfNotificationRules.add(crfNotificationRule);
        }
        for (CRFNotificationRule crfNotificationRule : crfNotificationRules) {
            crf.getCrfNotificationRules().remove(crfNotificationRule);
            genericRepository.delete(crfNotificationRule);
        }
        crf = genericRepository.save(crf);
        String[] ruleIds = request.getParameterValues("ruleIds");
        String rulesToDelete = "," + request.getParameter("rulesToDelete");
        int displayOrder = 1;
        if (ruleIds != null) {
            for (String ruleId : ruleIds) {
                if (rulesToDelete.indexOf("," + ruleId + ",") == -1) {
                    CRFNotificationRule crfNotificationRule = createCrfNotificationRule(request, ruleId, displayOrder, genericRepository);
                    crf.addCrfNotificationRule(crfNotificationRule);
                    displayOrder++;
                }
            }
        }
    }

    private CRFNotificationRule createCrfNotificationRule(HttpServletRequest request, String ruleId, int displayOrder, GenericRepository genericRepository) {
        CRFNotificationRule crfNotificationRule = new CRFNotificationRule();
        crfNotificationRule.setDisplayOrder(displayOrder);

        NotificationRule notificationRule = createNotificationRule(request, ruleId, genericRepository);
        crfNotificationRule.setNotificationRule(notificationRule);
        return crfNotificationRule;
    }

    private SiteCRFNotificationRule createSiteCrfNotificationRule(HttpServletRequest request, String ruleId, int displayOrder, GenericRepository genericRepository) {
        SiteCRFNotificationRule siteCRFNotificationRule = new SiteCRFNotificationRule();
        siteCRFNotificationRule.setDisplayOrder(displayOrder);
        siteCRFNotificationRule.setCrf(crf);

        NotificationRule notificationRule = createNotificationRule(request, ruleId, genericRepository);
        siteCRFNotificationRule.setNotificationRule(notificationRule);
        return siteCRFNotificationRule;
    }

    private NotificationRule createNotificationRule(HttpServletRequest request, String ruleId, GenericRepository genericRepository) {
        NotificationRule notificationRule = new NotificationRule();
        notificationRule.setTitle("Rule");

        String[] notifications = request.getParameterValues("notifications_" + ruleId);
        if (notifications != null) {
            for (String notification : notifications) {
                notificationRule.addNotificationRuleRole(new NotificationRuleRole(Role.getByCode(notification)));
            }
        }
        String[] symptoms = request.getParameterValues("symptoms_" + ruleId);
        if (symptoms != null) {
            for (String proCtcTermId : symptoms) {
                notificationRule.addNotificationRuleSymptom(new NotificationRuleSymptom(genericRepository.findById(ProCtcTerm.class, Integer.parseInt(proCtcTermId))));
            }
        }

        String[] conditions = request.getParameterValues("conditions_" + ruleId);
        String conditionsToDelete = "," + request.getParameter("delete_conditions_" + ruleId);
        if (conditions != null) {
            for (String conditionId : conditions) {
                if (conditionsToDelete.indexOf(conditionId) == -1) {
                    String questionType = request.getParameter("questiontype_" + ruleId + "_" + conditionId);
                    String operator = request.getParameter("operator_" + ruleId + "_" + conditionId);
                    String threshold = request.getParameter("threshold_" + ruleId + "_" + conditionId);
                    NotificationRuleCondition notificationRuleCondition = new NotificationRuleCondition();
                    notificationRuleCondition.setProCtcQuestionType(ProCtcQuestionType.getByCode(questionType));
                    notificationRuleCondition.setNotificationRuleOperator(NotificationRuleOperator.getByCode(operator));
                    notificationRuleCondition.setThreshold(Integer.parseInt(threshold));
                    notificationRule.addNotificationRuleCondition(notificationRuleCondition);
                }
            }
        }

        String override = request.getParameter("override_" + ruleId);
        notificationRule.setSiteOverRide(new Boolean(override));
        return notificationRule;
    }

    public SiteCRFNotificationRule addRuleToSite() {
        NotificationRule notificationRule = createNotificationRule();
        SiteCRFNotificationRule siteCRFNotificationRule = new SiteCRFNotificationRule();
        siteCRFNotificationRule.setDisplayOrder(myOrg.getSiteCRFNotificationRules().size());
        siteCRFNotificationRule.setNotificationRule(notificationRule);
        siteCRFNotificationRule.setCrf(crf);
        myOrg.addSiteCRFNotificationRules(siteCRFNotificationRule);
        return siteCRFNotificationRule;
    }

    public NotificationRuleCondition addCondition(NotificationRule notificationRule) {
        NotificationRuleCondition notificationRuleCondition = new NotificationRuleCondition();
        Set<ProCtcQuestionType> proCtcQuestionTypes= crf.getAllQuestionTypes();
        if(proCtcQuestionTypes.size()>0){
            notificationRuleCondition.setProCtcQuestionType((new ArrayList<ProCtcQuestionType>(crf.getAllQuestionTypes())).get(0));
            notificationRuleCondition.setNotificationRuleOperator(NotificationRuleOperator.GREATER_EQUAL);
            notificationRuleCondition.setThreshold(3);
            notificationRule.addNotificationRuleCondition(notificationRuleCondition);
            return notificationRuleCondition;
        }
        return notificationRuleCondition;
    }
}
