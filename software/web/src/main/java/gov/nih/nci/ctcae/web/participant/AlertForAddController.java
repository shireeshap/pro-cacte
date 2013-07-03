package gov.nih.nci.ctcae.web.participant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Created by IntelliJ IDEA.
 * User: allareddy
 * Date: 7/21/11
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AlertForAddController extends AbstractController {
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        ModelAndView modelAndView = new ModelAndView("participant/alertForAdd");
        return modelAndView;
    }
}
