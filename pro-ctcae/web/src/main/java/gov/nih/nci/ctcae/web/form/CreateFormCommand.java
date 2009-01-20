package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateFormCommand implements Serializable {

    private static Log logger = LogFactory.getLog(CreateFormCommand.class);

    private CRF crf;

    private Boolean advance = Boolean.FALSE;

    private String questionsIds;
    private String numberOfQuestionsInEachPage;
    private String crfPageNumbers;
    private String crfPageNumbersToRemove = "";


    public String getTitle() {
        String title = getCrf().getTitle();
        return !org.apache.commons.lang.StringUtils.isBlank(title) ? title : "Click here to name";
    }


    public CreateFormCommand() {
        crf = new CRF();
        crf.setStatus(CrfStatus.DRAFT);
        crf.setCrfVersion("1.0");

    }

    public void updateCrfItems(FinderRepository finderRepository) {


        addOrUpdateQuestions(finderRepository);


    }

    private void addOrUpdateQuestions(final FinderRepository finderRepository) {


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
                ProCtcQuestion proCtcQuestion = finderRepository.findById(ProCtcQuestion.class, questionId);
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
            CRFPage crfPage = crf.getCrfPages().get(index);
            //now delete the extra questions
            crfPage.removeExtraCrfItemsInCrfPage(questionsToKeepMap.get(index));

        }

        //now remove the pages;
        String[] crfPageNumberArrayToRemove = StringUtils.commaDelimitedListToStringArray(crfPageNumbersToRemove);
        for (String crfPageNumberToRemove : crfPageNumberArrayToRemove) {
            getCrf().removeCrfPageByPageNumber(Integer.valueOf(crfPageNumberToRemove));
        }

        //finally update the crf page numbers;
        getCrf().updatePageNumberOfCrfPages();

        //reorder crf page items

        for (CRFPage crfPage : crf.getCrfPages()) {
            crfPage.updateDisplayOrderOfCrfPageItems();
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
        if (advance) {
            CRFPage crfPage = crf.addCrfPage();
            return crfPage;
        }
        throw new CtcAeSystemException("You can not add new page in basic form creation mode.");

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

    public Boolean getAdvance() {
        return advance;
    }

    public void setAdvance(Boolean advance) {
        this.advance = advance;
    }

    public CRFPage addCrfPage(ProCtcQuestion proCtcQuestion) {
        if (advance) {
            CRFPage crfPage = crf.addCrfPage(proCtcQuestion);
            return crfPage;
        }
        throw new CtcAeSystemException("You can not add individual questions in basic form creation mode.");


    }

    public CRFPage addCrfPage(ProCtcTerm proCtcTerm) {
        CRFPage crfPage = crf.addCrfPage(proCtcTerm);
        return crfPage;

    }
}
