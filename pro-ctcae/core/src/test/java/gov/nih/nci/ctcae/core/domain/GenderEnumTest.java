package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;

import junit.framework.TestCase;

public class GenderEnumTest extends TestCase {

	public void testGender() {
		Gender gender = Gender.MALE;
		assertEquals("Male", gender.toString());
		gender = Gender.FEMALE;
		assertEquals("Female", gender.toString());
		gender = Gender.UNKNOWN;
		assertEquals("Unknown", gender.toString());
	}


	public void testGetAllGenders() {
		ArrayList<Gender> genders = Gender.getAllGenders();
		for(Gender gender: Gender.values()){
			assertTrue(genders.contains(gender));
		}
	}

}
