package gov.nih.nci.ctcae.web.form;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.CrfItem;
import gov.nih.nci.ctcae.core.repository.CommonRepository;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class AddOneQuestionController extends AbstractController {


    private CommonRepository commonRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/oneQustionSection");


        Integer questionId = ServletRequestUtils.getIntParameter(request, "questionId");

        ProCtcTerm proCtcTerm = commonRepository.findById(ProCtcTerm.class, questionId);
        if (proCtcTerm != null) {
            CreateFormCommand createFormCommand = FormControllersUtils.getFormCommand(request);
            CrfItem crfItem = new CrfItem();
            crfItem.setProCtcTerm(proCtcTerm);
            createFormCommand.getCrf().addCrfItem(crfItem);
            modelAndView.addObject("crfItem", crfItem);
        } else {
            logger.error("can not add question because pro ctc term is null for id:" + questionId);
        }

        return modelAndView;


    }


    public AddOneQuestionController() {
        setSupportedMethods(new String[]{"GET"});

    }

    @Required
    public void setCommonRepository(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }
}