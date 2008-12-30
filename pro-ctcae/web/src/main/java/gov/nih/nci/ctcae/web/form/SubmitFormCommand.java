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

    StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    Hashtable<Integer, String> displayRules = new Hashtable<Integer, String>();
    Hashtable<Integer, String> reverseDisplayRules = new Hashtable<Integer, String>();
    FinderRepository finderRepository;
    GenericRepository genericRepository;


    int nextQuestionIndex;

    public void initialize() {

        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            CrfPageItem crfItem = studyParticipantCrfItem.getCrfPageItem();

            String displayRule = "";
            for (CrfItemDisplayRule crfItemDisplayRule : crfItem.getCrfItemDisplayRules()) {
                Integer validValueId = crfItemDisplayRule.getRequiredObjectId();
                displayRule = displayRule + "~" + validValueId;

                if (reverseDisplayRules.containsKey(validValueId)) {
                    String crfItemIds = reverseDisplayRules.get(validValueId);
                    crfItemIds = crfItemIds + "~" + crfItem.getId();
                    reverseDisplayRules.put(validValueId, crfItemIds);
                } else {
                    reverseDisplayRules.put(validValueId, "" + crfItem.getId());
                }

            }
            displayRules.put(crfItem.getId(), displayRule);
        }
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
                        studyParticipantCrf.addStudyParticipantCrfSchedule(newSchedule);
                        studyParticipantCrfSchedule.getStudyParticipantCrf().removeCrfSchedule(studyParticipantCrfSchedule);
                        genericRepository.save(studyParticipantAssignment);
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

    public int getNextQuestionIndex() {
        return nextQuestionIndex;
    }

    public void setNextQuestionIndex(int nextQuestionIndex) {
        this.nextQuestionIndex = nextQuestionIndex;
    }

    public Hashtable<Integer, String> getReverseDisplayRules() {
        return reverseDisplayRules;
    }

    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

}