package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Dec 22, 2008
 */
public class RemoveConditionsController extends AbstractController {


    private FinderRepository finderRepository;

    public RemoveConditionsController() {
        setSupportedMethods(new String[]{"GET"});
    }

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/removeConditions");


        if (!StringUtils.isBlank(request.getParameter("questionId"))) {
            Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");

            ProCtcQuestion proCtcQuestion = finderRepository.findAndInitializeProCtcQuestion(questionId);

            CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

            CrfPageItem crfPageItem = createFormCommand.getCrf().getCrfPageItemByQuestion(proCtcQuestion);

            crfPageItem.removeCrfPageItemDisplayRulesByProCtcValidValueIds(request.getParameter("proCtcValidValueId"));
        } else if (!StringUtils.isBlank(request.getParameter("conditionalTriggeredQuestionId"))) {

            Integer conditionalTriggeredQuestionId = ServletRequestUtils.getIntParameter(request, "conditionalTriggeredQuestionId");

            ProCtcQuestion proCtcQuestion = finderRepository.findAndInitializeProCtcQuestion(conditionalTriggeredQuestionId);

            CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

            createFormCommand.getCrf().updateCrfPageItemDisplayRules(proCtcQuestion);

        } else if (!StringUtils.isBlank(request.getParameter("selectedCrfPageNumber"))) {

            Integer selectedCrfPageNumber = ServletRequestUtils.getIntParameter(request, "selectedCrfPageNumber");


            CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);


            createFormCommand.getCrf().updateCrfPageItemDisplayRules(selectedCrfPageNumber);

        }

        return modelAndView;

    }


    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}