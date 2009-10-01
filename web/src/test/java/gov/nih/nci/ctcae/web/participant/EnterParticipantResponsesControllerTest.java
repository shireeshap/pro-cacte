package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;

/**
 * @author Mehul Gulati
 *         Date: Jul 2, 2009
 */
public class EnterParticipantResponsesControllerTest extends AbstractWebTestCase {

    EnterParticipantResponsesController controller;
    StudyParticipantCrfSchedule spcs;

    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        controller = new EnterParticipantResponsesController();
        controller.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
        controller.setWebControllerValidator(new WebControllerValidatorImpl());
        spcs = ParticipantTestHelper.getDefaultParticipant().getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(0);

    }

    public void testFormBackingObject() throws Exception{
        request.setParameter("id", spcs.getId().toString());
        Object spCrfSchedule = controller.formBackingObject(request);
        assertNotNull(spCrfSchedule);
        assertEquals(spcs, spCrfSchedule);
    }

    public void testOnSubmit() throws Exception{
        request.setMethod("POST");
        request.setParameter("id", spcs.getId().toString());
        controller.handleRequest(request, response);
        assertEquals("COMPLETED", spcs.getStatus().getDisplayName().toUpperCase());
    }
}
