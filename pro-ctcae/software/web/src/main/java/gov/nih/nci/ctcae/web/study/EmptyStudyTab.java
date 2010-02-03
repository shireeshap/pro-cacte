package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.context.SecurityContextHolder;

//
/**
 * The Class EmptyStudyTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
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

    @Override
    public void onDisplay(HttpServletRequest httpServletRequest, StudyCommand studyCommand) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        studyCommand.setOdc(user.isODCOnStudy(studyCommand.getStudy()));
    }

    public EmptyStudyTab() {
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_VIEW_STUDY;
    }
}
