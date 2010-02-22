package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author Mehul Gulati
 *         Date: Jun 2, 2009
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
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

    public CtcTerm findCtcTermForMeddraTerm(String meddraTerm) {
        MeddraQuery meddraQuery = new MeddraQuery();
        meddraQuery.filterByMeddraTerm(meddraTerm);
        LowLevelTerm lowLevelTerm = genericRepository.findSingle(meddraQuery);
        String meddraCode = lowLevelTerm.getMeddraCode();
        if (meddraCode == null) {
            return null;
        }
        CtcTerm ctcTerm = findCtcTermByMeddraCode(meddraCode);
        if (ctcTerm == null) {
            Integer meddraPtId = lowLevelTerm.getMeddraPtId();
            meddraQuery = new MeddraQuery();
            meddraQuery.filterByMeddraPtId(meddraPtId);
            List<LowLevelTerm> lowLevelTerms = genericRepository.find(meddraQuery);
            for (LowLevelTerm llt : lowLevelTerms) {
                ctcTerm = findCtcTermByMeddraCode(llt.getMeddraCode());
                if (ctcTerm != null) {
                    return ctcTerm;
                }
            }
        }
        return ctcTerm;
    }

    private CtcTerm findCtcTermByMeddraCode(String meddraCode) {
        CtcQuery ctcQuery = new CtcQuery(true);
        ctcQuery.filterByCtepCode(meddraCode);
        CtcTerm ctcTerm = genericRepository.findSingle(ctcQuery);
        return ctcTerm;
    }
}
