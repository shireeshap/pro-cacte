package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.cabig.ctms.tools.DataSourceSelfDiscoveringPropertiesFactoryBean;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.web.form.CtcAeSecuredTabbedFlowController;
import gov.nih.nci.ctcae.web.study.EmptyStudyTab;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.context.SecurityContextHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

//
/**
 * The Class ParticipantController.
 *
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */

public class EditParticipantController extends ParticipantController {
    private static final String PARTICIPANT_ID = "id";

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
        String id = request.getParameter(PARTICIPANT_ID);
        ParticipantCommand command = new ParticipantCommand();
        populateOrganizationsForUser(command);
        Participant participant = participantRepository.findById(Integer.valueOf(id));
        participant.getUser().setConfirmPassword(participant.getUser().getPassword());
        for (UserRole userRole : participant.getUser().getUserRoles()) {
            userRole.getId();
        }
        command.setReadOnly(false);
        for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
            StudyOrganization studySite = studyParticipantAssignment.getStudySite();
            studySite.getStudy().getCrfs();
            studySite.getStudy().getStudySponsor();
        }

        command.setParticipant(participant);

        if (participant.getStudyParticipantAssignments().size() > 0) {
            Organization organization = participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization();
            if (!command.getClinicalStaffOrgs().contains(organization)) {
                command.setReadOnly(true);
            }
            String siteName = organization.getName();
            command.setOrganizationId(organization.getId());
            command.setSiteName(siteName);
        }
        String mode = proCtcAEProperties.getProperty("mode.identifier");
        command.setMode(mode);
        return command;
    }

    @Override
    public Flow<ParticipantCommand> getFlow(ParticipantCommand command) {
        if (command.isReadOnly()) {
            Flow readOnlyFlow = new Flow("Enter Participant");
            readOnlyFlow.addTab(new ParticipantReviewTab());
            return readOnlyFlow;
        } else {
            return super.getFlow(command);
        }
    }
}