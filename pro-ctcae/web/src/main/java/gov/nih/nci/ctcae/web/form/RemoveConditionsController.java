package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
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

    private ProCtcQuestionRepository proCtcQuestionRepository;

    public RemoveConditionsController() {
        setSupportedMethods(new String[]{"GET"});
    }

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/removeConditions");


        if (!StringUtils.isBlank(request.getParameter("questionId"))) {
            Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");

            ProCtcQuestion proCtcQuestion = proCtcQuestionRepository.findById(questionId);

            CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

            CrfPageItem crfPageItem = createFormCommand.getCrf().getCrfPageItemByQuestion(proCtcQuestion);

            crfPageItem.removeCrfPageItemDisplayRulesByProCtcValidValueIds(request.getParameter("proCtcValidValueId"));
        }
        return modelAndView;

    }


    @Required
    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }
}