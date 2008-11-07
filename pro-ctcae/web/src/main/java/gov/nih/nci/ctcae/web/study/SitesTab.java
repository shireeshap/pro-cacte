package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class SitesTab extends Tab<StudyCommand> {

    public SitesTab() {
        super("Sites", "Sites", "study/study_sites");
    }

    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        super.postProcess(request, command, errors);
        command.removeStudySites();

    }
}
