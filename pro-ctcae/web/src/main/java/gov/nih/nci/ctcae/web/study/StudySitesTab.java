package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

//
/**
 * The Class SitesTab.
 *
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class StudySitesTab extends SecuredTab<StudyCommand> {

    /**
     * Instantiates a new sites tab.
     */
    public StudySitesTab() {
        super("study.tab.sites", "study.tab.sites", "study/study_sites");
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.ctms.web.tabs.Tab#postProcess(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        super.postProcess(request, command, errors);

    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_ADD_STUDY_SITE;


    }
}
