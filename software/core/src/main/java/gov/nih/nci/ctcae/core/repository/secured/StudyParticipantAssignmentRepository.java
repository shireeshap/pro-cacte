package gov.nih.nci.ctcae.core.repository.secured;

import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.StudyParticipantAssignmentQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.Repository;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//
/**
 * The Class StudyParticipantAssignmentRepository.
 *
 * @author mehul
 */
@org.springframework.stereotype.Repository
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)

public class StudyParticipantAssignmentRepository implements Repository<StudyParticipantAssignment, StudyParticipantAssignmentQuery> {

    private GenericRepository genericRepository;

    public StudyParticipantAssignment findById(Integer id) {
        return genericRepository.findById(StudyParticipantAssignment.class, id);


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public StudyParticipantAssignment save(StudyParticipantAssignment studyParticipantAssignment) {
        return genericRepository.save(studyParticipantAssignment);


    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(StudyParticipantAssignment studyParticipantAssignment) {
        throw new CtcAeSystemException("delete method not supported");

    }

    public Collection<StudyParticipantAssignment> find(StudyParticipantAssignmentQuery query) {
        return genericRepository.find(query);


    }

    public StudyParticipantAssignment findSingle(StudyParticipantAssignmentQuery query) {
        return genericRepository.findSingle(query);


    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}