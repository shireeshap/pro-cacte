package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: AgarwalH
 * Date: Jan 12, 2010
 * Time: 12:37:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class DisplayQuestion {
    private Question question;
    private ValidValue selectedValidValue;
    private GenericRepository genericRepository;
    private boolean participantAdded = false;
    private StudyParticipantCrfItem studyParticipantCrfItem;
    private StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion;
    private SubmitFormCommand command;
    private boolean mandatory = false;

    public DisplayQuestion(GenericRepository genericRepository, SubmitFormCommand command) {
        this.genericRepository = genericRepository;
        this.command = command;
    }

    public String getQuestionText() {
        return question.getQuestionText();
    }

    public String getQuestionSymptom() {
        return question.getQuestionSymptom();
    }

    public String getSymptomGender() {
        return question.getSymptomGender();
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


    public List<ValidValue> getValidValues() {

        List<ValidValue> validValues = new ArrayList<ValidValue>();
        if (question instanceof ProCtcQuestion) {
            ProCtcQuestion q = genericRepository.findById(ProCtcQuestion.class, question.getId());
            for (ValidValue validValue : q.getValidValues()) {
                validValues.add(validValue);
            }
        } else {
            if (question instanceof MeddraQuestion) {
                MeddraQuestion q = genericRepository.findById(MeddraQuestion.class, question.getId());
                for (ValidValue validValue : q.getValidValues()) {
                    validValues.add(validValue);
                }
            }
        }
        return validValues;
    }

    public ValidValue getSelectedValidValue() {
        return selectedValidValue;
    }

    public void setSelectedValidValue(ValidValue selectedValidValue) {
        this.selectedValidValue = selectedValidValue;
    }

    public void setSelectedValidValueId(String selectedValidValueId) {
        ValidValue validValue = null;
        if (!StringUtils.isBlank(selectedValidValueId)) {
            int selectedId = Integer.parseInt(selectedValidValueId);
            if (question instanceof ProCtcQuestion) {
                validValue = genericRepository.findById(ProCtcValidValue.class, selectedId);
            }
            if (question instanceof MeddraQuestion) {
                validValue = genericRepository.findById(MeddraValidValue.class, selectedId);
            }
        }
        if (participantAdded) {
            getStudyParticipantCrfScheduleAddedQuestion().setValidValue(validValue);
        } else {
            getStudyParticipantCrfItem().setValidValue(validValue);
        }
        setSelectedValidValue(validValue);

    }


    public boolean isParticipantAdded() {
        return participantAdded;
    }

    public void setParticipantAdded(boolean participantAdded) {
        this.participantAdded = participantAdded;
    }

    public void setStudyParticipantCrfItem(StudyParticipantCrfItem studyParticipantCrfItem) {
        this.studyParticipantCrfItem = studyParticipantCrfItem;
    }

    public void setStudyParticipantCrfScheduleAddedQuestion(StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion) {
        this.studyParticipantCrfScheduleAddedQuestion = studyParticipantCrfScheduleAddedQuestion;
    }

    public StudyParticipantCrfScheduleAddedQuestion getStudyParticipantCrfScheduleAddedQuestion() {
        for (StudyParticipantCrfScheduleAddedQuestion question : command.getSchedule().getStudyParticipantCrfScheduleAddedQuestions()) {
            if (studyParticipantCrfScheduleAddedQuestion != null) {
                if (question.getId().equals(studyParticipantCrfScheduleAddedQuestion.getId())) {
                    return question;
                }
            }
        }
        return null;
    }

    public StudyParticipantCrfItem getStudyParticipantCrfItem() {
        for (StudyParticipantCrfItem item : command.getSchedule().getStudyParticipantCrfItems()) {
            if (studyParticipantCrfItem != null) {
                if (item.getId().equals(studyParticipantCrfItem.getId())) {
                    return item;
                }
            }
        }
        return null;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isProCtcQuestion() {
        return question instanceof ProCtcQuestion;
    }

    public boolean isMeddraQuestion() {
        return question instanceof MeddraQuestion;
    }
}
