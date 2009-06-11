package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.*;
import gov.nih.nci.ctcae.core.repository.*;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

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

        int pageNumber = ((SubmitFormCommand) command).getTotalPages();
        request.getSession().setAttribute("questionstobedeletedlist", ((SubmitFormCommand) command).getQuestionsToBeDeleted());
        if ("continue".equals(((SubmitFormCommand) command).getDirection())) {
            request.getSession().setAttribute("gotopage", "" + (pageNumber + 1));
            request.getSession().setAttribute("skipaddquestion", "true");
            String[] selectedSymptoms = request.getParameterValues("symptomsByParticipants");
            if (selectedSymptoms != null) {
                ((SubmitFormCommand) command).addParticipantAddedQuestions(selectedSymptoms);
            }
        } else {
            request.getSession().setAttribute("gotopage", "" + pageNumber);
        }
        return new ModelAndView(new RedirectView("submit?id=" + ((SubmitFormCommand) command).getStudyParticipantCrfSchedule().getId()));
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


}