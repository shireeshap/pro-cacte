package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Collections;

import junit.framework.TestCase;

public class ProCtcTermComparatorTest extends TestCase{
	

	public void testProCtcTermComparator(){
		List<ProCtcTerm> proCtcTermList = new ArrayList<ProCtcTerm>();
		proCtcTermList = buildProCtcTermList();
		assert(proCtcTermList.get(0).getProCtcTermVocab().getTermEnglish().equalsIgnoreCase("Constipation"));
		assert(proCtcTermList.get(1).getProCtcTermVocab().getTermEnglish().equalsIgnoreCase("Dizziness"));
		assert(proCtcTermList.get(2).getProCtcTermVocab().getTermEnglish().equalsIgnoreCase("Acne"));
		
		Collections.sort(proCtcTermList, new ProCtcTermComparator());
		assert(proCtcTermList.get(0).getProCtcTermVocab().getTermEnglish().equalsIgnoreCase("Acne"));
		assert(proCtcTermList.get(1).getProCtcTermVocab().getTermEnglish().equalsIgnoreCase("Constipation"));
		assert(proCtcTermList.get(2).getProCtcTermVocab().getTermEnglish().equalsIgnoreCase("Dizziness"));
	}
	
	public List<ProCtcTerm> buildProCtcTermList(){
		List<ProCtcTerm> proCtcTermList = new ArrayList<ProCtcTerm>();
		
		ProCtcTerm proCtcTerm1 = new ProCtcTerm();
		ProCtcTermVocab proCtcTermVocab1 = new ProCtcTermVocab();
		proCtcTermVocab1.setTermEnglish("Acne");
		proCtcTerm1.setProCtcTermVocab(proCtcTermVocab1);
		
		ProCtcTerm proCtcTerm2 = new ProCtcTerm();
		ProCtcTermVocab proCtcTermVocab2 = new ProCtcTermVocab();
		proCtcTermVocab2.setTermEnglish("Constipation");
		proCtcTerm2.setProCtcTermVocab(proCtcTermVocab2);
		
		ProCtcTerm proCtcTerm3 = new ProCtcTerm();
		ProCtcTermVocab proCtcTermVocab3 = new ProCtcTermVocab();
		proCtcTermVocab3.setTermEnglish("Dizziness");
		proCtcTerm3.setProCtcTermVocab(proCtcTermVocab3);
		
		proCtcTermList.add(proCtcTerm2);
		proCtcTermList.add(proCtcTerm3);
		proCtcTermList.add(proCtcTerm1);
		return proCtcTermList;
	}

}
