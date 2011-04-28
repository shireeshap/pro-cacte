package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

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
    private int addMoreQuestionPageIndex = 0;
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
        addMoreQuestionPageIndex = addQuestionPageIndex + 1;
        totalPages = totalQuestionPages + 2;
        reviewPageIndex = totalQuestionPages + 3;
    }

    private void generateDisplayQuestionsMap() {
        for (StudyParticipantCrfItem item : schedule.getStudyParticipantCrfItems()) {
            String symptomGender = item.getCrfPageItem().getProCtcQuestion().getProCtcTerm().getGender();
            if (StringUtils.isBlank(symptomGender)) {
                symptomGender = "both";
            }

            if (symptomGender.equals(getParticipantGender()) || symptomGender.equals("both")) {
                DisplayQuestion displayQuestion = addQuestionToSymptomMap(item.getCrfPageItem().getProCtcQuestion());
                displayQuestion.setSelectedValidValue(item.getProCtcValidValue());
                displayQuestion.setStudyParticipantCrfItem(item);
                displayQuestion.setMandatory(item.getCrfPageItem().getResponseRequired());
            }
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

    public String getParticipantGender() {
        String participantGender = getSchedule().getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().getGender();
        if (StringUtils.isBlank(participantGender)) {
            participantGender = "both";
        } else {
            participantGender.toLowerCase();
        }
        return participantGender;
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

        List<DisplayQuestion> questions = symptomQuestionMap.get(question.getQuestionSymptom());
        questions.add(displayQuestion);
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
        // schedule = genericRepository.save(schedule);
        //lazyInitializeSchedule();
        boolean submit = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        if ("save".equals(direction)) {
            deleteQuestions();
            schedule.setStatus(CrfStatus.COMPLETED);
            schedule.setFormSubmissionMode(AppMode.HOMEWEB);
            //adding the notifications scheduled for the form submission
            schedule.setCompletionDate(new Date());
            if (schedule.getStudyParticipantCrfScheduleNotification() == null) {
                StudyParticipantCrfScheduleNotification studyParticipantCrfScheduleNotification = new StudyParticipantCrfScheduleNotification();
                studyParticipantCrfScheduleNotification.setStudyParticipantCrfSchedule(schedule);
                schedule.setStudyParticipantCrfScheduleNotification(studyParticipantCrfScheduleNotification);
            }

            //TODO:Suneel A, Needs to remove the block after testing
            /*NotificationsEvaluationService notificationsEvaluationService = new NotificationsEvaluationService();
            notificationsEvaluationService.setGenericRepository(genericRepository);
            notificationsEvaluationService.executeRules(schedule, schedule.getStudyParticipantCrf().getCrf(), schedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite());*/
            setFlashMessage("You have successfully submitted the form.");
            submit = true;
        } else {
            schedule.setStatus(CrfStatus.INPROGRESS);
            List<DisplayQuestion> questions = this.getCurrentPageQuestions();
            if (questions != null) {
                for (DisplayQuestion question : questions) {
                    if (question != null) {
                        StudyParticipantCrfItem spCrfItem = question.getStudyParticipantCrfItem();
                        if (spCrfItem != null) {
                            spCrfItem.setReponseDate(new Date());
                            spCrfItem.setResponseMode(AppMode.HOMEWEB);
                            spCrfItem.setUpdatedBy(user.getUsername());
                        }
                        StudyParticipantCrfScheduleAddedQuestion spcsAddedQuestion = question.getStudyParticipantCrfScheduleAddedQuestion();
                        if (spcsAddedQuestion != null) {
                            spcsAddedQuestion.setReponseDate(new Date());
                            spcsAddedQuestion.setResponseMode(AppMode.HOMEWEB);
                            spcsAddedQuestion.setUpdatedBy(user.getUsername());
                        }
                    }
                }
            }
        }
        genericRepository.saveOrUpdate(schedule);
        //lazyInitializeSchedule();
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
//                spc.removeStudyParticipantCrfAddedQuestion(spcaq);   
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
            displayList.add(symptom.getProCtcTermVocab().getTermEnglish());
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
        int position = totalQuestionPages + 2;

        for (String symptom : selectedSymptoms) {
            List<StudyParticipantCrfScheduleAddedQuestion> newlyAddedQuestions = new ArrayList<StudyParticipantCrfScheduleAddedQuestion>();
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
            for (StudyParticipantCrfScheduleAddedQuestion spcsaq : newlyAddedQuestions) {
                addParticipantAddedQuestionToSymptomMap(spcsaq);
            }
            for (StudyParticipantCrfScheduleAddedQuestion spcsaq : newlyAddedQuestions) {
                int nextPos = addQuestionToDisplayMap(position, spcsaq.getProCtcOrMeddraQuestion());
                position = nextPos;
            }
        }
        totalPages = totalQuestionPages + 2;
        addMoreQuestionPageIndex = totalQuestionPages + 2;
        reviewPageIndex = totalQuestionPages + 3;

    }


public void addMoreParticipantAddedQuestions(String[] selectedSymptoms, boolean firstTime) {
        lazyInitializeSchedule();
        int position = totalQuestionPages + 3;

        for (String symptom : selectedSymptoms) {
            List<StudyParticipantCrfScheduleAddedQuestion> newlyAddedQuestions = new ArrayList<StudyParticipantCrfScheduleAddedQuestion>();
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
            for (StudyParticipantCrfScheduleAddedQuestion spcsaq : newlyAddedQuestions) {
                addParticipantAddedQuestionToSymptomMap(spcsaq);
            }
            for (StudyParticipantCrfScheduleAddedQuestion spcsaq : newlyAddedQuestions) {
                int nextPos = addQuestionToDisplayMap(position, spcsaq.getProCtcOrMeddraQuestion());
                position = nextPos;
            }
        }
        totalPages = totalQuestionPages + 2;
        reviewPageIndex = totalQuestionPages + 3;

    }



    public boolean ctcTermAlreadyExistsInForm(List<CtcTerm> ctcTerms) {
        if (ctcTerms != null && ctcTerms.size() > 0) {
            for (StudyParticipantCrfItem item : schedule.getStudyParticipantCrfItems()) {
                for (CtcTerm ctcTerm : ctcTerms) {
                    if (ctcTerm.getTerm(SupportedLanguageEnum.ENGLISH).toLowerCase().equals(item.getCrfPageItem().getProCtcQuestion().getProCtcTerm().getCtcTerm().getTerm(SupportedLanguageEnum.ENGLISH).toLowerCase())) {
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
                            if (ctcTerm.getTerm(SupportedLanguageEnum.ENGLISH).toLowerCase().equals(ctcTermToCompare.getTerm(SupportedLanguageEnum.ENGLISH).toLowerCase())) {
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
        if (symptomDoesNotExistInForm(proCtcTerm.getProCtcTermVocab().getTermEnglish())) {
            for (ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()) {
                StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = AddParticipantSelectedQuestion(proCtcQuestion, false);
                newlyAddedQuestions.add(studyParticipantCrfScheduleAddedQuestion);
            }
            totalQuestionPages++;
        }
    }

    private boolean symptomDoesNotExistInForm(String term) {
        for (Integer i : displayQuestionsMap.keySet()) {
            List<DisplayQuestion> questions = displayQuestionsMap.get(i);
            for (DisplayQuestion displayQuestion : questions) {
                if (displayQuestion.getQuestionSymptom().equals(term)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void addMeddraQuestion(LowLevelTerm lowLevelTerm, boolean firstTime, List<StudyParticipantCrfScheduleAddedQuestion> newlyAddedQuestions) {
        if (symptomDoesNotExistInForm(lowLevelTerm.getMeddraTerm())) {
            List<MeddraQuestion> meddraQuestions = lowLevelTerm.getMeddraQuestions();
            MeddraQuestion meddraQuestion;
            if (meddraQuestions.size() > 0) {
                meddraQuestion = meddraQuestions.get(0);
            } else {
                meddraQuestion = createMeddraQuestion(lowLevelTerm);
            }
            StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = AddParticipantSelectedQuestion(meddraQuestion, firstTime);
            newlyAddedQuestions.add(studyParticipantCrfScheduleAddedQuestion);
            totalQuestionPages++;
        }
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
        meddraValidValue.setValue("Yes", SupportedLanguageEnum.ENGLISH);
        meddraValidValue.setDisplayOrder(1);
        MeddraValidValue meddraValidValue1 = new MeddraValidValue();
        meddraValidValue1.setValue("No", SupportedLanguageEnum.ENGLISH);
        meddraValidValue1.setDisplayOrder(0);
        meddraQuestion.addValidValue(meddraValidValue);
        meddraQuestion.addValidValue(meddraValidValue1);
        if (meddraQuestion.getLowLevelTerm().isParticipantAdded()) {
            meddraQuestion.setQuestionText("Please confirm if you have experienced " + lowLevelTerm.getMeddraTerm().toUpperCase() + " " + schedule.getStudyParticipantCrf().getCrf().getRecallPeriod() + ":", SupportedLanguageEnum.ENGLISH);
        } else {
            meddraQuestion.setQuestionText("Did you have any " + lowLevelTerm.getMeddraTerm() + "?", SupportedLanguageEnum.ENGLISH);
        }
        meddraQuestion.setDisplayOrder(1);
        genericRepository.save(meddraQuestion);
        return meddraQuestion;
    }

    /**
     * Sets the currentPageIndex to first unanswered question.
     */
    public void firstUnAnsweredPageIndex() {
        int total = getTotalQuestionPages();
        List<ValidValue> valid = new ArrayList<ValidValue>();
        List<DisplayQuestion> displayQuestionsList = new ArrayList<DisplayQuestion>();
        int i = 0, tempIndex = 1;
        for (i = 1; i <= total; i++) {
            displayQuestionsList = getCurrentPageQuestions();
            int count = 0;
            if (displayQuestionsList != null) {
                for (DisplayQuestion displayQuestion : displayQuestionsList) {
                    if (displayQuestion.getSelectedValidValue() != null) {
                        count++;
                        valid.add(displayQuestion.getSelectedValidValue());
                        if (displayQuestionsList.size() > 1) {
                            if (displayQuestion.getSelectedValidValue().getDisplayOrder() == 0 || count == displayQuestionsList.size()) {
                                tempIndex = tempIndex + 1;
                                setCurrentPageIndex(Integer.toString(tempIndex));
                            } else
                                setCurrentPageIndex(Integer.toString(tempIndex));
                        } else {
                            tempIndex = tempIndex + 1;
                            setCurrentPageIndex(Integer.toString(tempIndex));
                        }
                    }
                }
            } else {
                tempIndex = tempIndex + 1;
                setCurrentPageIndex(Integer.toString(tempIndex));
            }
        }
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

    public int getAddMoreQuestionPageIndex() {
        return addMoreQuestionPageIndex;
    }

    public void setAddMoreQuestionPageIndex(int addMoreQuestionPageIndex) {
        this.addMoreQuestionPageIndex = addMoreQuestionPageIndex;
    }

    public void lazyInitializeSchedule() {
        schedule = genericRepository.findById(StudyParticipantCrfSchedule.class, schedule.getId());
        schedule.getStudyParticipantCrfItems().size();
        schedule.getStudyParticipantCrfScheduleAddedQuestions().size();
        schedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().size();
    }

}