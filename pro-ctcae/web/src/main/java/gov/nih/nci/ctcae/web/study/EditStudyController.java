package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.Arm;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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
        for(Arm arm :study.getArms()){
            arm.getTitle();
        }
        StudyCommand studyCommand = new StudyCommand(study);


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


}