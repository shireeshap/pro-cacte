package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;

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


    private String organizationClinicalStaffIndexToRemove = "";


    /**
     * Instantiates a new clinical staff command.
     */
    public ClinicalStaffCommand() {
        super();
        clinicalStaff = new ClinicalStaff();
        clinicalStaff.addOrganizationClinicalStaff(new OrganizationClinicalStaff());
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


    public String getOrganizationClinicalStaffIndexToRemove() {
        return organizationClinicalStaffIndexToRemove;
    }

    public void setOrganizationClinicalStaffIndexToRemove(String organizationClinicalStaffIndexToRemove) {
        this.organizationClinicalStaffIndexToRemove = organizationClinicalStaffIndexToRemove;
    }


    public void apply() {

        if (!org.apache.commons.lang.StringUtils.isBlank(getOrganizationClinicalStaffIndexToRemove())) {
            Integer organizationClinicalStaffIndex = Integer.valueOf(organizationClinicalStaffIndexToRemove);
            this.getClinicalStaff().removeOrganizationClinicalStaff(organizationClinicalStaffIndex);
        }
        setOrganizationClinicalStaffIndexToRemove("");


    }


}
