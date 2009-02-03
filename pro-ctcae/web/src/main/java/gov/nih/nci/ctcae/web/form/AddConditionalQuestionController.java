package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.CrfPageItemDisplayRule;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
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

//
/**
 * The Class AddConditionalQuestionController.
 *
 * @author Vinay Kumar
 * @crated Dec 22, 2008
 */
public class AddConditionalQuestionController extends AbstractController {


    /**
     * The pro ctc question repository.
     */
    private ProCtcQuestionRepository proCtcQuestionRepository;

    /**
     * The finder repository.
     */
    private FinderRepository finderRepository;

    /**
     * Instantiates a new adds the conditional question controller.
     */
    public AddConditionalQuestionController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/conditions");


        Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");

        ProCtcQuestion proCtcQuestion = proCtcQuestionRepository.findById(questionId);

        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        CrfPageItem crfPageItem = createFormCommand.getCrf().getCrfPageItemByQuestion(proCtcQuestion);

        String[] selectedValidValuesIds = StringUtils.commaDelimitedListToStringArray(request.getParameter("selectedValidValues"));

        List<ProCtcValidValue> proCtcValidValues = new ArrayList<ProCtcValidValue>();
        for (String id : selectedValidValuesIds) {
            ProCtcValidValue proCtcValidValue = finderRepository.findById(ProCtcValidValue.class, Integer.valueOf(id));
            CrfPageItemDisplayRule crfPageItemDisplayRule = new CrfPageItemDisplayRule();
            crfPageItemDisplayRule.setProCtcValidValue(proCtcValidValue);
            proCtcValidValues.add(proCtcValidValue);

        }
        List<CrfPageItemDisplayRule> addedCrfPageItemDisplayRules = crfPageItem.addCrfPageItemDisplayRules(proCtcValidValues);

        modelAndView.addObject("crfPageItemDisplayRules", addedCrfPageItemDisplayRules);
        modelAndView.addObject("selectedQuestionId", questionId);

        return modelAndView;

    }


    /**
     * Sets the finder repository.
     *
     * @param finderRepository the new finder repository
     */
    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    /**
     * Sets the pro ctc question repository.
     *
     * @param proCtcQuestionRepository the new pro ctc question repository
     */
    @Required
    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }
}
