package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;


//
/**
 * The Class SubmitFormCommand.
 *
 * @author Harsh Agarwal
 * @crated Nov 12, 2008
 */
public class SubmitFormCommand implements Serializable {

    /**
     * The study participant crf schedule.
     */
    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

    /**
     * The display rules.
     */
    private Hashtable<Integer, String> displayRules = new Hashtable<Integer, String>();

    /**
     * The finder repository.
     */
    private FinderRepository finderRepository;

    /**
     * The generic repository.
     */
    private GenericRepository genericRepository;

    /**
     * The current page index.
     */
    private int currentPageIndex;

    /**
     * The total pages.
     */
    private int totalPages;

    /**
     * The direction.
     */
    private String direction = "";

    /**
     * The flash message.
     */
    private String flashMessage;

    /**
     * The pro ctc questions.
     */
    private List<ProCtcQuestion> proCtcQuestions;

    /**
     * The has participant added questions.
     */
    private boolean hasParticipantAddedQuestions = false;

    /**
     * The deleted questions.
     */
    private String deletedQuestions;

    /**
     * The participant added question index.
     */
    private int participantAddedQuestionIndex = 0;

    /**
     * The page header.
     */
    private String pageHeader = "";

    /**
     * Initialize.
     */
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

    /**
     * Find latest crf and create schedule.
     *
     * @param studyParticipantCrfSchedule the study participant crf schedule
     * @return the study participant crf schedule
     */
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

    /**
     * Gets the display rules.
     *
     * @return the display rules
     */
    public Hashtable<Integer, String> getDisplayRules() {
        return displayRules;
    }

    /**
     * Gets the study participant crf schedule.
     *
     * @return the study participant crf schedule
     */
    public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
        return studyParticipantCrfSchedule;
    }

    /**
     * Sets the study participant crf schedule.
     *
     * @param studyParticipantCrfSchedule the new study participant crf schedule
     */
    public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        this.studyParticipantCrfSchedule = findLatestCrfAndCreateSchedule(studyParticipantCrfSchedule);
    }

    /**
     * Gets the current page index.
     *
     * @return the current page index
     */
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

    /**
     * Sets the current page index.
     *
     * @param currentPageIndex the new current page index
     */
    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    /**
     * Gets the total pages.
     *
     * @return the total pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Sets the total pages.
     *
     * @param totalPages the new total pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Gets the direction.
     *
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the direction.
     *
     * @param direction the new direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Sets the finder repository.
     *
     * @param finderRepository the new finder repository
     */
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    /**
     * Sets the generic repository.
     *
     * @param genericRepository the new generic repository
     */
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    /**
     * Gets the flash message.
     *
     * @return the flash message
     */
    public String getFlashMessage() {
        return flashMessage;
    }

    /**
     * Sets the flash message.
     *
     * @param flashMessage the new flash message
     */
    public void setFlashMessage(String flashMessage) {
        this.flashMessage = flashMessage;
    }

    /**
     * Gets the arranged questions.
     *
     * @return the arranged questions
     */
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

    /**
     * Gets the sorted symptoms.
     *
     * @return the sorted symptoms
     */
    public ArrayList<String> getSortedSymptoms() {
        Hashtable<String, List<ProCtcQuestion>> arrangedQuestions = getArrangedQuestions();
        ArrayList<String> sortedList = new ArrayList(arrangedQuestions.keySet());
        Collections.sort(sortedList);

        return sortedList;

    }

    /**
     * Sets the pro ctc questions.
     *
     * @param proCtcQuestions the new pro ctc questions
     */
    public void setProCtcQuestions(List<ProCtcQuestion> proCtcQuestions) {
        this.proCtcQuestions = proCtcQuestions;
    }

    /**
     * Gets the pro ctc questions.
     *
     * @return the pro ctc questions
     */
    public List<ProCtcQuestion> getProCtcQuestions() {
        return proCtcQuestions;
    }

    /**
     * Checks if is checks for participant added questions.
     *
     * @return true, if is checks for participant added questions
     */
    public boolean isHasParticipantAddedQuestions() {
        return hasParticipantAddedQuestions;
    }

    /**
     * Sets the checks for participant added questions.
     *
     * @param hasParticipantAddedQuestions the new checks for participant added questions
     */
    public void setHasParticipantAddedQuestions(boolean hasParticipantAddedQuestions) {
        this.hasParticipantAddedQuestions = hasParticipantAddedQuestions;
    }

    /**
     * Gets the deleted questions.
     *
     * @return the deleted questions
     */
    public String getDeletedQuestions() {
        return deletedQuestions;
    }

    /**
     * Sets the deleted questions.
     *
     * @param deletedQuestions the new deleted questions
     */
    public void setDeletedQuestions(String deletedQuestions) {
        this.deletedQuestions = deletedQuestions;
    }

    /**
     * Delete questions.
     *
     * @param questions the questions
     */
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

    /**
     * Gets the participant added question index.
     *
     * @return the participant added question index
     */
    public int getParticipantAddedQuestionIndex() {
        return participantAddedQuestionIndex;
    }

    /**
     * Sets the participant added question index.
     *
     * @param participantAddedQuestionIndex the new participant added question index
     */
    public void setParticipantAddedQuestionIndex(int participantAddedQuestionIndex) {
        this.participantAddedQuestionIndex = participantAddedQuestionIndex;
    }

    /**
     * Gets the page header.
     *
     * @return the page header
     */
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