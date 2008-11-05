package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class SelectStudyForFormTab extends Tab<CreateFormCommand> {

    public SelectStudyForFormTab() {
        super("Form", "Select study", "form/select_study");

    }
}
