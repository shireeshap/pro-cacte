package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;

import java.util.Date;

/**
 * @author Mehul Gulati
 *         Date: Jul 2, 2009
 */
public class ParticipantOffStudyControllerTest extends AbstractWebTestCase {
    ParticipantOffStudyController controller;
    StudyParticipantAssignment spAssignment;

    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        login(StudyTestHelper.getDefaultStudy().getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser().getUsername());
        controller = new ParticipantOffStudyController();
        controller.setStudyParticipantAssignmentRepository(studyParticipantAssignmentRepository);
        controller.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
        controller.setWebControllerValidator(new WebControllerValidatorImpl());
        spAssignment = ParticipantTestHelper.getDefaultParticipant().getStudyParticipantAssignments().get(0);
    }

    public void testFormBackingObject() throws Exception {
        request.setParameter("id", spAssignment.getId().toString());
        Object studyParticipantAssignment = controller.formBackingObject(request);
        assertNotNull(studyParticipantAssignment);
        assertEquals(spAssignment, studyParticipantAssignment);
    }

    public void testOnSubmit() throws Exception {
        request.setMethod("POST");
        request.setParameter("id", spAssignment.getId().toString());
        Date date = new Date();
        spAssignment.setOffTreatmentDate(date);
        controller.handleRequest(request, response);
        assertEquals("OFFSTUDY", spAssignment.getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(2).getStatus().getDisplayName().toUpperCase());
    }
}
