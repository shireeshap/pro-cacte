package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaff;

//
/**
 * The Class ClinicalStaffCommand.
 *
 * @author Mehul Gulati
 *         Date: Oct 30, 2008
 */
public class ClinicalStaffCommand {

    /**
     * The clinical staff.
     */
    private ClinicalStaff clinicalStaff;


    private String siteClinicalStaffIndexToRemove = "";


    /**
     * Instantiates a new clinical staff command.
     */
    public ClinicalStaffCommand() {
        super();
        clinicalStaff = new ClinicalStaff();
        clinicalStaff.addSiteClinicalStaff(new SiteClinicalStaff());
    }

    /**
     * Gets the clinical staff.
     *
     * @return the clinical staff
     */
    public ClinicalStaff getClinicalStaff() {
        return clinicalStaff;
    }

    /**
     * Sets the clinical staff.
     *
     * @param clinicalStaff the new clinical staff
     */
    public void setClinicalStaff(ClinicalStaff clinicalStaff) {
        this.clinicalStaff = clinicalStaff;
    }


    public String getSiteClinicalStaffIndexToRemove() {
        return siteClinicalStaffIndexToRemove;
    }

    public void setSiteClinicalStaffIndexToRemove(String siteClinicalStaffIndexToRemove) {
        this.siteClinicalStaffIndexToRemove = siteClinicalStaffIndexToRemove;
    }


    public void apply() {

        if (!org.apache.commons.lang.StringUtils.isBlank(getSiteClinicalStaffIndexToRemove())) {
            Integer siteClinicalStaffIndex = Integer.valueOf(siteClinicalStaffIndexToRemove);
            this.getClinicalStaff().removeSiteClinicalStaff(siteClinicalStaffIndex);
        }
        setSiteClinicalStaffIndexToRemove("");


    }


}
