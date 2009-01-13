package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Dec 11, 2008
 */
public class AddOneProCtcTermController extends AbstractCrfController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String crfPageNumber = request.getParameter("crfPageNumber");
        Integer proCtcTermId = ServletRequestUtils.getIntParameter(request, "proCtcTermId");

        ModelAndView modelAndView = null;

        if (StringUtils.isBlank(crfPageNumber)) {
            modelAndView = new ModelAndView(new RedirectView("addOneCrfPage?subview=subview&proCtcTermId=" + proCtcTermId));

        } else {

            modelAndView = new ModelAndView("form/ajax/oneProCtcTermSection");


            ProCtcTerm proCtcTerm = proCtcTermRepository.findAndInitializeTerm(proCtcTermId);

            CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);
            if (proCtcTerm != null) {


                CRFPage crfPage = createFormCommand.getCrf().getCrfPageByPageNumber(Integer.valueOf(crfPageNumber));

                List<CrfPageItem> addedCrfPageItems = crfPage.removeExistingAndAddNewCrfItem(proCtcTerm);
                modelAndView.addObject("crfPageItems", addedCrfPageItems);


                modelAndView.addAllObjects(referenceData(createFormCommand));
            } else {
                logger.error("can not add proCtcTerm because pro ctc term is null for id:" + proCtcTermId);
                return null;
            }
        }
        return modelAndView;


    }


    public AddOneProCtcTermController() {
        setSupportedMethods(new String[]{"GET"});

    }

}