package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.core.domain.rules.CRFNotificationRule;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

//
/**
 * @author Harsh Agarwal
 */
public class AddFormRuleController extends AbstractController {

    CRFRepository crfRepository;
    StudyOrganizationRepository studyOrganizationRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {


        ModelAndView modelAndView = new ModelAndView("form/ajax/formRule");
        CreateFormCommand command = ControllersUtils.getFormCommand(request);
        String isSite = request.getParameter("isSite");
        int index = -1;
        if ("true".equals(isSite)) {
            command.addRuleToSite();
            index = command.getMyOrg().getSiteCRFNotificationRules().size() - 1;
            command.setMyOrg(studyOrganizationRepository.save(command.getMyOrg()));
            modelAndView.addObject("rule", command.getMyOrg().getSiteCRFNotificationRules().get(index).getNotificationRule());
        } else {
            command.addRuleToCrf();
            index = command.getCrf().getCrfNotificationRules().size() - 1;
            command.setCrf(crfRepository.save(command.getCrf()));
            modelAndView.addObject("rule", command.getCrf().getCrfNotificationRules().get(index).getNotificationRule());
        }
        modelAndView.addObject("ruleIndex", index);
        modelAndView.addObject("crfSymptoms", ListValues.getSymptomsForCRF(command.getCrf()));
        modelAndView.addObject("notifications", ListValues.getNotificationOptions());

        return modelAndView;
    }

    @Required
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    @Required
    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }
}