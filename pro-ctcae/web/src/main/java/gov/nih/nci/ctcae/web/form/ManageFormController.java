package gov.nih.nci.ctcae.web.form;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class ManageFormController extends AbstractController {


    public ManageFormController() {
        setSupportedMethods(new String[]{"GET"});
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView("form/manageForm");

        return modelAndView;


    }

}
