package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantMode;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;

/**
 * The Class ParticipantController.
 *
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */

public class EditParticipantController extends ParticipantController {
    private static final String PARTICIPANT_ID = "id";
    private GenericRepository genericRepository;

    protected void layoutTabs(final Flow<ParticipantCommand> flow) {
        flow.addTab(new ParticipantReviewTab());
        flow.addTab(new ParticipantDetailsTab());
        flow.addTab(new ParticipantClinicalStaffTab());
        flow.addTab(new ScheduleCrfTab());
    }

    @Override
    protected Object formBackingObject(final HttpServletRequest request) throws ServletException {

        request.getSession().setAttribute(CreateParticipantController.class.getName() + ".FORM." + "command", null);

        String id = request.getParameter(PARTICIPANT_ID);
        ParticipantCommand command = new ParticipantCommand();
        populateOrganizationsForUser(command);
        Participant participant = participantRepository.findById(Integer.valueOf(id));
        participant.getUser().setConfirmPassword(participant.getUser().getPassword());
        participant.setConfirmPinNumber(participant.getPinNumber());
        participant.setPassword(participant.getUser().getPassword());
        command.setReadOnly(false);
        if (participant.getUser().getUsername()==null || participant.getUser().getUsername()== "") {
            command.setReadOnlyUserName(false);
        } else {
        command.setReadOnlyUserName(true);
        }
        command.setParticipant(participant);
        command.initialize();

        if (participant.getStudyParticipantAssignments().size() > 0) {
            Organization organization = participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization();
            command.setSelectedStudyParticipantAssignmentId(participant.getStudyParticipantAssignments().get(0).getId());

            String siteName = organization.getName();
            command.setOrganizationId(organization.getId());
            command.setSiteName(siteName);
        }
        String mode = proCtcAEProperties.getProperty("mode.nonidentifying");

        command.setMode(mode);
        command.setEdit(true);
        return command;
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        ParticipantCommand participantCommand = (ParticipantCommand) command;
        Map<String, Object> map = super.referenceData(request, command, errors, page);
        List<StudyParticipantMode> homeModes = new ArrayList<StudyParticipantMode>();
        for (StudyParticipantMode studyParticipantMode : participantCommand.getParticipant().getStudyParticipantAssignments().get(0).getStudyParticipantModes()) {
            if (studyParticipantMode.getMode().equals(AppMode.HOMEWEB) || studyParticipantMode.getMode().equals(AppMode.IVRS)) {
                homeModes.add(studyParticipantMode);
            }
        }
        
        if (StringUtils.isEmpty(participantCommand.getParticipant().getUser().getUsername())) {
        	participantCommand.setReadOnlyUserName(false);
        } else {
        	participantCommand.setReadOnlyUserName(true);
        }
        map.put("homeModeCount", homeModes.size());
        return map;
    }

    @Override
    public Flow<ParticipantCommand> getFlow(ParticipantCommand command) {
        if (command.isReadOnly()) {
            Flow<ParticipantCommand> readOnlyFlow = new Flow<ParticipantCommand>("Enter Participant");
            ParticipantReviewTab participantReviewTab = new ParticipantReviewTab();
            participantReviewTab.setGenericRepository(genericRepository);
            readOnlyFlow.addTab(participantReviewTab);

            return readOnlyFlow;
        } else {
            return super.getFlow(command);
        }
    }

    @Override
    protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
        return super.getTargetPage(request, command, errors, currentPage);
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}