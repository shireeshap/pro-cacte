package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
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
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class AddOneCrfPageItemController extends AbstractController {


	private FinderRepository finderRepository;

	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = new ModelAndView("form/ajax/oneCrfPageItemSection");


		Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");


		ProCtcQuestion proCtcQuestion = finderRepository.findAndInitializeProCtcQuestion(questionId);
		CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);
		if (proCtcQuestion != null) {

			List<CRFPage> crfPages = createFormCommand.getStudyCrf().getCrf().getCrfPages();

			int index = 0;
			//crfPages.size() - 1;

			CRFPage crfPage = crfPages.get(index);

			CrfPageItem crfPageItem = crfPage.removeExistingAndAddNewCrfItem(proCtcQuestion);
			modelAndView.addObject("crfPageItem", crfPageItem);
			modelAndView.addObject("crfPageIndex", index);
			modelAndView.addObject("index", crfPage.getCrfPageItems().size()-1);
			modelAndView.addObject("responseRequired", ListValues.getResponseRequired());
			modelAndView.addObject("crfItemAllignments", ListValues.getCrfItemAllignments());
			modelAndView.addObject("selectedCrfPageItems", createFormCommand.getStudyCrf().getCrf().getAllCrfPageItems());

		} else {
			logger.error("can not add question because can not find any question for given question id:" + questionId);
			return null;
		}

		return modelAndView;


	}


	public AddOneCrfPageItemController() {
		setSupportedMethods(new String[]{"GET"});

	}

	@Required
	public void setFinderRepository(FinderRepository finderRepository) {
		this.finderRepository = finderRepository;
	}
}