package gov.nih.nci.ctcae.core.csv.loader;

import java.util.List;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.repository.MeddraLoaderRepository;

public class UpdateMeddraLoaderTest extends TestDataManager{
	
	public static MeddraLoaderRepository meddraLoaderRepository;
	
	public void testUpdateMeddraTerms() throws Exception{
		
		deleteLowLevelTermInTestData();
		
		MeddraQuery meddraQuery = new MeddraQuery();
		List<LowLevelTerm> lowLevelTerms = genericRepository.find(meddraQuery);
		assertTrue(lowLevelTerms.isEmpty());
		
		UpdateMeddraLoader updateMeddraLoader = new UpdateMeddraLoader();
		updateMeddraLoader.setGenericRepository(genericRepository);
		updateMeddraLoader.setMeddraLoaderRepository(meddraLoaderRepository);
		updateMeddraLoader.updateMeddraTerms();
		
		lowLevelTerms = genericRepository.find(meddraQuery);
		assertFalse(lowLevelTerms.isEmpty());
	}

	public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository){
		UpdateMeddraLoaderTest.meddraLoaderRepository = meddraLoaderRepository;
	}
}
