package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateFormCommand implements Serializable {

	private static Log logger = LogFactory.getLog(CreateFormCommand.class);

	private StudyCrf studyCrf;


	private String questionsIds;
	private String numberOfQuestionsInEachPage;


	public String getTitle() {
		String title = getStudyCrf().getCrf().getTitle();
		return !org.apache.commons.lang.StringUtils.isBlank(title) ? title : "Click here to name";
	}


	public CreateFormCommand() {
		CRF crf = new CRF();
		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");

		CRFPage crfPage = new CRFPage();

		crf.addCrfPge(crfPage);
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
		String[] numberOfQuestionInEachPageArray = StringUtils.commaDelimitedListToStringArray(numberOfQuestionsInEachPage);

		logger.debug("number of questions each page:" + numberOfQuestionsInEachPage);
		int k = 0;
		for (int j = 0; j < numberOfQuestionInEachPageArray.length; j++) {
			String questionsInEachPage = numberOfQuestionInEachPageArray[j];

			for (int i = k; i < k+Integer.valueOf(questionsInEachPage); i++) {
				Integer questionId = Integer.parseInt(questionIdsArrays[i]);
				ProCtcQuestion proCtcQuestion = finderRepository.findById(ProCtcQuestion.class, questionId);
				if (proCtcQuestion != null) {
					int displayOrder = i + 1;
					studyCrf.getCrf().addOrUpdateCrfItemInCrfPage(j, proCtcQuestion, displayOrder);
				} else {
					logger.error("can not add question because pro ctc question is null for id:" + questionId);
				}

			}
			k = k + Integer.valueOf(questionsInEachPage);

		}

	}

	private void deleteQuestions() {
		studyCrf.getCrf().removeCrfPageItem(questionsIds);
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

	public String getNumberOfQuestionsInEachPage() {
		return numberOfQuestionsInEachPage;
	}

	public void setNumberOfQuestionsInEachPage(final String numberOfQuestionsInEachPage) {
		this.numberOfQuestionsInEachPage = numberOfQuestionsInEachPage;
	}

	public CRFPage addAnotherPage() {
		CRFPage crfPage = new CRFPage();
		studyCrf.getCrf().addCrfPge(crfPage);
		return crfPage;

	}
}
