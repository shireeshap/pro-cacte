package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.IvrsSchedule;
import gov.nih.nci.ctcae.core.query.IvrsScheduleQuery;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IvrsScheduleRepository.
 *
 * @author Vinay Gangoli
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class IvrsScheduleRepository implements Repository<IvrsSchedule, IvrsScheduleQuery> {
    
	private GenericRepository genericRepository;

    public IvrsSchedule findById(Integer id) {
        return genericRepository.findById(IvrsSchedule.class, id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public IvrsSchedule save(IvrsSchedule ivrsSchedule) {
        return genericRepository.save(ivrsSchedule);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(IvrsSchedule ivrsSchedule) {
        genericRepository.delete(ivrsSchedule);
    }

    public List<IvrsSchedule> find(IvrsScheduleQuery ivrsScheduleQuery) {
    	return genericRepository.find(ivrsScheduleQuery);
    }

    public IvrsSchedule findSingle(IvrsScheduleQuery ivrsScheduleQuery) {
    	return genericRepository.findSingle(ivrsScheduleQuery);
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
    
}

