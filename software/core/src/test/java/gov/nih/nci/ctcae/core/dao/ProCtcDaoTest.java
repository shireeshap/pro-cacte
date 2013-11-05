package gov.nih.nci.ctcae.core.dao;

import gov.nih.nci.ctcae.core.csv.loader.UpdateMeddraLoader;
import gov.nih.nci.ctcae.core.domain.LowLevelTermVocab;
import gov.nih.nci.ctcae.core.domain.MeddraVersion;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.repository.MeddraLoaderRepository;

import java.util.Arrays;
import java.util.List;

/** Test class for Dao Package.
 *  Tests cases covers ProCtcDao, MeddraVersionDao and LowLevelTermDao 
 * @author AmeyS
 *
 */
public class ProCtcDaoTest extends TestDataManager{
	public static MeddraLoaderRepository meddraLoaderRepository;
	
	public  void initialize() throws Exception{
		UpdateMeddraLoader updateMeddraLoader = new UpdateMeddraLoader();
		updateMeddraLoader.setGenericRepository(genericRepository);
		updateMeddraLoader.setMeddraLoaderRepository(meddraLoaderRepository);
		updateMeddraLoader.updateMeddraTerms();
	}

	

	public void testFindAll() throws Exception{
		initialize();
		List<LowLevelTerm> lowLevelTerm = lowLevelTermDao.findAll(null);
		assertFalse(lowLevelTerm.isEmpty());
	}
	
	
	public void testFindBySubname(){
		List<LowLevelTerm> lowLevelTerm = lowLevelTermDao.findBySubname(null, null, null,null, null);
		assertTrue(lowLevelTerm.size() == 0);
		
		lowLevelTerm = lowLevelTermDao.findBySubname(new String[]{"10003028"}, null, Arrays.asList("meddraCode"));
		assertEquals(1, lowLevelTerm.size());
		
		lowLevelTerm = lowLevelTermDao.findBySubname(new String[]{"1000302"}, Arrays.asList("meddraCode"), null);
		assertEquals(3, lowLevelTerm.size());
	}
	
	public void testGetByMeddraCode() throws Exception{
		initialize();
		List<LowLevelTerm> lowLevelTerm = lowLevelTermDao.getByMeddraCode("10000424");
		List<LowLevelTermVocab> lowLevelTermVocab = getLowLevelTermVocab(100005);
		if(!lowLevelTerm.isEmpty() && !lowLevelTermVocab.isEmpty()){
			LowLevelTerm llt = lowLevelTerm.get(0);
			assertEquals(llt.getLowLevelTermVocab().getMeddraTermEnglish(), "Ache");
		    assertTrue(lowLevelTermVocab.get(0).equals(llt.getLowLevelTermVocab()));
		    assert(lowLevelTermVocab.get(0).hashCode() == llt.getLowLevelTermVocab().hashCode());
		}
	}
	
	public void testSearchByExample(){
		LowLevelTerm example = new LowLevelTerm();
		example.setMeddraCode("10003028");
		example.setCurrency("Y");
		List<LowLevelTerm> lowLevelTermList = lowLevelTermDao.searchByExample(example);	
		assertEquals(1, lowLevelTermList.size());
		
		example.setMeddraCode("10003028");
		example.setCurrency("N");
		lowLevelTermList = lowLevelTermDao.searchByExample(example);
		assertEquals(0, lowLevelTermList.size());
	}
	
	public void testRefresh(){
		LowLevelTerm example = new LowLevelTerm();
		example.setMeddraCode("10003028");
		List<LowLevelTerm> lowLevelTermList = lowLevelTermDao.searchByExample(example);	
		
		// assert that JartCode is null
		assertNull(lowLevelTermList.get(0).getJartCode());
		// Set JartCode to some value and again refresh the domain object without saving
		lowLevelTermList.get(0).setJartCode("-99");
		lowLevelTermDao.refresh(lowLevelTermList.get(0));
		
		// Fetch the object from database and expect that its JartCode is null
		lowLevelTermList = lowLevelTermDao.searchByExample(example);	
		assertNotSame("-99", lowLevelTermList.get(0).getJartCode());
		
	}
	
	public List<LowLevelTermVocab> getLowLevelTermVocab(int id){
		return hibernateTemplate.find("from LowLevelTermVocab where id =?", new Object[]{id});
		
	}
	

	public void testGetMeddraByName(){
		MeddraVersion testMeddraVersion = new MeddraVersion();
		testMeddraVersion.setName("testMeddra");
		testMeddraVersion.setId(99);
		meddraVersionDao.save(testMeddraVersion);
		
		List<MeddraVersion> meddraVersion = meddraVersionDao.getMeddraByName("testMeddra");
		assertEquals(testMeddraVersion.getId(), meddraVersion.get(0).getId());
	}
	
	// testing the save and delete operations for MeddraVersionDao
	public void testSaveAndDelete(){
		MeddraVersion testMeddraVersion = new MeddraVersion();
		testMeddraVersion.setName("testMeddra");
		testMeddraVersion.setId(99);
	
		meddraVersionDao.save(testMeddraVersion);
		meddraVersionDao.flush();
		assertEquals(true, testMeddraVersion.equals(meddraVersionDao.getById(99)));
		
		meddraVersionDao.delete(testMeddraVersion);
		assertTrue(meddraVersionDao.getMeddraByName("testMeddra").size() == 0);	
	}
	
	public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository){
		ProCtcDaoTest.meddraLoaderRepository = meddraLoaderRepository;
	}
	
}
