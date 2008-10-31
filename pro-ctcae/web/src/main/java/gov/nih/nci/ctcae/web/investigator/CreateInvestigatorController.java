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

   // same controller for edit investigator

    private InvestigatorRepository investigatorRepository;


    public CreateInvestigatorController() {
        setCommandClass(InvestigatorCommand.class);
        setCommandName("investigatorCommand");
        setFormView("investigator/createInvestigator");
        setSuccessView("investigator/confirmInvestigator");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {

        InvestigatorCommand investigatorCommand = (InvestigatorCommand) oCommand;
        Investigator investigator = investigatorCommand.getInvestigator();


        investigator = investigatorRepository.save(investigator);
        investigatorCommand.setInvestigator(investigator);

        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        modelAndView.addObject("investigatorCommand", investigatorCommand);
        return modelAndView;
    }

    @Override
       protected Object formBackingObject(HttpServletRequest request)
               throws Exception {
           String investigatorId = request.getParameter("investigatorId");
           InvestigatorCommand investigatorCommand = new InvestigatorCommand();

        if (investigatorId == null){
               return investigatorCommand;
           }
           else {
           Investigator investigator = investigatorRepository.findById(new Integer(investigatorId));
           investigatorCommand.setInvestigator(investigator);
           return investigatorCommand;
           }
       }


    @Required
    public void setInvestigatorRepository(InvestigatorRepository investigatorRepository) {
        this.investigatorRepository = investigatorRepository;
    }
}
