package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.Map;


/**
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class ParticipantAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private ParticipantAjaxFacade participantAjaxFacade;
    protected Map parameterMap;
    private ParticipantRepository participantRepository;
    private Participant participant;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        participant = Fixture.createParticipant("Mehul", "Gulati", "1234");

        participant = participantRepository.save(participant);
    }

    public void testSearchParticipantByFirstName() {


        String table = participantAjaxFacade.searchParticipant(parameterMap, "Meh", "G", "", request);
        assertNotNull(table);
        assertTrue("must find atleast participant matching with first name", table.contains(participant.getFirstName()));
        assertTrue("must find atleast participant matching with first name", table.contains(participant.getLastName()));


        table = participantAjaxFacade.searchParticipant(parameterMap, "ehum", "lat", "", request);
        assertNotNull(table);
        assertFalse("must find atleast participant matching with first name", table.contains(participant.getFirstName()));


    }

    public void setParticipantAjaxFacade(ParticipantAjaxFacade participantAjaxFacade) {
        this.participantAjaxFacade = participantAjaxFacade;
    }

    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
}
