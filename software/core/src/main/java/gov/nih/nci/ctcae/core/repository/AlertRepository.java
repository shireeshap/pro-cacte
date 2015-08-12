package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Alert;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.AlertQuery;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AlertRepository implements Repository<Alert, AlertQuery> {
    private GenericRepository genericRepository;


    public Long findWithCount(AlertQuery query) {
        return genericRepository.findWithCount(query);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Alert save(Alert alert) {
        return genericRepository.save(alert);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Alert saveOrUpdate(Alert alert) {
    	if(alert.getStartDate() == null) {
    		throw new CtcAeSystemException("Cannot save system alert without a start date");
    	} 
    	if(alert.getEndDate() == null) {
    		throw new CtcAeSystemException("Cannot save system alert withoug an end date");
    	}
    	if(alert.getAlertMessage() == null) {
    		throw new CtcAeSystemException("Cannot save sysetm alert without a message");
    	}

    	return genericRepository.save(alert);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Alert copy(Alert alert) {
    	Alert copiedAlert = alert.copy();
    	return save(copiedAlert);
    }
    
    public void delete(Alert alert) {
        genericRepository.delete(alert);
    }

    public Collection<Alert> find(AlertQuery query) {
        return genericRepository.find(query);
    }

    public Alert findSingle(AlertQuery query) {
        return genericRepository.findSingle(query);
    }

    public Alert findById(Integer id) {
        return genericRepository.findById(Alert.class, id);
    }


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
	
}