package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author Mehul Gulati
 *         Date: Feb 5, 2010
 */
public class ViewClinicalStaffController extends CtcAeSimpleFormController {

    private ClinicalStaffRepository clinicalStaffRepository;
    protected final String CLINICAL_STAFF_ID = "clinicalStaffId";
    private static String CLINICAL_STAFF_SEARCH_STRING = "clinicalStaffSearchString";

    /**
     * Instantiates a new creates the clinical staff controller.
     */
    public ViewClinicalStaffController() {
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
        request.setAttribute("searchString", (String) request.getSession().getAttribute(CLINICAL_STAFF_SEARCH_STRING));
        return clinicalStaff;
    }

    @Required
     public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
         this.clinicalStaffRepository = clinicalStaffRepository;
     }

}
