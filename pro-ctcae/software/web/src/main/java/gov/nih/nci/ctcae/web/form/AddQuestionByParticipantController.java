package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//
/**
 * Author: Harsh Agarwal
 * Date: Dec 10, 2008
 * Time: 1:36:54 PM.
 */
public class AddQuestionByParticipantController extends CtcAeSimpleFormController {

    private GenericRepository genericRepository;

    public AddQuestionByParticipantController() {
        super();
        setFormView("form/addQuestionForParticipant");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        int pageNumber = Integer.parseInt(request.getParameter("p"));
        if ("continue".equals(((SubmitFormCommand) command).getDirection())) {
            String[] selectedSymptoms = request.getParameterValues("symptomsByParticipants");
            if (selectedSymptoms != null) {
                SubmitFormCommand sCommand = (SubmitFormCommand) command;
                sCommand.addParticipantAddedQuestions(selectedSymptoms, true);
            }
            pageNumber++;
        } else {
            if ("back".equals(((SubmitFormCommand) command).getDirection())) {
                pageNumber--;
            }
        }
        ModelAndView mv = showForm(request, errors, "");
        request.getSession().setAttribute(SubmitFormController.class.getName() + ".FORM." + "command", command);

        mv.setView(new RedirectView("submit?id=" + ((SubmitFormCommand) command).getSchedule().getId() + "&p=" + pageNumber));
        return mv;
    }


    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand)
                request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");
        Locale locale = (Locale) WebUtils.getSessionAttribute(request, org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        String language = locale.getLanguage();
        submitFormCommand.setLanguage(language);
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCoreItemsOnly();
        List l = genericRepository.find(query);
        ArrayList<ProCtcTerm> proCtcTerms = (ArrayList<ProCtcTerm>) l;
        submitFormCommand.computeAdditionalSymptoms(proCtcTerms);
        return submitFormCommand;
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand)request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");
        int currentPageIndex = 0;
        currentPageIndex = submitFormCommand.getNewPageIndex();
        ModelAndView mv;
        if (submitFormCommand.getSortedSymptoms().size() == 0) {
            currentPageIndex++;
            mv = showForm(request, errors, "");
            mv.setView(new RedirectView("addMorequestion?p=" + currentPageIndex));
            return mv;
        }
        return showForm(request, errors, getFormView());
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}