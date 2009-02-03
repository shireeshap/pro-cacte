package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;

//
/**
 * The Class ScheduleCrfTab.
 *
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class ScheduleCrfTab extends Tab<StudyParticipantCommand> {

    /**
     * Instantiates a new schedule crf tab.
     */
    public ScheduleCrfTab() {
        super("schedulecrf.label.schedule_form", "schedulecrf.label.schedule_form", "participant/schedulecrf");

    }
}