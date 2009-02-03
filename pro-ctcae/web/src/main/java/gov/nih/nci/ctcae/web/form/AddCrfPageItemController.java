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

// TODO: Auto-generated Javadoc
/**
 * The Class AddCrfPageItemController.
 * 
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class AddCrfPageItemController extends AbstractCrfController {


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String crfPageNumber = request.getParameter("crfPageNumber");
        Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");

        ModelAndView modelAndView = null;

        if (StringUtils.isBlank(crfPageNumber)) {
            modelAndView = new ModelAndView(new RedirectView("addOneCrfPage?subview=subview&questionId=" + questionId));

        } else {
            modelAndView = new ModelAndView("form/ajax/oneCrfPageItemSection");
            ProCtcQuestion proCtcQuestion = proCtcQuestionRepository.findById(questionId);
            CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);
            if (proCtcQuestion != null) {

                CRFPage crfPage = createFormCommand.getCrf().getCrfPageByPageNumber(Integer.valueOf(crfPageNumber));

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


    /**
     * Instantiates a new adds the crf page item controller.
     */
    public AddCrfPageItemController() {
        setSupportedMethods(new String[]{"GET"});

    }

}