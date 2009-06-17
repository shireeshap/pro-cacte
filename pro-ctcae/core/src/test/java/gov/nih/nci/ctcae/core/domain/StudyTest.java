package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @since Oct 7, 2008
 */
public class StudyTest extends TestCase {
	private Study study;

	public void testConstructor() {
		study = new Study();
		assertNull(study.getShortTitle());
		assertNull(study.getAssignedIdentifier());
		assertNull(study.getDescription());
	}

	public void testGetterAndSetter() {
		study = new Study();
		study.setShortTitle("short title");
		study.setAssignedIdentifier("identifier");
		study.setDescription("desc");
		assertEquals("short title", study.getShortTitle());
		assertEquals("identifier", study.getAssignedIdentifier());
		assertEquals("desc", study.getDescription());
	}

	public void testGetDisplayName() {
		study = new Study();
		study.setShortTitle("short title");
		assertEquals("short title", study.getDisplayName());

		study.setAssignedIdentifier("identifier");

		assertEquals("(identifier) short title", study.getDisplayName());
	}

	public void testAddStudyOrganization() {
		study = new Study();
		assertTrue("must not have any study organizations", study.getStudyOrganizations().isEmpty());
		study.addStudySite(null);
		assertTrue("must not add null study site", study.getStudyOrganizations().isEmpty());
	}

	public void testEqualsAndHashCode() {
		Study anotherStudy = null;
		assertEquals(anotherStudy, study);
		study = new Study();
		assertFalse(study.equals(anotherStudy));
		anotherStudy = new Study();
		assertEquals(anotherStudy, study);
		assertEquals(anotherStudy.hashCode(), study.hashCode());


		study.setShortTitle("short title");
		assertFalse(study.equals(anotherStudy));
		anotherStudy.setShortTitle("short title");
		assertEquals(anotherStudy.hashCode(), study.hashCode());
		assertEquals(anotherStudy, study);

		study.setAssignedIdentifier("identifier");
		assertFalse(study.equals(anotherStudy));
		anotherStudy.setAssignedIdentifier("identifier");
		assertEquals(anotherStudy.hashCode(), study.hashCode());
		assertEquals(anotherStudy, study);

		study.setLongTitle("long title");
		assertFalse(study.equals(anotherStudy));
		anotherStudy.setLongTitle("long title");
		assertEquals(anotherStudy.hashCode(), study.hashCode());
		assertEquals(anotherStudy, study);

	}

}