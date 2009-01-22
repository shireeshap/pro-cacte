package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Dec 11, 2008
 */
public class AddCrfComponentController extends AbstractCrfController {


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String componentType = request.getParameter("componentType");

        ModelAndView modelAndView = null;

        if (StringUtils.equals(componentType, PRO_CTC_TERM_COMPONENT)) {
            Integer proCtcTermId = ServletRequestUtils.getIntParameter(request, "proCtcTermId");

            ProCtcTerm proCtcTerm = proCtcTermRepository.findAndInitializeTerm(proCtcTermId);

            CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

            if (proCtcTerm != null) {
                Object object = createFormCommand.addProCtcTerm(proCtcTerm);

                if (object instanceof CRFPage) {
                    modelAndView = new ModelAndView("form/ajax/oneCrfPageSection");
                    CRFPage crfPage = (CRFPage) object;
                    modelAndView.addObject("crfPage", crfPage);
                    modelAndView.addObject("crfPageNumber", crfPage.getPageNumber());

                } else {
                    List<CrfPageItem> addedCrfPageItems = (List<CrfPageItem>) object;
                    modelAndView = new ModelAndView("form/ajax/oneProCtcTermSection");

                    if (!addedCrfPageItems.isEmpty()) {
                        CRFPage crfPage = addedCrfPageItems.get(0).getCrfPage();
                        Integer startIndex = crfPage.getCrfPageItems().size() - addedCrfPageItems.size();
                        modelAndView.addObject("crfPageNumber", crfPage.getPageNumber());
                        modelAndView.addObject("startIndex", startIndex);
                    }

                    modelAndView.addObject("crfPageItems", addedCrfPageItems);
                }

                modelAndView.addAllObjects(referenceData(createFormCommand));

            }
//            else if (proCtcTerm != null) {
//
//                String crfPageNumber = request.getParameter("crfPageNumber");
//
//                CRFPage crfPage = createFormCommand.getCrf().getCrfPageByPageNumber(Integer.valueOf(crfPageNumber));
//
//                List<CrfPageItem> addedCrfPageItems = crfPage.removeExistingAndAddNewCrfItem(proCtcTerm);
//                modelAndView.addObject("crfPageItems", addedCrfPageItems);
//
//
//                modelAndView.addAllObjects(referenceData(createFormCommand));
//            }
            else {
                logger.error("can not add proCtcTerm because pro ctc term is null for id:" + proCtcTermId);
                return null;
            }

        }
//        else if (StringUtils.isBlank(crfPageNumber)) {
//            // modelAndView = new ModelAndView(new RedirectView("addOneCrfPage?subview=subview&proCtcTermId=" + proCtcTermId));
//
//        }
        return modelAndView;


    }


    public AddCrfComponentController() {
        setSupportedMethods(new String[]{"GET"});

    }

}