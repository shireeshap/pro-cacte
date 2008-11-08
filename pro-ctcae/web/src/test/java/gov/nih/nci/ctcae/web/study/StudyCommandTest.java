package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public class StudyCommandTest extends WebTestCase {
    private StudyCommand studyCommand;
    private StudySite studySite1;
    private StudySite studySite2;
    private StudySite studySite3;
    private StudySite studySite4;
    private StudySite studySite5;
    private StudySite studySite6;


    @Override
    protected void setUp() throws Exception {
        super.setUp();


        studyCommand = new StudyCommand();
        studySite1 = new StudySite();
        studySite1.setOrganization(Fixture.createOrganization("org1", "nci1"));

        studySite2 = new StudySite();
        studySite1.setOrganization(Fixture.createOrganization("or21", "nci2"));

        studySite3 = new StudySite();
        studySite1.setOrganization(Fixture.createOrganization("org3", "nci3"));

        studySite4 = new StudySite();

        studySite5 = new StudySite();
        studySite1.setOrganization(Fixture.createOrganization("org4", "nci4"));

        studySite6 = new StudySite();
        studySite1.setOrganization(Fixture.createOrganization("org5", "nci5"));

    }

    public void testEmptyConstructor() {

        assertNotNull("must intantiate study", studyCommand.getStudy());
        assertNotNull(" study must have atleast one coordinating center", studyCommand.getStudy().getStudyCoordinatingCenter());
        assertNotNull("  study must have atleast one funding sponsor", studyCommand.getStudy().getStudyFundingSponsor());
    }


    public void testRemoveStudySites() {
        Study study = studyCommand.getStudy();
        study.addStudySite(studySite1);
        study.addStudySite(studySite2);
        study.addStudySite(studySite3);
        study.addStudySite(studySite4);
        study.addStudySite(studySite5);
        study.addStudySite(studySite6);
        assertEquals("must be 6 study sites", 6, study.getStudySites().size());

        studyCommand.setObjectsIdsToRemove("1,3,2");

        studyCommand.removeStudySites();
        assertEquals("must remove 3 study sites", 3, study.getStudySites().size());

        assertTrue("must preseve the order of study sites", study.getStudySites().contains(studySite1));
        assertTrue("must preseve the order of study sites", study.getStudySites().contains(studySite5));
        assertTrue("must preseve the order of study sites", study.getStudySites().contains(studySite6));

    }
}
