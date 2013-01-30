package gov.nih.nci.ctcae.core.dao;

import gov.nih.nci.ctcae.core.domain.LowLevelTermVocab;
import gov.nih.nci.ctcae.core.domain.MeddraVersion;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

import java.util.List;

/** Test class for Dao Package.
 *  Tests cases covers ProCtcDao, MeddraVersionDao and LowLevelTermDao 
 * @author AmeyS
 *
 */
public class ProCtcDaoTest extends TestDataManager{
	
	public ProCtcDaoTest(){
	}


	public void testFindAll(){
		List<LowLevelTerm> lowLevelTerm = lowLevelTermDao.findAll(null);
		assertEquals(lowLevelTerm.size(), 1084);
	}
	
	
	public void testFindBySubname(){
		List<LowLevelTerm> lowLevelTerm = lowLevelTermDao.findBySubname(null, null, null,null, null);
		assertTrue(lowLevelTerm.size() == 0);
		
	}
	
	public void testGetByMeddraCode(){
		List<LowLevelTerm> lowLevelTerm = lowLevelTermDao.getByMeddraCode("10000424");
		List<LowLevelTermVocab> lowLevelTermVocab = getLowLevelTermVocab(100005);
		if(!lowLevelTerm.isEmpty()){
			LowLevelTerm llt = lowLevelTerm.get(0);
			assertEquals(llt.getLowLevelTermVocab().getMeddraTermEnglish(), "Ache");
		    assertTrue(lowLevelTermVocab.get(0).equals(llt.getLowLevelTermVocab()));
		    assert(lowLevelTermVocab.get(0).hashCode() == llt.getLowLevelTermVocab().hashCode());
		}
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
	
}
