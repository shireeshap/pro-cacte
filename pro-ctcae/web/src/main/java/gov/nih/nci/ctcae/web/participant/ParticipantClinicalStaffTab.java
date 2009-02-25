package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;

/**
 * @author Vinay Kumar
 * @crated Feb 11, 2009
 */
public class ParticipantClinicalStaffTab extends Tab<ParticipantCommand> {
    public ParticipantClinicalStaffTab() {
        super("participant.tab.clinical_staff", "participant.tab.clinical_staff", "participant/participant_clinical_staff");
    }
}