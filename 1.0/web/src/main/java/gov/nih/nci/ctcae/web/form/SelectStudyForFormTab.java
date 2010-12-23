package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.web.security.SecuredTab;

//
/**
 * The Class SelectStudyForFormTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class SelectStudyForFormTab extends SecuredTab<CreateFormCommand> {

    /**
     * Instantiates a new select study for form tab.
     */
    public SelectStudyForFormTab() {
        super("form.tab.form", "form.tab.select_study", "form/select_study");

    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_CREATE_FORM;
    }

}
