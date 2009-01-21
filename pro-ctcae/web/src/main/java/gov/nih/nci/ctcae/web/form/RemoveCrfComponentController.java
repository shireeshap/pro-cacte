package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Jan 20, 2009
 */
public class RemoveCrfComponentController extends AbstractCrfController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {


        String componentType = request.getParameter("componentType");
        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        ModelAndView modelAndView = new ModelAndView("dummy");

        if (StringUtils.equals(componentType, PRO_CTC_QUESTIONS_COMPONENT)) {
            String[] questionIdsArrays = org.springframework.util.StringUtils.commaDelimitedListToStringArray(request.getParameter("questionIds"));


            createFormCommand.removeQuestionByQuesitonIds(questionIdsArrays);

        }

        modelAndView.addAllObjects(referenceData(createFormCommand));

        return modelAndView;


    }


    public RemoveCrfComponentController() {
        setSupportedMethods(new String[]{"GET"});

    }

}