package gov.nih.nci.ctcae.core.repository.secured;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.Repository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

//
/**
 * The Class CRFRepository.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CRFRepository implements Repository<CRF, CRFQuery> {

    /**
     * The study repository.
     */
    private StudyRepository studyRepository;

    private GenericRepository genericRepository;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF findById(Integer crfId) {
        CRF crf = genericRepository.findById(CRF.class, crfId);
        initializeCollections(crf);
        return crf;
    }

    /**
     * Update status to released.
     *
     * @param crf the crf
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF updateStatusToReleased(CRF crf) throws ParseException {
        crf.setStatus(CrfStatus.RELEASED);
        Study study = studyRepository.findById(crf.getStudy().getId());
        for (StudySite studySite : study.getStudySites()) {
            for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
                studyParticipantCrf.setStartDate(crf.getEffectiveStartDate());
                crf.addStudyParticipantCrf(studyParticipantCrf);
                studyParticipantCrf.setStudyParticipantAssignment(studyParticipantAssignment);
            }
        }
        CRF savedCrf = save(crf);
        generateSchedules(savedCrf);
        return save(savedCrf);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void generateSchedules(CRF crf) throws ParseException {
        for (StudyParticipantCrf studyParticipantCrf : crf.getStudyParticipantCrfs()) {
            studyParticipantCrf.generateSchedules();
        }
    }


    /* (non-Javadoc)
    * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#save(gov.nih.nci.ctcae.core.domain.Persistable)
    */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF save(CRF crf) {
        CRF tmp = null;
        if (crf.getParentVersionId() != null) {
            tmp = findById(crf.getParentVersionId());
            while (tmp.getParentVersionId() != null) {
                tmp = findById(tmp.getParentVersionId());
            }
        }

        if (tmp != null && (!tmp.getId().equals(crf.getId()))) {
            if (!tmp.getTitle().equals(crf.getTitle())) {
                throw (new CtcAeSystemException("You can not update the title if crf is versioned"));
            }
        }

        crf = genericRepository.save(crf);
        initializeCollections(crf);
        return crf;

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(CRF crf) {
        if (crf != null) {
            if (!crf.isReleased()) {
                genericRepository.delete(crf);
            } else {
                throw new CtcAeSystemException("Released CRF can not be deleted");
            }
        }
    }

    public Collection<CRF> find(CRFQuery query) {
        return genericRepository.find(query);


    }

    public CRF findSingle(CRFQuery query) {
        return genericRepository.findSingle(query);


    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF versionCrf(CRF crf) {
        crf = findById(crf.getId());
        Integer parentVersionId = crf.getId();
        String newVersion = "" + (new Float(crf.getCrfVersion()) + 1);
        CRF copiedCRF = crf.copy();
        copiedCRF.setTitle(crf.getTitle());
        copiedCRF.setCrfVersion(newVersion);
        copiedCRF.setParentVersionId(parentVersionId);
        genericRepository.save(copiedCRF);
        Integer nextVersionId = copiedCRF.getId();
        crf.setNextVersionId(nextVersionId);
        crf = genericRepository.save(crf);
        return copiedCRF;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF copy(CRF crf) {
        CRF copiedCrf = crf.copy();
        copiedCrf.setCrfVersion("1.0");
        return genericRepository.save(copiedCrf);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void initializeCollections(CRF crf) {
        for (StudyParticipantCrf studyParticipantCrf : crf.getStudyParticipantCrfs()) {
            studyParticipantCrf.getCrf();
        }

        List<CRFPage> crfPageList = crf.getCrfPagesSortedByPageNumber();
        for (CRFPage crfPage : crfPageList) {
            crfPage.getDescription();
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                crfPageItem.getDisplayOrder();
                for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItem.getCrfPageItemDisplayRules()) {
                    crfPageItemDisplayRule.getProCtcValidValue();
                }
            }
        }

        List<CrfPageItem> allCrfPageItems = crf.getAllCrfPageItems();
        for (CrfPageItem crfPageItem : allCrfPageItems) {
            crfPageItem.getProCtcQuestion().getProCtcTerm().getProCtcQuestions();
        }


        for (CRFCalendar crfCalendar : crf.getCrfCalendars()) {
            crfCalendar.getDueDateAmount();
        }
        for (CRFCycleDefinition crfCycleDefinition : crf.getCrfCycleDefinitions()) {
            crfCycleDefinition.getCrfCycles();
        }
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    /**
     * Sets the study repository.
     *
     * @param studyRepository the new study repository
     */
    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }


}
