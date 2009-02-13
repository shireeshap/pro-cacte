package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignment;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
//        Collection<ClinicalStaffAssignment> clinicalStaffCollection = clinicalstaff.getClinicalStaffAssignments();
//        for (ClinicalStaffAssignment siteClinicalStaff : clinicalStaffCollection) {
//            siteClinicalStaff.getOrganization().getDisplayName();
//        }
//        return clinicalstaff;
//
//
//    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public List<ClinicalStaffAssignment> save(List<ClinicalStaffAssignment> clinicalStaffAssignments) {
        List<ClinicalStaffAssignment> savedClinicalStaffAssignments = new ArrayList<ClinicalStaffAssignment>();
        for (ClinicalStaffAssignment clinicalStaffAssignment : clinicalStaffAssignments) {
            savedClinicalStaffAssignments.add(genericRepository.save(clinicalStaffAssignment));
        }
        return savedClinicalStaffAssignments;
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeClinicalStaffAssignment(ClinicalStaffAssignment clinicalStaffAssignment) {
        ClinicalStaff staff = clinicalStaffAssignment.getClinicalStaff();
        staff.removeClinicalStaffAssignment(clinicalStaffAssignment);
        genericRepository.delete(clinicalStaffAssignment);
        //genericRepository.save(staff);
    }
}
