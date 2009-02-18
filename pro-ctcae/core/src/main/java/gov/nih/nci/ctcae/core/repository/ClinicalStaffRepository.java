package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.SiteClinicalStaffQuery;

import java.util.Collection;
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


    @Override
    public ClinicalStaff findById(Integer id) {
        ClinicalStaff clinicalstaff = super.findById(id);

        Collection<SiteClinicalStaff> siteClinicalStaffCollection = clinicalstaff.getSiteClinicalStaffs();
        for (SiteClinicalStaff siteClinicalStaff : siteClinicalStaffCollection) {
            siteClinicalStaff.getOrganization().getDisplayName();
        }
        return clinicalstaff;


    }


    public List<SiteClinicalStaff> findByStudyOrganizationId(String text, Integer studyOrganizationId) {
        SiteClinicalStaffQuery query = new SiteClinicalStaffQuery();
        query.filterByFirstNameOrLastNameOrNciIdentifier(text);
        StudyOrganization studyOrganization = genericRepository.findById(StudyOrganization.class, studyOrganizationId);
        if (studyOrganization != null) {
            Integer organizationId = studyOrganization.getOrganization().getId();
            query.filterByOrganization(organizationId);

            return (List<SiteClinicalStaff>) genericRepository.find(query);
        }

        throw new CtcAeSystemException(String.format("no study organization found for given id %s", studyOrganizationId));
    }
}
