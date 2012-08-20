package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.PasswordCreationPolicyException;
import gov.nih.nci.ctcae.core.validation.ValidationError;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueParticipantEmailAddressValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueParticipantUserNumberValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueStudyIdentifierForParticipantValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.ivrs.callout.IvrsCallOutScheduler;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;

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

	protected static final Log logger = LogFactory.getLog(ParticipantDetailsTab.class);

	
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

        if (command.getSelectedOrganization() == null && command.getOrganizationId() != 0) {
            command.setSelectedOrganization((Organization) organizationRepository.findById(command.getOrganizationId()));
        }

        if (command.getParticipant().isPersisted()) {
            //Edit flow
//            for (StudyParticipantAssignment studyParticipantAssignment : command.getParticipant().getStudyParticipantAssignments()) {
            StudyParticipantAssignment studyParticipantAssignment = command.getSelectedStudyParticipantAssignment();
            StudySite studySite = studyParticipantAssignment.getStudySite();
            String studyParticipantIdentifier = request.getParameter("participantStudyIdentifier_" + studySite.getId());
            List<StudyParticipantMode> studyParticipantModes = new ArrayList();
            for (StudyParticipantMode spMode : studyParticipantAssignment.getStudyParticipantModes()) {
                studyParticipantModes.add(spMode);
            }
            command.getParticipant().setPassword(command.getParticipant().getUser().getPassword());
            command.setStudyParticipantModes(studyParticipantModes);
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
            String userName = request.getParameter("participant.username_" + studySite.getId());
            if (!StringUtils.isBlank(userName)) {
                try {
                    command.getParticipant().getUser().setUsername(userName);
                } catch (Exception e) {
                    command.getParticipant().getUser().setUsername(null);
                }
            }
            String password = request.getParameter("participant.password_" + studySite.getId());
            if (!StringUtils.isBlank(password)) {
                try {
                    command.getParticipant().getUser().setPassword(password);
                } catch (Exception e) {
                    command.getParticipant().getUser().setPassword(null);
                }
            }
            String userNumber = request.getParameter("participantUserNumber_" + studySite.getId());
            if (!StringUtils.isBlank(userNumber)) {
                try {
                    command.getParticipant().setUserNumber(userNumber);
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
            String confirmPinNumber = request.getParameter("participantPinNumberConfirm_" + studySite.getId());
            if (!StringUtils.isBlank(confirmPinNumber)) {
                try {
                    command.getParticipant().setConfirmPinNumber(Integer.parseInt(confirmPinNumber));
                } catch (Exception e) {
                    command.getParticipant().setConfirmPinNumber(null);
                }
            }
            String email = request.getParameter("participant.emailAddress");
            if (!StringUtils.isBlank(email)) {
                try {
                    command.getParticipant().setEmailAddress(email);
                } catch (Exception e) {
                    command.getParticipant().setEmailAddress(null);
                }
            }
            String phone = request.getParameter("participant.phoneNumber");
            if (!StringUtils.isBlank(phone)) {
                try {
                    command.getParticipant().setPhoneNumber(phone);
                } catch (Exception e) {
                    command.getParticipant().setPhoneNumber(null);
                    logger.error("Invalid particpant phone number: setting phone number as null in exception flow.");		
                }
            } else {
            	command.getParticipant().setPhoneNumber(null);
            }
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
                String userName = request.getParameter("participant.username_" + studySite.getId());
                if (!StringUtils.isBlank(userName)) {
                    try {
                        command.getParticipant().getUser().setUsername(userName);
                    } catch (Exception e) {
                        command.getParticipant().getUser().setUsername(null);
                    }
                }
                String password = request.getParameter("participant.password_" + studySite.getId());
                if (!StringUtils.isBlank(password)) {
                    try {
                        command.getParticipant().getUser().setPassword(password);
                    } catch (Exception e) {
                        command.getParticipant().getUser().setPassword(null);
                    }
                }
                String userNumber = request.getParameter("participantUserNumber_" + studySite.getId());
                if (!StringUtils.isBlank(userNumber)) {
                    try {
                        command.getParticipant().setUserNumber(userNumber);
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
                String confirmPinNumber = request.getParameter("participantPinNumberConfirm_" + studySite.getId());
                if (!StringUtils.isBlank(confirmPinNumber)) {
                    try {
                        command.getParticipant().setConfirmPinNumber(Integer.parseInt(confirmPinNumber));
                    } catch (Exception e) {
                        command.getParticipant().setConfirmPinNumber(null);
                    }
                }

                String email = request.getParameter("participant.emailAddress");
                if (!StringUtils.isBlank(email)) {
                    try {
                        command.getParticipant().setEmailAddress(email);
                    } catch (Exception e) {
                        command.getParticipant().setEmailAddress(null);
                    }
                }
                String phone = request.getParameter("participant.phoneNumber");
                if (!StringUtils.isBlank(phone)) {
                    try {
                        command.getParticipant().setPhoneNumber(phone);
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

        //checking for unique username
        if (command.getParticipant().getUser().getUsername() != null && command.getParticipant().getUser().getId() == null) {
            boolean validUsername = userNameAndPasswordValidator.validateUniqueName(command.getParticipant().getUser());
            if (!validUsername) {
                errors.reject("user.user_exists");
            }
        }
        // checking for unique email address
        if (command.getParticipant().getEmailAddress() != null) {
            boolean validEmail = uniqueParticipantEmailAddressValidator.validateEmail(command.getParticipant().getEmailAddress(), command.getParticipant().getId());
            if (validEmail) {
//                errors.rejectValue("participant.emailAddress", "participant.unique_emailAddress", "participant.unique_emailAddress");
                errors.reject("participant.unique_emailAddress");
            }
        }
        //checking for unique phone number
        if (command.getParticipant().getPhoneNumber() != null) {
            String phoneNumber = command.getParticipant().getPhoneNumber().toString();
            boolean validUserNumber = uniqueParticipantUserNumberValidator.validatePhoneNumber(phoneNumber, command.getParticipant().getId());
            if (validUserNumber) {
                //   errors.rejectValue("studyParticipantAssignment.participant.phoneNumber" , "participant.unique_userNumber", "participant.unique_userNumber");
                errors.reject("participant.unique_phoneNumber");
            }
        }

        //checking for unique phone number
        if (command.getParticipant().getUserNumber() != null) {
            String userNumber = command.getParticipant().getUserNumber().toString();
            boolean validUserNumber = uniqueParticipantUserNumberValidator.validateUserNumber(userNumber, command.getParticipant().getId());
            if (validUserNumber) {
                //   errors.rejectValue("studyParticipantAssignment.participant.phoneNumber" , "participant.unique_userNumber", "participant.unique_userNumber");
                errors.reject("participant.unique_userNumber");
            }
        }

        //check for unique mrn
        if (command.getParticipant().getAssignedIdentifier() != null) {
            boolean duplicateMRN = uniqueStudyIdentifierForParticipantValidator.validateUniqueParticipantMrn(command.getOrganizationId(), command.getParticipant().getAssignedIdentifier(), command.getParticipant().getId());
            if (duplicateMRN) {
                errors.reject("participant.unique_mrn");
            }
        }
        //check for password policy
        User user = command.getParticipant().getUser();
        user.addUserRole(new UserRole(Role.PARTICIPANT));
          if (user.getPassword()!=null) {
            try {
                userNameAndPasswordValidator.validatePasswordPolicy(user);
            } catch (PasswordCreationPolicyException ex) {
                for (ValidationError ve : ex.getErrors().getErrors()) {
                    errors.reject("password", ve.getMessage());
                }
            }
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
        boolean showBook = false;
        if (command.getParticipant().getStudyParticipantAssignments().size() > 0) {
            for (StudyParticipantAssignment studyParticipantAssignment : command.getParticipant().getStudyParticipantAssignments()) {
                for (StudyOrganization studyOrganization : studyParticipantAssignment.getStudySite().getStudy().getStudyOrganizations()) {
                    studyOrganization.getDisplayName();
                }
                for (StudyMode studyMode : studyParticipantAssignment.getStudySite().getStudy().getStudyModes()) {
                    studyMode.getMode().getDisplayName();
                }
            }
            command.setOnDefaultArm(command.getSelectedStudyParticipantAssignment().getArm().isDefaultArm());
            for (AppMode appMode : command.getSelectedStudyParticipantAssignment().getSelectedAppModes()) {
                if (appMode.equals(AppMode.IVRS)) {
                    showTime = true;
                }
                if (appMode.equals(AppMode.HOMEWEB) || appMode.equals(AppMode.CLINICWEB)) {
                    showWeb = true;
                }
                if (appMode.equals(AppMode.HOMEBOOKLET)) {
                    showBook = true;
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
        referenceData.put("showBook", showBook);
        return referenceData;
    }


    @Override
    public void postProcess(HttpServletRequest request, ParticipantCommand command, Errors errors) {
        command.initialize();
        StudyParticipantAssignment studyParticipantAssignment = command.getSelectedStudyParticipantAssignment();
        if (!errors.hasErrors()) {
            try {
                if (!command.getParticipant().isPersisted()) {
                    command.assignCrfsToParticipant(false);
                } else {
                    Date newStartDate = command.getNewStartDate();
                    int offSetDiff = 0;

                    if (DateUtils.compareDate(studyParticipantAssignment.getStudyStartDate(), newStartDate) != 0 && studyParticipantAssignment.getOffTreatmentDate() == null) {
                        offSetDiff = DateUtils.daysBetweenDates(newStartDate, studyParticipantAssignment.getStudyStartDate());
                        studyParticipantAssignment.setStudyStartDate(newStartDate);
                        studyParticipantAssignment.removeSpCrfsIfNoCompletedSchedules();
                        command.assignCrfsToParticipant(false);
                    }
                    if (!studyParticipantAssignment.getArm().getId().equals(command.getArmId())) {
                        for (Arm arm : studyParticipantAssignment.getStudySite().getStudy().getArms()) {
                            if (command.getArmId() != null && arm.getId().equals(command.getArmId())) {
                                studyParticipantAssignment.setArm(arm);
                            }
                        }
                        studyParticipantAssignment.removeSpCrfsIfNoCompletedSchedules();
                        command.assignCrfsToParticipant(true);
                    }

                    boolean doNotCreate = false;
                    if (studyParticipantAssignment.getOffTreatmentDate() != null) {
                        doNotCreate = true;
                    }
                    for (StudyParticipantMode uiMode : studyParticipantAssignment.getStudyParticipantModes()) {
                        for (StudyParticipantMode spMode : command.getStudyParticipantModes()) {
                            if (uiMode.equals(spMode)) {
                                doNotCreate = true;
                                break;
                            }
                        }
                        AppMode mode;
                        for (int i = 0; i < studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems().size() - 1; i++) {
                            mode = studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems().get(i).getMode();
                            if (uiMode.getMode().equals(mode)) {
                                doNotCreate = true;
                                break;
                            }
                        }
                        if (!doNotCreate && uiMode.getMode().equals(AppMode.IVRS)) {
                            for (StudyParticipantCrf spCrf : command.getSelectedStudyParticipantAssignment().getStudyParticipantCrfs()) {
                                for (StudyParticipantCrfSchedule spcSchedule : spCrf.getStudyParticipantCrfSchedules()) {
                                    if (spcSchedule.getIvrsSchedules() == null || spcSchedule.getIvrsSchedules().size() == 0) {
                                        spCrf.createIvrsSchedules(spcSchedule);
                                    }
                                }
                            }
                        }
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