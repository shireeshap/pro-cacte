package gov.nih.nci.ctcae.core.csv.loader;

import java.util.Map;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProctcaeGradeMapping;
import gov.nih.nci.ctcae.core.domain.ProctcaeGradeMappingVersion;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

/**Test class for the loader helper.
 * 
 * @author VinayG
 */
public class ProctcaeGradeMappingsLoaderTest extends TestDataManager {

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
	}
	
	@Override
	protected void onTearDownInTransaction() throws Exception {
		super.onTearDownInTransaction();
		deleteProctcaeGradeMappings();
	}
	
	
	public void testLoadProctcaeGradeMappings(){
		
		ProctcaeGradeMappingsLoader pgmLoader = new ProctcaeGradeMappingsLoader();
		pgmLoader.setGenericRepository(genericRepository);
		pgmLoader.setProCtcTermRepository(proCtcTermRepository);
		
		try {
			pgmLoader.loadProctcaeGradeMappings();
		} catch (Exception e) {
			fail();
		}
		commitAndStartNewTransaction();
		
		ProCtcTerm pTerm = getDefaultProCtcTerm();
		ProctcaeGradeMappingVersion pgmv = getDefaultProctcaeGradeMappingVersion();
		ProctcaeGradeMapping pgMapping = new ProctcaeGradeMapping(0, 0, 0, null, pgmv, pTerm);
		
		Map<ProctcaeGradeMapping, String> pgmMap = pTerm.getProctcaeGradeMappingMap();
		assertEquals(ProctcaeGradeMapping.ZERO, pgmMap.get(pgMapping));
	}
}
