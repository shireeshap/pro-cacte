package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

/**
 * @author Harsh Agarwal
 * @crated Nov 12, 2008
 */
public class SubmitFormCommand implements Serializable {

    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    private String direction = "";
    private String flashMessage;
    private int totalPages;
    private int currentPageIndex;
    ArrayList<List<StudyParticipantCrfItem>> pages;
    List<ProCtcQuestion> proCtcQuestions = new ArrayList<ProCtcQuestion>();
    ArrayList<Integer> includedQuestionIds;

    public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
        return studyParticipantCrfSchedule;
    }

    public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
        buildPagesForSymptoms();
        totalPages = pages.size();
        currentPageIndex = getPageForUnansweredQuestion();
    }

    private void buildPagesForSymptoms() {
        pages = new ArrayList<List<StudyParticipantCrfItem>>();
        List<StudyParticipantCrfItem> studyParticipantCrfItems = studyParticipantCrfSchedule.getStudyParticipantCrfItems();
        includedQuestionIds = new ArrayList<Integer>();

        Hashtable<ProCtcTerm, Integer> tempHash = new Hashtable<ProCtcTerm, Integer>();
        int i = 0;
        int j = 0;

        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfItems) {

            includedQuestionIds.add(studyParticipantCrfItem.getCrfItem().getProCtcQuestion().getId());

            List<StudyParticipantCrfItem> tmp;
            ProCtcTerm symptom = studyParticipantCrfItem.getCrfItem().getProCtcQuestion().getProCtcTerm();

            if (tempHash.containsKey(symptom)) {
                tmp = pages.get(tempHash.get(symptom).intValue());
            } else {
                tmp = new ArrayList<StudyParticipantCrfItem>();
                pages.add(tmp);
                tempHash.put(symptom, i++);
            }
            studyParticipantCrfItem.setItemIndex(j);
            tmp.add(studyParticipantCrfItem);
            j++;
        }
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getFlashMessage() {
        return flashMessage;
    }

    public void setFlashMessage(String flashMessage) {
        this.flashMessage = flashMessage;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public void saveResponseAndGetQuestion(FinderRepository finderRepository, GenericRepository genericRepository) {

        if ("continue".equals(getDirection())) {
            if (currentPageIndex == 0) {
                studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
            }
            currentPageIndex = currentPageIndex + 1;
        }

        if ("back".equals(getDirection())) {
            currentPageIndex = currentPageIndex - 1;
        }

        if ("save".equals(getDirection())) {
            studyParticipantCrfSchedule.setStatus(CrfStatus.COMPLETED);
        }

        genericRepository.save(studyParticipantCrfSchedule);
        studyParticipantCrfSchedule = finderRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId());
        buildPagesForSymptoms();
    }

    public int getTotalPages() {
        return totalPages;
    }

    public ArrayList<List<StudyParticipantCrfItem>> getPages() {
        return pages;
    }

    private int getPageForUnansweredQuestion() {

        int tmp = 0;
        for (List<StudyParticipantCrfItem> page : pages) {
            boolean severityQuestionAnswerIsNone = false;

            for (StudyParticipantCrfItem studyParticipantCrfItem : page) {
                if (studyParticipantCrfItem.getCrfItem().getProCtcQuestion().getProCtcQuestionType().equals(ProCtcQuestionType.SEVERITY)) {
                    if (studyParticipantCrfItem.getProCtcValidValue() == null) {
                        return tmp;
                    }
                    if (studyParticipantCrfItem.getProCtcValidValue().getValue().equals(0)) {
                        severityQuestionAnswerIsNone = true;
                    }
                } else {
                    if (!severityQuestionAnswerIsNone) {
                        if (studyParticipantCrfItem.getProCtcValidValue() == null) {
                            return tmp;
                        }
                    }
                }
            }
            tmp++;
        }
        return tmp;
    }

    public List<ProCtcQuestion> getProCtcQuestions() {

        return proCtcQuestions;
    }

    public void setProCtcQuestions(List<ProCtcQuestion> proCtcQuestions) {
        this.proCtcQuestions = proCtcQuestions;
    }

    public Hashtable<String, List<ProCtcQuestion>> getArrangedQuestions() {
        Hashtable<String, List<ProCtcQuestion>> arrangedQuestions = new Hashtable<String, List<ProCtcQuestion>>();
        List<ProCtcQuestion> l;
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
}