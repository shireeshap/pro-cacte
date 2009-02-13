package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.ListValues;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.AbstractController;

import java.util.HashMap;
import java.util.Map;

//
/**
 * The Class AbstractCrfController.
 *
 * @author Vinay Kumar
 * @crated Dec 31, 2008
 */
public abstract class AbstractCrfController extends AbstractController {

    /**
     * The pro ctc question repository.
     */
    protected ProCtcQuestionRepository proCtcQuestionRepository;

    /**
     * The Constant PRO_CTC_TERM_COMPONENT.
     */
    public static final String PRO_CTC_TERM_COMPONENT = "proCtcTerm";

    /**
     * The Constant CRF_PAGE_COMPONENT.
     */
    public static final String CRF_PAGE_COMPONENT = "crfPage";

    /**
     * The Constant PRO_CTC_QUESTIONS_COMPONENT.
     */
    public static final String PRO_CTC_QUESTIONS_COMPONENT = "proCtcQuestion";

    /**
     * The Constant CTC_CATEGORY_COMPONENT.
     */
    public static final String CTC_CATEGORY_COMPONENT = "ctcCategory";

    /**
     * The pro ctc term repository.
     */
    protected ProCtcTermRepository proCtcTermRepository;

    /**
     * Reference data.
     *
     * @param command the command
     * @return the map< string, object>
     */
    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("responseRequired", ListValues.getResponseRequired());
        map.put("advance", command.getCrf().getAdvance());
        map.put("crfItemAllignments", ListValues.getCrfItemAllignments());
        map.put("selectedCrfPageItems", command.getCrf().getAllCrfPageItems());


        return map;
    }


    /**
     * Sets the pro ctc term repository.
     *
     * @param proCtcTermRepository the new pro ctc term repository
     */
    @Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    /**
     * Sets the pro ctc question repository.
     *
     * @param proCtcQuestionRepository the new pro ctc question repository
     */
    @Required
    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }
}
