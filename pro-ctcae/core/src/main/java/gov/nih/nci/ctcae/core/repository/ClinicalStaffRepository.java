package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;

//
/**
 * The Class ClinicalStaffRepository.
 *
 * @author Mehul Gulati
 *         Date: Oct 15, 2008
 */
public class ClinicalStaffRepository extends AbstractRepository<ClinicalStaff, ClinicalStaffQuery> {

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#getPersistableClass()
     */
    @Override
    protected Class<ClinicalStaff> getPersistableClass() {
        return ClinicalStaff.class;
    }

//    @Override
//    public ClinicalStaff findById(Integer id) {
//        ClinicalStaff clinicalstaff = super.findById(id);
//
//        Collection<SiteClinicalStaff> clinicalStaffCollection = clinicalstaff.getSiteClinicalStaffs();
//        for (SiteClinicalStaff siteClinicalStaff : clinicalStaffCollection) {
//            siteClinicalStaff.getOrganization().getDisplayName();
//        }
//        return clinicalstaff;
//
//
//    }
}
