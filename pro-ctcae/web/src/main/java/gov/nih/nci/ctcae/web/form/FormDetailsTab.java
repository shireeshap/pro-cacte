package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class FormDetailsTab extends Tab<CreateFormCommand> {

    private ProCtcTermRepository proCtcTermRepository;
    private FinderRepository finderRepository;

    public FormDetailsTab() {
        super("Form details", "Form details", "form/form_details");
    }

    @Override
    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map<String, Object> map = super.referenceData(command);

        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcTermHavingQuestionsOnly();
        Collection<ProCtcTerm> proCtcTerms = proCtcTermRepository.findAndInitializeTerm(query);
        map.put("proCtcTerms", proCtcTerms);
        map.put("totalQuestions", command.getStudyCrf().getCrf().getCrfItems().size());

        return map;


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
