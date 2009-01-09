package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;


/**
 * @author Harsh Agarwal
 * @crated Nov 12, 2008
 */
public class SubmitFormCommand implements Serializable {

	private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
	private Hashtable<Integer, String> displayRules = new Hashtable<Integer, String>();
	private FinderRepository finderRepository;
	private GenericRepository genericRepository;
	private int currentPageIndex;
	private int totalPages;
	private String direction = "";
	private String flashMessage;
	private List<ProCtcQuestion> proCtcQuestions;
	private boolean hasParticipantAddedQuestions = false;
	private String deletedQuestions;

	public void initialize() {

		for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
			CrfPageItem crfPageItem = studyParticipantCrfItem.getCrfPageItem();

			String displayRule = "";
			for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItem.getCrfPageItemDisplayRules()) {
				Integer validValueId = crfPageItemDisplayRule.getProCtcValidValue().getId();
				displayRule = displayRule + "~" + validValueId;
			}
			displayRules.put(crfPageItem.getId(), displayRule);
		}
		currentPageIndex = 1;
		totalPages = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyCrf().getCrf().getCrfPages().size();
		if (studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().size() > 0) {
			hasParticipantAddedQuestions = true;
			totalPages = totalPages + 1;
		}
	}

	private StudyParticipantCrfSchedule findLatestCrfAndCreateSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
		CRF originalCrf = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyCrf().getCrf();
		if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED)) {
			Date today = new Date();
			CRF latestEffectiveCrf = originalCrf;
			Integer nextVersionId = latestEffectiveCrf.getNextVersionId();
			while (nextVersionId != null) {
				CRF nextCrf = finderRepository.findById(CRF.class, nextVersionId);
				if (nextCrf.getStatus().equals(CrfStatus.RELEASED) && today.after(nextCrf.getEffectiveStartDate())) {
					latestEffectiveCrf = nextCrf;
				}
				nextVersionId = nextCrf.getNextVersionId();
			}

			if (!originalCrf.getId().equals(latestEffectiveCrf.getId())) {
				StudyParticipantAssignment studyParticipantAssignment = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment();
				for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
					if (studyParticipantCrf.getStudyCrf().getCrf().getId().equals(latestEffectiveCrf.getId())) {
						StudyParticipantCrfSchedule newSchedule = new StudyParticipantCrfSchedule();
						newSchedule.setStartDate(studyParticipantCrfSchedule.getStartDate());
						newSchedule.setDueDate(studyParticipantCrfSchedule.getDueDate());
						newSchedule.setStatus(CrfStatus.SCHEDULED);
						studyParticipantCrf.addStudyParticipantCrfSchedule(newSchedule, latestEffectiveCrf);

						genericRepository.save(newSchedule);
						genericRepository.delete(studyParticipantCrfSchedule);
						return newSchedule;
					}
				}
			}
		}
		if (studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().size() > 0) {
			if (studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions().size() == 0) {
				for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
					studyParticipantCrfSchedule.addStudyParticipantCrfScheduleAddedQuestion(new StudyParticipantCrfScheduleAddedQuestion());
				}
			}
		}
		return studyParticipantCrfSchedule;
	}

	public Hashtable<Integer, String> getDisplayRules() {
		return displayRules;
	}

	public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
		return studyParticipantCrfSchedule;
	}

	public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
		this.studyParticipantCrfSchedule = findLatestCrfAndCreateSchedule(studyParticipantCrfSchedule);
	}

	public int getCurrentPageIndex() {
		if (direction.equals("back")) {
			currentPageIndex--;
		}
		if (direction.equals("continue")) {
			currentPageIndex++;
		}
		direction = "";
		return currentPageIndex;
	}

	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void setFinderRepository(FinderRepository finderRepository) {
		this.finderRepository = finderRepository;
	}

	public void setGenericRepository(GenericRepository genericRepository) {
		this.genericRepository = genericRepository;
	}

	public String getFlashMessage() {
		return flashMessage;
	}

	public void setFlashMessage(String flashMessage) {
		this.flashMessage = flashMessage;
	}

	public Hashtable<String, List<ProCtcQuestion>> getArrangedQuestions() {
		Hashtable<String, List<ProCtcQuestion>> arrangedQuestions = new Hashtable<String, List<ProCtcQuestion>>();
		List<ProCtcQuestion> l;
		ArrayList<Integer> includedQuestionIds = new ArrayList<Integer>();

		for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
			includedQuestionIds.add(studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getId());
		}
		if (hasParticipantAddedQuestions) {
			studyParticipantCrfSchedule = finderRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId());
			for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
				includedQuestionIds.add(studyParticipantCrfAddedQuestion.getProCtcQuestion().getId());
			}
		}

		for (ProCtcQuestion proCtcQuestion : proCtcQuestions) {

			if (!includedQuestionIds.contains(proCtcQuestion.getId())) {
				if (arrangedQuestions.containsKey(proCtcQuestion.getProCtcTerm().getTerm())) {
					l = arrangedQuestions.get(proCtcQuestion.getProCtcTerm().getTerm());
				} else {
					l = new ArrayList<ProCtcQuestion>();
				}

				l.add(proCtcQuestion);
				arrangedQuestions.put(proCtcQuestion.getProCtcTerm().getTerm(), l);
			}
		}

		return arrangedQuestions;
	}

	public void setProCtcQuestions(List<ProCtcQuestion> proCtcQuestions) {
		this.proCtcQuestions = proCtcQuestions;
	}

	public boolean isHasParticipantAddedQuestions() {
		return hasParticipantAddedQuestions;
	}

	public void setHasParticipantAddedQuestions(boolean hasParticipantAddedQuestions) {
		this.hasParticipantAddedQuestions = hasParticipantAddedQuestions;
	}

	public String getDeletedQuestions() {
		return deletedQuestions;
	}

	public void setDeletedQuestions(String deletedQuestions) {
		this.deletedQuestions = deletedQuestions;
	}

	public void deleteQuestions(String questions) {

		if (!StringUtils.isBlank(questions)) {
			StringTokenizer st = new StringTokenizer(questions, ",");
			while (st.hasMoreTokens()) {
				StudyParticipantCrfAddedQuestion s = finderRepository.findById(StudyParticipantCrfAddedQuestion.class, new Integer(st.nextToken()));
				genericRepository.delete(s);
			}
		}

	}
}