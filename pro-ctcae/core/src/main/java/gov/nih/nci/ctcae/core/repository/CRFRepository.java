package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CRFRepository extends AbstractRepository<CRF, CRFQuery> {

    private StudyRepository studyRepository;

    @Override
    protected Class<CRF> getPersistableClass() {
        return CRF.class;

    }

    @Override
    public CRF findById(Integer crfId) {
        CRF crf = genericRepository.findById(CRF.class, crfId);
        if (crf != null) {
            for (StudyParticipantCrf studyParticipantCrf : crf.getStudyParticipantCrfs()) {
                studyParticipantCrf.getCrf();
            }
        }

        List<CRFPage> crfPageList = crf.getCrfPages();
        for (CRFPage crfPage : crfPageList) {
            crfPage.getDescription();
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                crfPageItem.getDisplayOrder();
                for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItem.getCrfPageItemDisplayRules()) {
                    crfPageItemDisplayRule.getProCtcValidValue();
                }
            }
        }
        return crf;


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateStatusToReleased(CRF crf) {


        if (crf != null) {
            crf.setStatus(CrfStatus.RELEASED);

            Study study = studyRepository.findById(crf.getStudy().getId());

            for (StudySite studySite : study.getStudySites()) {
                for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                    StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
                    studyParticipantCrf.setStudyParticipantAssignment(studyParticipantAssignment);
                    crf.addStudyParticipantCrf(studyParticipantCrf);
                }
            }
        }
        save(crf);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF save(CRF crf) {
        CRF tmp = null;
        if (crf.getParentVersionId() != null) {
            tmp = findById(crf.getParentVersionId());
            while (tmp.getParentVersionId() != null) {
                tmp = findById(tmp.getParentVersionId());
            }
        }

        if (tmp != null && (tmp.getId() != crf.getId())) {
            if (!tmp.getTitle().equals(crf.getTitle())) {
                throw (new CtcAeSystemException("You can not update the title if crf is versioned"));
            }
        }

//        if (crf.getOldTitle() != null) {
//            if ((crf.getParentVersionId() != null || crf.getNextVersionId() != null) && !crf.getOldTitle().equals(crf.getTitle())) {
//                throw (new CtcAeSystemException("You can not update the title if crf is versioned"));
//            }
//
//        }
        return super.save(crf);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

}
