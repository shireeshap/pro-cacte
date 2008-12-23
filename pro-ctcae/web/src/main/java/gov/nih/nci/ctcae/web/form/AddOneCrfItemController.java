package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfItem;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class AddOneCrfItemController extends AbstractController {


	private FinderRepository finderRepository;

	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = new ModelAndView("form/ajax/oneCrfItemSection");


		Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");


		ProCtcQuestion proCtcQuestion = finderRepository.findAndInitializeProCtcQuestion(questionId);
		CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);
		if (proCtcQuestion != null) {
			CrfItem crfItem = createFormCommand.getStudyCrf().getCrf().removeExistingAndAddNewCrfItem(proCtcQuestion);
			modelAndView.addObject("crfItem", crfItem);
			int index = createFormCommand.getStudyCrf().getCrf().getCrfItems().size() - 1;
			modelAndView.addObject("index", index);
			modelAndView.addObject("responseRequired", ListValues.getResponseRequired());
			modelAndView.addObject("crfItemAllignments", ListValues.getCrfItemAllignments());
			modelAndView.addObject("selectedCrfItems", createFormCommand.getStudyCrf().getCrf().getCrfItems());

		} else {
			logger.error("can not add question because can not find any question for given question id:" + questionId);
			return null;
		}

		return modelAndView;


	}


	public AddOneCrfItemController() {
		setSupportedMethods(new String[]{"GET"});

	}

	@Required
	public void setFinderRepository(FinderRepository finderRepository) {
		this.finderRepository = finderRepository;
	}
}