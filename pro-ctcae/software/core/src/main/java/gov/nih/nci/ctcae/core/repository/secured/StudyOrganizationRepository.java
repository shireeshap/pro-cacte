package gov.nih.nci.ctcae.core.repository.secured;

import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.Repository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

//
/**
 * The Class StudyRepository.
 *
 * @author Vinay Kumar
 * @since Oct 7, 2008
 */

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class StudyOrganizationRepository implements Repository<StudyOrganization, StudyOrganizationQuery> {
    private GenericRepository genericRepository;


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }


    public StudyOrganization findById(Integer id) {
        return genericRepository.findById(StudyOrganization.class, id);


    }

    public StudyOrganization save(StudyOrganization studyOrganization) {
        return genericRepository.save(studyOrganization);


    }

    public void delete(StudyOrganization studyOrganization) {


    }

    public Collection<StudyOrganization> find(StudyOrganizationQuery query) {
        return genericRepository.find(query);


    }

    public StudyOrganization findSingle(StudyOrganizationQuery query) {
        return genericRepository.findSingle(query);


    }

    public List<StudyOrganization> findByStudyId(String text, Integer studyId) {
        StudyOrganizationQuery query = new StudyOrganizationQuery();
        query.filterByOrganizationName(text);
        query.filterByStudyId(studyId);
        query.filterByStudySiteAndLeadSiteOnly();
        return (List<StudyOrganization>) find(query);

    }

}