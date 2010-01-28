package gov.nih.nci.ctcae.web.form;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;

/**
 * @author Mehul Gulati
 *         Date: Jul 30, 2009
 */
public class ViewFormController extends AbstractController {

    private CRFRepository crfRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        String crfId = request.getParameter("crfId");
        CRF crf = crfRepository.findById(Integer.parseInt(crfId));
        CreateFormCommand command = new CreateFormCommand();
        command.setCrf(crf);
//        command.setProCtcAERulesService(proCtcAERulesService);
//        command.initializeRulesForForm();
        ModelAndView modelAndView = new ModelAndView("form/viewForm");
        modelAndView.addObject("crf", crf);
//        modelAndView.addObject("rules", command.getFormOrStudySiteRules());
        return modelAndView;
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }


}
