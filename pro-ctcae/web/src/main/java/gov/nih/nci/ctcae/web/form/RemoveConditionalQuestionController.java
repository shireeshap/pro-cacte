package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfItem;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
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
public class RemoveConditionalQuestionController extends AbstractController {


	private FinderRepository finderRepository;

	public RemoveConditionalQuestionController() {
		setSupportedMethods(new String[]{"GET"});
	}

	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = new ModelAndView("form/ajax/removeConditions");


		Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");

		ProCtcQuestion proCtcQuestion = finderRepository.findAndInitializeProCtcQuestion(questionId);

		CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

		CrfItem crfItem = createFormCommand.getStudyCrf().getCrf().getCrfItemByQuestion(proCtcQuestion);

		crfItem.removeCrfItemDisplayRulesByIds(request.getParameter("proCtcValidValueId"));


		return modelAndView;

	}


	@Required
	public void setFinderRepository(FinderRepository finderRepository) {
		this.finderRepository = finderRepository;
	}
}