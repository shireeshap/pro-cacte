package gov.nih.nci.ctcae.web.clinicalStaff;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mehul Gulati
 *         Date: Oct 30, 2008
 */
public class ClinicalStaffControllerUtils {
    public static gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand getClinicalStaffCommand(HttpServletRequest request) {
        gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand clinicalStaffCommand = (gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand)
                request.getSession().getAttribute(gov.nih.nci.ctcae.web.clinicalStaff.CreateClinicalStaffController.class.getName() + ".FORM." + "clinicalStaffCommand");
        return clinicalStaffCommand;
    }
}
