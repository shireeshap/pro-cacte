package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public class StudyCommandTest extends WebTestCase {
    private StudyCommand studyCommand;
    private StudySite studySite1;


    @Override
    protected void setUp() throws Exception {
        super.setUp();


        studyCommand = new StudyCommand();
        studySite1 = new StudySite();
        studySite1.setOrganization(Fixture.createOrganization("org1", "nci1"));


    }

    public void testEmptyConstructor() {

        assertNotNull("must intantiate study", studyCommand.getStudy());
        assertNotNull(" study must have atleast one coordinating center", studyCommand.getStudy().getStudyCoordinatingCenter());
        assertNotNull("  study must have atleast one funding sponsor", studyCommand.getStudy().getStudyFundingSponsor());
    }


}
