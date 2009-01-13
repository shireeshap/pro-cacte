package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Finder repository is used for finding objects
 *
 * @author Vinay Kumar
 */

@org.springframework.stereotype.Repository
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class FinderRepository {
    protected final Log logger = LogFactory.getLog(getClass());

    private GenericRepository genericRepository;

    public <T extends Persistable> T findById(Class<T> classArg, Integer id) {
        return genericRepository.findById(classArg, id);
    }

    public <T extends Persistable> List<? extends Persistable> find(Query query) {
        return genericRepository.find(query);
    }

    @Required
    public void setGenericRepository(final GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }


    public CRF findAndInitializeCrf(final Integer crfId) {
        CRF crf = findById(CRF.class, crfId);
        if (crf != null) {
            for (StudyParticipantCrf studyParticipantCrf : crf.getStudyParticipantCrfs()) {
                studyParticipantCrf.getCrf();
            }
        }

        List<CRFPage> crfPageList = crf.getCrfPages();
        for (CRFPage crfPage : crfPageList) {
            crfPage.getDescription();
        }
        return crf;


    }


    public ProCtcQuestion findAndInitializeProCtcQuestion(final Integer questionId) {
        ProCtcQuestion proCtcQuestion = findById(ProCtcQuestion.class, questionId);
        if (proCtcQuestion != null) {
            for (ProCtcValidValue validValue : proCtcQuestion.getValidValues()) {
                validValue.getValue();
            }
        }
        return proCtcQuestion;


    }
}
