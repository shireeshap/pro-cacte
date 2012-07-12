package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcTermComparator;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//
/**
 * The Class AddCrfComponentController.
 *
 * @author Vinay Kumar
 * @since Dec 11, 2008
 */
public class AddCrfComponentController extends AbstractCrfController {


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String componentType = request.getParameter("componentType");

        ModelAndView modelAndView = null;

        if (StringUtils.equals(componentType, PRO_CTC_TERM_COMPONENT)) {
            Integer proCtcTermId = ServletRequestUtils.getIntParameter(request, "proCtcTermId");

            ProCtcTerm proCtcTerm = proCtcTermRepository.findById(proCtcTermId);

            CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

            if (proCtcTerm != null) {
                Object object = createFormCommand.addProCtcTerm(proCtcTerm);

                if (object instanceof CRFPage) {
                    modelAndView = new ModelAndView("form/ajax/oneCrfPageSection");
                    CRFPage crfPage = (CRFPage) object;
                    crfPage.setDescription(proCtcTerm.getProCtcTermVocab().getTermEnglish());
                    crfPage.setProCtcTerm(proCtcTerm);
                    modelAndView.addObject("crfPage", crfPage);

                } else {
                    List<CrfPageItem> addedCrfPageItems = (List<CrfPageItem>) object;
                    modelAndView = new ModelAndView("form/ajax/oneProCtcTermSection");
                    modelAndView.addObject("crfPageItems", addedCrfPageItems);
                }

                modelAndView.addAllObjects(referenceData(createFormCommand));

            } else {
                logger.error("can not add proCtcTerm because pro ctc term is null for id:" + proCtcTermId);
                return null;
            }

        } else if (StringUtils.equals(componentType, CTC_CATEGORY_COMPONENT)) {
            Integer ctcCategoryId = ServletRequestUtils.getIntParameter(request, "ctcCategoryId");


            ProCtcTermQuery query = new ProCtcTermQuery();
            query.filterByCtcTermHavingQuestionsOnly();
            query.filterByCtcCategoryId(ctcCategoryId);
            query.filterByCurrency();
            query.filterByCoreItemsOnly();
            List<ProCtcTerm> proCtcTerms = new ArrayList<ProCtcTerm>(proCtcTermRepository.find(query));
            Collections.sort(proCtcTerms, new ProCtcTermComparator());

            List<CRFPage> addedCrfPages = new ArrayList<CRFPage>();
            List<CrfPageItem> addedCrfPageItems = new ArrayList<CrfPageItem>();

            CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);
            modelAndView = new ModelAndView("form/ajax/oneCtcCategorySection");

            for (ProCtcTerm proCtcTerm : proCtcTerms) {

                Object object = createFormCommand.addProCtcTerm(proCtcTerm);

                if (object instanceof CRFPage) {
                    CRFPage crfPage = (CRFPage) object;
                    addedCrfPages.add(crfPage);
                    crfPage.setDescription(proCtcTerm.getProCtcTermVocab().getTermEnglish());
                    crfPage.setProCtcTerm(proCtcTerm);

                } else {
                    addedCrfPageItems.addAll((List<CrfPageItem>) object);
                }


            }
            modelAndView.addAllObjects(referenceData(createFormCommand));
            modelAndView.addObject("crfPageItems", addedCrfPageItems);

            modelAndView.addObject("crfPages", addedCrfPages);

        }

        return modelAndView;


    }


    /**
     * Instantiates a new adds the crf component controller.
     */
    public AddCrfComponentController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

}