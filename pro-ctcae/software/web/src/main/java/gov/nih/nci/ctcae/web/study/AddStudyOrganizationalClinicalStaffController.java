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
	
	private static String DELETE_LCRA = "deleteLCRA";
	private static String DELETE_PI = "deletePI";
	private static String DELETE_ODC = "deleteODC";
	private static String Add_LCRA = "addLeadCRA";
	private static String Add_PI = "addPI";
	private static String Add_ODC = "addODC";
	


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        StudyCommand studyCommand = ControllersUtils.getStudyCommand(request);
        
        ModelAndView modelAndView = null;
        String action = request.getParameter("action");
        if (DELETE_LCRA.equals(action)) {
            Integer lcraIndexToRemove = Integer.parseInt(request.getParameter("lcraIndexToRemove"));
            studyCommand.getLCRAIndexesToRemove().add(lcraIndexToRemove);
        } else if(DELETE_ODC.equals(action)){
        	Integer odcIndexToRemove = Integer.parseInt(request.getParameter("odcIndexToRemove"));
            studyCommand.getOdcIndexesToRemove().add(odcIndexToRemove);
        } else if(DELETE_PI.equals(action)){
        	Integer piIndexToRemove = Integer.parseInt(request.getParameter("piIndexToRemove"));
            studyCommand.getPiIndexesToRemove().add(piIndexToRemove);
        } else if(Add_LCRA.equals(action)) {
        	modelAndView = new ModelAndView("study/ajax/oneStudyOrganizationalClinicalStaffSection");
	        StudyOrganizationClinicalStaff socs = new StudyOrganizationClinicalStaff();
	        socs.setRole(Role.LEAD_CRA);
	        studyCommand.getLeadCRAs().add(socs);
	        request.setAttribute("command", studyCommand);
	        modelAndView.addObject("index", studyCommand.getLeadCRAs().size()-1);
        } else if(Add_ODC.equals(action)){
        	modelAndView = new ModelAndView("study/ajax/oneODCStaffSection");
  	        StudyOrganizationClinicalStaff socs = new StudyOrganizationClinicalStaff();
  	        socs.setRole(Role.ODC);
  	        studyCommand.getOverallDataCoordinators().add(socs);
  	        request.setAttribute("command", studyCommand);
  	        modelAndView.addObject("index", studyCommand.getOverallDataCoordinators().size()-1);
        } else if(Add_PI.equals(action)){
        	modelAndView = new ModelAndView("study/ajax/onePIStaffSection");
  	        StudyOrganizationClinicalStaff socs = new StudyOrganizationClinicalStaff();
  	        socs.setRole(Role.PI);
  	        studyCommand.getPrincipalInvestigators().add(socs);
  	        request.setAttribute("command", studyCommand);
  	        modelAndView.addObject("index", studyCommand.getPrincipalInvestigators().size()-1);
        }
        return modelAndView;
    }


    /**
     * Instantiates a new adds the study site controller.
     */
    public AddStudyOrganizationalClinicalStaffController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }

}