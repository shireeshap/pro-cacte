package gov.nih.nci.ctcae.core.repository;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.ctcae.core.domain.AddedSymptomVerbatim;
import gov.nih.nci.ctcae.core.query.AddedSymptomVerbatimQuery;

@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
public class AddedSymptomVerbatimRepository implements Repository<AddedSymptomVerbatim, AddedSymptomVerbatimQuery>{
	GenericRepository genericRepository;

	public AddedSymptomVerbatim findById(Integer id) {
		return (AddedSymptomVerbatim) genericRepository.findById(AddedSymptomVerbatim.class, id);
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public AddedSymptomVerbatim save(AddedSymptomVerbatim addedSymptomVerbatim) {
		return genericRepository.save(addedSymptomVerbatim);
	}
	
	public void delete(AddedSymptomVerbatim addedSymptomVerbatim) {
		genericRepository.delete(addedSymptomVerbatim);
	}

	public Collection<AddedSymptomVerbatim> find(AddedSymptomVerbatimQuery query) {
		return genericRepository.find(query);
	}

	public AddedSymptomVerbatim findSingle(AddedSymptomVerbatimQuery query) {
		return genericRepository.findSingle(query);
	}
	
	@Required
	public void setGenericRepository(GenericRepository genericRepository) {
		this.genericRepository = genericRepository;
	}

}