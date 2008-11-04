package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;

/**
 * @author Mehul Gulati
 *         Date: Oct 15, 2008
 */
public class ClinicalStaffRepository extends AbstractRepository<ClinicalStaff, ClinicalStaffQuery> {

    @Override
    protected Class<ClinicalStaff> getPersistableClass() {
        return ClinicalStaff.class;
    }

//    @Override
//    public ClinicalStaff findById(Integer id) {
//        ClinicalStaff clinicalStaff = super.findById(id);
//
//        Collection<SiteClinicalStaff> clinicalStaffCollection = clinicalStaff.getSiteClinicalStaffs();
//        for (SiteClinicalStaff siteClinicalStaff : clinicalStaffCollection) {
//            siteClinicalStaff.getOrganization().getDisplayName();
//        }
//        return clinicalStaff;
//
//
//    }
}
