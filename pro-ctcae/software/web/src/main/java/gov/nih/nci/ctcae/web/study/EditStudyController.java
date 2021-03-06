package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class StudyController.
 *
 * @author Vinay Kumar
 * @since Oct 17, 2008
 */
public class EditStudyController extends StudyController {

    private static final String STUDY_ID = "studyId";

    @Override
    protected Object formBackingObject(final HttpServletRequest request) throws ServletException {
        Integer studyId = ServletRequestUtils.getRequiredIntParameter(request, STUDY_ID);

        Study study = studyRepository.findById(Integer.valueOf(studyId));
        StudyCommand studyCommand = new StudyCommand(study);

        for (Arm arm : study.getArms()) {
            arm = genericRepository.findById(Arm.class, arm.getId());
            if (arm.isDefaultArm() && (arm.getStudyParticipantAssignments().size() > 0 || arm.getFormArmSchedules().size() > 0)) {
                studyCommand.setActiveDefaultArm(true);
            }
            if(!arm.isDefaultArm()){
            	studyCommand.getNonDefaultArms().add(arm);
            }
        }
        
        studyCommand.initailizeStudy();
        return studyCommand;
    }

    @Override
    protected void layoutTabs(final Flow<StudyCommand> flow) {
        flow.addTab(new EmptyStudyTab("study.tab.overview", "study.tab.overview", "study/study_reviewsummary"));
        flow.addTab(new StudyDetailsTab());
        flow.addTab(new StudySitesTab());
        flow.addTab(new StudyClinicalStaffTab());
        flow.addTab(new StudySiteClinicalStaffTab());
    }

    @Override
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return super.processFinish(request, response, command, errors);
    }
    
    @Override
    protected void postProcessPage(HttpServletRequest request, Object oCommand, Errors errors, int page) throws Exception{
    	super.postProcessPage(request, oCommand, errors, page);
    	((StudyCommand) oCommand).initailizeStudy();
    }

    @Override
    protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
        return super.getTargetPage(request, command, errors, currentPage);
    }
}