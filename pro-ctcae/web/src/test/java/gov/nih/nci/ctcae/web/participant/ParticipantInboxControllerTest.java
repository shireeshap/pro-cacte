package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class ParticipantInboxControllerTest extends AbstractWebTestCase {

    public void testFormBackingObject() throws Exception {
        ParticipantInboxController controller = new ParticipantInboxController();
        request.setMethod("GET");
        controller.setParticipantRepository(participantRepository);

        login(ParticipantTestHelper.getDefaultParticipant().getUser().getUsername());
        controller.handleRequest(request, response);
        assertEquals(gov.nih.nci.ctcae.core.domain.Participant.class, controller.getCommandClass());

        login(ClinicalStaffTestHelper.getDefaultClinicalStaff().getUser().getUsername());
        try {
            controller.handleRequest(request, response);
            fail("Not a valid participant. Expecting error");
        } catch (CtcAeSystemException e) {

        }

    }
}