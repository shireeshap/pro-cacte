package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

/**
 * @author Vinay Kumar
 * @since Nov 4, 2008
 */
public class ParticipantInboxControllerTest extends AbstractWebTestCase {

    public void testControllerParticipant() throws Exception {
        login(ParticipantTestHelper.getDefaultParticipant().getUser().getUsername());
        ParticipantInboxController controller = new ParticipantInboxController();
        request.setMethod("GET");
        controller.setUserRepository(userRepository);
        controller.handleRequest(request, response);
        assertEquals(gov.nih.nci.ctcae.core.domain.Participant.class, controller.getCommandClass());
    }

    public void testControllerClinicalStaff() throws Exception {
        ParticipantInboxController controller = new ParticipantInboxController();
        request.setMethod("GET");
        controller.setUserRepository(userRepository);

        login(ClinicalStaffTestHelper.getDefaultClinicalStaff().getUser().getUsername());
        try {
            controller.handleRequest(request, response);
            fail("Not a valid participant. Expecting error");
        } catch (CtcAeSystemException e) {
        }
    }
}