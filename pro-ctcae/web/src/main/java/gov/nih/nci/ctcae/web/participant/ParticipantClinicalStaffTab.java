package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantClinicalStaff;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @since Feb 11, 2009
 */
public class ParticipantClinicalStaffTab extends SecuredTab<ParticipantCommand> {

    public ParticipantClinicalStaffTab() {
        super("participant.tab.clinical_staff", "participant.tab.clinical_staff", "participant/participant_clinical_staff");
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_PARTICIPANT_ADD_NOTIFICATION_CLINICAL_STAFF;


    }

    @Override
    public void postProcess(HttpServletRequest request, ParticipantCommand command, Errors errors) {
        if (!StringUtils.isBlank(command.getNotificationIndexToRemove())) {
            String nToRemove = command.getNotificationIndexToRemove();
            String spaIndex = nToRemove.substring(0, nToRemove.indexOf("~"));
            String notificationIndex = nToRemove.substring(nToRemove.indexOf("~") + 1);
            Integer spaIndexInt = Integer.valueOf(spaIndex);
            Integer notificationIndexInt = Integer.valueOf(notificationIndex);
            StudyParticipantClinicalStaff studyParticipantClinicalStaff = command.getParticipant().getStudyParticipantAssignments().get(spaIndexInt).getNotificationClinicalStaff().get(notificationIndexInt);
            command.getParticipant().getStudyParticipantAssignments().get(spaIndexInt).getStudyParticipantClinicalStaffs().remove(studyParticipantClinicalStaff);
            command.getParticipant().getStudyParticipantAssignments().get(spaIndexInt).getNotificationClinicalStaff().remove(studyParticipantClinicalStaff);
            command.setNotificationIndexToRemove("");
        } else {
            command.assignStaff();
        }
    }

    @Override
    public void onDisplay(HttpServletRequest request, ParticipantCommand command) {
        for (StudyParticipantAssignment studyParticipantAssignment : command.getParticipant().getStudyParticipantAssignments()) {
            studyParticipantAssignment.getTreatingPhysician();
        }
    }

    @Override
    public Map<String, Object> referenceData(ParticipantCommand command) {
        Map<String, Object> referenceData = super.referenceData(command);

        List<StudyParticipantAssignment> studyParticipantAssignments = command.getParticipant().getStudyParticipantAssignments();
        referenceData.put("studyParticipantAssignments", studyParticipantAssignments);
        referenceData.put("notifyOptions", ListValues.getNotificationRequired());

        return referenceData;
    }

}