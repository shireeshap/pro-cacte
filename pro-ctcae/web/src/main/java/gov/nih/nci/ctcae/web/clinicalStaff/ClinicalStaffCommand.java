package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignment;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.springframework.util.StringUtils;

import java.util.List;

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


    private String clinicalStaffAssignmentIndexToRemove = "";
    private String clinicalStaffAssignmentRoleIndexToRemove = "";


    /**
     * Instantiates a new clinical staff command.
     */
    public ClinicalStaffCommand() {
        super();
        clinicalStaff = new ClinicalStaff();
        //clinicalstaff.addClinicalStaffAssignment(new ClinicalStaffAssignment());
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


    public String getClinicalStaffAssignmentIndexToRemove() {
        return clinicalStaffAssignmentIndexToRemove;
    }

    public void setClinicalStaffAssignmentIndexToRemove(String clinicalStaffAssignmentIndexToRemove) {
        this.clinicalStaffAssignmentIndexToRemove = clinicalStaffAssignmentIndexToRemove;
    }


    public void apply() {

        if (!org.apache.commons.lang.StringUtils.isBlank(getClinicalStaffAssignmentIndexToRemove())) {
            Integer clinicalStaffAssignmentIndex = Integer.valueOf(clinicalStaffAssignmentIndexToRemove);
            this.getClinicalStaff().removeClinicalStaffAssignment(clinicalStaffAssignmentIndex);
        } else if (!org.apache.commons.lang.StringUtils.isBlank(getClinicalStaffAssignmentRoleIndexToRemove())) {
            String[] siteIndexVsSiteRoleIndex = StringUtils.split(getClinicalStaffAssignmentRoleIndexToRemove(), "-");
            Integer clinicalStaffAssignmentIndex = Integer.valueOf(siteIndexVsSiteRoleIndex[0]);
            Integer clinicalStaffAssignmentRoleIndex = Integer.valueOf(siteIndexVsSiteRoleIndex[1]);
            this.getClinicalStaff().removeClinicalStaffAssignmentRole(clinicalStaffAssignmentIndex, clinicalStaffAssignmentRoleIndex);
        }

        setClinicalStaffAssignmentRoleIndexToRemove("");


        setClinicalStaffAssignmentIndexToRemove("");


    }

    public void updateDisplayNameOfClinicalStaff(FinderRepository finderRepository) {
        //now update the display name of clinical staff assignments
        List<ClinicalStaffAssignment> clinicalStaffAssignments = clinicalStaff.getClinicalStaffAssignments();
        for (ClinicalStaffAssignment clinicalStaffAssignment : clinicalStaffAssignments) {
            if (org.apache.commons.lang.StringUtils.equals(clinicalStaffAssignment.getDomainObjectClass(), Organization.class.getName())) {
                Organization organization = finderRepository.findById(Organization.class, clinicalStaffAssignment.getDomainObjectId());
                clinicalStaffAssignment.setDisplayName(organization.getDisplayName());
            }
        }
    }


    public String getClinicalStaffAssignmentRoleIndexToRemove() {
        return clinicalStaffAssignmentRoleIndexToRemove;
    }

    public void setClinicalStaffAssignmentRoleIndexToRemove(String clinicalStaffAssignmentRoleIndexToRemove) {
        this.clinicalStaffAssignmentRoleIndexToRemove = clinicalStaffAssignmentRoleIndexToRemove;
    }
}
