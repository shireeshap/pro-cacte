package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.Errors;

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
        if (studyCommand.getStudy().getArms().size() == 0) {
            Study study = studyCommand.getStudy();
            Arm arm = new Arm();
            arm.setTitle("Default Arm");
            arm.setDescription("This is a default arm on the study.");
            study.addArm(arm);
        }

    }
}
