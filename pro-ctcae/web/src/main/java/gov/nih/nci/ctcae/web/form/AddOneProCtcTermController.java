package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
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
 * @crated Dec 11, 2008
 */
public class AddOneProCtcTermController extends AbstractController {


	private ProCtcTermRepository proCtcTermRepository;

	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = new ModelAndView("form/ajax/oneProCtcTermSection");


		Integer proCtcTermId = ServletRequestUtils.getIntParameter(request, "proCtcTermId");

		ProCtcTerm proCtcTerm = proCtcTermRepository.findAndInitializeTerm(proCtcTermId);

		CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);
		if (proCtcTerm != null) {

			List<CRFPage> crfPages = createFormCommand.getStudyCrf().getCrf().getCrfPages();
			int index = 0;
			//crfPages.size() - 1;

			CRFPage crfPage = crfPages.get(index);


			List<CrfPageItem> addedCrfPageItems = crfPage.removeExistingAndAddNewCrfItem(proCtcTerm);
			modelAndView.addObject("crfPageItems", addedCrfPageItems);
			modelAndView.addObject("responseRequired", ListValues.getResponseRequired());
			modelAndView.addObject("crfPageIndex", index);
			modelAndView.addObject("crfItemAllignments", ListValues.getCrfItemAllignments());
			modelAndView.addObject("selectedCrfPageItems", createFormCommand.getStudyCrf().getCrf().getAllCrfPageItems());
		} else {
			logger.error("can not add proCtcTerm because pro ctc term is null for id:" + proCtcTermId);
			return null;
		}

		return modelAndView;


	}


	public AddOneProCtcTermController() {
		setSupportedMethods(new String[]{"GET"});

	}

	@Required
	public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
		this.proCtcTermRepository = proCtcTermRepository;
	}
}