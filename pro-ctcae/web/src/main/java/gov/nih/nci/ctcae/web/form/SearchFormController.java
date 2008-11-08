package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class SearchFormController extends CtcAeSimpleFormController {


    public SearchFormController() {
        //setCommandClass(PostRideCommand.class);
        setFormView("form/createForm");
        setSuccessView("form/confirmForm");
        setBindOnNewForm(true);
        setSessionForm(true);
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


    ////// CONFIGURATION


}