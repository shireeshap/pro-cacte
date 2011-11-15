package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

//
/**
 * The Class ProCtcQuestionRepository.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)

public class ProCtcValidValueRepository implements Repository<ProCtcValidValue, ProCtcQuestionQuery> {

    private GenericRepository genericRepository;


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    public ProCtcValidValue findById(Integer id) {
        return genericRepository.findById(ProCtcValidValue.class, id);


    }

    public ProCtcValidValue save(ProCtcValidValue proCtcValidValue) {
        return null;


    }

    public void delete(ProCtcValidValue proCtcValidValue) {


    }

    public Collection<ProCtcValidValue> find(ProCtcQuestionQuery query) {
        return null;


    }

    public ProCtcValidValue findSingle(ProCtcQuestionQuery query) {
        return null;


    }
}