package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
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
    //private int currentIndex;
    //private int totalQuestions;
    private String direction = "";
    private String flashMessage;
    private int totalPages;
    private int currentPageIndex;
    ArrayList<List<StudyParticipantCrfItem>> pages;

    public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
        return studyParticipantCrfSchedule;
    }

    public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
        buildPagesForSymptoms();
      //  totalQuestions = studyParticipantCrfSchedule.getStudyParticipantCrfItems().size();
      //  currentIndex = getUnansweredQuestionIndex();
        totalPages = pages.size();
        currentPageIndex = getPageForUnansweredQuestion();
    }

    private void buildPagesForSymptoms() {
        pages = new ArrayList<List<StudyParticipantCrfItem>>();
        List<StudyParticipantCrfItem> studyParticipantCrfItems = studyParticipantCrfSchedule.getStudyParticipantCrfItems();

        Hashtable<ProCtcTerm, Integer> tempHash = new Hashtable<ProCtcTerm, Integer>();
        int i = 0;
        int j = 0;

        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfItems) {

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

//    public int getCurrentIndex() {
//
//        return currentIndex;
//    }

//    public int getTotalQuestions() {
//        return totalQuestions;
//    }

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

    private int getUnansweredQuestionIndex() {
        int tmp = 0;
        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {

            if (studyParticipantCrfItem.getProCtcValidValue() == null) {
                break;
            } else {
                tmp++;
            }
        }
        return tmp;
    }

    private int getPageForUnansweredQuestion() {
        int tmp = 0;
        int unansweredQuestionIndex =getUnansweredQuestionIndex();
        if(unansweredQuestionIndex == studyParticipantCrfSchedule.getStudyParticipantCrfItems().size()){
            return pages.size();
        }

        StudyParticipantCrfItem unansweredItem = studyParticipantCrfSchedule.getStudyParticipantCrfItems().get(getUnansweredQuestionIndex());

        for (List<StudyParticipantCrfItem> symptom : pages) {
            for (StudyParticipantCrfItem studyParticipantCrfItem : symptom) {
                if (studyParticipantCrfItem.equals(unansweredItem)) {
                    return tmp;
                }
            }
            tmp++;
        }
        return tmp;
    }

}