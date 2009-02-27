package gov.nih.nci.ctcae.web;

import gov.nih.nci.cabig.ctms.web.chrome.Section;

/**
 * @author Vinay Kumar
 * @crated Feb 27, 2009
 */
public class MockSection extends Section {
    private String mainUrl;

    @Override
    public String getMainUrl() {
        return mainUrl;


    }


    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }
}
