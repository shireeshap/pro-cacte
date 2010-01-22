package gov.nih.nci.ctcae.web.clinicalStaff;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//
/**
 * The Class SearchClinicalStaffController.
 *
 * @autjor Mehul Gulati
 * Date: Oct 22, 2008
 * Time: 10:21:08 AM
 */
public class SearchClinicalStaffController extends AbstractController {


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("clinicalStaff/searchClinicalStaff");
        Object obj = request.getSession().getAttribute("ClinicalStaffSearch");
        Map<String, String> pMap;
        String firstName = "%";
        String lastName = "%";
        String nciIdentifier = "";
        if (obj == null) {
            pMap = new HashMap<String, String>();
            pMap.put("firstName", firstName);
            pMap.put("lastName", lastName);
            pMap.put("nciIdentifier", nciIdentifier);
        } else {
            pMap = (HashMap) obj;
            firstName = pMap.get("firstName");
            lastName = pMap.get("lastName");
            nciIdentifier = pMap.get("nciIdentifier");
        }
        modelAndView.addObject("lastName", lastName);
        modelAndView.addObject("firstName", firstName);
        modelAndView.addObject("nciIdentifier", nciIdentifier);
        return modelAndView;
    }

    /**
     * Instantiates a new search clinical staff controller.
     */
    public SearchClinicalStaffController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

}
