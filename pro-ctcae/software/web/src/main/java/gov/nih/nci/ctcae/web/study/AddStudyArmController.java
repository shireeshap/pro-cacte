package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 *         Date: Jul 14, 2009
 */
public class AddStudyArmController extends AbstractController {

    public AddStudyArmController() {
           super();
           setSupportedMethods(new String[]{"GET"});

       }


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("study/ajax/oneStudyArmSection");

        StudyCommand studyCommand = ControllersUtils.getStudyCommand(request);
        Study study = studyCommand.getStudy();
        Arm arm = new Arm();
        study.addArm(arm);
        if (study.getCrfs().size()>0) {
            for (CRF crf : study.getCrfs()) {
                crf.addFormArmSchedule(arm);
            }
        }

        int index = study.getArms().size() - 1;
        modelAndView.addObject("index", index);
        modelAndView.addObject("arm", arm);
        return modelAndView;
    }


}
