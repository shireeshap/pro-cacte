package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;


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


        participant = Fixture.createParticipantWithStudyAssignment("Mehul", "Gulati", "1234", StudyTestHelper.getDefaultStudy().getLeadStudySite());
        participant = participantRepository.save(participant);
    }

    public void testSearchParticipantForStudy() {

//        ArrayList<Participant> participants = (ArrayList<Participant>) scheduleCrfAjaxFacade.matchParticipants("gu", defaultStudy.getId());
//        assertEquals(1, participants.size());
//
//        participants = (ArrayList<Participant>) scheduleCrfAjaxFacade.matchParticipants("abc", defaultStudy.getId());
//        assertEquals(0, participants.size());

    }

    public void testSearchStudyForParticipant() {

//        ArrayList<Study> studies = (ArrayList<Study>) scheduleCrfAjaxFacade.matchStudies("gu", participant.getId());
//        assertEquals(1, studies.size());
//
//        studies = (ArrayList<Study>) scheduleCrfAjaxFacade.matchStudies("xyzz", participant.getId());
//        assertEquals(0, studies.size());
    }

    public ScheduleCrfAjaxFacade getScheduleCrfAjaxFacade() {
        return scheduleCrfAjaxFacade;
    }

    public void setScheduleCrfAjaxFacade(ScheduleCrfAjaxFacade scheduleCrfAjaxFacade) {
        this.scheduleCrfAjaxFacade = scheduleCrfAjaxFacade;
    }

}