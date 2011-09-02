package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: Apr 7, 2011
 * Time: 3:32:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddMoreQuestionByParticipantController extends CtcAeSimpleFormController {

    private GenericRepository genericRepository;

    public AddMoreQuestionByParticipantController() {
        super();
        setFormView("form/addMoreQuestionForParticipant");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        int pageNumber = Integer.parseInt(request.getParameter("p"));
        if ("continue".equals(((SubmitFormCommand) command).getDirection())) {
            String[] selectedSymptoms = request.getParameterValues("symptomsByParticipants");
            if (selectedSymptoms != null) {
                SubmitFormCommand sCommand = (SubmitFormCommand) command;
                sCommand.addMoreParticipantAddedQuestions(selectedSymptoms, true);
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

//        ProCtcTermQuery query = new ProCtcTermQuery();
//        query.filterByCoreItemsOnly();
//        List l = genericRepository.find(query);
//        ArrayList<ProCtcTerm> proCtcTerms = (ArrayList<ProCtcTerm>) l;
//        submitFormCommand.computeAdditionalSymptoms(proCtcTerms);
        return submitFormCommand;
    }


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
