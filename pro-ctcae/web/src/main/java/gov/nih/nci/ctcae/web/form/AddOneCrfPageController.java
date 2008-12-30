package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class AddOneCrfPageController extends AbstractController {


	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = new ModelAndView("form/ajax/oneCrfPageSection");


		CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

		CRFPage crfPage = createFormCommand.addAnotherPage();

		modelAndView.addObject("crfPage", crfPage);
		int index = createFormCommand.getStudyCrf().getCrf().getCrfPages().size() - 1;
		modelAndView.addObject("index", index);



		return modelAndView;


	}


	public AddOneCrfPageController() {
		setSupportedMethods(new String[]{"GET"});

	}

}