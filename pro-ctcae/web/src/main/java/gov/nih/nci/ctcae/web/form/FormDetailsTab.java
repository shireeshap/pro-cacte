package gov.nih.nci.ctcae.web.form;

import edu.nwu.bioinformatics.commons.CollectionUtils;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.CtcCategory;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import java.util.*;

//
/**
 * The Class FormDetailsTab.
 *
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class FormDetailsTab extends SecuredTab<CreateFormCommand> {

    /**
     * The pro ctc term repository.
     */
    private ProCtcTermRepository proCtcTermRepository;

    /**
     * The finder repository.
     */
    private FinderRepository finderRepository;

    /**
     * Instantiates a new form details tab.
     */
    public FormDetailsTab() {
        super("form.tab.form", "form.tab.form_details", "form/form_details");
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_CREATE_FORM;


    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.ctms.web.tabs.Tab#referenceData(java.lang.Object)
     */
    @Override
    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map<String, Object> map = super.referenceData(command);

        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcTermHavingQuestionsOnly();
        Collection<ProCtcTerm> proCtcTerms = proCtcTermRepository.find(query);


        Map<CtcCategory, List<ProCtcTerm>> ctcCategoryMap = new HashMap<CtcCategory, List<ProCtcTerm>>();

        for (ProCtcTerm proCtcTerm : proCtcTerms) {
            CollectionUtils.putInMappedList(ctcCategoryMap, proCtcTerm.getCtcTerm().getCategory(), proCtcTerm);

        }


        List<CtcCategory> ctcCategoryList = new ArrayList<CtcCategory>(ctcCategoryMap.keySet());
        Collections.sort(ctcCategoryList, new CtcCAtegoryComparator());
        Map<CtcCategory, List<ProCtcTerm>> result = new LinkedHashMap<CtcCategory, List<ProCtcTerm>>();

        for (Iterator<CtcCategory> it = ctcCategoryList.iterator(); it.hasNext();) {
            CtcCategory ctcCategory = it.next();
            List<ProCtcTerm> proCtcTermList = ctcCategoryMap.get(ctcCategory);
            Collections.sort(proCtcTermList, new ProCtcTermComparator());

            result.put(ctcCategory, proCtcTermList);
        }


        map.put("ctcCategoryMap", result);
        //map.put("totalQuestions", command.getCRF().getCrf().getCrfPageItems().size());
        map.put("responseRequired", ListValues.getResponseRequired());
        map.put("crfItemAllignments", ListValues.getCrfItemAllignments());
        map.put("recallPeriods", ListValues.getRecallPeriods());

        List<CrfPageItem> crfPageItems = command.getCrf().getAllCrfPageItems();
        map.put("selectedCrfPageItems", crfPageItems);

        List<Integer> selectedProCtcTerms = command.getSelectedProCtcTerms();
        map.put("selectedProCtcTerms", selectedProCtcTerms);
        return map;
    }


    /**
     * Sets the pro ctc term repository.
     *
     * @param proCtcTermRepository the new pro ctc term repository
     */
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    /**
     * Sets the finder repository.
     *
     * @param finderRepository the new finder repository
     */
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}
