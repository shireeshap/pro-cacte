package gov.nih.nci.ctcae.web.participant;


import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

//
/**
 * The Class ParticipantControllerUtils.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class ParticipantControllerUtils {

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
        ParticipantCommand participantCommand;
        if (StringUtils.isBlank(request.getParameter("id"))) {
            participantCommand = (ParticipantCommand)
                    request.getSession().getAttribute(CreateParticipantController.class.getName() + ".FORM." + "command");
        } else {
            participantCommand = (ParticipantCommand)
                    request.getSession().getAttribute(EditParticipantController.class.getName() + ".FORM." + "command");
        }
        return participantCommand;
    }
}