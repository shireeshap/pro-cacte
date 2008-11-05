package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class SelectStudyParticipantTab extends Tab<StudyParticipantCommand> {

    public SelectStudyParticipantTab() {
        super("StudyParticipant", "Select study and participant", "participant/selectstudyparticipant");

    }
}