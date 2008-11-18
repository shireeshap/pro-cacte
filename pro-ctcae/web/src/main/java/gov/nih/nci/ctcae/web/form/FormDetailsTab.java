package gov.nih.nci.ctcae.web.form;

import edu.nwu.bioinformatics.commons.CollectionUtils;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.CtcCategory;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
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
            CollectionUtils.putInMappedList(ctcCategoryMap, proCtcTerm.getCategory(), proCtcTerm);

        }

        List<CtcCategory> ctcCategoryList = new ArrayList<CtcCategory>(ctcCategoryMap.keySet());
        Collections.sort(ctcCategoryList, new CtcCAtegoryComparator());
        Map result = new LinkedHashMap();

        for (Iterator<CtcCategory> it = ctcCategoryList.iterator(); it.hasNext();) {
            CtcCategory ctcCategory = it.next();
            result.put(ctcCategory, ctcCategoryMap.get(ctcCategory));
        }


        map.put("ctcCategoryMap", result);
        map.put("totalQuestions", command.getStudyCrf().getCrf().getCrfItems().size());

        return map;


    }

    @Override
    public void validate(final CreateFormCommand command, final Errors errors) {
        super.validate(command, errors);
        if (StringUtils.isBlank(command.getStudyCrf().getCrf().getTitle())) {

        }

    }

    @Override
    public void postProcess(HttpServletRequest request, CreateFormCommand command, Errors errors) {
        super.postProcess(request, command, errors);

        command.updateCrfItems(finderRepository);


    }

    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}
