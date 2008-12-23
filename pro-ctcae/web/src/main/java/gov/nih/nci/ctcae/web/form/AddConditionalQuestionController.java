package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfItem;
import gov.nih.nci.ctcae.core.domain.CrfItemDisplayRule;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Dec 22, 2008
 */
public class AddConditionalQuestionController extends AbstractController {


	private FinderRepository finderRepository;

	public AddConditionalQuestionController() {
		setSupportedMethods(new String[]{"GET"});
	}

	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = new ModelAndView("form/ajax/conditions");


		Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");

		ProCtcQuestion proCtcQuestion = finderRepository.findAndInitializeProCtcQuestion(questionId);

		CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

		CrfItem crfItem = createFormCommand.getStudyCrf().getCrf().getCrfItemByQuestion(proCtcQuestion);

		String[] selectedValidValuesIds = StringUtils.commaDelimitedListToStringArray(request.getParameter("selectedValidValues"));

		List<CrfItemDisplayRule> crfItemDisplayRuleList = new ArrayList<CrfItemDisplayRule>();
		for (String id : selectedValidValuesIds) {
			ProCtcValidValue proCtcValidValue = finderRepository.findById(ProCtcValidValue.class, Integer.valueOf(id));
			CrfItemDisplayRule crfItemDisplayRule = new CrfItemDisplayRule();
			crfItemDisplayRule.setPersistable(proCtcValidValue);
			crfItemDisplayRuleList.add(crfItemDisplayRule);

		}
		List<CrfItemDisplayRule> addedCrfItemDisplayRules = crfItem.addCrfItemDisplayRules(crfItemDisplayRuleList);

		modelAndView.addObject("crfItemDisplayRuleList", addedCrfItemDisplayRules);
		modelAndView.addObject("selectedQuestionId", questionId);

		return modelAndView;

	}


	@Required
	public void setFinderRepository(FinderRepository finderRepository) {
		this.finderRepository = finderRepository;
	}
}
