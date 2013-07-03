package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.web.ControllersUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * The Class AddStudySiteController.
 *
 * @author Vinay Gangoli
 */
public class AddStudyOrganizationalClinicalStaffController extends AbstractController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        StudyCommand studyCommand = ControllersUtils.getStudyCommand(request);

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            Integer siteIndexToRemove = Integer.parseInt(request.getParameter("craIndexToRemove"));
            studyCommand.getCraIndexesToRemove().add(siteIndexToRemove);
            return null;
        } else {
	        ModelAndView modelAndView = new ModelAndView("study/ajax/oneStudyOrganizationalClinicalStaffSection");
	        StudyOrganizationClinicalStaff socs = new StudyOrganizationClinicalStaff();
	        socs.setRole(Role.LEAD_CRA);
	        studyCommand.getLeadCRAs().add(socs);
	        request.setAttribute("command", studyCommand);
	        modelAndView.addObject("index", studyCommand.getLeadCRAs().size()-1);
	        return modelAndView;
        }
    }


    /**
     * Instantiates a new adds the study site controller.
     */
    public AddStudyOrganizationalClinicalStaffController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }

}