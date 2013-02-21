package gov.nih.nci.ctcae.core.csv.loader;

import java.util.List;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.repository.MeddraLoaderRepository;

public class UpdateMeddraSpanishLoaderTest extends TestDataManager{
	
	public static MeddraLoaderRepository meddraLoaderRepository;
	
	public void testUpdateMeddraTermsSpanish() throws Exception{
		
		deleteLowLevelTermInTestData();
		UpdateMeddraLoader updateMeddraLoader = new UpdateMeddraLoader();
		updateMeddraLoader.setGenericRepository(genericRepository);
		updateMeddraLoader.setMeddraLoaderRepository(meddraLoaderRepository);
		updateMeddraLoader.updateMeddraTerms();
		
		MeddraQuery meddraQuery = new MeddraQuery();
		List<LowLevelTerm> lowLevelTerms = genericRepository.find(meddraQuery);
		assertFalse(lowLevelTerms.isEmpty());
		assertEquals(null, lowLevelTerms.get(0).getLowLevelTermVocab().getMeddraTermSpanish());
		
		UpdateMeddraSpanishLoader updateMeddraSpanishLoader = new UpdateMeddraSpanishLoader();
		updateMeddraSpanishLoader.setMeddraLoaderRepository(meddraLoaderRepository);
        updateMeddraSpanishLoader.setGenericRepository(genericRepository);
        updateMeddraSpanishLoader.updateMeddraTerms();
		//hibernateTemplate.flush();
        
		assertFalse(lowLevelTerms.isEmpty());
		//assertNotSame(null, lowLevelTerms.get(0).getLowLevelTermVocab().getMeddraTermSpanish());
	}

	public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository){
		UpdateMeddraSpanishLoaderTest.meddraLoaderRepository = meddraLoaderRepository;
	}
}
