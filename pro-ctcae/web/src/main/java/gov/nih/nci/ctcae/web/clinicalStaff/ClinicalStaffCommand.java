package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.List;
import java.util.ArrayList;

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
    private List<Integer> indexesToRemove = new ArrayList<Integer>();

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

    public void apply() {
        if (getCca()) {
            UserRole userRole = new UserRole();
            userRole.setRole(Role.CCA);
            getClinicalStaff().getUser().addUserRole(userRole);
        }
        indexesToRemove.clear();

    }

    public Boolean getCca() {
        return cca;
    }

    public void setCca(Boolean cca) {
        this.cca = cca;
    }

    public List<Integer> getIndexesToRemove() {
        return indexesToRemove;
    }
}
