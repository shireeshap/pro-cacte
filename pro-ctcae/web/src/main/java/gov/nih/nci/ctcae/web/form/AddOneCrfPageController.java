package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class AddOneCrfPageController extends AbstractCrfController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/oneCrfPageSection");


        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        CRFPage crfPage = null;

        if (!StringUtils.isBlank(request.getParameter("questionId"))) {
            Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");
            ProCtcQuestion proCtcQuestion = proCtcQuestionRepository.findById(questionId);

            crfPage = createFormCommand.addCrfPage(proCtcQuestion);

        }
//        else if (!StringUtils.isBlank(request.getParameter("proCtcTermId"))) {
//            Integer proCtcTermId = ServletRequestUtils.getIntParameter(request, "proCtcTermId");
//
//            ProCtcTerm proCtcTerm = proCtcTermRepository.findAndInitializeTerm(proCtcTermId);
//
//            crfPage = createFormCommand.addCrfPage(proCtcTerm);
//
//        } else {
//            crfPage = createFormCommand.addCrfPage();
//        }

        modelAndView.addObject("crfPage", crfPage);

        modelAndView.addAllObjects(referenceData(createFormCommand));

        return modelAndView;


    }


    public AddOneCrfPageController() {
        setSupportedMethods(new String[]{"GET"});

    }

}