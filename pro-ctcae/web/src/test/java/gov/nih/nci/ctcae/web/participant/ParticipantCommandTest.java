package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class ParticipantCommandTest extends WebTestCase {

    private ParticipantCommand command;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        command = new ParticipantCommand();

    }

    public void testConstructor() {

        assertNotNull(command.getParticipant());
        assertEquals(0, command.getOrganizationId());
        assertNull(command.getSiteName());
        assertNull(command.getStudySite());
    }

    public void testSetters() {

        Participant p = Fixture.createParticipant("test", "test", "id");
        command.setParticipant(p);
        command.setOrganizationId(1);
        command.setSiteName("MySite");

        assertEquals(p, command.getParticipant());
        assertEquals(1, command.getOrganizationId());
        assertEquals("MySite", command.getSiteName());

    }
}