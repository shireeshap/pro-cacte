package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @since Oct 7, 2008
 */
public class StudyParticipantCrfItemTest extends TestCase {
	private StudyParticipantCrfItem studyParticipantCrfItem;

	public void testConstructor() {
		studyParticipantCrfItem = new StudyParticipantCrfItem();
		assertNull(studyParticipantCrfItem.getCrfPageItem());
		assertNull(studyParticipantCrfItem.getId());
		assertNull(studyParticipantCrfItem.getProCtcValidValue());
	}

	public void testGetterAndSetter() {
		studyParticipantCrfItem = new StudyParticipantCrfItem();
		CrfPageItem crfPageItem = new CrfPageItem();
		ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
		StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();

		studyParticipantCrfItem.setCrfPageItem(crfPageItem);
		studyParticipantCrfItem.setProCtcValidValue(proCtcValidValue);
		studyParticipantCrfItem.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
		studyParticipantCrfItem.setId(1);

		assertEquals(crfPageItem, studyParticipantCrfItem.getCrfPageItem());
		assertEquals(new Integer(1), studyParticipantCrfItem.getId());
		assertEquals(proCtcValidValue, studyParticipantCrfItem.getProCtcValidValue());


	}

	public void testEqualsHashCode() {
		studyParticipantCrfItem = new StudyParticipantCrfItem();
		StudyParticipantCrfItem studyParticipantCrfItem2 = new StudyParticipantCrfItem();

		assertEquals(studyParticipantCrfItem2.hashCode(), studyParticipantCrfItem.hashCode());

		CrfPageItem crfPageItem = new CrfPageItem();
		ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
		StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();

		studyParticipantCrfItem.setCrfPageItem(crfPageItem);
		assertFalse(studyParticipantCrfItem2.equals(studyParticipantCrfItem));
		studyParticipantCrfItem2.setCrfPageItem(crfPageItem);
		assertTrue(studyParticipantCrfItem.equals(studyParticipantCrfItem2));
		assertEquals(studyParticipantCrfItem2.hashCode(), studyParticipantCrfItem.hashCode());

		studyParticipantCrfItem.setProCtcValidValue(proCtcValidValue);
		assertFalse(studyParticipantCrfItem2.equals(studyParticipantCrfItem));
		studyParticipantCrfItem2.setProCtcValidValue(proCtcValidValue);
		assertTrue(studyParticipantCrfItem.equals(studyParticipantCrfItem2));
		assertEquals(studyParticipantCrfItem2.hashCode(), studyParticipantCrfItem.hashCode());

		studyParticipantCrfItem.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
		assertFalse(studyParticipantCrfItem2.equals(studyParticipantCrfItem));
		studyParticipantCrfItem2.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
		assertTrue(studyParticipantCrfItem.equals(studyParticipantCrfItem2));
		assertEquals(studyParticipantCrfItem2.hashCode(), studyParticipantCrfItem.hashCode());


		studyParticipantCrfItem.setId(1);

		assertTrue("must not consider id", studyParticipantCrfItem.equals(studyParticipantCrfItem2));
		assertEquals("must not consider id", studyParticipantCrfItem2.hashCode(), studyParticipantCrfItem.hashCode());

		studyParticipantCrfItem2.setId(2);

		assertTrue("must not consider id", studyParticipantCrfItem.equals(studyParticipantCrfItem2));
		assertEquals("must not consider id", studyParticipantCrfItem2.hashCode(), studyParticipantCrfItem.hashCode());

	}


}