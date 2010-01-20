package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;


//

/**
 * The Class SubmitFormCommand.
 *
 * @author Harsh Agarwal
 * @since Nov 12, 2008
 */
public class SubmitFormCommand implements Serializable {

    private StudyParticipantCrfSchedule schedule;
    private Map<Integer, List<DisplayQuestion>> displayQuestions = new HashMap<Integer, List<DisplayQuestion>>();
    private int totalPages = 0;
    private int addQuestionPageIndex = 0;
    private int currentPageIndex = 0;

//    private GenericRepository genericRepository;

    public SubmitFormCommand(String crfScheduleId, GenericRepository genericRepository) {
//        this.genericRepository = genericRepository;
        schedule = genericRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt(crfScheduleId));
        schedule.addParticipantAddedQuestions();
        schedule = genericRepository.save(schedule);
        generateDisplayQuestionsMap();
        totalPages = displayQuestions.keySet().size();
        addQuestionPageIndex = totalPages + 1;
    }

    private void generateDisplayQuestionsMap() {
        int position = 1;
        for (StudyParticipantCrfItem item : schedule.getStudyParticipantCrfItems()) {
            addQuestionToDisplayMap(position, item.getCrfPageItem().getProCtcQuestion());
            position++;
        }
        for (StudyParticipantCrfScheduleAddedQuestion participantQuestion : schedule.getStudyParticipantCrfScheduleAddedQuestions()) {
            addQuestionToDisplayMap(position, participantQuestion.getProCtcOrMeddraQuestion());
            position++;
        }
    }

    private void addQuestionToDisplayMap(int position, Question question) {
        DisplayQuestion displayQuestion = new DisplayQuestion();
        displayQuestion.setQuestion(question);
        if (!displayQuestions.containsKey(position)) {
            displayQuestions.put(position, new ArrayList<DisplayQuestion>());
        }
        displayQuestions.get(position).add(displayQuestion);
    }

    public StudyParticipantCrfSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(StudyParticipantCrfSchedule schedule) {
        this.schedule = schedule;
    }

    public Map<Integer, List<DisplayQuestion>> getDisplayQuestions() {
        return displayQuestions;
    }
}