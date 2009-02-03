package gov.nih.nci.ctcae.web.form;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//
/**
 * The Class SetNameController.
 *
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class SetNameController extends AbstractController {


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/name");
        Map map = new HashMap();
        map.put("crfTitle", request.getParameter("crfTitle"));
        modelAndView.addAllObjects(map);

        return modelAndView;


    }


    /**
     * Instantiates a new sets the name controller.
     */
    public SetNameController() {
        super();
        setSupportedMethods(new String[]{"POST"});

    }


}