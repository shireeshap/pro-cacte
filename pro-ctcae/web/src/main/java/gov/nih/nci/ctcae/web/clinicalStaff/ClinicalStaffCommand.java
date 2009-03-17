package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.*;

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

    private Boolean cca = false;


    private String organizationClinicalStaffIndexToRemove = "";


    /**
     * Instantiates a new clinical staff command.
     */
    public ClinicalStaffCommand() {
        super();
        clinicalStaff = new ClinicalStaff();
        clinicalStaff.addOrganizationClinicalStaff(new OrganizationClinicalStaff());
        clinicalStaff.setUser(new User());
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
        if (getCca()) {
            UserRole userRole = new UserRole();
            userRole.setRole(Role.CCA);
            getClinicalStaff().getUser().addUserRole(userRole);
        }

    }

    public Boolean getCca() {
        return cca;
    }

    public void setCca(Boolean cca) {
        this.cca = cca;
    }
}
