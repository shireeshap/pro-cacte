package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;

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

    public void initialize() {

        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            CrfPageItem crfPageItem = studyParticipantCrfItem.getCrfPageItem();

            String displayRule = "";
            for (CrfItemDisplayRule crfItemDisplayRule : crfPageItem.getCrfItemDisplayRules()) {
                Integer validValueId = crfItemDisplayRule.getRequiredObjectId();
                displayRule = displayRule + "~" + validValueId;
            }
            displayRules.put(crfPageItem.getId(), displayRule);
        }
        currentPageIndex = 1;
        totalPages = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyCrf().getCrf().getCrfPages().size();
    }

    private StudyParticipantCrfSchedule findLatestCrfAndCreateSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        CRF originalCrf = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyCrf().getCrf();
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
                    if (studyParticipantCrf.getStudyCrf().getCrf().getId().equals(latestEffectiveCrf.getId())) {
                        StudyParticipantCrfSchedule newSchedule = new StudyParticipantCrfSchedule();
                        newSchedule.setStartDate(studyParticipantCrfSchedule.getStartDate());
                        newSchedule.setDueDate(studyParticipantCrfSchedule.getDueDate());
                        newSchedule.setStatus(CrfStatus.SCHEDULED);
                        studyParticipantCrf.addStudyParticipantCrfSchedule(newSchedule, latestEffectiveCrf);

                        genericRepository.save(newSchedule);
                        genericRepository.delete(studyParticipantCrfSchedule);
                        return newSchedule;
                    }
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
}