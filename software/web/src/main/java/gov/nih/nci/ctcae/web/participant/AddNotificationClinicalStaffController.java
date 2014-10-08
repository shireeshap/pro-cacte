package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantClinicalStaff;
import gov.nih.nci.ctcae.web.ListValues;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Harsh Agarwal, Vinay Gangoli
 * @created Feb 25, 2008
 */
public class AddNotificationClinicalStaffController extends AbstractController {


    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        String action = request.getParameter("action");
        if ("deleteTp".equals(action)) {
        	deleteTreatingPhysician(request);
            return null;
        }
        if ("deleteRn".equals(action)) {
        	deleteResearchNurse(request);
            return null;
        }
        return addNotificationClinicalStaff(request, action);
    }

    private ModelAndView addNotificationClinicalStaff(HttpServletRequest request, String action) {
        ModelAndView modelAndView = new ModelAndView("participant/ajax/notificationClinicalStaff");
        Integer studyParticipantAssignmentIndex = Integer.parseInt(request.getParameter("index"));
        ParticipantCommand command = ParticipantControllerUtils.getParticipantCommand(request);
        StudyParticipantAssignment studyParticipantAssignment = command.getParticipant().getStudyParticipantAssignments().get(studyParticipantAssignmentIndex);
        
        StudyParticipantClinicalStaff studyParticipantClinicalStaff = new StudyParticipantClinicalStaff();
        studyParticipantClinicalStaff.setPrimary(false);
        
        if("addRn".equalsIgnoreCase(action)){
        	studyParticipantAssignment.addResearchNurse(studyParticipantClinicalStaff);

        } else if("addTp".equalsIgnoreCase(action)){
        	studyParticipantAssignment.addTreatingPhysician(studyParticipantClinicalStaff);
        }
        
        modelAndView.addObject("index", studyParticipantAssignmentIndex);
        modelAndView.addObject("addAction", action);

        modelAndView.addObject("rnIndex", studyParticipantAssignment.getResearchNurses().size() - 1);
        modelAndView.addObject("tpIndex", studyParticipantAssignment.getTreatingPhysicians().size() - 1);

        modelAndView.addObject("studySiteId", studyParticipantAssignment.getStudySite().getId());
        modelAndView.addObject("notifyOptions", ListValues.getNotificationRequired());
        return modelAndView;
    }
    
    private void deleteResearchNurse(HttpServletRequest request) {
        Integer rnIndex = Integer.parseInt(request.getParameter("rnIndex"));
        Integer studyParticipantAssignmentIndex = Integer.parseInt(request.getParameter("index"));
        ParticipantCommand command = ParticipantControllerUtils.getParticipantCommand(request);
        
        StudyParticipantClinicalStaff studyParticipantClinicalStaff = command.getParticipant().getStudyParticipantAssignments().get(studyParticipantAssignmentIndex).getResearchNurses().get(rnIndex);
        command.addResearchNursesToRemove(studyParticipantClinicalStaff);
    }
    
    private void deleteTreatingPhysician(HttpServletRequest request) {
        Integer tpIndex = Integer.parseInt(request.getParameter("tpIndex"));
        Integer studyParticipantAssignmentIndex = Integer.parseInt(request.getParameter("index"));
        ParticipantCommand command = ParticipantControllerUtils.getParticipantCommand(request);
        
        StudyParticipantClinicalStaff studyParticipantClinicalStaff = command.getParticipant().getStudyParticipantAssignments().get(studyParticipantAssignmentIndex).getTreatingPhysicians().get(tpIndex);
        command.addTreatingPhysicianToRemove(studyParticipantClinicalStaff);
    }


    /**
     * Instantiates a new adds the crf schedule controller.
     */
    public AddNotificationClinicalStaffController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }

}