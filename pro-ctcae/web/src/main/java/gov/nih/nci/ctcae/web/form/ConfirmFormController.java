package gov.nih.nci.ctcae.web.form;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class ConfirmFormController extends ParameterizableViewController {
    private String confirmationPage = "form/confirmForm";

    public ConfirmFormController() {
        setViewName(confirmationPage);
    }

    public ModelAndView handleRequestInternal(HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {

        setViewName(confirmationPage);
        ModelAndView mav = new ModelAndView(confirmationPage);

        return mav;
    }
}
