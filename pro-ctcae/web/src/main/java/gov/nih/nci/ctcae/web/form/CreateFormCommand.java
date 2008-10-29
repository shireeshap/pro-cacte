package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;

import java.io.Serializable;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateFormCommand implements Serializable {

    private static Log logger = LogFactory.getLog(CreateFormCommand.class);

    private StudyCrf studyCrf;

    private String title;

    private String questionsIds;

    public CreateFormCommand() {
        this.studyCrf = new StudyCrf();
        studyCrf.setCrf(new CRF());
    }

    public void updateCrfItems(FinderRepository finderRepository) {
        List<String> quetionsIdSet = new ArrayList<String>(StringUtils.commaDelimitedListToSet(questionsIds));
        studyCrf.getCrf().getCrfItems().clear();
        logger.debug("found questions to add:" + questionsIds);
        for (int i = 0; i < quetionsIdSet.size(); i++) {
            Integer questionId = Integer.parseInt(quetionsIdSet.get(i));
            ProCtcTerm proCtcTerm = finderRepository.findById(ProCtcTerm.class, questionId);
            if (proCtcTerm != null) {
                CrfItem crfItem = new CrfItem();
                crfItem.setProCtcTerm(proCtcTerm);
                crfItem.setDisplayOrder(i + 1);
                studyCrf.getCrf().addCrfItem(crfItem);
            } else {
                logger.error("can not add question because pro ctc term is null for id:" + questionId);
            }
        }


    }

    public StudyCrf getStudyCrf() {
        return studyCrf;
    }

    public void setStudyCrf(StudyCrf studyCrf) {
        this.studyCrf = studyCrf;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestionsIds() {
        return questionsIds;
    }

    public void setQuestionsIds(String questionsIds) {
        this.questionsIds = questionsIds;
    }


}
