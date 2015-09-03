package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.Alert;
import gov.nih.nci.ctcae.web.alert.AlertAjaxFacade;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Created by IntelliJ IDEA.
 * User: c3pr
 * Date: 11/11/11
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginParameterizableViewController extends ParameterizableViewController {

    private Properties properties;
    private AlertAjaxFacade alertAjaxFacade;
	public static final String HELP_VIDEO_URL_EN = "help.video.url.en";
    public static final String HELP_VIDEO_URL_ES = "help.video.url.es";

    public LoginParameterizableViewController(){
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = super.handleRequestInternal(request, response);
        String lang = request.getParameter("lang");
        if (lang == null || lang.equals("")) {
            Locale locale = RequestContextUtils.getLocale(request);
            if (locale != null && locale.equals(new Locale("es"))) {
                lang = "es";
            } else {
                lang = "en";
            }
        }
        List<Alert> alerts = alertAjaxFacade.fetchUpcommingAlerts();
        mv.addObject("alerts", alerts);
        
        String videoUrl = properties.getProperty(HELP_VIDEO_URL_EN);
        if (lang.equals("es"))
            videoUrl = properties.getProperty(HELP_VIDEO_URL_ES);
        mv.addObject("videoUrl", videoUrl);

        return mv;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
    	this.properties = properties;
    }

    public void setAlertAjaxFacade(AlertAjaxFacade alertAjaxFacade) {
		this.alertAjaxFacade = alertAjaxFacade;
	}
}
