package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Vinay Kumar
 * @since Oct 27, 2008
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
        assertNotNull(" study must have atleast one coordinating center", studyCommand.getStudy().getDataCoordinatingCenter());
        assertNotNull("  study must have atleast one funding sponsor", studyCommand.getStudy().getStudySponsor());
    }


}
