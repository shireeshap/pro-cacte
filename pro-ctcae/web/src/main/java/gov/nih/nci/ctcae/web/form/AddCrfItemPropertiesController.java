package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfItem;
import gov.nih.nci.ctcae.core.domain.CrfItemAllignment;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Nov 21, 2008
 */
public class AddCrfItemPropertiesController extends AbstractController {


    private FinderRepository finderRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        if (request.getMethod().toUpperCase().equals("GET")) {
            ModelAndView modelAndView = new ModelAndView("form/ajax/crfItemProperties");


            Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");

            ProCtcQuestion proCtcQuestion = finderRepository.findById(ProCtcQuestion.class, questionId);
            CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);
            CrfItem crfItem = createFormCommand.getStudyCrf().getCrf().getCrfItemByQuestion(proCtcQuestion);
            modelAndView.addObject("crfItem", crfItem);
            modelAndView.addObject("responseRequired", ListValues.getResponseRequired());
            modelAndView.addObject("crfItemAllignments", ListValues.getCrfItemAllignments());

            return modelAndView;
        } else {
            Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");


            String instructions = request.getParameter("instructions");
            String allignment = request.getParameter("crfItemAllignment");
            String responseRequired = request.getParameter("responseRequired");

            ProCtcQuestion proCtcQuestion = finderRepository.findById(ProCtcQuestion.class, questionId);
            CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);
            CrfItem crfItem = createFormCommand.getStudyCrf().getCrf().getCrfItemByQuestion(proCtcQuestion);

            crfItem.setInstructions(instructions);

            crfItem.setCrfItemAllignment(CrfItemAllignment.getByCode(allignment));
            crfItem.setResponseRequired(Boolean.valueOf(responseRequired));

            return null;
        }

    }


    public AddCrfItemPropertiesController() {
        setSupportedMethods(new String[]{"GET", "POST"});

    }

    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}