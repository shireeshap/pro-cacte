package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.web.security.SecuredTab;

//
/**
 * The Class StudyDetailsTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class StudyDetailsTab extends SecuredTab<StudyCommand> {

    /**
     * Instantiates a new study details tab.
     */
    public StudyDetailsTab() {
        super("study.tab.study_details", "study.tab.study_details", "study/study_details");
    }


    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_CREATE_STUDY;

    }
}
