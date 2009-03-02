package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;

/**
 * @author Vinay Kumar
 * @crated Mar 2, 2009
 */
public abstract class SecuredTab<T> extends Tab<T> {
    public SecuredTab(String longTitle, String shortTitle, String viewName) {
        super(longTitle, shortTitle, viewName);
    }

    protected SecuredTab() {
    }

    public abstract String getRequiredPrivilege();

    

}
