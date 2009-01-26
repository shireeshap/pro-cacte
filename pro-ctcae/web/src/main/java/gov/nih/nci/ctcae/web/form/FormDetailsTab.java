package gov.nih.nci.ctcae.web.form;

import edu.nwu.bioinformatics.commons.CollectionUtils;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.CtcCategory;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.ListValues;

import java.util.*;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class FormDetailsTab extends Tab<CreateFormCommand> {

    private ProCtcTermRepository proCtcTermRepository;
    private FinderRepository finderRepository;

    public FormDetailsTab() {
        super("form.tab.form", "form.tab.form_details", "form/form_details");
    }

    @Override
    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map<String, Object> map = super.referenceData(command);

        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcTermHavingQuestionsOnly();
        Collection<ProCtcTerm> proCtcTerms = proCtcTermRepository.findAndInitializeTerm(query);


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
        //map.put("totalQuestions", command.getCRF().getCrf().getCrfItemsSortedByDislayOrder().size());
        map.put("responseRequired", ListValues.getResponseRequired());
        map.put("crfItemAllignments", ListValues.getCrfItemAllignments());
        map.put("recallPeriods", ListValues.getRecallPeriods());
        map.put("selectedCrfPageItems", command.getCrf().getAllCrfPageItems());

        return map;


    }


    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}
