package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;

import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class ScheduleCrfTab extends Tab<StudyParticipantCommand> {

    public ScheduleCrfTab() {
        super("ScheduleCrf", "Schedule Form", "participant/schedulecrf");

    }
}