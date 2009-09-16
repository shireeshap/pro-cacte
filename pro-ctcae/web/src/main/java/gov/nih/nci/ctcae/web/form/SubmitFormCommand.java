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

    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    private GenericRepository genericRepository;
    private Hashtable<Integer, String> displayRules = new Hashtable<Integer, String>();
    private int currentPageIndex;
    private int totalPages;
    private String direction = "";
    private String flashMessage;
    private String deletedQuestions;
    private int participantAddedQuestionIndex = 0;
    private Set<String> questionsToBeDeleted = new HashSet<String>();
    private ArrayList<ProCtcTerm> sortedSymptoms;

    public void initialize() {
        setDisplayRules();
        currentPageIndex = 1;
        totalPages = studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getCrfPagesSortedByPageNumber().size();
        participantAddedQuestionIndex = totalPages + 1;
        totalPages = totalPages + getNumberOfParticipantAddedSymptoms();
    }

    private void setDisplayRules() {
        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            CrfPageItem crfPageItem = studyParticipantCrfItem.getCrfPageItem();
            String displayRule = "";
            for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItem.getCrfPageItemDisplayRules()) {
                displayRule = displayRule + "~" + crfPageItemDisplayRule.getProCtcValidValue().getId();
            }
            displayRules.put(crfPageItem.getId(), displayRule);
        }
    }

    private int getNumberOfParticipantAddedSymptoms() {
        HashSet symptoms = new HashSet();
        for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
            if (studyParticipantCrfAddedQuestion.getProCtcQuestion() != null) {
                symptoms.add(studyParticipantCrfAddedQuestion.getProCtcQuestion().getProCtcTerm().getTerm());
            }
            if (studyParticipantCrfAddedQuestion.getMeddraQuestion() != null) {
                symptoms.add(studyParticipantCrfAddedQuestion.getMeddraQuestion().getLowLevelTerm().getMeddraTerm());
            }
        }
        return symptoms.size();
    }

    private void createInstanceOfAddedQuestions() {
        studyParticipantCrfSchedule = genericRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId());
        StudyParticipantCrf studyParticipantCrf = studyParticipantCrfSchedule.getStudyParticipantCrf();
        if (studyParticipantCrf.getStudyParticipantCrfAddedQuestions().size() > 0) {
            if (studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions().size() == 0) {
                for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
                    studyParticipantCrfSchedule.addStudyParticipantCrfScheduleAddedQuestion(studyParticipantCrfAddedQuestion);
                }
            }
        }
    }


    public void deleteQuestions() {
        if (questionsToBeDeleted != null && questionsToBeDeleted.size() > 0) {
            studyParticipantCrfSchedule = genericRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId());
            int myPageNumber = -1;
            for (String id : questionsToBeDeleted) {
                if (!StringUtils.isBlank(id)) {
                    StudyParticipantCrfScheduleAddedQuestion spcsaq = genericRepository.findById(StudyParticipantCrfScheduleAddedQuestion.class, new Integer(id));
                    if (spcsaq != null) {
                        StudyParticipantCrfAddedQuestion s = genericRepository.findById(StudyParticipantCrfAddedQuestion.class, spcsaq.getStudyParticipantCrfAddedQuestionId());
                        if (s != null) {
                            myPageNumber = s.getPageNumber();
                            if (s.getProCtcQuestion().getDisplayOrder() != 1) {
                                return;
                            }
                            studyParticipantCrfSchedule.getStudyParticipantCrf().removeStudyParticipantCrfAddedQuestion(s);
                            genericRepository.delete(s);

                            if (myPageNumber != -1) {
                                List<StudyParticipantCrfAddedQuestion> l = new ArrayList<StudyParticipantCrfAddedQuestion>();
                                for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
                                    if (studyParticipantCrfAddedQuestion.getPageNumber() == myPageNumber) {
                                        l.add(studyParticipantCrfAddedQuestion);
                                    }
                                }

                                for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : l) {
                                    studyParticipantCrfSchedule.getStudyParticipantCrf().removeStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion);
                                    genericRepository.delete(studyParticipantCrfAddedQuestion);
                                }

                                for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
                                    if (studyParticipantCrfAddedQuestion.getPageNumber() > myPageNumber) {
                                        int oldPageNumber = studyParticipantCrfAddedQuestion.getPageNumber();
                                        studyParticipantCrfAddedQuestion.setPageNumber(oldPageNumber - 1);
                                        genericRepository.save(studyParticipantCrfAddedQuestion);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            questionsToBeDeleted.clear();
        }

    }

    public void addQuestionToDeleteList(String questionid) {
        if (!StringUtils.isBlank(questionid)) {
            if (questionid.indexOf('-') != -1) {
                String q = questionid.substring(1);
                questionsToBeDeleted.remove(q);
            } else {
                questionsToBeDeleted.add(questionid);
            }
        }
    }


    public void computeAdditionalSymptoms(ArrayList<ProCtcTerm> proCtcTerms) {
        studyParticipantCrfSchedule = genericRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId());

        for (StudyParticipantCrfItem s : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            proCtcTerms.remove(s.getCrfPageItem().getProCtcQuestion().getProCtcTerm());
        }
        for (StudyParticipantCrfScheduleAddedQuestion s : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
            if (s.getProCtcQuestion() != null) {
                proCtcTerms.remove(s.getProCtcQuestion().getProCtcTerm());
            }
        }
        sortedSymptoms = proCtcTerms;
    }

    private void addProCtcQuestion(ProCtcTerm proCtcTerm, int pageNumber, StudyParticipantCrf studyParticipantCrf, StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        for (ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()) {
            AddParticipantSelectedQuestion(pageNumber, studyParticipantCrf, studyParticipantCrfSchedule, proCtcQuestion);
        }
    }


    private void addMeddraQuestion(LowLevelTerm lowLevelTerm, int pageNumber, StudyParticipantCrf studyParticipantCrf, StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        List<MeddraQuestion> meddraQuestions = lowLevelTerm.getMeddraQuestions();
        MeddraQuestion meddraQuestion;
        if (meddraQuestions.size() > 0) {
            meddraQuestion = meddraQuestions.get(0);
        } else {
            meddraQuestion = createMeddraQuestion(lowLevelTerm);
        }
        AddParticipantSelectedQuestion(pageNumber, studyParticipantCrf, studyParticipantCrfSchedule, meddraQuestion);
    }

    private void AddParticipantSelectedQuestion(int pageNumber, StudyParticipantCrf studyParticipantCrf, StudyParticipantCrfSchedule studyParticipantCrfSchedule, Question question) {
        StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion = studyParticipantCrf.addStudyParticipantCrfAddedQuestion(question, pageNumber);
        genericRepository.save(studyParticipantCrfAddedQuestion);
        StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = studyParticipantCrfSchedule.addStudyParticipantCrfScheduleAddedQuestion(studyParticipantCrfAddedQuestion);
        genericRepository.save(studyParticipantCrfScheduleAddedQuestion);
    }

    private MeddraQuestion createMeddraQuestion(LowLevelTerm lowLevelTerm) {
        MeddraQuestion meddraQuestion = new MeddraQuestion();
        MeddraValidValue meddraValidValue = new MeddraValidValue();
        meddraQuestion.setLowLevelTerm(lowLevelTerm);
        meddraQuestion.setProCtcQuestionType(ProCtcQuestionType.PRESENT);
        meddraValidValue.setValue("Yes");
        meddraValidValue.setDisplayOrder(0);
        MeddraValidValue meddraValidValue1 = new MeddraValidValue();
        meddraValidValue1.setValue("No");
        meddraValidValue1.setDisplayOrder(1);
        meddraQuestion.addValidValue(meddraValidValue);
        meddraQuestion.addValidValue(meddraValidValue1);
        meddraQuestion.setQuestionText("Did you have any " + lowLevelTerm.getMeddraTerm() + "?");
        meddraQuestion.setDisplayOrder(0);
        genericRepository.save(meddraQuestion);
        return meddraQuestion;
    }

    public void addParticipantAddedQuestions(String[] selectedSymptoms) {
        StudyParticipantCrf studyParticipantCrf = studyParticipantCrfSchedule.getStudyParticipantCrf();
        for (String symptom : selectedSymptoms) {
            ProCtcTerm proCtcTerm = findProCtcTermBySymptom(symptom);
            if (proCtcTerm != null) {
                addProCtcQuestion(proCtcTerm, totalPages, studyParticipantCrf, studyParticipantCrfSchedule);
            } else {
                LowLevelTerm lowLevelTerm = findMeddraTerm(symptom);
                CtcTerm ctcTerm = findCtcTerm(lowLevelTerm.getMeddraCode());
                if (ctcTerm == null) {
                    addMeddraQuestion(lowLevelTerm, totalPages, studyParticipantCrf, studyParticipantCrfSchedule);
                } else {
                    proCtcTerm = findProCtcTermByCtcTermId(ctcTerm.getId());
                    if (proCtcTerm == null) {
                        addMeddraQuestion(lowLevelTerm, totalPages, studyParticipantCrf, studyParticipantCrfSchedule);
                    } else {
                        addProCtcQuestion(proCtcTerm, totalPages, studyParticipantCrf, studyParticipantCrfSchedule);
                    }
                }
            }
            totalPages++;
        }
    }

    private ProCtcTerm findProCtcTermBySymptom(String symptom) {
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        proCtcTermQuery.filterByTerm(symptom);
        return genericRepository.findSingle(proCtcTermQuery);
    }

    private ProCtcTerm findProCtcTermByCtcTermId(Integer ctcTermId) {
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        proCtcTermQuery.filterByCtcTermId(ctcTermId);
        return genericRepository.findSingle(proCtcTermQuery);
    }

    private LowLevelTerm findMeddraTerm(String symptom) {
        MeddraQuery meddraQuery = new MeddraQuery();
        meddraQuery.filterByMeddraTerm(symptom);
        return genericRepository.findSingle(meddraQuery);
    }

    private CtcTerm findCtcTerm(String ctepCode) {
        CtcQuery ctcQuery = new CtcQuery();
        ctcQuery.filterByCtepCode(ctepCode);
        return genericRepository.findSingle(ctcQuery);

    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    public Hashtable<Integer, String> getDisplayRules() {
        return displayRules;
    }


    public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
        return studyParticipantCrfSchedule;
    }


    public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
        createInstanceOfAddedQuestions();
        genericRepository.save(studyParticipantCrfSchedule);
    }

    public int getCurrentPageIndex() {
        if (direction.equals("back")) {
            direction = "";
            currentPageIndex--;
        }
        if (direction.equals("continue")) {
            direction = "";
            currentPageIndex++;
        }

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


    public String getFlashMessage() {
        return flashMessage;
    }


    public void setFlashMessage(String flashMessage) {
        this.flashMessage = flashMessage;
    }


    public ArrayList<ProCtcTerm> getSortedSymptoms() {
        return sortedSymptoms;
    }


    public ArrayList<String> getDisplaySymptoms() {

        ArrayList<ProCtcTerm> sortedList = getSortedSymptoms();
        ArrayList<String> displayList = new ArrayList<String>();
        int i = 0;
        for (ProCtcTerm symptom : sortedList) {
            if (symptom.getTerm().length() < 35) {
                displayList.add(symptom.getTerm());
                i++;
            }
            if (i > 20) {
                break;
            }
        }

        return displayList;

    }

    public String getDeletedQuestions() {
        return deletedQuestions;
    }

    public void setDeletedQuestions(String deletedQuestions) {
        this.deletedQuestions = deletedQuestions;
    }

    public int getParticipantAddedQuestionIndex() {
        return participantAddedQuestionIndex;
    }


    public void setParticipantAddedQuestionIndex(int participantAddedQuestionIndex) {
        this.participantAddedQuestionIndex = participantAddedQuestionIndex;
    }

    public Set<String> getQuestionsToBeDeleted() {
        return questionsToBeDeleted;
    }

    public void setQuestionsToBeDeleted(Set<String> questionsToBeDeleted) {
        this.questionsToBeDeleted = questionsToBeDeleted;
    }

}