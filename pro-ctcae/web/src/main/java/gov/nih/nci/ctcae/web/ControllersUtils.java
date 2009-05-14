package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.FormController;
import gov.nih.nci.ctcae.web.study.StudyCommand;
import gov.nih.nci.ctcae.web.study.StudyController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

//
/**
 * The Class ControllersUtils.
 *
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class ControllersUtils {

    public static final ArrayList<String> pdaBrowserUserAgentsOS = new ArrayList<String>();

    static {
        pdaBrowserUserAgentsOS.add("Windows CE");
        pdaBrowserUserAgentsOS.add("iPhone OS");
        pdaBrowserUserAgentsOS.add("Profile/MIDP");
    }

    /**
     * Gets the form command.
     *
     * @param request the request
     * @return the form command
     */
    public static CreateFormCommand getFormCommand(final HttpServletRequest request) {
        return (CreateFormCommand) request.getSession().getAttribute(FormController.class.getName() + ".FORM." + "command");


    }


    /**
     * Gets the study command.
     *
     * @param request the request
     * @return the study command
     */
    public static StudyCommand getStudyCommand(HttpServletRequest request) {
        StudyCommand studyCommand = (StudyCommand)
                request.getSession().getAttribute(StudyController.class.getName() + ".FORM." + "command");
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
}
