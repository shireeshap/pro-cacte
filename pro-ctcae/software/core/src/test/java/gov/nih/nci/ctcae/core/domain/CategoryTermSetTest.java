package gov.nih.nci.ctcae.core.domain;

import java.util.List;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

public class CategoryTermSetTest extends TestDataManager {
	private static String CATEGORYNAME = "Skin and subcutaneous tissue disorders";
	private static String CTC_TERM_VOCAB_ENGLISH = "Rash acneiform";
	private static String OTHER_CTC_TERM_VOCAB_ENGLISH = "Skin ulceration";
	
	
	public void testEqualsAndHashCode(){
		CategoryTermSet categoryTermSet = getCategoryTermSet(CATEGORYNAME, CTC_TERM_VOCAB_ENGLISH).get(0);
		CategoryTermSet otherCategoryTermSet = getCategoryTermSet(CATEGORYNAME,OTHER_CTC_TERM_VOCAB_ENGLISH).get(0);
		
		assertFalse(categoryTermSet.equals(otherCategoryTermSet));
		assertFalse(categoryTermSet.hashCode() == otherCategoryTermSet.hashCode());
		
		CategoryTermSet fetchFromRepository = getCategoryTermSetById(20).get(0);
		assertTrue(categoryTermSet.equals(fetchFromRepository));
		assertTrue(categoryTermSet.hashCode() == fetchFromRepository.hashCode());
	}
	
	
	public List<CategoryTermSet> getCategoryTermSet(String categoryName, String ctcTermVocabEnglish){
		return hibernateTemplate.find(" from CategoryTermSet cts where cts.category.name = ? and cts.ctcTerm.ctcTermVocab.termEnglish = ? ",new Object[]{categoryName, ctcTermVocabEnglish});
	}
	
	public List<CategoryTermSet> getCategoryTermSetById(Integer id){
		return hibernateTemplate.find(" from CategoryTermSet cts where cts.id = ? ",new Object[]{id});
	}
}
