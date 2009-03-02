package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.web.security.SecuredTab;

//
/**
 * The Class EmptyStudyTab.
 *
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class EmptyStudyTab extends SecuredTab<StudyCommand> {

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

    public EmptyStudyTab() {
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_VIEW_STUDY;


    }
}
