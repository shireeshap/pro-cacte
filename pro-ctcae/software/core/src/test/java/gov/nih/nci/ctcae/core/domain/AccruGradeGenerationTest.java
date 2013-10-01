package gov.nih.nci.ctcae.core.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

public class AccruGradeGenerationTest extends TestDataManager {
	String queryString = "from ProctcaeGradeMapping";
	Map<ProCtcTerm, Map<ProCtcQuestionType, String>> proResponseMap;
	Map<LowLevelTerm, Map<ProCtcQuestionType, String>> meddraResponseMap;
	Map<ProCtcTerm, List<String>> ctcaeGradesForProTerms;
	Map<LowLevelTerm, List<String>> ctcaeGradesForLowLevelTerms;
	
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		proResponseMap = new HashMap<ProCtcTerm, Map<ProCtcQuestionType, String>>();
		meddraResponseMap  = new HashMap<LowLevelTerm, Map<ProCtcQuestionType, String>>();
	}
	
	public void generateFinalGradeFromResponsesTest(){
		List<ProctcaeGradeMapping> proCtcaeGradeMappingList =  hibernateTemplate.find(queryString);
		
		for(ProctcaeGradeMapping proctcaeGradeMapping : proCtcaeGradeMappingList){
			
			
		}
		
		
	}

}
