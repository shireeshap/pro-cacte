package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.Errors;
import org.apache.commons.lang.StringUtils;

//
/**
 * The Class StudyDetailsTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class StudyDetailsTab extends SecuredTab<StudyCommand> {

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
    public void postProcess(HttpServletRequest httpServletRequest, StudyCommand studyCommand, Errors errors) {
        super.postProcess(httpServletRequest, studyCommand, errors);


        if (!StringUtils.isBlank(studyCommand.getArmIndexToRemove())) {
            Integer armIndex = Integer.valueOf(studyCommand.getArmIndexToRemove());
            Arm arm = studyCommand.getStudy().getArms().get(armIndex);
            studyCommand.getStudy().getArms().remove(arm);
            studyCommand.setArmIndexToRemove("");
        } else {
            if (studyCommand.getStudy().getArms().size() == 0) {
                Study study = studyCommand.getStudy();
                Arm arm = new Arm();
                arm.setTitle("Default Arm");
                arm.setDescription("This is a default arm on the study.");
                arm.setDefaultArm(true);
                study.addArm(arm);
            } else {
                if (studyCommand.getStudy().getNonDefaultArms().size() > 0) {
                    for (Arm arm : studyCommand.getStudy().getArms()) {
                        if (arm.isDefaultArm()) {
                            studyCommand.getStudy().getArms().remove(arm);
                        }
                    }
                }
            }
        }
    }
}
