package gov.nih.nci.ctcae.web;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author Vinay Kumar
 * @since Jan 26, 2009
 */
public class SeleniumProperties {
    private String waitTime;
    private String baseUrl;
    private String seleniumClientUrl;
    private String browser;
    private String serverHost;
    private Integer serverPort;


    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getSeleniumClientUrl() {
        return seleniumClientUrl;
    }

    public String getBrowser() {
        return browser;
    }

    @Required
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Required
    public void setBrowser(String browser) {
        this.browser = browser;
    }

    @Required
    public void setSeleniumClientUrl(String seleniumClientUrl) {
        this.seleniumClientUrl = seleniumClientUrl;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    @Required
    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }
}
