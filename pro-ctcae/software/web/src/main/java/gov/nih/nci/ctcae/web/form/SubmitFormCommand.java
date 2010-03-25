package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.rules.NotificationsEvaluationService;
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
    private Map<Integer, List<DisplayQuestion>> displayQuestionsMap = new HashMap<Integer, List<DisplayQuestion>>();
    private int totalPages = 0;
    private int totalQuestionPages = 0;
    private int addQuestionPageIndex = 0;
    private int reviewPageIndex = 0;
    private int currentPageIndex = 1;
    private String direction = "";
    private GenericRepository genericRepository;
    private String flashMessage;
    private List<ProCtcTerm> sortedSymptoms = new ArrayList<ProCtcTerm>();
    private Map<String, List<DisplayQuestion>> symptomQuestionMap = new HashMap<String, List<DisplayQuestion>>();
    private ProCtcTermRepository proCtcTermRepository;
    private MeddraRepository meddraRepository;

    public SubmitFormCommand(String crfScheduleId, GenericRepository genericRepository, ProCtcTermRepository proCtcTermRepository, MeddraRepository meddraRepository) {
        this.genericRepository = genericRepository;
        this.proCtcTermRepository = proCtcTermRepository;
        this.meddraRepository = meddraRepository;
        schedule = genericRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt(crfScheduleId));
        lazyInitializeSchedule();
        schedule.addParticipantAddedQuestions();
        schedule = genericRepository.save(schedule);
        generateDisplayQuestionsMap();
        totalQuestionPages = displayQuestionsMap.keySet().size();
        addQuestionPageIndex = totalQuestionPages + 1;
        totalPages = totalQuestionPages + 1;
        reviewPageIndex = totalQuestionPages + 2;
    }

    private void generateDisplayQuestionsMap() {

        for (StudyParticipantCrfItem item : schedule.getStudyParticipantCrfItems()) {
            DisplayQuestion displayQuestion = addQuestionToSymptomMap(item.getCrfPageItem().getProCtcQuestion());
            displayQuestion.setSelectedValidValue(item.getProCtcValidValue());
            displayQuestion.setStudyParticipantCrfItem(item);
            displayQuestion.setMandatory(item.getCrfPageItem().getResponseRequired());
        }
        for (StudyParticipantCrfScheduleAddedQuestion participantQuestion : schedule.getStudyParticipantCrfScheduleAddedQuestions()) {
            addParticipantAddedQuestionToSymptomMap(participantQuestion);
        }

        int position = 1;
        for (StudyParticipantCrfItem item : schedule.getStudyParticipantCrfItems()) {
            int nextPos = addQuestionToDisplayMap(position, item.getCrfPageItem().getProCtcQuestion());
            position = nextPos;

        }
        for (StudyParticipantCrfScheduleAddedQuestion participantQuestion : schedule.getStudyParticipantCrfScheduleAddedQuestions()) {
            int nextPos = addQuestionToDisplayMap(position, participantQuestion.getQuestion());
            position = nextPos;
        }
    }

    private void addParticipantAddedQuestionToSymptomMap(StudyParticipantCrfScheduleAddedQuestion participantQuestion) {
        DisplayQuestion displayQuestion = addQuestionToSymptomMap(participantQuestion.getQuestion());
        displayQuestion.setSelectedValidValue(participantQuestion.getValidValue());
        displayQuestion.setParticipantAdded(true);
        displayQuestion.setStudyParticipantCrfScheduleAddedQuestion(participantQuestion);
    }

    private int addQuestionToDisplayMap(int position, Question question) {
        String symptom;
        symptom = question.getQuestionSymptom();
        if (symptomQuestionMap.containsKey(symptom)) {
            displayQuestionsMap.put(position, symptomQuestionMap.get(symptom));
            symptomQuestionMap.remove(symptom);
            position = position + 1;
        }
        return position;
    }

    private DisplayQuestion addQuestionToSymptomMap(Question question) {
        DisplayQuestion displayQuestion = new DisplayQuestion(genericRepository, this);
        displayQuestion.setQuestion(question);
        if (!symptomQuestionMap.containsKey(question.getQuestionSymptom())) {
            symptomQuestionMap.put(question.getQuestionSymptom(), new ArrayList<DisplayQuestion>());
        }
        symptomQuestionMap.get(question.getQuestionSymptom()).add(displayQuestion);
        return displayQuestion;
    }


    public StudyParticipantCrfSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(StudyParticipantCrfSchedule schedule) {
        this.schedule = schedule;
    }

    public Map<Integer, List<DisplayQuestion>> getDisplayQuestionsMap() {
        return displayQuestionsMap;
    }

    public List<DisplayQuestion> getCurrentPageQuestions() {
        return displayQuestionsMap.get(getNewPageIndex());
    }

    public List<DisplayQuestion> getSubmittedPageQuestions() {
        return displayQuestionsMap.get(currentPageIndex);
    }

    public int getNewPageIndex() {
        if (direction.equals("back")) {
            currentPageIndex--;
        }
        if (direction.equals("continue")) {
            currentPageIndex++;
        }
        direction = "";
        return currentPageIndex;
    }

    public void setCurrentPageIndex(String currentPageIndex) {
        direction = "";
        if (StringUtils.isBlank(currentPageIndex)) {
            this.currentPageIndex = 1;
        } else {
            this.currentPageIndex = Integer.parseInt(currentPageIndex);
        }
    }

    public int getTotalPages() {
        return totalPages;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean save() throws Exception {
        schedule = genericRepository.save(schedule);
        lazyInitializeSchedule();
        boolean submit = false;
        if ("save".equals(direction)) {
            deleteQuestions();
            schedule.setStatus(CrfStatus.COMPLETED);
            NotificationsEvaluationService notificationsEvaluationService = new NotificationsEvaluationService();
            notificationsEvaluationService.setGenericRepository(genericRepository);
            notificationsEvaluationService.executeRules(schedule, schedule.getStudyParticipantCrf().getCrf(), schedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite());
            setFlashMessage("You have successfully submitted the form.");
            submit = true;
        } else {
            schedule.setStatus(CrfStatus.INPROGRESS);
        }
        schedule = genericRepository.save(schedule);
        lazyInitializeSchedule();
        return submit;
    }

    private void setFlashMessage(String flashMessage) {
        this.flashMessage = flashMessage;
    }

    public String getFlashMessage() {
        return flashMessage;
    }

    public void deleteQuestions() {
        for (StudyParticipantCrfScheduleAddedQuestion spcaq : schedule.getStudyParticipantCrfScheduleAddedQuestions()) {
            if (spcaq.getProCtcQuestion() != null && spcaq.getProCtcValidValue() != null) {
                if (spcaq.getProCtcQuestion().getDisplayOrder() == 1 && spcaq.getProCtcValidValue().getDisplayOrder() == 0) {
                    deleteSingleQuestion(spcaq);
                }
            } else {
                if (spcaq.getMeddraQuestion() != null && spcaq.getMeddraValidValue() != null) {
                    if (spcaq.getMeddraValidValue().getDisplayOrder() == 0) {
                        deleteSingleQuestion(spcaq);
                    }
                }
            }
        }
    }

    private void deleteSingleQuestion(StudyParticipantCrfScheduleAddedQuestion spcsaq) {
        if (spcsaq != null) {
            StudyParticipantCrfAddedQuestion spcaq = genericRepository.findById(StudyParticipantCrfAddedQuestion.class, spcsaq.getStudyParticipantCrfAddedQuestionId());
            if (spcaq != null) {
                String symptom = spcsaq.getProCtcOrMeddraQuestion().getQuestionSymptom();
                StudyParticipantCrf spc = schedule.getStudyParticipantCrf();
                spc.removeStudyParticipantCrfAddedQuestion(spcaq);
                List<StudyParticipantCrfAddedQuestion> l = new ArrayList<StudyParticipantCrfAddedQuestion>();
                for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : spc.getStudyParticipantCrfAddedQuestions()) {
                    if (studyParticipantCrfAddedQuestion.getProCtcOrMeddraQuestion().getQuestionSymptom().equals(symptom)) {
                        l.add(studyParticipantCrfAddedQuestion);
                    }
                }
                for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : l) {
                    spc.removeStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion);
                    genericRepository.delete(studyParticipantCrfAddedQuestion);
                }
            }
        }
    }

    public void computeAdditionalSymptoms(ArrayList<ProCtcTerm> proCtcTerms) {
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = genericRepository.findById(StudyParticipantCrfSchedule.class, schedule.getId());
        for (StudyParticipantCrfItem item : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            proCtcTerms.remove(item.getCrfPageItem().getProCtcQuestion().getProCtcTerm());
        }
        for (StudyParticipantCrfScheduleAddedQuestion participantQuestion : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
            if (participantQuestion.getProCtcQuestion() != null) {
                proCtcTerms.remove(participantQuestion.getProCtcQuestion().getProCtcTerm());
            }
        }
        sortedSymptoms = proCtcTerms;
    }

    public List<ProCtcTerm> getSortedSymptoms() {
        Collections.sort(sortedSymptoms, new ProCtcTermComparator());
        return sortedSymptoms;
    }


    public List<String> getDisplaySymptoms() {
        List<ProCtcTerm> sortedList = getSortedSymptoms();
        List<String> displayList = new ArrayList<String>();
        for (ProCtcTerm symptom : sortedList) {
            displayList.add(symptom.getTerm());
        }
        return displayList;
    }

    public LowLevelTerm findMeddraTermBySymptom(String symptom) {
        MeddraQuery meddraQuery = new MeddraQuery();
        meddraQuery.filterByMeddraTerm(symptom);
        return genericRepository.findSingle(meddraQuery);
    }

    public void addParticipantAddedQuestions(String[] selectedSymptoms, boolean firstTime) {
        lazyInitializeSchedule();
        int questionPagesBeforeAdd = totalQuestionPages;
        List<StudyParticipantCrfScheduleAddedQuestion> newlyAddedQuestions = new ArrayList<StudyParticipantCrfScheduleAddedQuestion>();
        for (String symptom : selectedSymptoms) {
            ProCtcTerm proCtcTerm = proCtcTermRepository.findProCtcTermBySymptom(symptom);
            if (proCtcTerm != null) {
                addProCtcQuestion(proCtcTerm, newlyAddedQuestions);
            } else {
                LowLevelTerm lowLevelTerm = findMeddraTermBySymptom(symptom);
                if (lowLevelTerm == null) {
                    LowLevelTerm participantAddedLlt = new LowLevelTerm();
                    participantAddedLlt.setMeddraTerm(symptom);
                    participantAddedLlt.setParticipantAdded(true);
                    LowLevelTerm term = genericRepository.save(participantAddedLlt);
                    addMeddraQuestion(term, firstTime, newlyAddedQuestions);
                } else {
                    List<CtcTerm> ctcTerms = meddraRepository.findCtcTermForMeddraTerm(lowLevelTerm.getMeddraTerm());
                    if (ctcTerms == null || ctcTerms.size() == 0) {
                        addMeddraQuestion(lowLevelTerm, false, newlyAddedQuestions);
                    } else {
                        List<ProCtcTerm> proCtcTerms = ctcTerms.get(0).getProCtcTerms();
                        if (proCtcTerms.size() == 0) {
                            addMeddraQuestion(lowLevelTerm, false, newlyAddedQuestions);
                        } else {
                            addProCtcQuestion(proCtcTerms.get(0), newlyAddedQuestions);
                        }
                    }
                }
            }
        }
        totalPages = totalQuestionPages + 1;
        reviewPageIndex = totalQuestionPages + 2;
        int position = questionPagesBeforeAdd + 2;
        for (StudyParticipantCrfScheduleAddedQuestion spcsaq : newlyAddedQuestions) {
            addParticipantAddedQuestionToSymptomMap(spcsaq);
        }
        for (StudyParticipantCrfScheduleAddedQuestion spcsaq : newlyAddedQuestions) {
            int nextPos = addQuestionToDisplayMap(position, spcsaq.getProCtcOrMeddraQuestion());
            position = nextPos;
        }
    }

    public boolean ctcTermAlreadyExistsInForm(List<CtcTerm> ctcTerms) {
        if (ctcTerms != null && ctcTerms.size() > 0) {
            for (StudyParticipantCrfItem item : schedule.getStudyParticipantCrfItems()) {
                for (CtcTerm ctcTerm : ctcTerms) {
                    if (ctcTerm.getTerm().toLowerCase().equals(item.getCrfPageItem().getProCtcQuestion().getProCtcTerm().getCtcTerm().getTerm().toLowerCase())) {
                        return true;
                    }
                }
            }
            for (StudyParticipantCrfScheduleAddedQuestion question : schedule.getStudyParticipantCrfScheduleAddedQuestions()) {
                Question ctcOrMeddraQuestion = question.getProCtcOrMeddraQuestion();
                List<CtcTerm> ctcTermsToCompare = new ArrayList<CtcTerm>();
                if (ctcOrMeddraQuestion instanceof ProCtcQuestion) {
                    CtcTerm temp = ((ProCtcQuestion) ctcOrMeddraQuestion).getProCtcTerm().getCtcTerm();
                    if (temp != null) {
                        ctcTermsToCompare.add(temp);
                    }
                }
                if (ctcOrMeddraQuestion instanceof MeddraQuestion) {
                    ctcTermsToCompare = meddraRepository.findCtcTermForMeddraTerm(((MeddraQuestion) ctcOrMeddraQuestion).getLowLevelTerm().getMeddraTerm());
                }

                if (ctcTermsToCompare != null && ctcTermsToCompare.size() > 0) {
                    for (CtcTerm ctcTerm : ctcTerms) {
                        for (CtcTerm ctcTermToCompare : ctcTermsToCompare) {
                            if (ctcTerm.getTerm().toLowerCase().equals(ctcTermToCompare.getTerm().toLowerCase())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    private void addProCtcQuestion(ProCtcTerm proCtcTerm, List<StudyParticipantCrfScheduleAddedQuestion> newlyAddedQuestions) {
        totalQuestionPages++;
        for (ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()) {
            StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = AddParticipantSelectedQuestion(proCtcQuestion, false);
            newlyAddedQuestions.add(studyParticipantCrfScheduleAddedQuestion);
        }
    }

    private void addMeddraQuestion(LowLevelTerm lowLevelTerm, boolean firstTime, List<StudyParticipantCrfScheduleAddedQuestion> newlyAddedQuestions) {

        totalQuestionPages++;
        List<MeddraQuestion> meddraQuestions = lowLevelTerm.getMeddraQuestions();
        MeddraQuestion meddraQuestion;
        if (meddraQuestions.size() > 0) {
            meddraQuestion = meddraQuestions.get(0);
        } else {
            meddraQuestion = createMeddraQuestion(lowLevelTerm);
        }
        StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = AddParticipantSelectedQuestion(meddraQuestion, firstTime);
        newlyAddedQuestions.add(studyParticipantCrfScheduleAddedQuestion);
    }

    private StudyParticipantCrfScheduleAddedQuestion AddParticipantSelectedQuestion(Question question, boolean firstTime) {
        StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion = schedule.getStudyParticipantCrf().addStudyParticipantCrfAddedQuestion(question, totalQuestionPages - 1);
        genericRepository.save(studyParticipantCrfAddedQuestion);
        StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = schedule.addStudyParticipantCrfScheduleAddedQuestion(studyParticipantCrfAddedQuestion, firstTime);
        if (studyParticipantCrfScheduleAddedQuestion != null) {
            genericRepository.save(studyParticipantCrfScheduleAddedQuestion);
        }
        return studyParticipantCrfScheduleAddedQuestion;
    }

    private MeddraQuestion createMeddraQuestion(LowLevelTerm lowLevelTerm) {
        MeddraQuestion meddraQuestion = new MeddraQuestion();
        MeddraValidValue meddraValidValue = new MeddraValidValue();
        meddraQuestion.setLowLevelTerm(lowLevelTerm);
        meddraQuestion.setProCtcQuestionType(ProCtcQuestionType.PRESENT);
        meddraValidValue.setValue("Yes");
        meddraValidValue.setDisplayOrder(1);
        MeddraValidValue meddraValidValue1 = new MeddraValidValue();
        meddraValidValue1.setValue("No");
        meddraValidValue1.setDisplayOrder(0);
        meddraQuestion.addValidValue(meddraValidValue);
        meddraQuestion.addValidValue(meddraValidValue1);
        if (meddraQuestion.getLowLevelTerm().isParticipantAdded()) {
            meddraQuestion.setQuestionText("Please confirm that this is a " + lowLevelTerm.getMeddraTerm() + " that you have experienced " + schedule.getStudyParticipantCrf().getCrf().getRecallPeriod() + ":");
        } else {
            meddraQuestion.setQuestionText("Did you have any " + lowLevelTerm.getMeddraTerm() + "?");
        }
        meddraQuestion.setDisplayOrder(1);
        genericRepository.save(meddraQuestion);
        return meddraQuestion;
    }

    public int getAddQuestionPageIndex() {
        return addQuestionPageIndex;
    }

    public void setAddQuestionPageIndex(int addQuestionPageIndex) {
        this.addQuestionPageIndex = addQuestionPageIndex;
    }

    public int getReviewPageIndex() {
        return reviewPageIndex;
    }

    public void setReviewPageIndex(int reviewPageIndex) {
        this.reviewPageIndex = reviewPageIndex;
    }

    public int getTotalQuestionPages() {
        return totalQuestionPages;
    }

    public void setTotalQuestionPages(int totalQuestionPages) {
        this.totalQuestionPages = totalQuestionPages;
    }

    public void lazyInitializeSchedule() {
        schedule = genericRepository.findById(StudyParticipantCrfSchedule.class, schedule.getId());
        schedule.getStudyParticipantCrfItems();
        schedule.getStudyParticipantCrfScheduleAddedQuestions();
        schedule.getStudyParticipantCrf();
        schedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions();
    }

}