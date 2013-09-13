package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.domain.LowLevelTermVocab;
import gov.nih.nci.ctcae.core.domain.MeddraQuestion;
import gov.nih.nci.ctcae.core.domain.MeddraValidValue;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.Question;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfAddedQuestion;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.ValidValue;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;


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
    private String language = "en";
    private GenericRepository genericRepository;
    private String flashMessage;
    private List<ProCtcTerm> sortedSymptoms = new ArrayList<ProCtcTerm>();
    private Map<String, List<DisplayQuestion>> symptomQuestionMap = new HashMap<String, List<DisplayQuestion>>();
    private ProCtcTermRepository proCtcTermRepository;
    private MeddraRepository meddraRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    public SubmitFormCommand(String crfScheduleId, GenericRepository genericRepository, StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository, ProCtcTermRepository proCtcTermRepository, MeddraRepository meddraRepository) {
        this.genericRepository = genericRepository;
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
        this.proCtcTermRepository = proCtcTermRepository;
        this.meddraRepository = meddraRepository;
        schedule = studyParticipantCrfScheduleRepository.findById(Integer.parseInt(crfScheduleId));
        lazyInitializeSchedule();
        schedule.addParticipantAddedQuestions();
        schedule = genericRepository.save(schedule);
        generateDisplayQuestionsMap();
        totalQuestionPages = displayQuestionsMap.keySet().size();
        addMoreQuestionPageIndex = totalQuestionPages + 1;
        if(getIsEq5dCrf()){
            totalPages = totalQuestionPages + 1;
            reviewPageIndex = totalQuestionPages + 1 + 1;
//            schedule.setHealthAmount(50);
        } else {
            totalPages = totalQuestionPages + 1;
            reviewPageIndex = totalQuestionPages + 2;
        }

    }

    private void generateDisplayQuestionsMap() {
        for (StudyParticipantCrfItem item : schedule.getStudyParticipantCrfItems()) {
            String symptomGender = item.getCrfPageItem().getProCtcQuestion().getProCtcTerm().getGender();
            if (StringUtils.isBlank(symptomGender)) {
                symptomGender = "both";
            }

            if (symptomGender.equals(getParticipantGender().toLowerCase()) || symptomGender.equals("both")) {
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

    
    public boolean getIsEq5dCrf(){
    	return schedule.getStudyParticipantCrf().getCrf().isEq5d();
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
        return displayQuestionsMap.get(getCurrentPageIndex());
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
    
    private Integer getCurrentPageIndex(){
    	return currentPageIndex;
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

    public String getUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return user.getUsername();
    }

    public void setFlashMessage(String flashMessage) {
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
                String symptom = spcsaq.getProCtcOrMeddraQuestion().getQuestionSymptom();
                StudyParticipantCrf spc = schedule.getStudyParticipantCrf();

                List<StudyParticipantCrfAddedQuestion> l = new ArrayList<StudyParticipantCrfAddedQuestion>();
                for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : spc.getStudyParticipantCrfAddedQuestions()) {
                    if (studyParticipantCrfAddedQuestion.getProCtcOrMeddraQuestion().getQuestionSymptom().equals(symptom)) {
                        l.add(studyParticipantCrfAddedQuestion);
                    }
                }
                for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : l) {
                    spc.removeStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion);
                    genericRepository.deleteNoReload(studyParticipantCrfAddedQuestion);
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
        Collections.sort(sortedSymptoms);
        return sortedSymptoms;
    }


    public List<String> getDisplaySymptoms() {
        StudyParticipantAssignment spAssignment = getSchedule().getStudyParticipantCrf().getStudyParticipantAssignment();
        List<ProCtcTerm> sortedList = getSortedSymptoms();
        List<String> displayList = new ArrayList<String>();
        String language = this.getLanguage();
        if (language == null || language == "") {
            language = "en";
        }
        for (ProCtcTerm symptom : sortedList) {
            if (language != null) {
                if (language.equals("en")) {
                    displayList.add(symptom.getProCtcTermVocab().getTermEnglish());
                }
                if (language.equals("es")) {
                    displayList.add(symptom.getProCtcTermVocab().getTermSpanish());
                }
            } else {
                displayList.add(symptom.getProCtcTermVocab().getTermEnglish());
            }
        }
        return displayList;
    }

    public LowLevelTerm findMeddraTermBySymptom(String symptom) {
        String language = this.getLanguage();
        if (language == null || language == "") {
            language = "en";
        }
        MeddraQuery meddraQuery = new MeddraQuery();
        if (language.equals("en")) {
            meddraQuery.filterByMeddraTerm(symptom);
        } else {
            meddraQuery.filterBySpanishMeddraTerm(symptom);
        }
        return genericRepository.findSingle(meddraQuery);
    }

    public void addParticipantAddedQuestions(String[] selectedSymptoms, boolean firstTime) {
        lazyInitializeSchedule();
        int position = totalQuestionPages + 2;
        String language = this.getLanguage();
        if (language == null || language == "") {
            language = "en";
        }
        for (String symptom : selectedSymptoms) {
            List<StudyParticipantCrfScheduleAddedQuestion> newlyAddedQuestions = new ArrayList<StudyParticipantCrfScheduleAddedQuestion>();
            ProCtcTerm proCtcTerm = null;
            if (language.equals("en")) {
                proCtcTerm = proCtcTermRepository.findProCtcTermBySymptom(symptom);
            } else {
                proCtcTerm = proCtcTermRepository.findSpanishProTermBySymptom(symptom);
            }
            if (proCtcTerm != null) {
                addProCtcQuestion(proCtcTerm, newlyAddedQuestions);
            } else {
                LowLevelTerm lowLevelTerm = findMeddraTermBySymptom(symptom);
                if (lowLevelTerm == null) {
                    LowLevelTerm participantAddedLlt = new LowLevelTerm();
                    participantAddedLlt.setMeddraTerm(symptom, SupportedLanguageEnum.ENGLISH);
                    participantAddedLlt.setParticipantAdded(true);
                    LowLevelTerm term = genericRepository.save(participantAddedLlt);
                    addMeddraQuestion(term, firstTime, newlyAddedQuestions);
                } else {
                    List<CtcTerm> ctcTerms = meddraRepository.findCtcTermForMeddraTerm(lowLevelTerm.getMeddraTerm(SupportedLanguageEnum.ENGLISH));
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
                    LowLevelTermVocab participantAddedLltVocab = new LowLevelTermVocab(participantAddedLlt);
                    participantAddedLlt.setMeddraTerm(symptom, SupportedLanguageEnum.ENGLISH);
                    participantAddedLlt.setMeddraTerm(symptom, SupportedLanguageEnum.SPANISH);
                    participantAddedLlt.setParticipantAdded(true);
                    participantAddedLltVocab.setMeddraTermEnglish(symptom);
                    participantAddedLltVocab.setMeddraTermSpanish(symptom);
                    participantAddedLlt.setLowLevelTermVocab(participantAddedLltVocab);
                    LowLevelTerm term = genericRepository.save(participantAddedLlt);
                    addMeddraQuestion(term, firstTime, newlyAddedQuestions);
                } else {
                    List<CtcTerm> ctcTerms = meddraRepository.findCtcTermForMeddraTerm(lowLevelTerm.getMeddraTerm(SupportedLanguageEnum.ENGLISH));
                    if (ctcTerms == null || ctcTerms.size() == 0) {
                        addMeddraQuestion(lowLevelTerm, false, newlyAddedQuestions);
                    } else {
                        List<ProCtcTerm> proCtcTerms = ctcTerms.get(0).getProCtcTerms();
                        if (proCtcTerms.size() > 0 && "Y".equalsIgnoreCase(ctcTerms.get(0).getProCtcTerms().get(0).getCurrency())) {
                            addProCtcQuestion(proCtcTerms.get(0), newlyAddedQuestions);
                            } else {
                            addMeddraQuestion(lowLevelTerm, false, newlyAddedQuestions);
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
        totalPages = totalQuestionPages + 1;
        reviewPageIndex = totalQuestionPages + 2;

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
                    ctcTermsToCompare = meddraRepository.findCtcTermForMeddraTerm(((MeddraQuestion) ctcOrMeddraQuestion).getLowLevelTerm().getMeddraTerm(SupportedLanguageEnum.ENGLISH));
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
        if (symptomDoesNotExistInForm(lowLevelTerm.getMeddraTerm(SupportedLanguageEnum.ENGLISH))) {
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
        meddraValidValue.setValue("Sí", SupportedLanguageEnum.SPANISH);
        meddraValidValue.setDisplayOrder(1);
        MeddraValidValue meddraValidValue1 = new MeddraValidValue();
        meddraValidValue1.setValue("No", SupportedLanguageEnum.ENGLISH);
        meddraValidValue1.setValue("No", SupportedLanguageEnum.SPANISH);
        meddraValidValue1.setDisplayOrder(0);
        meddraQuestion.addValidValue(meddraValidValue);
        meddraQuestion.addValidValue(meddraValidValue1);
        if (meddraQuestion.getLowLevelTerm().isParticipantAdded()) {
            meddraQuestion.setQuestionText("Please confirm if you have experienced " + lowLevelTerm.getMeddraTerm(SupportedLanguageEnum.ENGLISH).toUpperCase() + " " + ":", SupportedLanguageEnum.ENGLISH);
            meddraQuestion.setQuestionText("Por favor, confirme si ha experimentado " + lowLevelTerm.getMeddraTerm(SupportedLanguageEnum.SPANISH).toUpperCase() + " " + ":", SupportedLanguageEnum.SPANISH);
        } else {
            meddraQuestion.setQuestionText("Did you have any " + lowLevelTerm.getMeddraTerm(SupportedLanguageEnum.ENGLISH).toUpperCase() + "?", SupportedLanguageEnum.ENGLISH);
            meddraQuestion.setQuestionText("Tuvo alguna " + lowLevelTerm.getMeddraTerm(SupportedLanguageEnum.SPANISH).toUpperCase() + "?", SupportedLanguageEnum.SPANISH);
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void lazyInitializeSchedule() {
        schedule = genericRepository.findById(StudyParticipantCrfSchedule.class, schedule.getId());
        schedule.getStudyParticipantCrfItems().size();
        schedule.getStudyParticipantCrfScheduleAddedQuestions().size();
        schedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().size();
    }

}