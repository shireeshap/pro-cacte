package gov.nih.nci.ctcae.core.domain;



import junit.framework.TestCase;

import java.util.Date;

/**
 * @author Suneel Allareddy
 * @since Dec 20, 2010
 */
public class StudyParticipantReportingModeHistoryTest extends TestCase {
    private StudyParticipantReportingModeHistory studyParticipantReportingModeHistory;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    public void testConstructor() {
        studyParticipantReportingModeHistory = new StudyParticipantReportingModeHistory();
        assertNull(studyParticipantReportingModeHistory.getMode());
        assertNotNull(studyParticipantReportingModeHistory.getEffectiveStartDate());
        assertNull(studyParticipantReportingModeHistory.getEffectiveEndDate());
        assertNull(studyParticipantReportingModeHistory.getStudyParticipantAssignment());
    }

    public void testGetterAndSetter() {
        studyParticipantReportingModeHistory = new StudyParticipantReportingModeHistory();
        studyParticipantReportingModeHistory.setEffectiveEndDate(new Date());
        studyParticipantReportingModeHistory.setMode(AppMode.HOMEBOOKLET); 
        studyParticipantReportingModeHistory.setStudyParticipantAssignment(new StudyParticipantAssignment());

        assertNull(studyParticipantReportingModeHistory.getId());
        assertNotNull(studyParticipantReportingModeHistory.getMode());
        assertEquals(AppMode.HOMEBOOKLET,studyParticipantReportingModeHistory.getMode());
        assertEquals("Paper Form",studyParticipantReportingModeHistory.getMode().getCode());
        assertNotNull(studyParticipantReportingModeHistory.getEffectiveStartDate());
        assertNotNull(studyParticipantReportingModeHistory.getEffectiveEndDate());
        assertNotNull(studyParticipantReportingModeHistory.getStudyParticipantAssignment());

    }
}
