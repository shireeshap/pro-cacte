package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.AbstractController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Dec 31, 2008
 */
public abstract class AbstractCrfController extends AbstractController {
    protected FinderRepository finderRepository;
    public static final String PRO_CTC_TERM_COMPONENT = "proCtcTerm";
    public static final String CRF_PAGE_COMPONENT = "crfPage";
    public static final String PRO_CTC_QUESTIONS_COMPONENT = "proCtcQuestion";

    protected ProCtcTermRepository proCtcTermRepository;

    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("responseRequired", ListValues.getResponseRequired());
        map.put("advance", command.getCrf().getAdvance());
        map.put("crfItemAllignments", ListValues.getCrfItemAllignments());
        map.put("selectedCrfPageItems", command.getCrf().getAllCrfPageItems());


        return map;
    }


    @Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}
