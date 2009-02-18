package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.web.ControllersUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * @author Vinay Kumar
 */
public class AddStudyComponentController extends AbstractController {


    public static final String COMPONENT_TYPE = "componentTyep";
    protected final String STUDY_SITE_ID = "studySiteId";
    protected final String CLINICAL_STAFF_ID = "clinicalStaffId";
    protected final String CLINICAL_STAFF_ASSIGNMENT_INDEX = "clinicalStaffAssignmentIndex";


//    protected final String SITE_CLINICAL_STAFF_INDEX = "clinicalStaffAssignmentIndex";

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = null;
        StudyCommand studyCommand = ControllersUtils.getStudyCommand(request);


        //FIXME:SAURABH delete study site will not work in edit mode

        String componentTyep = request.getParameter(COMPONENT_TYPE);

        return modelAndView;
    }


    public AddStudyComponentController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }
}