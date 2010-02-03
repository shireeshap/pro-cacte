package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.security.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @since Feb 11, 2009
 */
public class ParticipantReviewTab extends SecuredTab<ParticipantCommand> {
    public ParticipantReviewTab() {
        super("participant.tab.overview", "participant.tab.overview", "participant/participant_reviewsummary");
    }

    @Override
    public void onDisplay(HttpServletRequest httpServletRequest, ParticipantCommand participantCommand) {
        Study study = participantCommand.getSelectedStudyParticipantAssignment().getStudySite().getStudy();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        participantCommand.setOdc(user.isODCOnStudy(study));
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_VIEW_PARTICIPANT;
    }
}