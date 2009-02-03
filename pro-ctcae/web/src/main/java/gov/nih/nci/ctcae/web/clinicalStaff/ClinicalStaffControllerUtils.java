package gov.nih.nci.ctcae.web.clinicalStaff;

import javax.servlet.http.HttpServletRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class ClinicalStaffControllerUtils.
 * 
 * @author Mehul Gulati
 * Date: Oct 30, 2008
 */
public class ClinicalStaffControllerUtils {
    
    /**
     * Gets the clinical staff command.
     * 
     * @param request the request
     * 
     * @return the clinical staff command
     */
    public static gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand getClinicalStaffCommand(HttpServletRequest request) {
        gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand clinicalStaffCommand = (gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand)
                request.getSession().getAttribute(gov.nih.nci.ctcae.web.clinicalStaff.CreateClinicalStaffController.class.getName() + ".FORM." + "clinicalStaffCommand");
        return clinicalStaffCommand;
    }
}
