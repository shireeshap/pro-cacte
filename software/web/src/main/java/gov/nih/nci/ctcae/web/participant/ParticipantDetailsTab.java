package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
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

    protected CRFRepository crfRepository;
    private StudyOrganizationRepository studyOrganizationRepository;
    private UserNameAndPasswordValidator userNameAndPasswordValidator;
    private UniqueParticipantEmailAddressValidator uniqueParticipantEmailAddressValidator;
    private UniqueParticipantUserNumberValidator uniqueParticipantUserNumberValidator;
    private UniqueStudyIdentifierForParticipantValidator uniqueStudyIdentifierForParticipantValidator;
    private OrganizationRepository organizationRepository;

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

        if(command.getSelectedOrganization() == null && command.getOrganizationId() != 0){
            command.setSelectedOrganization((Organization) organizationRepository.findById(command.getOrganizationId()));
        }

        if (command.getParticipant().isPersisted()) {
            //Edit flow
//            for (StudyParticipantAssignment studyParticipantAssignment : command.getParticipant().getStudyParticipantAssignments()) {
            StudyParticipantAssignment studyParticipantAssignment = command.getSelectedStudyParticipantAssignment();
            StudySite studySite = studyParticipantAssignment.getStudySite();
            String studyParticipantIdentifier = request.getParameter("participantStudyIdentifier_" + studySite.getId());
            command.getStudySubjectIdentifierMap().put(studySite.getStudy().getId(), studyParticipantIdentifier);
            command.setParticipantModeHistory(studyParticipantAssignment.getStudySite(), studyParticipantAssignment, request);
            command.setParticipantModesAndReminders(studyParticipantAssignment.getStudySite(), studyParticipantAssignment, request);
            studyParticipantAssignment.setStudyParticipantIdentifier(studyParticipantIdentifier);
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
//            String userNumber = request.getParameter("participantUserNumber_" + studySite.getId());
//                if (!StringUtils.isBlank(userNumber)) {
//                    try {
//                        command.getParticipant().setUserNumber(Integer.parseInt(userNumber));
//                    } catch (Exception e) {
//                        command.getParticipant().setUserNumber(null);
//                    }
//                }
            String pinNumber = request.getParameter("participantPinNumber_" + studySite.getId());
            if (!StringUtils.isBlank(pinNumber)) {
                try {
                    command.getParticipant().setPinNumber(Integer.parseInt(pinNumber));
                } catch (Exception e) {
                    command.getParticipant().setPinNumber(null);
                }
            }
            String email = request.getParameter("participant.emailAddress_" + studySite.getId());
            if (!StringUtils.isBlank(email)) {
                try {
                    command.getParticipant().setEmailAddress(email);
                } catch (Exception e) {
                    command.getParticipant().setEmailAddress(null);
                }
            }
            String phone = request.getParameter("participant.phoneNumber_" + studySite.getId());
            if (!StringUtils.isBlank(phone)) {
                try {
                    command.getParticipant().setPhoneNumber(phone);
                    String userNumber = phone.replaceAll("-", "");
                    command.getParticipant().setUserNumber(userNumber);
                } catch (Exception e) {
                    command.getParticipant().setPhoneNumber(null);
                }
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
//                String userNumber = request.getParameter("participantUserNumber_" + studySite.getId());
//                if (!StringUtils.isBlank(userNumber)) {
//                    try {
//                        command.getParticipant().setUserNumber(Integer.parseInt(userNumber));
//                    } catch (Exception e) {
//                        command.getParticipant().setUserNumber(null);
//                    }
//                }
                String pinNumber = request.getParameter("participantPinNumber_" + studySite.getId());
                if (!StringUtils.isBlank(pinNumber)) {
                    try {
                        command.getParticipant().setPinNumber(Integer.parseInt(pinNumber));
                    } catch (Exception e) {
                        command.getParticipant().setPinNumber(null);
                    }
                }
                String email = request.getParameter("participant.emailAddress_" + studySite.getId());
                if (!StringUtils.isBlank(email)) {
                    try {
                        command.getParticipant().setEmailAddress(email);
                    } catch (Exception e) {
                        command.getParticipant().setEmailAddress(null);
                    }
                }
                String phone = request.getParameter("participant.phoneNumber_" + studySite.getId());
                if (!StringUtils.isBlank(phone)) {
                    try {
                        command.getParticipant().setPhoneNumber(phone);
                        String userNumber = phone.replaceAll("-", "");
                        command.getParticipant().setUserNumber(userNumber);
                    } catch (Exception e) {
                        command.getParticipant().setPhoneNumber(null);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(command.getParticipant().getStudyParticipantAssignments())) {
                command.getSelectedStudyParticipantAssignment();
            }
        }
    }


    @Override
    public void validate(ParticipantCommand command, Errors errors) {

        if (command.getOrganizationId() == 0) {
        	errors.reject("participant.site");
        }
        if (command.getParticipant().getStudyParticipantAssignments() == null ||
                command.getParticipant().getStudyParticipantAssignments().size() == 0) {
            errors.reject("participant.study");
        }
        
        // checking for unique email address
        if (command.getParticipant().getEmailAddress() != null) {
            boolean validEmail = uniqueParticipantEmailAddressValidator.validateEmail(command.getParticipant().getEmailAddress(), command.getParticipant().getId());
            if (validEmail) {
                errors.rejectValue("participant.emailAddress", "participant.unique_emailAddress", "participant.unique_emailAddress");
            }
        }
        //checking for unique phone number
        if (command.getParticipant().getPhoneNumber() != null) {
            String phoneNumber = command.getParticipant().getPhoneNumber().toString();
            boolean validUserNumber = uniqueParticipantUserNumberValidator.validatePhoneNumber(phoneNumber, command.getParticipant().getId());
            if (validUserNumber) {
                //   errors.rejectValue("studyParticipantAssignment.participant.phoneNumber" , "participant.unique_userNumber", "participant.unique_userNumber");
                errors.reject("participant.unique_userNumber");
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
        boolean showTime = false;
        boolean showWeb = false;
        if (command.getParticipant().getStudyParticipantAssignments().size() > 0) {
            for (StudyParticipantAssignment studyParticipantAssignment : command.getParticipant().getStudyParticipantAssignments()) {
                for (StudyOrganization studyOrganization : studyParticipantAssignment.getStudySite().getStudy().getStudyOrganizations()) {
                    studyOrganization.getDisplayName();
                }
                for (StudyMode studyMode : studyParticipantAssignment.getStudySite().getStudy().getStudyModes()) {
                    studyMode.getMode().getDisplayName();
                }
            }
            for (AppMode appMode : command.getParticipant().getStudyParticipantAssignments().get(0).getSelectedAppModes()) {
                    if (appMode.equals(AppMode.IVRS)) {
                        showTime = true;
                    }
                    if (appMode.equals(AppMode.HOMEWEB)) {
                        showWeb = true;
                    }
                }
        }
        command.initialize();

        referenceData.put("genders", ListValues.getGenderType());
        referenceData.put("organizationsHavingStudySite", ListValues.getOrganizationsHavingStudySite(organizationsHavingStudySite));
        referenceData.put("patientId", command.getParticipant().getId());
        referenceData.put("userId", command.getParticipant().getUser().getId());
        referenceData.put("showTime", showTime);
        referenceData.put("showWeb", showWeb);
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
                    StudyParticipantAssignment studyParticipantAssignment = command.getSelectedStudyParticipantAssignment();
                    if (DateUtils.compareDate(studyParticipantAssignment.getStudyStartDate(), newStartDate) != 0) {
                        offSetDiff = DateUtils.daysBetweenDates(newStartDate, studyParticipantAssignment.getStudyStartDate());
                        studyParticipantAssignment.setStudyStartDate(newStartDate);
                        studyParticipantAssignment.removeSpCrfsIfNoCompletedSchedules();
                        command.assignCrfsToParticipant();
                    }
                    if (!studyParticipantAssignment.getArm().getId().equals(command.getArmId())) {
                        for (Arm arm : studyParticipantAssignment.getStudySite().getStudy().getArms()) {
                            if (command.getArmId() != null && arm.getId().equals(command.getArmId())) {
                                studyParticipantAssignment.setArm(arm);
                            }
                        }
                        studyParticipantAssignment.removeSpCrfsIfNoCompletedSchedules();
                        command.assignCrfsToParticipant();
                    }
                }
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

	public OrganizationRepository getOrganizationRepository() {
		return organizationRepository;
	}

	public void setOrganizationRepository(
			OrganizationRepository organizationRepository) {
		this.organizationRepository = organizationRepository;
	}
}