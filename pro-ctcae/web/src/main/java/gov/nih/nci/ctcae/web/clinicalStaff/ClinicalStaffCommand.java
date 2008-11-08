package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaff;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Mehul Gulati
 *         Date: Oct 30, 2008
 */
public class ClinicalStaffCommand {

    private ClinicalStaff clinicalStaff;


    private String objectsIdsToRemove;

    public ClinicalStaffCommand() {
        clinicalStaff = new ClinicalStaff();
        //clinicalstaff.addSiteClinicalStaff(new SiteClinicalStaff());
    }

    public ClinicalStaff getClinicalStaff() {
        return clinicalStaff;
    }

    public void setClinicalStaff(ClinicalStaff clinicalStaff) {
        this.clinicalStaff = clinicalStaff;
    }

    public String getObjectsIdsToRemove() {
        return objectsIdsToRemove;
    }

    public void setObjectsIdsToRemove(String objectsIdsToRemove) {
        this.objectsIdsToRemove = objectsIdsToRemove;
    }

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
