package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.ArrayList;


/**
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class ScheduleCrfAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private ScheduleCrfAjaxFacade scheduleCrfAjaxFacade;

    public void testSearchParticipantForStudy() {
        Study study = StudyTestHelper.getDefaultStudy();

        ArrayList<Participant> participants = (ArrayList<Participant>) scheduleCrfAjaxFacade.matchParticipants("juliet", study.getId());
        assertEquals(1, participants.size());

        participants = (ArrayList<Participant>) scheduleCrfAjaxFacade.matchParticipants("abc", study.getId());
        assertEquals(0, participants.size());

    }

    public void testSearchStudyForParticipant() {
        Participant participant = ParticipantTestHelper.getDefaultParticipant();

        ArrayList<Study> studies = (ArrayList<Study>) scheduleCrfAjaxFacade.matchStudies("Collection", participant.getId());
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