package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.FormController;
import gov.nih.nci.ctcae.web.study.StudyCommand;
import gov.nih.nci.ctcae.web.study.StudyController;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

//
/**
 * The Class ControllersUtils.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class ControllersUtils {

    public static final ArrayList<String> pdaBrowserUserAgentsOS = new ArrayList<String>();

    static {
        pdaBrowserUserAgentsOS.add("Windows CE");
        pdaBrowserUserAgentsOS.add("iPhone");
        pdaBrowserUserAgentsOS.add("Profile/MIDP");
    }

    /**
     * Gets the form command.
     *
     * @param request the request
     * @return the form command
     */
    public static CreateFormCommand getFormCommand(final HttpServletRequest request) {
    	String crfId = request.getParameter(FormController.CRF_ID);
    	if(StringUtils.isNotEmpty(crfId)) {
    		return (CreateFormCommand) request.getSession().getAttribute(FormController.class.getName() + ".FORM." + "command" + "." + Integer.parseInt(crfId));
    	}
    	
        return (CreateFormCommand) request.getSession().getAttribute(FormController.class.getName() + ".FORM." + "command");
        
    }


    /**
     * Gets the study command.
     *
     * @param request the request
     * @return the study command
     */
    public static StudyCommand getStudyCommand(HttpServletRequest request) {
        String formSessionAttributeName;
        
        //edit study flow will have a study specific formSessionAttribute (name appended with studyId); create flow has a generic name
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	String studyId = attr.getRequest().getParameter(StudyController.STUDY_ID);
    	if(StringUtils.isNotEmpty(studyId)) {
    		formSessionAttributeName = StudyController.class.getName() + ".FORM.command" + "." + Integer.parseInt(studyId);
    	} else {
    		formSessionAttributeName = StudyController.class.getName() + ".FORM.command" ;
    	}
        
        StudyCommand studyCommand = (StudyCommand) request
        										   .getSession()
        										   .getAttribute(formSessionAttributeName);
        return studyCommand;
    }

    public static boolean isRequestComingFromMobile(HttpServletRequest request) {
        String wapProfileHeader = request.getHeader("x-wap-profile");
        if (!StringUtils.isEmpty(wapProfileHeader)) {
            return true;
        }
        String userAgentHeader = request.getHeader("user-agent");
        if (!StringUtils.isEmpty(userAgentHeader)) {
            userAgentHeader = userAgentHeader.toLowerCase();
            for (String os : pdaBrowserUserAgentsOS) {
                if (userAgentHeader.indexOf(os.toLowerCase()) > -1) {
                    return true;
                }
            }
        }
        return false;
    }


    public static String removeParameterFromQueryString(String queryString, String parameterName) {
        if (!StringUtils.isBlank(queryString)) {
            String param = "&" + parameterName + "=";
            while (queryString.indexOf(param) > -1) {
                String a = queryString.substring(0, queryString.indexOf(param));
                String b = queryString.substring(queryString.indexOf(param) + param.length());
                if (b.indexOf("&") > -1) {
                    b = b.substring(b.indexOf("&"));
                } else {
                    b = "";
                }
                queryString = a + b;
            }

        }
        return queryString;
    }
}
