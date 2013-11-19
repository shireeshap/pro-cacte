package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.IvrsCallHistory;
import gov.nih.nci.ctcae.core.query.IvrsCallHistoryQuery;

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
public class IvrsCallHistoryRepository implements Repository<IvrsCallHistory, IvrsCallHistoryQuery> {
    
	private GenericRepository genericRepository;

    public IvrsCallHistory findById(Integer id) {
        return genericRepository.findById(IvrsCallHistory.class, id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public IvrsCallHistory save(IvrsCallHistory ivrsCallHistory) {
        return genericRepository.save(ivrsCallHistory);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(IvrsCallHistory ivrsCallHistory) {
        genericRepository.delete(ivrsCallHistory);
    }

    public List<IvrsCallHistory> find(IvrsCallHistoryQuery ivrsCallHistoryQuery) {
    	return genericRepository.find(ivrsCallHistoryQuery);
    }

    public IvrsCallHistory findSingle(IvrsCallHistoryQuery ivrsCallHistoryQuery) {
    	return genericRepository.findSingle(ivrsCallHistoryQuery);
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
    
}

