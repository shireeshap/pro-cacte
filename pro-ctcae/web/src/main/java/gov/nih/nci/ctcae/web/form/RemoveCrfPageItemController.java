package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Jan 20, 2009
 */
public class RemoveCrfPageItemController extends AbstractCrfController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");

        ModelAndView modelAndView = new ModelAndView("dummy");

        ProCtcQuestion proCtcQuestion = finderRepository.findAndInitializeProCtcQuestion(questionId);
        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);
        if (proCtcQuestion != null) {

            createFormCommand.getCrf().removeCrfPageItemByQuestion(proCtcQuestion);
            modelAndView.addAllObjects(referenceData(createFormCommand));
        } else {
            logger.error("can not add question because can not find any question for given question id:" + questionId);
            return null;
        }

        return modelAndView;


    }


    public RemoveCrfPageItemController() {
        setSupportedMethods(new String[]{"GET"});

    }

}