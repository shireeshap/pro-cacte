package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

//
/**
 * The Class StudyController.
 *
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class EditStudyController extends StudyController {

    private static final String STUDY_ID = "studyId";

    @Override
    protected Object formBackingObject(final HttpServletRequest request) throws ServletException {
        Integer studyId = ServletRequestUtils.getRequiredIntParameter(request, STUDY_ID);

        Study study = studyRepository.findById(Integer.valueOf(studyId));
        StudyCommand studyCommand = new StudyCommand(study);


        return studyCommand;
    }


}