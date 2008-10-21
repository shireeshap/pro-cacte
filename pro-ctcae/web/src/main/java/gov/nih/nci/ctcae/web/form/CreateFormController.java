package gov.nih.nci.ctcae.web.form;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;

import java.util.Map;
import java.util.Collection;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
@Controller
public class CreateFormController extends CtcAeSimpleFormController {


    private ProCtcTermRepository proCtcTermRepository;

    public CreateFormController() {
        setCommandClass(CreateFormCommand.class);
        setCommandName("createFormCommand");
        setFormView("form/createForm");
        setSuccessView("form/confirmForm");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @RequestMapping("/form/createForm")
    public void createForm(HttpServletRequest request) {

        CreateFormCommand createFormCommand = FormControllersUtils.getFormCommand(request);
        if (createFormCommand == null) {
            createFormCommand = new CreateFormCommand();
        } else {
            request.setAttribute("flashMessage", "You were already updating one form. Do you want to  reusme it or discard it.");
        }

    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map map = super.referenceData(request, command, errors);

        ProCtcTermQuery query = new ProCtcTermQuery();
        Collection<ProCtcTerm> proCtcTerms = proCtcTermRepository.find(query);
        map.put("proCtcTerms", proCtcTerms);
        return map;

    }


    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CreateFormCommand createFormCommand = FormControllersUtils.getFormCommand(request);
        if (createFormCommand == null) {
            createFormCommand = new CreateFormCommand();
        } else {
            request.setAttribute("flashMessage", "You were already updating one form. Do you want to  reusme it or discard it.");
        }
        return createFormCommand;


    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {
        // PostRideCommand command = (PostRideCommand) oCommand;
        //Ride ride = rideRepository.save(command.getRide());
        //command.setRide(ride);


        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        //modelAndView.addObject("ride", ride);
        return modelAndView;
    }

    @Autowired
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }


    ////// CONFIGURATION


}
