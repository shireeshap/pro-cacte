package gov.nih.nci.ctcae.web.study;

import java.util.Iterator;

import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.FormArmSchedule;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.ControllersUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

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
        
        String action = request.getParameter("action");
        if ("deleteArm".equals(action)) {
        	deleteArm(request, studyCommand);
            return null;
        }
        
        Arm arm = addArm(request, studyCommand);

        int index = studyCommand.getNonDefaultArms().size() - 1;
        modelAndView.addObject("index", index);
        modelAndView.addObject("arm", arm);
        return modelAndView;
    }
    
    private void deleteArm(HttpServletRequest request, StudyCommand studyCommand) {
        String armIndex = request.getParameter("armIndex");
        studyCommand.getArmIndicesToRemove().add(armIndex);
    }
    
    private Arm addArm(final HttpServletRequest request, StudyCommand studyCommand){
        Arm arm = new Arm();
        arm.setTitle(" ");
        studyCommand.getNonDefaultArms().add(arm);
        return arm;
    }


}
