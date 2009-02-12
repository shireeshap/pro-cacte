package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.domain.StudySiteClinicalStaff;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * @author Vinay Kumar
 */
public class AddStudyComponentController extends AbstractController {

    //    public static final String SITE_COMPONENT_TYPE = "site";
    //    public static final String ROLE_COMPONENT_TYPE = "role";
    //
    public static final String STUDY_SITE_CLINICAL_STAFF_COMPONENT_TYPE = "studySiteClinicalStaff";
    public static final String COMPONENT_TYPE = "componentTyep";
    protected final String STUDY_SITE_INDEX = "studySiteIndex";

//    protected final String SITE_CLINICAL_STAFF_INDEX = "clinicalStaffAssignmentIndex";

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = null;
        StudyCommand studyCommand = ControllersUtils.getStudyCommand(request);

        Study study = studyCommand.getStudy();

        //FIXME:SAURABH delete study site will not work in edit mode
        String componentTyep = request.getParameter(COMPONENT_TYPE);
        if (StringUtils.equals(componentTyep, STUDY_SITE_CLINICAL_STAFF_COMPONENT_TYPE)) {

            Integer studySiteIndex = ServletRequestUtils.getRequiredIntParameter(request, STUDY_SITE_INDEX);

            StudySite studySite = study.getStudySites().get(studySiteIndex);
                StudySiteClinicalStaff studySiteClinicalStaff = new StudySiteClinicalStaff();

                studySite.addStudySiteClinicalStaff(studySiteClinicalStaff);

                modelAndView = new ModelAndView("study/studySiteClinicalStaffSection");
                modelAndView.addObject("studySiteIndex", studySiteIndex);
                modelAndView.addObject("studySiteClinicalStaff", studySiteClinicalStaff);
                modelAndView.addObject("studySiteClinicalStaffIndex", studySite.getStudySiteClinicalStaffs().size() - 1);

        }
        return modelAndView;
    }

    public AddStudyComponentController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }
}