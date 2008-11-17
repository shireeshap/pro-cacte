package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateFormCommand implements Serializable {

    private static Log logger = LogFactory.getLog(CreateFormCommand.class);

    private StudyCrf studyCrf;


    public String getTitle() {
        String title = getStudyCrf().getCrf().getTitle();
        return !org.apache.commons.lang.StringUtils.isBlank(title) ? title : "Click here to name";
    }


    private String questionsIds;


    public CreateFormCommand() {
        CRF crf = new CRF();
        crf.setStatus(CrfStatus.DRAFT);
        crf.setCrfVersion("1.1");
        this.studyCrf = new StudyCrf();
        studyCrf.setCrf(crf);
        crf.setStudyCrf(studyCrf);
    }

    public void updateCrfItems(FinderRepository finderRepository) {


        addOrUpdateQuestions(finderRepository);
        //now delete the questions
        deleteQuestions();


    }

    private void addOrUpdateQuestions(final FinderRepository finderRepository) {
        String[] questionIdsArrays = StringUtils.commaDelimitedListToStringArray(questionsIds);

        logger.debug("found questions to add/update:" + questionsIds);
        for (int i = 0; i < questionIdsArrays.length; i++) {
            Integer questionId = Integer.parseInt(questionIdsArrays[i]);
            ProCtcQuestion proCtcQuestion = finderRepository.findById(ProCtcQuestion.class, questionId);
            if (proCtcQuestion != null) {
                int displayOrder = i + 1;
                studyCrf.getCrf().addOrUpdateCrfItem(proCtcQuestion, displayOrder);
            } else {
                logger.error("can not add question because pro ctc term is null for id:" + questionId);
            }
        }
    }

    private void deleteQuestions() {
        List<CrfItem> crfItemsToRemove = new ArrayList<CrfItem>();
        Set questionIdSet = StringUtils.commaDelimitedListToSet(questionsIds);
        for (CrfItem crfItem : studyCrf.getCrf().getCrfItems()) {
            if (!questionIdSet.contains(String.valueOf(crfItem.getProCtcQuestion().getId()))) {
                crfItemsToRemove.add(crfItem);
            }
        }


        for (CrfItem crfItem : crfItemsToRemove) {
            studyCrf.getCrf().removeCrfItem(crfItem);
        }
    }

    public StudyCrf getStudyCrf() {
        return studyCrf;
    }

    public void setStudyCrf(StudyCrf studyCrf) {
        this.studyCrf = studyCrf;
    }


    public String getQuestionsIds() {
        return questionsIds;
    }


    public void setQuestionsIds(String questionsIds) {
        this.questionsIds = questionsIds;
    }


}
