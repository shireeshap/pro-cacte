package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.web.security.SecuredTab;

/**
 * @author Vinay Kumar
 * @since Feb 11, 2009
 */
public class ParticipantReviewTab extends SecuredTab<ParticipantCommand> {
    public ParticipantReviewTab() {
        super("participant.tab.overview", "participant.tab.overview", "participant/participant_reviewsummary");
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_VIEW_PARTICIPANT;


    }
}