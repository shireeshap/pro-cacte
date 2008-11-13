package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
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
    private String direction;

    public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
        return studyParticipantCrfSchedule;
    }

    public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
        totalQuestions = studyParticipantCrfSchedule.getStudyParticipantCrfItems().size();
        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            if (studyParticipantCrfItem.getProCtcValidValue() == null) {
                break;
            } else {
                currentIndex++;
            }
        }
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

    public boolean saveResponseAndGetQuestion(FinderRepository finderRepository, GenericRepository genericRepository) {

        genericRepository.save(studyParticipantCrfSchedule);
        boolean displayConfirmation = false;

        if ("continue".equals(getDirection())) {
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

        if ("save".equals(getDirection())) {
           displayConfirmation = true;
        }
        studyParticipantCrfSchedule = finderRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId());
        return displayConfirmation;

    }
}