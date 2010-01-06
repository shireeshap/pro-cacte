package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

//
/**
 * Author: Harsh Agarwal
 * Date: Dec 10, 2008
 * Time: 1:36:54 PM.
 */
public class AddQuestionByParticipantController extends CtcAeSimpleFormController {

    private GenericRepository genericRepository;
    private String reviewView;

    public AddQuestionByParticipantController() {
        super();
        setFormView("form/addQuestionForParticipant");
        setSuccessView("form/confirmFormSubmission");
        setReviewView("form/reviewFormSubmission");

    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        int pageNumber = Integer.parseInt(request.getParameter("p"));
        if ("continue".equals(((SubmitFormCommand) command).getDirection())) {
            String[] selectedSymptoms = request.getParameterValues("symptomsByParticipants");
            if (selectedSymptoms != null) {
                ((SubmitFormCommand) command).addParticipantAddedQuestions(selectedSymptoms,true);
                ((SubmitFormCommand) command).setTotalPages(pageNumber + selectedSymptoms.length);
            }
            pageNumber++;
        } else {
            if ("back".equals(((SubmitFormCommand) command).getDirection())) {
                pageNumber--;
            }
        }
        ModelAndView mv = showForm(request, errors, "abc");
        request.getSession().setAttribute(SubmitFormController.class.getName() + ".FORM." + "command", command);
        mv.setView(new RedirectView("submit?id=" + ((SubmitFormCommand) command).getStudyParticipantCrfSchedule().getId() + "&p=" + pageNumber));
        return mv;
    }


    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand)
                request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");

        ProCtcTermQuery query = new ProCtcTermQuery();
        List l = genericRepository.find(query);
        ArrayList<ProCtcTerm> proCtcTerms = (ArrayList<ProCtcTerm>) l;
        submitFormCommand.computeAdditionalSymptoms(proCtcTerms);

        return submitFormCommand;
    }

    public String getReviewView() {
        return reviewView;
    }

    public void setReviewView(String reviewView) {
        this.reviewView = reviewView;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}