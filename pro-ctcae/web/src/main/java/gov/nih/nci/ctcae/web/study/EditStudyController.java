package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.Arm;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.Transient;

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
        }
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
    protected int getInitialPage(HttpServletRequest request, Object command) {
        if (!StringUtils.isBlank(request.getParameter("tab"))) {
            return Integer.parseInt(request.getParameter("tab"));
        }
        return super.getInitialPage(request, command);
    }

    @Override
    protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
        int targetPage = super.getTargetPage(request, command, errors, currentPage);
        if (currentPage == targetPage) {
            targetPage = 0;
        }
        return targetPage;
    }
}