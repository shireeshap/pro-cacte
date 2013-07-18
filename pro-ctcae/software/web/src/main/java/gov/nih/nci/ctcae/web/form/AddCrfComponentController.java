package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.constants.ItemBank;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcTermComparator;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String isConfirmation = request.getParameter("isConfirmation");
        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        ModelAndView modelAndView = null;

        //Find if confirmation popup is required, and redirect accordingly
        if(StringUtils.equals(isConfirmation, "true")){
        	if(!isValidSymptomSet(request)){
                Map<String, String> map = new HashMap<String, String>();
                modelAndView = new ModelAndView("form/ajax/deleteOnMixedItemBankConfirm");
                if (StringUtils.equals(componentType, PRO_CTC_TERM_COMPONENT)) {
                    map.put("crfPageNumber", request.getParameter("crfPageNumber"));
                    map.put("proCtcTermId", request.getParameter("proCtcTermId"));
                }
                if (StringUtils.equals(componentType, CTC_CATEGORY_COMPONENT)){
                    map.put("ctcCategoryId", request.getParameter("ctcCategoryId"));
                    map.put("crfPageNumbers", request.getParameter("crfPageNumbers"));
                }
                map.put("categoryName", request.getParameter("categoryName"));
                map.put("componentType", componentType);
                modelAndView.addAllObjects(map);
                return modelAndView;
        	}
        }
     
        consolidateAlreadyAddedSymptoms(request, createFormCommand);
        ItemBank selectedItemBank = getItemBankForCategoryName(request);
        if(selectedItemBank.equals(ItemBank.EQ5D3L)){
        	createFormCommand.getCrf().setEq5d(true);
        	createFormCommand.setSelectedItemBank(ItemBank.EQ5D3L.getCode());
        }else if(selectedItemBank.equals(ItemBank.EQ5D5L)){
        	createFormCommand.getCrf().setEq5d(true);
        	createFormCommand.setSelectedItemBank(ItemBank.EQ5D5L.getCode());
        } else {
        	createFormCommand.getCrf().setEq5d(false);
        	createFormCommand.setSelectedItemBank(ItemBank.PROCTCAE.getCode());
        }
        if (StringUtils.equals(componentType, PRO_CTC_TERM_COMPONENT)) {
            Integer proCtcTermId = ServletRequestUtils.getIntParameter(request, "proCtcTermId");

            ProCtcTerm proCtcTerm = proCtcTermRepository.findById(proCtcTermId);
            if (proCtcTerm != null) {
                Object object = createFormCommand.addProCtcTerm(proCtcTerm);

                if (object instanceof CRFPage) {
                    modelAndView = new ModelAndView("form/ajax/oneCrfPageSection");
                    CRFPage crfPage = (CRFPage) object;
                    crfPage.setDescription(proCtcTerm.getProCtcTermVocab().getTermEnglish());
                    crfPage.setProCtcTerm(proCtcTerm);
                    modelAndView.addObject("crfPage", crfPage);
                    modelAndView.addObject("crfPages", new ArrayList<CRFPage>().add(crfPage));

                } else {
                    List<CrfPageItem> addedCrfPageItems = (List<CrfPageItem>) object;
                    modelAndView = new ModelAndView("form/ajax/oneProCtcTermSection");
                    modelAndView.addObject("crfPageItems", addedCrfPageItems);

                }
                modelAndView.addObject("command", createFormCommand);
                modelAndView.addAllObjects(referenceData(createFormCommand));
            } else {
                logger.error("can not add proCtcTerm because pro ctc term is null for id:" + proCtcTermId);
                return null;
            }
        } else if (StringUtils.equals(componentType, CTC_CATEGORY_COMPONENT)) {
            Integer ctcCategoryId = ServletRequestUtils.getIntParameter(request, "ctcCategoryId");
            String categoryName = request.getParameter("categoryName");

            ProCtcTermQuery query = new ProCtcTermQuery();
            query.filterByCtcTermHavingQuestionsOnly();
            query.filterByCtcCategoryId(ctcCategoryId);
            query.filterByCurrency();
            List<ProCtcTerm> proCtcTerms = new ArrayList<ProCtcTerm>(proCtcTermRepository.find(query));
            if (categoryName.equals("Core symptoms")) {
                List<ProCtcTerm> termsToRemove = new ArrayList<ProCtcTerm>();
                for (ProCtcTerm pTerm : proCtcTerms) {
                    if (!pTerm.isCore()) {
                        termsToRemove.add(pTerm);
                    }
                }
                if (termsToRemove.size() > 0) {
                    for (ProCtcTerm removeTerm : termsToRemove) {
                        proCtcTerms.remove(removeTerm);
                    }
                }
            }
            if(categoryName.startsWith("EQ5D")){
            	Collections.sort(proCtcTerms, new ProCtcTermComparator());
            } else {
            	Collections.sort(proCtcTerms);
            }
            
            List<CRFPage> addedCrfPages = new ArrayList<CRFPage>();
            List<CrfPageItem> addedCrfPageItems = new ArrayList<CrfPageItem>();

            modelAndView = new ModelAndView("form/ajax/reloadFormBuilderDiv");
            
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
            modelAndView.addObject("command", createFormCommand);
        }
       
        return modelAndView;
    }

    /**
     *  Ensures that the added symptoms all belong to the same Item bank. Incase it finds that the user is mixing an EQ5D symptom with a 
     *  PRO symptom, it'll clear out the existing symptoms and then add the new one.
     * @throws ServletRequestBindingException 
     */
    private void consolidateAlreadyAddedSymptoms(final HttpServletRequest request, CreateFormCommand createFormCommand) throws ServletRequestBindingException {
    	if(!isValidSymptomSet(request)){
    		clearAllAddedSymptoms(request, createFormCommand);
    	}
	}

	private void clearAllAddedSymptoms(final HttpServletRequest request, CreateFormCommand createFormCommand ) {
		String crfPageNumbers = request.getParameter("crfPageNumbers");
		String[] pageNos = crfPageNumbers.split(",");
    	for(int i = 0; i < pageNos.length;i++){
			createFormCommand.setCrfPageNumberToRemove("0");
    		createFormCommand.setCrfPageNumbers(crfPageNumbers);
    		createFormCommand.updateCrfItems(proCtcQuestionRepository);
    	}
	}

	private boolean isValidSymptomSet(final HttpServletRequest request) throws ServletRequestBindingException {
		CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);
		CRF crf = createFormCommand.getCrf();
		ItemBank itemCategory = getItemBankForCategoryName(request);
        for (CRFPage crfPage : crf.getCrfPagesSortedByPageNumber()) {
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
	           	 ProCtcTerm proCtcTerm = crfPageItem.getProCtcQuestion().getProCtcTerm();
	           	 if( !getItemCategoryForProCtcTerm(proCtcTerm).equalsIgnoreCase(itemCategory.getCode())){
	           		 return false;
	           	 }
            }
        }
        return true;
	}
	
	private String getItemCategoryForProCtcTerm(ProCtcTerm proCtcTerm) {
		if(proCtcTerm.getCtcTerm().getCategoryTermSets().get(0).getCategory().getName().equalsIgnoreCase(ItemBank.EQ5D3L.getCode())){
			return ItemBank.EQ5D3L.getCode();
		}
		if(proCtcTerm.getCtcTerm().getCategoryTermSets().get(0).getCategory().getName().equalsIgnoreCase(ItemBank.EQ5D5L.getCode())){
			return ItemBank.EQ5D5L.getCode();
		}
		return ItemBank.PROCTCAE.getCode();
	}

	private ItemBank getItemBankForCategoryName(final HttpServletRequest request){
		String categoryName = request.getParameter("categoryName");
		if("EQ5D-3L".equalsIgnoreCase(categoryName)){
			return ItemBank.EQ5D3L;
		} else if("EQ5D-5L".equalsIgnoreCase(categoryName)){
			return ItemBank.EQ5D5L;
		} else {
			return ItemBank.PROCTCAE;
		}
	}

	/**
     * Instantiates a new adds the crf component controller.
     */
    public AddCrfComponentController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

}