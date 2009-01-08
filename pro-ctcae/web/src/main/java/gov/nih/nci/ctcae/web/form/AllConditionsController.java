package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class AllConditionsController extends AbstractController {


	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		String questionsIds = request.getParameter("questionsIds");

		String[] questionsIdsArray = StringUtils.commaDelimitedListToStringArray(questionsIds);

		List<CrfPageItem> selectedCrfPageItems = new LinkedList<CrfPageItem>();

		CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

		for (String questionsId : questionsIdsArray) {
			selectedCrfPageItems.add(createFormCommand.getStudyCrf().getCrf().getCrfPageItemByQuestion(Integer.valueOf(questionsId)));
		}

		ModelAndView modelAndView = new ModelAndView("form/ajax/allConditions");

		Map<String, Object> map = new HashMap<String, Object>();


		map.put("selectedCrfPageItems", selectedCrfPageItems);
		modelAndView.addAllObjects(map);

		return modelAndView;


	}


	public AllConditionsController() {
		setSupportedMethods(new String[]{"GET"});

	}

}