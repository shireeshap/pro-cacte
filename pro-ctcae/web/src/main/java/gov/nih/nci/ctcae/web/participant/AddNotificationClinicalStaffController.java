package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.StudyParticipantClinicalStaff;
import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * @author Harsh Agarwal
 * @created Feb 25, 2008
 */
public class AddNotificationClinicalStaffController extends AbstractController {


    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/ajax/notificationClinicalStaff");
        Integer studyParticipantAssignmentIndex = Integer.parseInt(request.getParameter("index"));
        ParticipantCommand command = ParticipantControllerUtils.getParticipantCommand(request);
        StudyParticipantClinicalStaff studyParticipantClinicalStaff = new StudyParticipantClinicalStaff();
        studyParticipantClinicalStaff.setPrimary(false);
        command.getParticipant().getStudyParticipantAssignments().get(studyParticipantAssignmentIndex).addNotificationClinicalStaff(studyParticipantClinicalStaff);
        modelAndView.addObject("index", studyParticipantAssignmentIndex);
        modelAndView.addObject("notificationindex", command.getParticipant().getStudyParticipantAssignments().get(studyParticipantAssignmentIndex).getNotificationClinicalStaff().size() - 1);
        modelAndView.addObject("studySiteId", command.getParticipant().getStudyParticipantAssignments().get(studyParticipantAssignmentIndex).getStudySite().getId());
        modelAndView.addObject("notifyOptions", ListValues.getNotificationRequired());
        return modelAndView;
    }


    /**
     * Instantiates a new adds the crf schedule controller.
     */
    public AddNotificationClinicalStaffController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

}