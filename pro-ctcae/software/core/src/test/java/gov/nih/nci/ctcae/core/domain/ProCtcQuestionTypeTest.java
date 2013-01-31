package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.aspectj.util.CollectionUtil;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 */
public class ProCtcQuestionTypeTest extends TestCase {

	public void testStatus() {
		ProCtcQuestionType proCtcQuestionType = ProCtcQuestionType.FREQUENCY;
		assertEquals("Frequency", proCtcQuestionType.toString());
		proCtcQuestionType = ProCtcQuestionType.INTERFERENCE;
		assertEquals("Interference", proCtcQuestionType.toString());
		proCtcQuestionType = ProCtcQuestionType.SEVERITY;
		assertEquals("Severity", proCtcQuestionType.toString());
	}

	public void testGetByCode() {
		assertEquals(ProCtcQuestionType.FREQUENCY, ProCtcQuestionType.getByCode("Frequency"));
		assertEquals(ProCtcQuestionType.INTERFERENCE, ProCtcQuestionType.getByCode("Interference"));
		assertEquals(ProCtcQuestionType.SEVERITY, ProCtcQuestionType.getByCode("Severity"));

	}
	
	public void testGetByDisplayName(){
		ProCtcQuestionType proCtcQuestionType = ProCtcQuestionType.FREQUENCY;
		assertEquals(proCtcQuestionType.getByDisplayName("Frequency"), proCtcQuestionType.FREQUENCY);
	}

	public void testGetValidValues(){
		ProCtcQuestionType proCtcQuestionType = ProCtcQuestionType.FREQUENCY;
		List<String> expectedValues = Arrays.asList(new String[]{"Never", "Rarely", "Occasionally", "Frequently", "Almost Constantly"});
		List<String> result = Arrays.asList(proCtcQuestionType.getValidValues());
		assertEquals(expectedValues, result);
		
		proCtcQuestionType = ProCtcQuestionType.SEVERITY;
		expectedValues = Arrays.asList(new String[]{"None", "Mild", "Moderate", "Severe", "Very severe"});
		result = Arrays.asList(proCtcQuestionType.getValidValues());
		assertEquals(expectedValues, result);
	}
	
	public void testGetAllDisplayTypes(){
	
		ArrayList<ProCtcQuestionType> expectedTypes = new ArrayList<ProCtcQuestionType>();
		expectedTypes.add(ProCtcQuestionType.FREQUENCY);
        expectedTypes.add(ProCtcQuestionType.INTERFERENCE);
        expectedTypes.add(ProCtcQuestionType.SEVERITY);
        expectedTypes.add(ProCtcQuestionType.AMOUNT);
        
        assertNotSame(ProCtcQuestionType.getAllDisplayTypes(), expectedTypes);
        assertNotSame(ProCtcQuestionType.getAllDisplayTypesForSharedAEReport(), expectedTypes);
        
        expectedTypes.add(ProCtcQuestionType.PRESENT);
        assertEquals(ProCtcQuestionType.getAllDisplayTypes(), expectedTypes);
        
	}
	
	

}