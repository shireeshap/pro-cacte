package gov.nih.nci.ctcae.web.participant;

import java.util.List;
import java.util.Map;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.context.SecurityContextHolder;

/**
 * @author Vinay Kumar
 * @since Feb 11, 2009
 */
public class ParticipantReviewTab extends SecuredTab<ParticipantCommand> {
    //TODO:SA - to be removed
    GenericRepository genericRepository;

    public ParticipantReviewTab() {
        super("participant.tab.overview", "participant.tab.overview", "participant/participant_reviewsummary");
    }

    @Override
    public Map<String, Object> referenceData(ParticipantCommand command) {
        Map<String, Object> referenceData = super.referenceData(command);
        command.initialize();
        List<StudyParticipantAssignment> studyParticipantAssignments = command.getParticipant().getStudyParticipantAssignments();
        referenceData.put("studyParticipantAssignments", studyParticipantAssignments);
        referenceData.put("notifyOptions", ListValues.getNotificationRequired());

        return referenceData;
    }
    
    @Override
    public void onDisplay(HttpServletRequest httpServletRequest, ParticipantCommand participantCommand) {        
        Study study = participantCommand.getSelectedStudyParticipantAssignment().getStudySite().getStudy();
        
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        participantCommand.setOdc(user.isODCOnStudy(study));
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_VIEW_PARTICIPANT;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

}