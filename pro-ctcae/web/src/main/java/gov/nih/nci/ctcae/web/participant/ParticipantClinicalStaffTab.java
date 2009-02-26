package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantClinicalStaff;
import gov.nih.nci.ctcae.core.domain.CRFCycle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.Errors;
import org.apache.commons.lang.StringUtils;

/**
 * @author Vinay Kumar
 * @crated Feb 11, 2009
 */
public class ParticipantClinicalStaffTab extends Tab<ParticipantCommand> {
    public ParticipantClinicalStaffTab() {
        super("participant.tab.clinical_staff", "participant.tab.clinical_staff", "participant/participant_clinical_staff");
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
            for (StudyParticipantAssignment studyParticipantAssignment : command.getParticipant().getStudyParticipantAssignments()) {
                studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantAssignment.getTreatingPhysician());
                studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantAssignment.getResearchNurse());
                for (StudyParticipantClinicalStaff studyParticipantClinicalStaff : studyParticipantAssignment.getNotificationClinicalStaff()) {
                    studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantClinicalStaff);
                }
            }
        }
    }
}