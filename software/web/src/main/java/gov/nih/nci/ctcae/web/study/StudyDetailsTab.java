package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueIdentifierForStudyValidator;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

//

/**
 * The Class StudyDetailsTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class StudyDetailsTab extends SecuredTab<StudyCommand> {


    UserRepository userRepository;
    private UniqueIdentifierForStudyValidator uniqueIdentifierForStudyValidator;

    /**
     * Instantiates a new study details tab.
     */
    public StudyDetailsTab() {
        super("study.tab.study_details", "study.tab.study_details", "study/study_details");
    }


    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_CREATE_STUDY;

    }

    @Override
    public void validate(StudyCommand command, Errors errors) {
        StudyCommand studyCommand = (StudyCommand) command;
        String studyId = "";
        if (studyCommand.getStudy().getId() != null) {
            studyId = (studyCommand.getStudy().getId()).toString();
        }
        boolean isunique = uniqueIdentifierForStudyValidator.validateUniqueIdentifier(studyId, studyCommand.getStudy().getAssignedIdentifier());
        if (isunique) {
            errors.rejectValue("study.assignedIdentifier", "study.unique_assignedIdentifier", "study.unique_assignedIdentifier");
        }
    }

    @Override
    public void onBind(HttpServletRequest request, StudyCommand command, Errors errors) {
        super.onBind(request, command, errors);    //To change body of overridden methods use File | Settings | File Templates.
        if(request.getParameter("appModes")==null){
            command.setAppModes(null);
        }
    }

    @Override
    public void onDisplay(HttpServletRequest httpServletRequest, StudyCommand studyCommand) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.isAdmin()) {
            studyCommand.setAdmin(true);
        } else {
            ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);
            if (clinicalStaff != null) {
                List<Organization> organizationsWithCCARole = clinicalStaff.getOrganizationsWithCCARole();
                if (organizationsWithCCARole == null || organizationsWithCCARole.size() == 0) {
                    throw new CtcAeSystemException("Logged in user is not a CCA on any organization.");
                }
                if (organizationsWithCCARole.size() == 1) {
                    studyCommand.getStudy().getStudySponsor().setOrganization(organizationsWithCCARole.get(0));
                }
                studyCommand.setOrganizationsWithCCARole(organizationsWithCCARole);

            } else {
                throw new CtcAeSystemException("Logged in user is not a valid clinical staff");
            }
        }

        if (!studyCommand.getStudy().getStudyModes().isEmpty()) {
            List<String> appModes = new ArrayList<String>();
            for (StudyMode studyMode : studyCommand.getStudy().getStudyModes()) {
                appModes.add(studyMode.getMode().getName());
            }
            studyCommand.setAppModes(appModes.toArray(new String[]{}));
        }

    }

    @Override
    public Map<String, Object> referenceData() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> appModes = new ArrayList<String>();

        for (AppMode appMode : AppMode.values()) {
            appModes.add(appMode.getDisplayName());
        }

        map.put("appModes", appModes);
        return map;
    }

    @Override
    public void postProcess(HttpServletRequest httpServletRequest, StudyCommand studyCommand, Errors errors) {
        super.postProcess(httpServletRequest, studyCommand, errors);
        Integer callBackHour = ServletRequestUtils.getIntParameter(httpServletRequest, "call_back_hour", 0);
        Integer callBackFrequency = ServletRequestUtils.getIntParameter(httpServletRequest, "call_back_frequency", 0);
        studyCommand.getStudy().setCallBackHour(callBackHour);
        studyCommand.getStudy().setCallBackFrequency(callBackFrequency);

        studyCommand.getStudy().getStudyModes().clear();
        if (studyCommand.getAppModes() != null) {
            for (String string : studyCommand.getAppModes()) {
                AppMode appMode = AppMode.valueOf(string);
                StudyMode studyMode = new StudyMode();
                studyMode.setMode(appMode);
                studyCommand.getStudy().addStudyMode(studyMode);
            }
        }

        List<Arm> armsToRemove = new ArrayList<Arm>();
        for(int i = 0; i < studyCommand.getArmIndicesToRemove().size(); i++){
        	Integer armIndex = Integer.valueOf(studyCommand.getArmIndicesToRemove().get(i));
            Arm armToRemove = studyCommand.getStudy().getArms().get(armIndex);
            
            FormArmSchedule fas;
            if (studyCommand.getStudy().getCrfs().size() > 0) {
                for (CRF crf : studyCommand.getStudy().getCrfs()) {
                    fas = crf.getFormArmScheduleForArm(armToRemove);
                    crf.getFormArmSchedules().remove(fas);
                }
            }
            armsToRemove.add(armToRemove);
        }
        for(Arm armToRemove : armsToRemove){
        	studyCommand.getStudy().getArms().remove(armToRemove);
        }
        studyCommand.getArmIndicesToRemove().clear();
        
        if (studyCommand.getStudy().getArms().size() == 0) {
            Study study = studyCommand.getStudy();
            Arm arm = new Arm();
            arm.setTitle("Default Arm");
            arm.setDescription("This is a default arm on the study.");
            arm.setDefaultArm(true);
            study.addArm(arm);
        } else {
        	List<Arm> defaultArmsToBeDeleted = new ArrayList<Arm>();
            if (studyCommand.getStudy().getNonDefaultArms().size() > 0) {
                for (Iterator<Arm> arm = studyCommand.getStudy().getArms().iterator(); arm.hasNext();) {
                    Arm arm1 = arm.next();
                    if (arm1.isDefaultArm()) {
                    	defaultArmsToBeDeleted.add(arm1);
                    }
                }
                for(Arm arm : defaultArmsToBeDeleted){
                	studyCommand.getStudy().getArms().remove(arm);
                }
            }
        }
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUniqueIdentifierForStudyValidator(UniqueIdentifierForStudyValidator uniqueIdentifierForStudyValidator) {
        this.uniqueIdentifierForStudyValidator = uniqueIdentifierForStudyValidator;
    }
}
