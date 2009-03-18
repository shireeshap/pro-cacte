package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.ArrayList;


/**
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class ScheduleCrfAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private ScheduleCrfAjaxFacade scheduleCrfAjaxFacade;
    private Participant participant;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        participant = Fixture.createParticipantWithStudyAssignment("Mehul", "Gulati", "1234", defaultStudySite);
        participant = participantRepository.save(participant);
    }

    public void testSearchParticipantForStudy() {

        ArrayList<Participant> participants = (ArrayList<Participant>) scheduleCrfAjaxFacade.matchParticipants("gu", defaultStudy.getId());
        assertEquals(1, participants.size());

        participants = (ArrayList<Participant>) scheduleCrfAjaxFacade.matchParticipants("abc", defaultStudy.getId());
        assertEquals(0, participants.size());

    }

    public void testSearchStudyForParticipant() {

        ArrayList<Study> studies = (ArrayList<Study>) scheduleCrfAjaxFacade.matchStudies("my", participant.getId());
        assertEquals(1, studies.size());

        studies = (ArrayList<Study>) scheduleCrfAjaxFacade.matchStudies("xyzz", participant.getId());
        assertEquals(0, studies.size());
    }

    public ScheduleCrfAjaxFacade getScheduleCrfAjaxFacade() {
        return scheduleCrfAjaxFacade;
    }

    public void setScheduleCrfAjaxFacade(ScheduleCrfAjaxFacade scheduleCrfAjaxFacade) {
        this.scheduleCrfAjaxFacade = scheduleCrfAjaxFacade;
    }

}