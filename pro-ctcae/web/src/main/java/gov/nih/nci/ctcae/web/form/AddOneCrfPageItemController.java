package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class AddOneCrfPageItemController extends AbstractCrfController {


	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		String crfPageIndex = request.getParameter("crfPageIndex");
		Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");

		ModelAndView modelAndView = null;

		if (StringUtils.isBlank(crfPageIndex)) {
			modelAndView = new ModelAndView(new RedirectView("addOneCrfPage?subview=subview&questionId=" + questionId));

		} else {
			modelAndView = new ModelAndView("form/ajax/oneCrfPageItemSection");
			ProCtcQuestion proCtcQuestion = finderRepository.findAndInitializeProCtcQuestion(questionId);
			CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);
			if (proCtcQuestion != null) {

				CRFPage crfPage = createFormCommand.addAnotherPage();

				CrfPageItem crfPageItem = crfPage.removeExistingAndAddNewCrfItem(proCtcQuestion);
				modelAndView.addObject("crfPageItem", crfPageItem);

				modelAndView.addObject("index", crfPage.getCrfPageItems().size() - 1);

				modelAndView.addAllObjects(referenceData(createFormCommand));
			} else {
				logger.error("can not add question because can not find any question for given question id:" + questionId);
				return null;
			}
		}
		return modelAndView;


	}


	public AddOneCrfPageItemController() {
		setSupportedMethods(new String[]{"GET"});

	}

}