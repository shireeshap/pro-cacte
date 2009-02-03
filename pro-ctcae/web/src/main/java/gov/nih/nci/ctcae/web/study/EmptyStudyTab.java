package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;

//
/**
 * The Class EmptyStudyTab.
 *
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class EmptyStudyTab extends Tab<StudyCommand> {

    /**
     * Instantiates a new empty study tab.
     *
     * @param longTitle  the long title
     * @param shortTitle the short title
     * @param viewName   the view name
     */
    public EmptyStudyTab(String longTitle, String shortTitle, String viewName) {
        super(longTitle, shortTitle, viewName);
    }
}
