package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.MeddraValidValue;
import gov.nih.nci.ctcae.core.query.Query;
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

public class MeddraValidValueRepository implements Repository<MeddraValidValue, Query> {

    private GenericRepository genericRepository;


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }                                                                                 

    public MeddraValidValue findById(Integer id) {
        return genericRepository.findById(MeddraValidValue.class, id);


    }

    public MeddraValidValue save(MeddraValidValue meddraValidValue) {
        return null;


    }

    public void delete(MeddraValidValue meddraValidValue) {


    }

    public Collection<MeddraValidValue> find(Query query) {
        return null;


    }

    public MeddraValidValue findSingle(Query query) {
        return null;


    }
}