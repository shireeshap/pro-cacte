package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.io.Serializable;

/**
 * @author Harsh Agarwal
 * @crated Nov 12, 2008
 */
public class SubmitFormCommand implements Serializable {

    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    private int currentIndex;
    private int totalQuestions;
    private String direction = "";
    private String flashMessage;
    private int questionIndex;

    public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
        return studyParticipantCrfSchedule;
    }

    public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
        totalQuestions = studyParticipantCrfSchedule.getStudyParticipantCrfItems().size();
        currentIndex = getUnansweredQuestionIndex();
    }

    public int getCurrentIndex() {

        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
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

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public void saveResponseAndGetQuestion(FinderRepository finderRepository, GenericRepository genericRepository) {

        if ("continue".equals(getDirection())) {
            if (currentIndex == 0) {
                studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
            }
            currentIndex = currentIndex + 1;
            if (currentIndex > (totalQuestions - 1)) {
                currentIndex = totalQuestions;
            }
        }
        if ("back".equals(getDirection())) {
            currentIndex = currentIndex - 1;
            if (currentIndex < 0) {
                currentIndex = 0;
            }
        }
        if ("jump".equals(getDirection())) {
            currentIndex = getQuestionIndex();
        }
        if ("save".equals(getDirection())) {
            studyParticipantCrfSchedule.setStatus(CrfStatus.COMPLETED);
        }

        genericRepository.save(studyParticipantCrfSchedule);
        studyParticipantCrfSchedule = finderRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId());
    }

    public int getUnansweredQuestionIndex() {
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
}
