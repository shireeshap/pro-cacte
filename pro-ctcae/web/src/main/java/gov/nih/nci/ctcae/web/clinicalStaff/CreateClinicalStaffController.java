package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class CreateClinicalStaffController.
 *
 * @author Mehul Gulati
 */
public class CreateClinicalStaffController extends CtcAeSimpleFormController {

    // same controller for edit clinicalstaff

    /**
     * The clinical staff repository.
     */
    private ClinicalStaffRepository clinicalStaffRepository;


    /**
     * Instantiates a new creates the clinical staff controller.
     */
    public CreateClinicalStaffController() {
        super();
        setCommandClass(gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand.class);
        setCommandName("clinicalStaffCommand");
        setFormView("clinicalStaff/createClinicalStaff");
        setSuccessView("clinicalStaff/confirmClinicalStaff");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {

        ClinicalStaffCommand clinicalStaffCommand = (ClinicalStaffCommand) oCommand;
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();


        clinicalStaff = clinicalStaffRepository.save(clinicalStaff);
        clinicalStaffCommand.setClinicalStaff(clinicalStaff);

        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        modelAndView.addObject("clinicalStaffCommand", clinicalStaffCommand);
        return modelAndView;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        String clinicalStaffId = request.getParameter("clinicalStaffId");
        ClinicalStaffCommand clinicalStaffCommand = new ClinicalStaffCommand();

        if (clinicalStaffId == null) {
            return clinicalStaffCommand;
        } else {
            ClinicalStaff clinicalStaff = clinicalStaffRepository.findById(new Integer(clinicalStaffId));
            clinicalStaffCommand.setClinicalStaff(clinicalStaff);
            return clinicalStaffCommand;
        }
    }


    /**
     * Sets the clinical staff repository.
     *
     * @param clinicalStaffRepository the new clinical staff repository
     */
    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }
}
