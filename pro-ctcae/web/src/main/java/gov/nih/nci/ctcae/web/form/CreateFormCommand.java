package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateFormCommand implements Serializable {

	private static Log logger = LogFactory.getLog(CreateFormCommand.class);

	private StudyCrf studyCrf;


	private String questionsIds;
	private String numberOfQuestionsInEachPage;
	private String crfPageNumbers;
	private String crfPageNumbersToRemove = "";


	public String getTitle() {
		String title = getStudyCrf().getCrf().getTitle();
		return !org.apache.commons.lang.StringUtils.isBlank(title) ? title : "Click here to name";
	}


	public CreateFormCommand() {
		CRF crf = new CRF();
		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");

		this.studyCrf = new StudyCrf();
		studyCrf.setCrf(crf);
		crf.setStudyCrf(studyCrf);


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

			List<Integer> questionsToKeep = new ArrayList<Integer>();
			int displayOrder = CrfPageItem.INITIAL_ORDER;

			for (int i = k; i < k + Integer.valueOf(questionsInEachPage); i++) {
				Integer questionId = Integer.parseInt(questionIdsArrays[i]);
				ProCtcQuestion proCtcQuestion = finderRepository.findById(ProCtcQuestion.class, questionId);
				if (proCtcQuestion != null) {
					studyCrf.getCrf().addOrUpdateCrfItemInCrfPage(Integer.valueOf(crfPageNumberArray[j]), proCtcQuestion, displayOrder);
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
			CRFPage crfPage = studyCrf.getCrf().getCrfPages().get(index);
			//now delete the extra questions
			crfPage.removeExtraCrfItemsInCrfPage(questionsToKeepMap.get(index));

		}

		//now remove the pages;
		String[] crfPageNumberArrayToRemove = StringUtils.commaDelimitedListToStringArray(crfPageNumbersToRemove);
		for (String crfPageNumberToRemove : crfPageNumberArrayToRemove) {
			getStudyCrf().getCrf().removeCrfPageByPageNumber(Integer.valueOf(crfPageNumberToRemove));
		}

		//finally update the crf page numbers;
		getStudyCrf().getCrf().updatePageNumberOfCrfPageItems();

		//reorder crf page items

		for (CRFPage crfPage : studyCrf.getCrf().getCrfPages()) {
			crfPage.updateDisplayOrderOfCrfPageItems();
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
}
