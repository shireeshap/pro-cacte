package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfItem;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

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
        assertEquals(0,command.getSiteId());
        assertNull(command.getSiteName());
        assertNull(command.getStudyId());
    }

    public void testSetters(){
        
        Participant p = Fixture.createParticipant("test","test","id");
        command.setParticipant(p);
        command.setSiteId(1);
        command.setSiteName("MySite");
        command.setStudyId(new int[] {223});

        assertEquals(p, command.getParticipant());
        assertEquals(1, command.getSiteId());
        assertEquals("MySite", command.getSiteName());
        assertEquals(223, command.getStudyId()[0]);

    }
}