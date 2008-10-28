package gov.nih.nci.ctcae.web.investigator;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.repository.InvestigatorRepository;
import gov.nih.nci.ctcae.core.domain.Investigator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 */
public class CreateInvestigatorController extends CtcAeSimpleFormController {

    private InvestigatorRepository investigatorRepository;

    public CreateInvestigatorController() {
        setCommandClass(Investigator.class);
        setCommandName("investigatorCommand");
        setFormView("investigator/createInvestigator");
        setSuccessView("investigator/confirmInvestigator");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {

        Investigator investigator = (Investigator) oCommand;

        investigator = investigatorRepository.save(investigator);
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        modelAndView.addObject("investigatorCommand", investigator);


        return modelAndView;
    }

    @Override
       protected Object formBackingObject(HttpServletRequest request)
               throws Exception {
           String investigatorId = request.getParameter("investigatorId");
           if (investigatorId == null){
               return new Investigator();
           }
           else {
           Investigator investigator = investigatorRepository.findById(new Integer(investigatorId));
           return investigator;
           }
       }


    @Required
    public void setInvestigatorRepository(InvestigatorRepository investigatorRepository) {
        this.investigatorRepository = investigatorRepository;
    }
}
