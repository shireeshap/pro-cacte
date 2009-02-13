package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.ClinicalStaffAssignmentQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @created Feb 06, 2009
 */
public class ClinicalStaffAssignmentIntegrationTest extends AbstractHibernateIntegrationTestCase {


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        defaultClinicalStaffAssignment.getClinicalStaffAssignmentRoles().clear();
        defaultClinicalStaffAssignment.setDomainObjectClass(StudySite.class.getName());
        defaultClinicalStaffAssignment.setDomainObjectId(defaultStudySite.getId());
        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);
        commitAndStartNewTransaction();

        defaultClinicalStaff = clinicalStaffRepository.findById(defaultClinicalStaff.getId());
        assertFalse("must have clinical staff assignment", defaultClinicalStaff.getClinicalStaffAssignments().isEmpty());
        assertEquals("must update the assignment", StudySite.class.getName(), defaultClinicalStaffAssignment.getDomainObjectClass());
        assertEquals("must update the assignment", Integer.valueOf(defaultStudySite.getId()), defaultClinicalStaffAssignment.getDomainObjectId());
        defaultClinicalStaffAssignment = defaultClinicalStaff.getClinicalStaffAssignments().get(0);


    }

    public void testFindAssignmentByStudySite() {
        ClinicalStaffAssignmentQuery query = new ClinicalStaffAssignmentQuery();
        List<Integer> studySiteIds = new ArrayList<Integer>();
        studySiteIds.add(defaultStudySite.getId());
        studySiteIds.add(-10001); //add non existing study site id also

        query.filterByStudySiteIds(studySiteIds);
        List<ClinicalStaffAssignment> clinicalStaffAssignments = (List<ClinicalStaffAssignment>) finderRepository.find(query);
        assertFalse("must find assignments", clinicalStaffAssignments.isEmpty());


        for (ClinicalStaffAssignment clinicalStaffAssignment : clinicalStaffAssignments) {
            assertEquals("must find the  assignment", StudySite.class.getName(), clinicalStaffAssignment.getDomainObjectClass());
            assertEquals("must find the assignment", Integer.valueOf(defaultStudySite.getId()), clinicalStaffAssignment.getDomainObjectId());

        }


    }


}