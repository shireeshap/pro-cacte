package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;

/**
 * @author Vinay Kumar
 * @crated Feb 11, 2009
 */
public class ParticipantReviewTab extends Tab<ParticipantCommand> {
    public ParticipantReviewTab() {
        super("participant.tab.overview", "participant.tab.overview", "participant/participant_reviewsummary");
    }
}