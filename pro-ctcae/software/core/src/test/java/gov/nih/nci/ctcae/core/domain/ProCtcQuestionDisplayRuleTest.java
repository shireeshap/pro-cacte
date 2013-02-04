package gov.nih.nci.ctcae.core.domain;

import java.util.List;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;

public class ProCtcQuestionDisplayRuleTest extends TestDataManager{
	
	private String PROCTC_QUESTION_DISPLAY_RULE = "ProCtcQuestionDisplayRule";
	
	
	public void testEquals(){
		ProCtcQuestionQuery query = new ProCtcQuestionQuery();
		List<ProCtcQuestion> questionList = genericRepository.find(query);
		
		ProCtcQuestion question = questionList.get(1);
		ProCtcQuestionDisplayRule questionDisplayRule = question.getProCtcQuestionDisplayRules().get(0);
		ProCtcQuestionDisplayRule fetchQuestionDisplayRule = getById(PROCTC_QUESTION_DISPLAY_RULE, questionDisplayRule.getId()).get(0);
		
		assertEquals(questionDisplayRule, fetchQuestionDisplayRule);
		assertEquals(questionDisplayRule.hashCode(), fetchQuestionDisplayRule.hashCode());
		
	}
	
	public List<ProCtcQuestionDisplayRule> getById(String domainObject, Integer id){
		return hibernateTemplate.find("from "+ domainObject +" where id = ?",new Object[]{id});
	}

}
