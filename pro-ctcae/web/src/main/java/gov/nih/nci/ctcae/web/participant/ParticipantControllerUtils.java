package gov.nih.nci.ctcae.web.participant;


import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class ParticipantControllerUtils.
 * 
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class ParticipantControllerUtils {
    
    /**
     * Gets the study participant command.
     * 
     * @param request the request
     * 
     * @return the study participant command
     */
    public static StudyParticipantCommand getStudyParticipantCommand(HttpServletRequest request) {
        StudyParticipantCommand studyParticipantCommand = (StudyParticipantCommand)
                request.getSession().getAttribute(ScheduleCrfController.class.getName() + ".FORM." + "command");
        return studyParticipantCommand;
    }
}