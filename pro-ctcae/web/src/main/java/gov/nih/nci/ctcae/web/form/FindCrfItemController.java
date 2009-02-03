package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class FindCrfItemController.
 *
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class FindCrfItemController extends AbstractController {


    /**
     * The finder repository.
     */
    private FinderRepository finderRepository;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/questionReviewSection");


        Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");
        Integer displayOrder = ServletRequestUtils.getIntParameter(request, "displayOrder");

        ProCtcQuestion proCtcQuestion = finderRepository.findById(ProCtcQuestion.class, questionId);

        CreateFormCommand command = ControllersUtils.getFormCommand(request);
        CrfPageItem crfPageItem = command.getCrf().getCrfPageItemByQuestion(proCtcQuestion);

        crfPageItem.setProCtcQuestion(proCtcQuestion);
        modelAndView.addObject("crfPageItem", crfPageItem);
        modelAndView.addObject("displayOrder", displayOrder);


        String nextQuestionIndex = request.getParameter("nextQuestionIndex");
        if (!StringUtils.isBlank(nextQuestionIndex)) {
            modelAndView.addObject("nextQuestionIndex", nextQuestionIndex);

        }
        String previousQuestionIndex = request.getParameter("previousQuestionIndex");
        if (!StringUtils.isBlank(previousQuestionIndex)) {
            modelAndView.addObject("previousQuestionIndex", previousQuestionIndex);

        }
        return modelAndView;


    }


    /**
     * Instantiates a new find crf item controller.
     */
    public FindCrfItemController() {
        super();
        setSupportedMethods(new String[]{"GET"});

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
}