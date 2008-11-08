package gov.nih.nci.ctcae.web.participant;


import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class ParticipantControllerUtils {
    public static StudyParticipantCommand getStudyParticipantCommand(HttpServletRequest request) {
        StudyParticipantCommand studyParticipantCommand = (StudyParticipantCommand)
                request.getSession().getAttribute(ScheduleCrfController.class.getName() + ".FORM." + "command");
        return studyParticipantCommand;
    }
}