package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.PasswordCreationPolicyException;
import gov.nih.nci.ctcae.core.validation.ValidationError;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueParticipantEmailAddressValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueParticipantUserNumberValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueStudyIdentifierForParticipantValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
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
    private UniqueParticipantEmailAddressValidator uniqueParticipantEmailAddressValidator;
    private UniqueParticipantUserNumberValidator uniqueParticipantUserNumberValidator;
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
    public void onBind(HttpServletRequest request, ParticipantCommand command, Errors errors) {
        super.onBind(request, command, errors);
        command.getStudySubjectIdentifierMap().clear();

        if (command.getParticipant().isPersisted()) {
            //Edit flow
//            for (StudyParticipantAssignment studyParticipantAssignment : command.getParticipant().getStudyParticipantAssignments()) {
            StudyParticipantAssignment studyParticipantAssignment = command.getSelectedStudyParticipantAssignment();
            StudySite studySite = studyParticipantAssignment.getStudySite();
            command.setParticipantModeHistory(studyParticipantAssignment.getStudySite(), studyParticipantAssignment, request);
            command.setParticipantModesAndReminders(studyParticipantAssignment.getStudySite(), studyParticipantAssignment, request);
            String newStartDate = request.getParameter("study_date_" + studyParticipantAssignment.getStudySite().getId());
            String armId = request.getParameter("arm_" + studyParticipantAssignment.getStudySite().getId());
            try {
                command.setNewStartDate(DateUtils.parseDate(newStartDate));
            } catch (Exception e) {
                command.setNewStartDate(null);
            }
            if (armId != null) {
                command.setArmId(Integer.parseInt(armId));
            }
//            }
        } else {
            //Create flow (Participant is not saved yet)
            command.getParticipant().removeAllStudyParticipantAssignments();
            for (StudySite studySite : command.getStudySites()) {
                String studyParticipantIdentifier = request.getParameter("participantStudyIdentifier_" + studySite.getId());
                command.getStudySubjectIdentifierMap().put(studySite.getStudy().getId(), studyParticipantIdentifier);
                command.setSiteName(studySite.getOrganization().getName());
                StudyParticipantAssignment studyParticipantAssignment = command.createStudyParticipantAssignment(studySite,
                        studyParticipantIdentifier, request.getParameter("arm_" + studySite.getId()));
                command.setParticipantModeHistory(studySite, studyParticipantAssignment, request);
                command.setParticipantModesAndReminders(studySite, studyParticipantAssignment, request);
                String studyStartDate = request.getParameter("study_date_" + studySite.getId());
                if (!StringUtils.isBlank(studyStartDate)) {
                    try {
                        studyParticipantAssignment.setStudyStartDate(DateUtils.parseDate(studyStartDate));
                    } catch (Exception e) {
                        studyParticipantAssignment.setStudyStartDate(null);
                    }

                }
                String userNumber = request.getParameter("participantUserNumber_" + studySite.getId());
                if (!StringUtils.isBlank(userNumber)) {
                    try {
                        command.getParticipant().setUserNumber(Integer.parseInt(userNumber));
                    } catch (Exception e) {
                        command.getParticipant().setUserNumber(null);
                    }
                }
                String pinNumber = request.getParameter("participantPinNumber_" + studySite.getId());
                if (!StringUtils.isBlank(pinNumber)) {
                    try {
                        command.getParticipant().setPinNumber(Integer.parseInt(pinNumber));
                    } catch (Exception e) {
                        command.getParticipant().setPinNumber(null);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(command.getParticipant().getStudyParticipantAssignments())) {
                command.getSelectedStudyParticipantAssignment();
            }

            String email[];
            if (command.getParticipant().getEmailAddress() != null) {
                email = command.getParticipant().getEmailAddress().split(",");
                if (email.length <= 0) {
                    command.getParticipant().setEmailAddress(null);
                }

                for (String em : email) {
                    if (!em.equals("")) {
                        command.getParticipant().setEmailAddress(em);
                    }
                }
            }
            String phone[];
            if (command.getParticipant().getPhoneNumber() != null) {
                phone = command.getParticipant().getPhoneNumber().split(",");
                if (phone.length <= 0) {
                    command.getParticipant().setPhoneNumber(null);
                }
                for (String ph : phone) {
                    if (!ph.equals("")) {
                        command.getParticipant().setPhoneNumber(ph);
                    }
                }
            }
        }
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
        // checking for unique email address
        if (command.getParticipant().getEmailAddress() != null) {
            boolean validEmail = uniqueParticipantEmailAddressValidator.validateEmail(command.getParticipant().getEmailAddress(), command.getParticipant().getId());
            if (validEmail) {
                errors.rejectValue("participant.emailAddress", "participant.unique_emailAddress", "participant.unique_emailAddress");
            }
        }
        //checking for unique user number
        if (command.getParticipant().getUserNumber() != null) {

            String userNumber = command.getParticipant().getUserNumber().toString();
            boolean validUserNumber = uniqueParticipantUserNumberValidator.validateUserNumber(userNumber, command.getParticipant().getId());
            if (validUserNumber) {
                errors.rejectValue("participant.userNumber", "participant.unique_userNumber", "participant.unique_userNumber");
            }
        }
        User user = command.getParticipant().getUser();
        user.addUserRole(new UserRole(Role.PARTICIPANT));
        command.setReadOnlyUserName(false);
        try {
            boolean validUser = userNameAndPasswordValidator.validate(user);
            if (!validUser) {
                if (userNameAndPasswordValidator.message().contains("Username")) {
                    errors.rejectValue("participant.user.username", userNameAndPasswordValidator.message(), userNameAndPasswordValidator.message());
                } else if (userNameAndPasswordValidator.message().contains("Password")) {
                    errors.rejectValue("participant.user.password", userNameAndPasswordValidator.message(), userNameAndPasswordValidator.message());
                }
            }
        } catch (PasswordCreationPolicyException ex) {
            for (ValidationError ve : ex.getErrors().getErrors()) {
                errors.rejectValue("participant.user.password", ve.getMessage(), ve.getMessage());
            }
            command.setReadOnlyUserName(false);
        }


        for (Integer studySiteId : command.getStudySubjectIdentifierMap().keySet()) {
            String ssi = command.getStudySubjectIdentifierMap().get(studySiteId);
            if (ssi == null || ssi.length() == 0) {
                errors.reject("participant.missing.assignedIdentifier", "participant.missing.assignedIdentifier");
                continue;
            }
            boolean duplicate = uniqueStudyIdentifierForParticipantValidator.validateUniqueParticipantIdentifier(studySiteId, ssi, command.getParticipant().getId());
            if (duplicate) {
                errors.reject("participant.unique_assignedIdentifier", "participant.unique_assignedIdentifier");
            }
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
        /**
         * Initializing studyOrganization and studyModes
         */
        if (command.getParticipant().getStudyParticipantAssignments().size() > 0) {
            for (StudyParticipantAssignment studyParticipantAssignment : command.getParticipant().getStudyParticipantAssignments()) {
                for (StudyOrganization studyOrganization : studyParticipantAssignment.getStudySite().getStudy().getStudyOrganizations()) {
                    studyOrganization.getDisplayName();
                }
                for (StudyMode studyMode : studyParticipantAssignment.getStudySite().getStudy().getStudyModes()) {
                    studyMode.getMode().getDisplayName();
                }
            }
        }
        command.initialize();

        referenceData.put("genders", ListValues.getGenderType());
        referenceData.put("organizationsHavingStudySite", ListValues.getOrganizationsHavingStudySite(organizationsHavingStudySite));
        referenceData.put("patientId", command.getParticipant().getId());
        referenceData.put("userId", command.getParticipant().getUser().getId());
        return referenceData;
    }


    @Override
    public void postProcess(HttpServletRequest request, ParticipantCommand command, Errors errors) {
        command.initialize();

        if (!errors.hasErrors()) {
            try {
                if (!command.getParticipant().isPersisted()) {
                    command.assignCrfsToParticipant();
                } else {
                    Date newStartDate = command.getNewStartDate();
                    int offSetDiff = 0;
//                    for (StudyParticipantAssignment studyParticipantAssignment : command.getParticipant().getStudyParticipantAssignments()) {
                    StudyParticipantAssignment studyParticipantAssignment = command.getSelectedStudyParticipantAssignment();
//                        if (!studyParticipantAssignment.getStudyStartDate().equals(newStartDate)) {
                    if (DateUtils.compareDate(studyParticipantAssignment.getStudyStartDate(), newStartDate) != 0) {
                        offSetDiff = DateUtils.daysBetweenDates(newStartDate, studyParticipantAssignment.getStudyStartDate());
                        studyParticipantAssignment.setStudyStartDate(newStartDate);
                        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                                studyParticipantCrf.moveSingleSchedule(studyParticipantCrfSchedule, offSetDiff);
                            }
                        }
                    }
                    if (!studyParticipantAssignment.getArm().getId().equals(command.getArmId())) {
                        for (Arm arm : studyParticipantAssignment.getStudySite().getStudy().getArms()) {
                            if (command.getArmId() != null && arm.getId().equals(command.getArmId())) {
                                studyParticipantAssignment.setArm(arm);
                            }
                        }
//                            studyParticipantAssignment.removeAllSchedules();
                        studyParticipantAssignment.removeSpCrfsIfNoCompletedSchedules();
                        command.assignCrfsToParticipant();
                    }
                }
//                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


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

    public void setUniqueParticipantEmailAddressValidator(UniqueParticipantEmailAddressValidator uniqueParticipantEmailAddressValidator) {
        this.uniqueParticipantEmailAddressValidator = uniqueParticipantEmailAddressValidator;
    }

    public void setUniqueParticipantUserNumberValidator(UniqueParticipantUserNumberValidator uniqueParticipantUserNumberValidator) {
        this.uniqueParticipantUserNumberValidator = uniqueParticipantUserNumberValidator;
    }
}