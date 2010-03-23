package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author Mehul Gulati
 *         Date: Jun 2, 2009
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class MeddraRepository implements Repository<LowLevelTerm, MeddraQuery> {

    private GenericRepository genericRepository;

    public LowLevelTerm findById(Integer id) {
        return genericRepository.findById(LowLevelTerm.class, id);
    }

    public LowLevelTerm save(LowLevelTerm lowLevelTerm) {
        return genericRepository.save(lowLevelTerm);
    }

    public Collection<LowLevelTerm> find(MeddraQuery query) {
        return genericRepository.find(query);
    }

    public void delete(LowLevelTerm lowLevelTerm) {
        throw new CtcAeSystemException("method not supported");
    }

    public LowLevelTerm findSingle(MeddraQuery query) {
        return genericRepository.findSingle(query);
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    public List<CtcTerm> findCtcTermForMeddraTerm(String meddraTerm) {
        MeddraQuery meddraQuery = new MeddraQuery();
        meddraQuery.filterByMeddraTerm(meddraTerm);
        LowLevelTerm lowLevelTerm = genericRepository.findSingle(meddraQuery);
        String meddraCode = lowLevelTerm.getMeddraCode();
        if (meddraCode == null) {
            return null;
        }
        List<CtcTerm> ctcTerms = findCtcTermByMeddraCode(meddraCode);
        if (ctcTerms == null || ctcTerms.size() == 0) {
            Integer meddraPtId = lowLevelTerm.getMeddraPtId();
            meddraQuery = new MeddraQuery();
            meddraQuery.filterByMeddraPtId(meddraPtId);
            List<LowLevelTerm> lowLevelTerms = genericRepository.find(meddraQuery);
            for (LowLevelTerm llt : lowLevelTerms) {
                ctcTerms = findCtcTermByMeddraCode(llt.getMeddraCode());
                if (ctcTerms != null && ctcTerms.size() > 0) {
                    return ctcTerms;
                }
            }
        }
        return ctcTerms;
    }

    private List<CtcTerm> findCtcTermByMeddraCode(String meddraCode) {
        CtcQuery ctcQuery = new CtcQuery(true);
        ctcQuery.filterByCtepCode(meddraCode);
        List<CtcTerm> ctcTerm = genericRepository.find(ctcQuery);
        return ctcTerm;
    }
}
