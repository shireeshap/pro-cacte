package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.web.ListValues;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
    protected final String CLINICAL_STAFF_ID = "clinicalStaffId";


    /**
     * Instantiates a new creates the clinical staff controller.
     */
    public CreateClinicalStaffController() {
        super();
        setCommandClass(ClinicalStaffCommand.class);
        setCommandName("clinicalStaffCommand");
        setFormView("clinicalStaff/createClinicalStaff");
        setSuccessView("clinicalStaff/confirmClinicalStaff");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map map = super.referenceData(request, command, errors);
        map.put("siteRoles", ListValues.getSiteRolesType());
        map.put("roleStatus", ListValues.getRoleStatusType());

        return map;


    }/* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {

        ClinicalStaffCommand clinicalStaffCommand = (ClinicalStaffCommand) oCommand;

        clinicalStaffCommand.apply();
        ModelAndView modelAndView = showForm(request, response, errors);

        if (!StringUtils.isBlank(request.getParameter("showForm"))) {
            if (shouldSave(request, clinicalStaffCommand)) {
                save(clinicalStaffCommand);
            }
        } else {

            save(clinicalStaffCommand);

            modelAndView = new ModelAndView(getSuccessView());
            modelAndView.addObject("clinicalStaffCommand", clinicalStaffCommand);
        }

        return modelAndView;
    }

    private boolean shouldSave(HttpServletRequest request, ClinicalStaffCommand clinicalStaffCommand) {

        return StringUtils.isBlank(request.getParameter(CLINICAL_STAFF_ID)) ? false : true;

    }

    private void save(ClinicalStaffCommand clinicalStaffCommand) {
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();
        clinicalStaff = clinicalStaffRepository.save(clinicalStaff);
        clinicalStaffCommand.setClinicalStaff(clinicalStaff);

    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        String clinicalStaffId = request.getParameter(CLINICAL_STAFF_ID);
        ClinicalStaffCommand clinicalStaffCommand = new ClinicalStaffCommand();

        if (clinicalStaffId != null) {
            ClinicalStaff clinicalStaff = clinicalStaffRepository.findById(new Integer(clinicalStaffId));
            clinicalStaffCommand.setClinicalStaff(clinicalStaff);
        }

        return clinicalStaffCommand;

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
