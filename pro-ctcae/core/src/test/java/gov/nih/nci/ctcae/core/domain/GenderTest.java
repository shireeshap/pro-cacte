package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.ArrayList;

public class GenderTest extends TestCase {

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
