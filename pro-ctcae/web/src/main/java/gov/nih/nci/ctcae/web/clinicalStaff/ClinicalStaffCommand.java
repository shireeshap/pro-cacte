package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaff;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class ClinicalStaffCommand.
 * 
 * @author Mehul Gulati
 * Date: Oct 30, 2008
 */
public class ClinicalStaffCommand {

    /** The clinical staff. */
    private ClinicalStaff clinicalStaff;


    /** The objects ids to remove. */
    private String objectsIdsToRemove;

    /**
     * Instantiates a new clinical staff command.
     */
    public ClinicalStaffCommand() {
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

    /**
     * Gets the objects ids to remove.
     * 
     * @return the objects ids to remove
     */
    public String getObjectsIdsToRemove() {
        return objectsIdsToRemove;
    }

    /**
     * Sets the objects ids to remove.
     * 
     * @param objectsIdsToRemove the new objects ids to remove
     */
    public void setObjectsIdsToRemove(String objectsIdsToRemove) {
        this.objectsIdsToRemove = objectsIdsToRemove;
    }

    /**
     * Removes the site clinical staff.
     */
    public void removeSiteClinicalStaff() {
        Set<String> indexes = org.springframework.util.StringUtils.commaDelimitedListToSet(objectsIdsToRemove);
        List<SiteClinicalStaff> siteClinicalStaffToRemove = new ArrayList<SiteClinicalStaff>();
        for (String index : indexes) {
            SiteClinicalStaff siteClinicalStaff = clinicalStaff.getSiteClinicalStaffs().get(Integer.parseInt(index));
            siteClinicalStaffToRemove.add(siteClinicalStaff);
        }
        for (SiteClinicalStaff siteClinicalStaff : siteClinicalStaffToRemove) {
            clinicalStaff.removeSiteClinicalStaff(siteClinicalStaff);

        }
    }

}
