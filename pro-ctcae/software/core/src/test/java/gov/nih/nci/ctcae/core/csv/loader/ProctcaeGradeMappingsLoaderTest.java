package gov.nih.nci.ctcae.core.csv.loader;

import java.util.Map;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProctcaeGradeMapping;
import gov.nih.nci.ctcae.core.domain.ProctcaeGradeMappingVersion;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ProctcaeGradeMappingVersionQuery;

/**Test class for the loader helper.
 * 
 * @author VinayG
 */
public class ProctcaeGradeMappingsLoaderTest extends TestDataManager {

    private ProctcaeGradeMappingVersion pgmv;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		pgmv = getDefaultProctcaeGradeMappingVersion();
	}
	
	@Override
	protected void onTearDownInTransaction() throws Exception {
		super.onTearDownInTransaction();
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
		ProCtcTerm pTerm = getDefaultProCtcTerm();
		ProctcaeGradeMapping pgMapping = new ProctcaeGradeMapping(0, 0, 0, null, pgmv, pTerm);
		
		Map<ProctcaeGradeMapping, String> pgmMap = pTerm.getProctcaeGradeMappingMap();
		assertEquals(ProctcaeGradeMapping.ZERO, pgmMap.get(pgMapping));
	}
	

	private ProctcaeGradeMappingVersion getPgmVersion() {
		ProctcaeGradeMappingVersionQuery pgmvQuery = new ProctcaeGradeMappingVersionQuery();
		pgmvQuery.filterByDefaultVersion();
		ProctcaeGradeMappingVersion defaultProctcaeGradeMappingVersion = genericRepository.findSingle(pgmvQuery);
		return defaultProctcaeGradeMappingVersion;
	}
}
