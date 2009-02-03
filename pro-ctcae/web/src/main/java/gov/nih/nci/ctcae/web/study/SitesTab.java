package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class SitesTab.
 * 
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class SitesTab extends Tab<StudyCommand> {

    /**
     * Instantiates a new sites tab.
     */
    public SitesTab() {
        super("study.tab.sites", "study.tab.sites", "study/study_sites");
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.ctms.web.tabs.Tab#postProcess(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        super.postProcess(request, command, errors);
        command.removeStudySites();

    }
}
