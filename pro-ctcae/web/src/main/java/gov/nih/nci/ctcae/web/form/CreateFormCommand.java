package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateFormCommand implements Serializable {

    private static Log logger = LogFactory.getLog(CreateFormCommand.class);

    private CRF crf;


    private String questionsIds;
    private String numberOfQuestionsInEachPage;
    private String crfPageNumbers;
    private String crfPageNumbersToRemove = "";
    private String questionIdsToRemove = "";


    public String getTitle() {
        String title = getCrf().getTitle();
        return !org.apache.commons.lang.StringUtils.isBlank(title) ? title : "Click here to name";
    }


    public CreateFormCommand() {
        crf = new CRF();
        crf.setStatus(CrfStatus.DRAFT);
        crf.setCrfVersion("1.0");

    }

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
        }

        crf.updatePageNumberOfCrfPages();
        crf.updateCrfPageInstructions();
        crf.updateDisplayOrderOfCrfPageItems();


    }

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


    public CRF getCrf() {
        return crf;
    }

    public void setCrf(CRF crf) {
        this.crf = crf;
    }


    public String getQuestionsIds() {
        return questionsIds;
    }


    public void setQuestionsIds(String questionsIds) {
        this.questionsIds = questionsIds;
    }

    public String getNumberOfQuestionsInEachPage() {
        return numberOfQuestionsInEachPage;
    }

    public void setNumberOfQuestionsInEachPage(final String numberOfQuestionsInEachPage) {
        this.numberOfQuestionsInEachPage = numberOfQuestionsInEachPage;
    }

    public CRFPage addCrfPage() {
        return crf.addCrfPage();
    }

    public String getCrfPageNumbers() {
        return crfPageNumbers;
    }

    public void setCrfPageNumbers(final String crfPageNumber) {
        this.crfPageNumbers = crfPageNumber;
    }

    public String getCrfPageNumbersToRemove() {
        return crfPageNumbersToRemove;
    }

    public void setCrfPageNumbersToRemove(final String crfPageNumbersToRemove) {
        this.crfPageNumbersToRemove = crfPageNumbersToRemove;
    }


    public CRFPage addCrfPage(ProCtcQuestion proCtcQuestion) {
        if (getCrf().getAdvance()) {
            CRFPage crfPage = crf.addCrfPage(proCtcQuestion);
            return crfPage;
        }
        throw new CtcAeSystemException("You can not add individual questions in basic form creation mode.");


    }

    public Object addProCtcTerm(ProCtcTerm proCtcTerm) {

        Object object = crf.addProCtcTerm(proCtcTerm);
        return object;


    }

    public void clearEmptyPages() {
        crf.clearEmptyPages();

    }

    public String getQuestionIdsToRemove() {
        return questionIdsToRemove;
    }

    public void setQuestionIdsToRemove(String questionIdsToRemove) {
        this.questionIdsToRemove = questionIdsToRemove;
    }
}
