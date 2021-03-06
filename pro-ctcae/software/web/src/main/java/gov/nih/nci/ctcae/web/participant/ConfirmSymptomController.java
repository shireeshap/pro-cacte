package gov.nih.nci.ctcae.web.participant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Mehul Gulati
 *         Date: Mar 16, 2010
 */
public class ConfirmSymptomController extends AbstractController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        ModelAndView modelAndView = new ModelAndView("participant/confirmSymptom");
        modelAndView.addObject("values", request.getParameter("values"));
        modelAndView.addObject("selectedChoice", request.getParameter("selectedChoice"));
        modelAndView.addObject("mappedValues", request.getParameter("mappedValues"));
        modelAndView.addObject("isMapped", request.getParameter("isMapped"));
        modelAndView.addObject("language", lang);
        return modelAndView;
    }

}
