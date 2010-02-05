package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.springframework.validation.Errors;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author Mehul Gulati
 *         Date: Feb 5, 2010
 */
public class EditClinicalStaffController extends CtcAeSimpleFormController {

    private ClinicalStaffRepository clinicalStaffRepository;
    protected final String CLINICAL_STAFF_ID = "clinicalStaffId";

    /**
     * Instantiates a new creates the clinical staff controller.
     */
    public EditClinicalStaffController() {
        super();
        setCommandClass(ClinicalStaff.class);
        setFormView("clinicalStaff/viewClinicalStaff");
        setBindOnNewForm(true);
        setSessionForm(true);
    }


    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        String clinicalStaffId = request.getParameter(CLINICAL_STAFF_ID);
        ClinicalStaff clinicalStaff = null;
        if (clinicalStaffId != null) {
            clinicalStaff = clinicalStaffRepository.findById(new Integer(clinicalStaffId));
        }

        return clinicalStaff;
    }

    @Required
     public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
         this.clinicalStaffRepository = clinicalStaffRepository;
     }

}
