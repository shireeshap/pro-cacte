package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.PasswordCreationPolicyException;
import gov.nih.nci.ctcae.core.validation.ValidationError;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueStudyIdentifierForParticipantValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
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
    private UserNameAndPasswordValidator userNameAndPasswordValidator;
//    protected Properties proCtcAEProperties;
    private UniqueStudyIdentifierForParticipantValidator uniqueStudyIdentifierForParticipantValidator;

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
        user.addUserRole(new UserRole(Role.PARTICIPANT));
        command.setReadOnlyUserName(false);
        try {
            boolean validUser = userNameAndPasswordValidator.validate(user);
            if (!validUser) {
                errors.rejectValue("participant.user.username", userNameAndPasswordValidator.message(), userNameAndPasswordValidator.message());
            }
        } catch (PasswordCreationPolicyException ex) {
            for (ValidationError ve : ex.getErrors().getErrors()) {
                errors.rejectValue("participant.user.username", ve.getMessage(), ve.getMessage());
            }
            command.setReadOnlyUserName(false);
        }
    }

    public Map<String, Object> referenceData(ParticipantCommand command) {
        HashMap<String, Object> referenceData = new HashMap<String, Object>();
        StudyOrganizationQuery query = new StudyOrganizationQuery();
        query.filterByStudySiteAndLeadSiteOnly();
        Collection<StudyOrganization> studySites = studyOrganizationRepository.find(query);


        Set<Organization> organizationsHavingStudySite = new HashSet<Organization>();
        for (StudyOrganization studySite : studySites) {
            Organization o = studySite.getOrganization();
            if (command.getClinicalStaffOrgs().contains(o)) {
                organizationsHavingStudySite.add(o);
            }
        }

        if (command.getParticipant().getStudyParticipantAssignments().size() > 0) {
            for (StudyParticipantAssignment studyParticipantAssignment : command.getParticipant().getStudyParticipantAssignments()) {
                for (StudyOrganization studyOrganization : studyParticipantAssignment.getStudySite().getStudy().getStudyOrganizations()) {
                    studyOrganization.getDisplayName();
                }
            }
        }
//        String mode = proCtcAEProperties.getProperty("mode.identifier");
//        command.setMode(mode);
        referenceData.put("genders", ListValues.getGenderType());
        referenceData.put("organizationsHavingStudySite", ListValues.getOrganizationsHavingStudySite(organizationsHavingStudySite));
        return referenceData;
    }


    @Override
    public void postProcess(HttpServletRequest request, ParticipantCommand command, Errors errors) {
        try {
            for (StudySite studySite : command.getStudySites()) {
                String studyParticipantIdentifier = request.getParameter("participantStudyIdentifier_" + studySite.getId());
                if (!studyParticipantIdentifier.equals(null)) {
                    boolean isunique = uniqueStudyIdentifierForParticipantValidator.validate(studySite.getStudy().getId(), studyParticipantIdentifier);
                    if (isunique) {
                        errors.reject("participant.unique_assignedIdentifier", "participant.unique_assignedIdentifier");
                    }
                }
            }
            command.apply(crfRepository, request);
    }

    catch(
    ParseException e
    )

    {
        e.printStackTrace();
        throw new RuntimeException(e);
    }

    super.

    postProcess(request, command, errors);

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

    public void setUserNameAndPasswordValidator(UserNameAndPasswordValidator userNameAndPasswordValidator) {
        this.userNameAndPasswordValidator = userNameAndPasswordValidator;
    }

//    public void setProCtcAEProperties(Properties proCtcAEProperties) {
//        this.proCtcAEProperties = proCtcAEProperties;
//    }

    public void setUniqueStudyIdentifierForParticipantValidator(UniqueStudyIdentifierForParticipantValidator uniqueStudyIdentifierForParticipantValidator) {
        this.uniqueStudyIdentifierForParticipantValidator = uniqueStudyIdentifierForParticipantValidator;
    }
}