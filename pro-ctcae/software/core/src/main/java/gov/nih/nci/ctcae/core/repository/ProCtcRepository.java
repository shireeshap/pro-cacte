package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

//
/**
 * The Class ProCtcRepository.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class ProCtcRepository implements Repository<ProCtc, ProCtcQuery> {

    private GenericRepository genericRepository;

    public ProCtc findById(Integer id) {
        return genericRepository.findById(ProCtc.class, id);


    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)

    public ProCtc save(ProCtc proCtc) {
        return genericRepository.save(proCtc);


    }

    public void delete(ProCtc proCtc) {
        throw new CtcAeSystemException("method not supported");

    }

    public Collection<ProCtc> find(ProCtcQuery query) {
        return genericRepository.find(query);


    }

    public ProCtc findSingle(ProCtcQuery query) {
        return genericRepository.findSingle(query);


    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
