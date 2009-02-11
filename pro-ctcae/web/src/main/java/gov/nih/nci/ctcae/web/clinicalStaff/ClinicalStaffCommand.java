package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import org.springframework.util.StringUtils;

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
    private String siteClinicalStaffRoleIndexToRemove = "";


    /**
     * Instantiates a new clinical staff command.
     */
    public ClinicalStaffCommand() {
        super();
        clinicalStaff = new ClinicalStaff();
        //clinicalstaff.addSiteClinicalStaff(new SiteClinicalStaff());
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
        } else if (!org.apache.commons.lang.StringUtils.isBlank(getSiteClinicalStaffRoleIndexToRemove())) {
            String[] siteIndexVsSiteRoleIndex = StringUtils.split(getSiteClinicalStaffRoleIndexToRemove(), "-");
            Integer siteClinicalStaffIndex = Integer.valueOf(siteIndexVsSiteRoleIndex[0]);
            Integer siteClinicalStaffRoleIndex = Integer.valueOf(siteIndexVsSiteRoleIndex[1]);
            this.getClinicalStaff().removeSiteClinicalStaffRole(siteClinicalStaffIndex, siteClinicalStaffRoleIndex);
        }

        setSiteClinicalStaffRoleIndexToRemove("");


        setSiteClinicalStaffIndexToRemove("");


    }

    public void setSiteClinicalStaffRoleIndexToRemove(String siteClinicalStaffRoleIndexToRemove) {
        this.siteClinicalStaffRoleIndexToRemove = siteClinicalStaffRoleIndexToRemove;
    }

    public String getSiteClinicalStaffRoleIndexToRemove() {
        return siteClinicalStaffRoleIndexToRemove;
    }
}
