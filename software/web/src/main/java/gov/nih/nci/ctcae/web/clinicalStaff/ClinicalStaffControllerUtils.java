package gov.nih.nci.ctcae.web.clinicalStaff;

import javax.servlet.http.HttpServletRequest;

//
/**
 * The Class ClinicalStaffControllerUtils.
 *
 * @author Mehul Gulati
 *         Date: Oct 30, 2008
 */
public class ClinicalStaffControllerUtils {

    /**
     * Gets the clinical staff command.
     *
     * @param request the request
     * @return the clinical staff command
     */
    public static ClinicalStaffCommand getClinicalStaffCommand(HttpServletRequest request) {
        ClinicalStaffCommand clinicalStaffCommand = (ClinicalStaffCommand)
                request.getSession().getAttribute(CreateClinicalStaffController.class.getName() + ".FORM." + "clinicalStaffCommand");
        return clinicalStaffCommand;
    }
}
