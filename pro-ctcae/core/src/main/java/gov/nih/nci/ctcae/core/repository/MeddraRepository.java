package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author Mehul Gulati
 * Date: Jun 2, 2009
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
}
