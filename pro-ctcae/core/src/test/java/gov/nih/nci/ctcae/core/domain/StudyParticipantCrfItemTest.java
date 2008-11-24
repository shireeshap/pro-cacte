package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
public class StudyParticipantCrfItemTest extends TestCase {
    private StudyParticipantCrfItem studyParticipantCrfItem;

    public void testConstructor() {
        studyParticipantCrfItem = new StudyParticipantCrfItem();
        assertNull(studyParticipantCrfItem.getCrfItem());
        assertNull(studyParticipantCrfItem.getId());
        assertNull(studyParticipantCrfItem.getProCtcValidValue());
        assertNull(studyParticipantCrfItem.getStudyParticipantCrfSchedule());
    }

    public void testGetterAndSetter() {
        studyParticipantCrfItem = new StudyParticipantCrfItem();
        CrfItem crfItem = new CrfItem();
        ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();

        studyParticipantCrfItem.setCrfItem(crfItem);
        studyParticipantCrfItem.setProCtcValidValue(proCtcValidValue);
        studyParticipantCrfItem.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);

        assertEquals(crfItem, studyParticipantCrfItem.getCrfItem());
        assertEquals(proCtcValidValue, studyParticipantCrfItem.getProCtcValidValue());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfItem.getStudyParticipantCrfSchedule());

        
    }


}