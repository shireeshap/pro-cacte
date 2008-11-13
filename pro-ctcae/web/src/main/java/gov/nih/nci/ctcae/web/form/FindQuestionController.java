package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.apache.commons.lang.StringUtils;
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
public class FindQuestionController extends AbstractController {


    private FinderRepository finderRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/questionReviewSection");


        Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");
        Integer displayOrder = ServletRequestUtils.getIntParameter(request, "displayOrder");

        ProCtcQuestion proCtcQuestion = finderRepository.findById(ProCtcQuestion.class, questionId);
        if (proCtcQuestion != null) {
            modelAndView.addObject("proCtcQuestion", proCtcQuestion);
            modelAndView.addObject("displayOrder", displayOrder);

        } else {
            logger.error("can not find question because pro ctc question is null for id:" + questionId);
            return null;
        }

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


    public FindQuestionController() {
        setSupportedMethods(new String[]{"GET"});

    }

    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}