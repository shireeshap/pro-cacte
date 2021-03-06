package gov.nih.nci.ctcae.web.participant;


import javax.servlet.http.HttpServletRequest;

import gov.nih.nci.ctcae.web.clinicalStaff.notifications.ClinicalStaffNotificationPublisher;
import org.apache.commons.lang.StringUtils;

//
/**
 * The Class ParticipantControllerUtils.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class ParticipantControllerUtils {
    private static ClinicalStaffNotificationPublisher clinicalStaffNotificationPublisher;
    
    /**
     * Gets the study participant command.
     *
     * @param request the request
     * @return the study participant command
     */
    public static StudyParticipantCommand getStudyParticipantCommand(HttpServletRequest request) {
        StudyParticipantCommand studyParticipantCommand = (StudyParticipantCommand)
                request.getSession().getAttribute(ScheduleCrfController.class.getName() + ".FORM." + "command");
        return studyParticipantCommand;
    }
    
    public static ParticipantCommand getParticipantCommand(HttpServletRequest request) {
        ParticipantCommand participantCommand = (ParticipantCommand)
                request.getSession().getAttribute(CreateParticipantController.class.getName() + ".FORM." + "command");
        
        if (participantCommand == null) {
        	String participantId = request.getParameter(ParticipantController.PARTICIPANT_ID);
        	if(StringUtils.isNotEmpty(participantId)) {
        		participantCommand = (ParticipantCommand) 
        				request.getSession().getAttribute(EditParticipantController.class.getName() + ".FORM." + "command" + "." + Integer.parseInt(participantId));
        		
        	} else {
        		participantCommand = (ParticipantCommand)
        				request.getSession().getAttribute(EditParticipantController.class.getName() + ".FORM." + "command");
        		
        	}
        }
        
        return participantCommand;
    }
    
    public static void clearParticipantCommand(HttpServletRequest request){
        request.getSession().getAttribute(CreateParticipantController.class.getName() + ".FORM." + "command");
        request.getSession().getAttribute(EditParticipantController.class.getName() + ".FORM." + "command");
    }

    public static void clearStudyParticipantCommand(HttpServletRequest request){
        request.getSession().getAttribute(ScheduleCrfController.class.getName() + ".FORM." + "command");
    }


    public static ClinicalStaffNotificationPublisher getClinicalStaffNotificationPublisher() {
        return clinicalStaffNotificationPublisher;
    }

    public static void setClinicalStaffNotificationPublisher(ClinicalStaffNotificationPublisher publisher) {

        if(clinicalStaffNotificationPublisher == null && publisher != null) {
            ParticipantControllerUtils.clinicalStaffNotificationPublisher = publisher;

        }
    }

}