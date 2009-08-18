package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueUserNameValidator;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

//
/**
 * @author Harsh Agarwal
 * @since Feb 19, 2009
 */
public class ParticipantDetailsTab extends SecuredTab<ParticipantCommand> {
    /**
     * The organization repository.
     */
    /**
     * The crf repository.
     */
    protected CRFRepository crfRepository;
    private StudyOrganizationRepository studyOrganizationRepository;
    private UniqueUserNameValidator uniqueUserNameValidator;

    /**
     * Instantiates a new participant details tab.
     */
    public ParticipantDetailsTab() {
        super("participant.tab.participant_details", "participant.tab.participant_details", "participant/createParticipant");
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_CREATE_PARTICIPANT;


    }

    @Override
    public void validate(ParticipantCommand command, Errors errors) {
        if (command.getStudySites() == null ||
                command.getStudySites().size() == 0) {
            if (command.getParticipant().getStudyParticipantAssignments() == null ||
                    command.getParticipant().getStudyParticipantAssignments().size() == 0) {
                errors.reject(
                        "studyId", "Please select at least one study.");
            }
        }
        User user = command.getParticipant().getUser();
        if (!uniqueUserNameValidator.validate(user)) {
            errors.rejectValue("participant.user.username", "user.user_exists", "user.user_exists");
        }
        if (!StringUtils.equals(user.getPassword(), user.getConfirmPassword())) {
            errors.rejectValue("participant.user.confirmPassword", "participant.user.confirm_password", "participant.user.confirm_password");
        }
    }

    public Map<String, Object> referenceData(ParticipantCommand command) {
        HashMap<String, Object> referenceData = new HashMap<String, Object>();


        StudyOrganizationQuery query = new StudyOrganizationQuery();
        query.filterByStudySiteAndLeadSiteOnly();
        Collection<StudyOrganization> studySites = studyOrganizationRepository.find(query);

        List<Organization> organizationsHavingStudySite = new ArrayList();
        for (StudyOrganization studySite : studySites) {
            if (!organizationsHavingStudySite.contains(studySite.getOrganization())) {
                organizationsHavingStudySite.add(studySite.getOrganization());
            }
        }

        if (command.getParticipant().getStudyParticipantAssignments().size() > 0) {
            for (StudyParticipantAssignment studyParticipantAssignment : command.getParticipant().getStudyParticipantAssignments()) {
                for (StudyOrganization studyOrganization : studyParticipantAssignment.getStudySite().getStudy().getStudyOrganizations()) {
                    studyOrganization.getDisplayName();
                }
            }
        }

        referenceData.put("genders", ListValues.getGenderType());
        referenceData.put("organizationsHavingStudySite", ListValues.getOrganizationsHavingStudySite(organizationsHavingStudySite));
        return referenceData;
    }


    @Override
    public void postProcess(HttpServletRequest request, ParticipantCommand command, Errors errors) {
        try {
            command.apply(crfRepository, request);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        super.postProcess(request, command, errors);
    }

    /**
     * Sets the crf repository.
     *
     * @param crfRepository the new crf repository
     */
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }

    public void setUniqueUserNameValidator(UniqueUserNameValidator uniqueUserNameValidator) {
        this.uniqueUserNameValidator = uniqueUserNameValidator;
    }
}