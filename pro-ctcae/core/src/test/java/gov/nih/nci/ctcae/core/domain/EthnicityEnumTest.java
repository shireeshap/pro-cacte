package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;

import junit.framework.TestCase;

public class EthnicityEnumTest extends TestCase {

	public void testEthnicity() {
		Ethnicity ethnicity = Ethnicity.HISPANIC;
		assertEquals("Hispanic or Latino", ethnicity.toString());
		ethnicity = Ethnicity.NONHISPANIC;
		assertEquals("Not Hispanic or Latino", ethnicity.toString());
		ethnicity = Ethnicity.NOTREPORTED;
		assertEquals("Not Reported", ethnicity.toString());
		ethnicity = Ethnicity.UNKNOWN;
		assertEquals("Unknown", ethnicity.toString());
	}


	public void testGetAllEthnicities() {
		ArrayList<Ethnicity> ethnicities = Ethnicity.getAllEthnicities();
		for(Ethnicity ethnicity: Ethnicity.values()){
			assertTrue(ethnicities.contains(ethnicity));
		}
	}

}
