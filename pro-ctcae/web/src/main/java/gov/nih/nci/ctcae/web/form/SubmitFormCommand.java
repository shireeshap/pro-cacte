package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;


/**
 * @author Harsh Agarwal
 * @crated Nov 12, 2008
 */
public class SubmitFormCommand implements Serializable {

    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    private Hashtable<Integer, String> displayRules = new Hashtable<Integer, String>();
    private FinderRepository finderRepository;
    private GenericRepository genericRepository;
    private int currentPageIndex;
    private int totalPages;
    private String direction = "";
    private String flashMessage;
    private List<ProCtcQuestion> proCtcQuestions;
    private boolean hasParticipantAddedQuestions = false;
    private String deletedQuestions;
    private int participantAddedQuestionIndex = 0;
    private String pageHeader = "";

    public void initialize() {

        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            CrfPageItem crfPageItem = studyParticipantCrfItem.getCrfPageItem();

            String displayRule = "";
            for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItem.getCrfPageItemDisplayRules()) {
                Integer validValueId = crfPageItemDisplayRule.getProCtcValidValue().getId();
                displayRule = displayRule + "~" + validValueId;
            }
            displayRules.put(crfPageItem.getId(), displayRule);
        }
        currentPageIndex = 1;
        totalPages = studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getCrfPagesSortedByPageNumber().size();
        participantAddedQuestionIndex = totalPages + 1;
        if (studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().size() > 0) {
            hasParticipantAddedQuestions = true;
            HashSet symptoms = new HashSet();
            for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
                symptoms.add(studyParticipantCrfAddedQuestion.getProCtcQuestion().getProCtcTerm().getTerm());
            }
            totalPages = totalPages + symptoms.size();
        }
    }

    private StudyParticipantCrfSchedule findLatestCrfAndCreateSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        CRF originalCrf = studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf();
        StudyParticipantCrf originalStudyParticipantCrf = studyParticipantCrfSchedule.getStudyParticipantCrf();
        if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED)) {
            Date today = new Date();
            CRF latestEffectiveCrf = originalCrf;
            Integer nextVersionId = latestEffectiveCrf.getNextVersionId();
            while (nextVersionId != null) {
                CRF nextCrf = finderRepository.findById(CRF.class, nextVersionId);
                if (nextCrf.getStatus().equals(CrfStatus.RELEASED) && today.after(nextCrf.getEffectiveStartDate())) {
                    latestEffectiveCrf = nextCrf;
                }
                nextVersionId = nextCrf.getNextVersionId();
            }

            if (!originalCrf.getId().equals(latestEffectiveCrf.getId())) {
                StudyParticipantAssignment studyParticipantAssignment = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment();
                for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                    if (studyParticipantCrf.getCrf().getId().equals(latestEffectiveCrf.getId())) {
                        int i = 1;
                        Hashtable<String, Integer> symptomPage = new Hashtable<String, Integer>();
                        StudyParticipantCrfSchedule newSchedule = new StudyParticipantCrfSchedule();
                        newSchedule.setStartDate(studyParticipantCrfSchedule.getStartDate());
                        newSchedule.setDueDate(studyParticipantCrfSchedule.getDueDate());
                        newSchedule.setStatus(CrfStatus.SCHEDULED);
                        studyParticipantCrf.addStudyParticipantCrfSchedule(newSchedule, latestEffectiveCrf);

                        for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : originalStudyParticipantCrf.getStudyParticipantCrfAddedQuestions()) {
                            boolean isAlreadyPresent = false;
                            for (CRFPage crfPage : latestEffectiveCrf.getCrfPagesSortedByPageNumber()) {
                                for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                                    if (crfPageItem.getProCtcQuestion().getId().equals(studyParticipantCrfAddedQuestion.getProCtcQuestion().getId())) {
                                        isAlreadyPresent = true;
                                    }
                                }
                            }
                            if (!isAlreadyPresent) {
                                StudyParticipantCrfAddedQuestion newStudyParticipantCrfAddedQuestion = studyParticipantCrfAddedQuestion.getCopy();
                                int myPageNumber = 0;
                                if (symptomPage.containsKey(studyParticipantCrfAddedQuestion.getProCtcQuestion().getProCtcTerm().getTerm())) {
                                    myPageNumber = symptomPage.get(studyParticipantCrfAddedQuestion.getProCtcQuestion().getProCtcTerm().getTerm());
                                } else {
                                    myPageNumber = studyParticipantCrf.getCrf().getCrfPagesSortedByPageNumber().size() - 1 + i;
                                    symptomPage.put(studyParticipantCrfAddedQuestion.getProCtcQuestion().getProCtcTerm().getTerm(), myPageNumber);
                                }
                                newStudyParticipantCrfAddedQuestion.setPageNumber(myPageNumber);
                                i++;
                                studyParticipantCrf.addStudyParticipantCrfAddedQuestion(newStudyParticipantCrfAddedQuestion);
                            }
                        }

                        genericRepository.save(newSchedule);
                        genericRepository.save(studyParticipantCrf);
                        genericRepository.delete(studyParticipantCrfSchedule);
                        studyParticipantCrfSchedule = newSchedule;
                        break;
                    }
                }
            }
        }
        if (studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions().size() > 0) {
            if (studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions().size() == 0) {
                for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
                    studyParticipantCrfSchedule.addStudyParticipantCrfScheduleAddedQuestion(new StudyParticipantCrfScheduleAddedQuestion());
                }
            }
        }
        return studyParticipantCrfSchedule;
    }

    public Hashtable<Integer, String> getDisplayRules() {
        return displayRules;
    }

    public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
        return studyParticipantCrfSchedule;
    }

    public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        this.studyParticipantCrfSchedule = findLatestCrfAndCreateSchedule(studyParticipantCrfSchedule);
    }

    public int getCurrentPageIndex() {
        if (direction.equals("back")) {
            currentPageIndex--;
        }
        if (direction.equals("continue")) {
            currentPageIndex++;
        }

        direction = "";
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

    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    public String getFlashMessage() {
        return flashMessage;
    }

    public void setFlashMessage(String flashMessage) {
        this.flashMessage = flashMessage;
    }

    public Hashtable<String, List<ProCtcQuestion>> getArrangedQuestions() {
        Hashtable<String, List<ProCtcQuestion>> arrangedQuestions = new Hashtable<String, List<ProCtcQuestion>>();
        List<ProCtcQuestion> l;
        ArrayList<Integer> includedQuestionIds = new ArrayList<Integer>();
        studyParticipantCrfSchedule = finderRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId());

        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            includedQuestionIds.add(studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getId());
        }
        if (hasParticipantAddedQuestions) {
            for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
                includedQuestionIds.add(studyParticipantCrfAddedQuestion.getProCtcQuestion().getId());
            }
        }

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

    public ArrayList<String> getSortedSymptoms(){
        Hashtable<String, List<ProCtcQuestion>> arrangedQuestions = getArrangedQuestions();
        ArrayList<String> sortedList = new ArrayList(arrangedQuestions.keySet());
        Collections.sort(sortedList);

        return sortedList;

    }
    public void setProCtcQuestions(List<ProCtcQuestion> proCtcQuestions) {
        this.proCtcQuestions = proCtcQuestions;
    }

    public List<ProCtcQuestion> getProCtcQuestions() {
        return proCtcQuestions;
    }

    public boolean isHasParticipantAddedQuestions() {
        return hasParticipantAddedQuestions;
    }

    public void setHasParticipantAddedQuestions(boolean hasParticipantAddedQuestions) {
        this.hasParticipantAddedQuestions = hasParticipantAddedQuestions;
    }

    public String getDeletedQuestions() {
        return deletedQuestions;
    }

    public void setDeletedQuestions(String deletedQuestions) {
        this.deletedQuestions = deletedQuestions;
    }

    public void deleteQuestions(String questions) {

        if (!StringUtils.isBlank(questions)) {

            studyParticipantCrfSchedule = finderRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId());
            StringTokenizer st = new StringTokenizer(questions, ",");
            int myPageNumber = -1;
            while (st.hasMoreTokens()) {

                String id = st.nextToken();
                StudyParticipantCrfAddedQuestion s = finderRepository.findById(StudyParticipantCrfAddedQuestion.class, new Integer(id));
                myPageNumber = s.getPageNumber();
                studyParticipantCrfSchedule.getStudyParticipantCrf().removeStudyParticipantCrfAddedQuestion(s);
                genericRepository.delete(s);
            }

            if (myPageNumber != -1) {
                for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
                    if (studyParticipantCrfAddedQuestion.getPageNumber() == myPageNumber) {
                        return;
                    }
                }
                for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
                    if (studyParticipantCrfAddedQuestion.getPageNumber() > myPageNumber) {
                        int oldPageNumber = studyParticipantCrfAddedQuestion.getPageNumber();
                        studyParticipantCrfAddedQuestion.setPageNumber(oldPageNumber - 1);
                        genericRepository.save(studyParticipantCrfAddedQuestion);
                    }
                }
                totalPages = totalPages - 1;
                currentPageIndex = currentPageIndex - 1;
            }
        }
    }

    public int getParticipantAddedQuestionIndex() {
        return participantAddedQuestionIndex;
    }

    public void setParticipantAddedQuestionIndex(int participantAddedQuestionIndex) {
        this.participantAddedQuestionIndex = participantAddedQuestionIndex;
    }

    public String getPageHeader() {
        String symptom = "";

        if (currentPageIndex <= totalPages) {
            for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                if (studyParticipantCrfItem.getCrfPageItem().getCrfPage().getPageNumber() == currentPageIndex - 1) {
                    pageHeader = studyParticipantCrfItem.getCrfPageItem().getCrfPage().getInstructions();
                }
            }
        }
        return pageHeader;
    }


}