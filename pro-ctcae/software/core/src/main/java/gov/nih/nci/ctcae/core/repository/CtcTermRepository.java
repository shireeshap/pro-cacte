package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.Repository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CtcTermRepository implements Repository<CtcTerm, CtcQuery> {

    private GenericRepository genericRepository;

    public CtcTerm findById(Integer id) {


        return genericRepository.findById(CtcTerm.class, id);


    }

    public CtcTerm save(CtcTerm ctcTerm) {

        throw new CtcAeSystemException("save is not supported for CtcTerm");


    }

    public void delete(CtcTerm t) {
        throw new CtcAeSystemException("Delete is not supported for ProCtcQuestion");
    }

    public List<CtcTerm> find(CtcQuery query) {
        return genericRepository.find(query);


    }

    public CtcTerm findSingle(CtcQuery query) {
        return genericRepository.findSingle(query);


    }


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}