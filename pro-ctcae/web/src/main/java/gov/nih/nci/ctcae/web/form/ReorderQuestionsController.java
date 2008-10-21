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

import java.util.Collection;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class ReorderQuestionsController extends AbstractController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/ajax/reorderSucessfull");


        CreateFormCommand createFormCommand = FormControllersUtils.getFormCommand(request);

        try {
            int[] order = ServletRequestUtils.getRequiredIntParameters(request, "questionText[]");

            List<CrfItem> crfItems = createFormCommand.getCrf().getCrfItems();
            for (int i = 0; i < order.length; i++) {
                crfItems.get(order[i]).setDisplayOrder(i);

            }
        } catch (Exception e) {
            logger.error("can not reorder");
        }


        return modelAndView;


    }


    public ReorderQuestionsController() {
        setSupportedMethods(new String[]{"GET"});

    }


}