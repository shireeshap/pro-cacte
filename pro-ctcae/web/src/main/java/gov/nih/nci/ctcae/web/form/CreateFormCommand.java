package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

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
     * The number of questions in each page.
     */
    private String numberOfQuestionsInEachPage;

    /**
     * The crf page numbers.
     */
    private String crfPageNumbers;

    /**
     * The crf page numbers to remove.
     */
    private String crfPageNumbersToRemove = "";

    /**
     * The question ids to remove.
     */
    private String questionIdsToRemove = "";


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

        if (getCrf().getAdvance()) {
            addOrUpdateQuestions(proCtcQuestionRepository);
        }


        removeQuestions(proCtcQuestionRepository);

        if (!org.apache.commons.lang.StringUtils.isBlank(crfPageNumbersToRemove)) {
            crf.removeCrfPageByPageNumber(Integer.valueOf(crfPageNumbersToRemove));
        }

        setQuestionIdsToRemove("");
        setCrfPageNumbersToRemove("");

        String[] crfPageNumberArray = StringUtils.commaDelimitedListToStringArray(crfPageNumbers);

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
        crf.updateDisplayOrderOfCrfPageItems();


    }

    /**
     * Removes the questions.
     *
     * @param proCtcQuestionRepository the pro ctc question repository
     */
    private void removeQuestions(ProCtcQuestionRepository proCtcQuestionRepository) {
        String[] questionIdsToRemoveArrays = StringUtils.commaDelimitedListToStringArray(questionIdsToRemove);
        Set<Integer> questionIds = new HashSet<Integer>();
        for (String questionId : questionIdsToRemoveArrays) {
            if (!org.apache.commons.lang.StringUtils.isBlank(questionId)) {
                questionIds.add(Integer.valueOf(questionId));
            }
        }
        crf.removeCrfPageItemByQuestionIds(questionIds);
        for (Integer questionId : questionIds) {
            ProCtcQuestion proCtcQuestion = proCtcQuestionRepository.findById(questionId);
            crf.updateCrfPageItemDisplayRules(proCtcQuestion);
        }
    }

    /**
     * Adds the or update questions.
     *
     * @param proCtcQuestionRepository the pro ctc question repository
     */
    private void addOrUpdateQuestions(final ProCtcQuestionRepository proCtcQuestionRepository) {


        String[] questionIdsArrays = StringUtils.commaDelimitedListToStringArray(questionsIds);
        String[] numberOfQuestionInEachPageArray = StringUtils.commaDelimitedListToStringArray(numberOfQuestionsInEachPage);
        String[] crfPageNumberArray = StringUtils.commaDelimitedListToStringArray(crfPageNumbers);

        logger.debug("number of questions each page:" + numberOfQuestionsInEachPage);
        int k = 0;

        Map<Integer, List<Integer>> questionsToKeepMap = new HashMap<Integer, List<Integer>>();

        for (int j = 0; j < numberOfQuestionInEachPageArray.length; j++) {
            String questionsInEachPage = numberOfQuestionInEachPageArray[j];

            List<Integer> questionsToKeep = new LinkedList<Integer>();
            int displayOrder = CrfPageItem.INITIAL_ORDER;

            for (int i = k; i < k + Integer.valueOf(questionsInEachPage); i++) {
                Integer questionId = Integer.parseInt(questionIdsArrays[i]);
                ProCtcQuestion proCtcQuestion = proCtcQuestionRepository.findById(questionId);
                if (proCtcQuestion != null) {
                    crf.addOrUpdateCrfItemInCrfPage(Integer.valueOf(crfPageNumberArray[j]), proCtcQuestion, displayOrder);
                    questionsToKeep.add(questionId);
                    displayOrder++;
                } else {
                    logger.error("can not add question because pro ctc question is null for id:" + questionId);
                }

            }
            questionsToKeepMap.put(Integer.valueOf(crfPageNumberArray[j]), questionsToKeep);
            k = k + Integer.valueOf(questionsInEachPage);

        }

        for (Integer index : questionsToKeepMap.keySet()) {
            CRFPage crfPage = crf.getCrfPagesSortedByPageNumber().get(index);
            //now delete the extra questions
            crfPage.removeExtraCrfItemsInCrfPage(questionsToKeepMap.get(index));

        }

        //now remove the pages;
        String[] crfPageNumberArrayToRemove = StringUtils.commaDelimitedListToStringArray(crfPageNumbersToRemove);
        for (String crfPageNumberToRemove : crfPageNumberArrayToRemove) {
            getCrf().removeCrfPageByPageNumber(Integer.valueOf(crfPageNumberToRemove));
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
     * Gets the number of questions in each page.
     *
     * @return the number of questions in each page
     */
    public String getNumberOfQuestionsInEachPage() {
        return numberOfQuestionsInEachPage;
    }

    /**
     * Sets the number of questions in each page.
     *
     * @param numberOfQuestionsInEachPage the new number of questions in each page
     */
    public void setNumberOfQuestionsInEachPage(final String numberOfQuestionsInEachPage) {
        this.numberOfQuestionsInEachPage = numberOfQuestionsInEachPage;
    }

    /**
     * Adds the crf page.
     *
     * @return the cRF page
     */
    public CRFPage addCrfPage() {
        return crf.addCrfPage();
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
    public String getCrfPageNumbersToRemove() {
        return crfPageNumbersToRemove;
    }

    /**
     * Sets the crf page numbers to remove.
     *
     * @param crfPageNumbersToRemove the new crf page numbers to remove
     */
    public void setCrfPageNumbersToRemove(final String crfPageNumbersToRemove) {
        this.crfPageNumbersToRemove = crfPageNumbersToRemove;
    }


    /**
     * Adds the crf page.
     *
     * @param proCtcQuestion the pro ctc question
     * @return the cRF page
     */
    public CRFPage addCrfPage(ProCtcQuestion proCtcQuestion) {
        if (getCrf().getAdvance()) {
            CRFPage crfPage = crf.addCrfPage(proCtcQuestion);
            return crfPage;
        }
        throw new CtcAeSystemException("You can not add individual questions in basic form creation mode.");


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
    public String getQuestionIdsToRemove() {
        return questionIdsToRemove;
    }

    /**
     * Sets the question ids to remove.
     *
     * @param questionIdsToRemove the new question ids to remove
     */
    public void setQuestionIdsToRemove(String questionIdsToRemove) {
        this.questionIdsToRemove = questionIdsToRemove;
    }
}
