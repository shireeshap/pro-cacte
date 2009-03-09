package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

//
/**
 * The Class ParticipantRepository.
 *
 * @author mehul
 */

public class ParticipantRepository implements Repository<Participant, ParticipantQuery> {

    private GenericRepository genericRepository;

    public Participant findById(Integer id) {
        return genericRepository.findById(Participant.class, id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Participant save(Participant participant) {
        Participant savedParticipant = genericRepository.save(participant);
        for (StudyParticipantAssignment studyParticipantAssignment : savedParticipant.getStudyParticipantAssignments()) {
            studyParticipantAssignment.getStudyParticipantCrfs();
            studyParticipantAssignment.getStudyParticipantClinicalStaffs();
        }
        return savedParticipant;
    }

    public void delete(Participant participant) {
        throw new CtcAeSystemException("delete method not supported");

    }

    public Collection<Participant> find(ParticipantQuery query) {
        return genericRepository.find(query);


    }

    public Participant findSingle(ParticipantQuery query) {
        return genericRepository.findSingle(query);

    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
