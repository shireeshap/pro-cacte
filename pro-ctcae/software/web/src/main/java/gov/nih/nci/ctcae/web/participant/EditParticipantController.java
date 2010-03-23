package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.Errors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

//
/**
 * The Class ParticipantController.
 *
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */

public class EditParticipantController extends ParticipantController {
    private static final String PARTICIPANT_ID = "id";
    private GenericRepository genericRepository;

    protected void layoutTabs(final Flow<ParticipantCommand> flow) {
        flow.addTab(new ParticipantReviewTab());
        flow.addTab(new ParticipantDetailsTab());
        flow.addTab(new ParticipantClinicalStaffTab());
    }

    //
    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
    */
    @Override
    protected Object formBackingObject(final HttpServletRequest request) throws ServletException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String id = request.getParameter(PARTICIPANT_ID);
        ParticipantCommand command = new ParticipantCommand();
        populateOrganizationsForUser(command);
        Participant participant = participantRepository.findById(Integer.valueOf(id));
        participant.getUser().setConfirmPassword(participant.getUser().getPassword());
        for (UserRole userRole : participant.getUser().getUserRoles()) {
            userRole.getId();
        }
        command.setReadOnly(false);
        command.setReadOnlyUserName(true);
        for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
            StudyOrganization studySite = studyParticipantAssignment.getStudySite();
            studySite.getStudy().getCrfs();
            studySite.getStudy().getStudySponsor();
        }

        command.setParticipant(participant);

        if (participant.getStudyParticipantAssignments().size() > 0) {
            Organization organization = participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization();
            command.setSelectedStudyParticipantAssignment(participant.getStudyParticipantAssignments().get(0));
            if (!user.isAdmin()) {
                if (!command.getClinicalStaffOrgs().contains(organization)) {
                    command.setReadOnly(true);
                }
            }
            String siteName = organization.getName();
            command.setOrganizationId(organization.getId());
            command.setSiteName(siteName);
        }
        String mode = proCtcAEProperties.getProperty("mode.identifier");
        command.setMode(mode);
        command.setEdit(true);
        return command;
    }

    @Override
    public Flow<ParticipantCommand> getFlow(ParticipantCommand command) {
        if (command.isReadOnly()) {
            Flow readOnlyFlow = new Flow("Enter Participant");
            ParticipantReviewTab participantReviewTab = new ParticipantReviewTab();
            participantReviewTab.setGenericRepository(genericRepository);
            readOnlyFlow.addTab(participantReviewTab);

            return readOnlyFlow;
        } else {
            return super.getFlow(command);
        }
    }

    @Override
    protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
        int targetPage = super.getTargetPage(request, command, errors, currentPage);
        if (currentPage == targetPage) {
            targetPage = 0;
        }
        return targetPage;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}