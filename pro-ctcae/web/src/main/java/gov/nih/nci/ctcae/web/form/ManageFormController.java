package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class ManageFormController extends AbstractController {

	private FinderRepository finderRepository;


	public ManageFormController() {
		setSupportedMethods(new String[]{"GET"});
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {


		ModelAndView modelAndView = new ModelAndView("form/manageForm");
		String studyId = request.getParameter("studyId");
		if (!StringUtils.isBlank(studyId)) {
			Study study = finderRepository.findById(Study.class, Integer.parseInt(studyId));
			if (study != null) {
				modelAndView.getModel().put("study", study);
			}
		}
		return modelAndView;
	}

	@Required
	public void setFinderRepository(FinderRepository finderRepository) {
		this.finderRepository = finderRepository;
	}

}
