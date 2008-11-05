package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 */
public class CreateClinicalStaffController extends CtcAeSimpleFormController {

    // same controller for edit clinicalstaff

    private ClinicalStaffRepository clinicalStaffRepository;


    public CreateClinicalStaffController() {
        setCommandClass(gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand.class);
        setCommandName("clinicalStaffCommand");
        setFormView("clinicalStaff/createClinicalStaff");
        setSuccessView("clinicalStaff/confirmClinicalStaff");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {

        gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand clinicalStaffCommand = (gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand) oCommand;
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();


        clinicalStaff = clinicalStaffRepository.save(clinicalStaff);
        clinicalStaffCommand.setClinicalStaff(clinicalStaff);

        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        modelAndView.addObject("clinicalStaffCommand", clinicalStaffCommand);
        return modelAndView;
    }

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


    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }
}
