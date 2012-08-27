package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.domain.CRFCycleDefinition;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.FormController;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


/**
 * The Class ConfirmationCheckController.
 *
 * @author Vinay Gangoli
 * @since Oct 21, 2008
 */
public class ConfirmationCheckController extends AbstractController {

    protected ProCtcTermRepository proCtcTermRepository;

    private static final String DELETE_CRF_CONFIRMATION_TYPE = "deleteCrf";

    private static final String DELETE_QUESTION_CONFIRMATION_TYPE = "deleteQuestion";

    private static final String DELETE_CRF_CYCLE = "deleteCrfCycle";
    
    private static final String DELETE_CRF_CYCLE_POST_CONFIRM = "deleteCrfCyclePostConfirm";


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = null;
        String confirmationType = request.getParameter("confirmationType");
        Integer proCtcTermId = ServletRequestUtils.getIntParameter(request, "proTermId");
        String description = "";
        if (proCtcTermId != null){
        ProCtcTerm proCtcTerm = proCtcTermRepository.findById(proCtcTermId);
            description = proCtcTerm.getProCtcTermVocab().getTermEnglish();
        }
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.equals(confirmationType, DELETE_CRF_CONFIRMATION_TYPE)) {
            modelAndView = new ModelAndView("form/ajax/deleteCrfConfirmationCheck");
            
            map.put("selectedCrfPageNumber", request.getParameter("selectedCrfPageNumber"));
            map.put("crfPageDescription", description);
            modelAndView.addAllObjects(map);
        } else if (StringUtils.equals(confirmationType, DELETE_QUESTION_CONFIRMATION_TYPE)) {
            modelAndView = new ModelAndView("form/ajax/deleteQuestionConfirmationCheck");
            map.put("questionId", request.getParameter("questionId"));
            map.put("proCtcTermId", request.getParameter("proCtcTermId"));
            modelAndView.addAllObjects(map);
        } else if (StringUtils.equals(confirmationType, DELETE_CRF_CYCLE)) {
            modelAndView = new ModelAndView("form/ajax/deleteCrfCycleConfirmationCheck");
            map.put("crfCycleIndex", request.getParameter("crfCycleIndex"));
            modelAndView.addAllObjects(map);
        } else if (StringUtils.equals(confirmationType, DELETE_CRF_CYCLE_POST_CONFIRM)) {
                Integer crfCycleDefinitionIndex = Integer.valueOf(request.getParameter("crfCycleIndex"));
                CreateFormCommand command = FormController.getCreateFormCommand(request);
                CRFCycleDefinition crfCycleDefinition = command.getSelectedFormArmSchedule().getCrfCycleDefinitions().get(crfCycleDefinitionIndex);
                command.getInvalidCycleDefinitions().add(crfCycleDefinition);
                //command.getSelectedFormArmSchedule().getCrfCycleDefinitions().remove(crfCycleDefinition);
                command.setCrfCycleDefinitionIndexToRemove("");
                map.put("cycleDefinitionIndex", "");
        }

        return modelAndView;
    }
    
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    /**
     * Instantiates a new confirmation check controller.
     */
    public ConfirmationCheckController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }


}