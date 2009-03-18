package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
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

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)

public class ParticipantRepository implements Repository<Participant, ParticipantQuery> {

    private GenericRepository genericRepository;

    public Participant findById(Integer id) {
        Participant participant = genericRepository.findById(Participant.class, id);

        initialzeParticipant(participant);
        return participant;

    }

    private void initialzeParticipant(Participant participant) {
        for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
            StudyOrganization studyOrganization = studyParticipantAssignment.getStudySite();
            studyOrganization.getStudy();
            studyOrganization.getOrganization();
            studyParticipantAssignment.getParticipant();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Participant save(Participant participant) {
        if (participant.getStudyParticipantAssignments().isEmpty()) {
            throw new CtcAeSystemException("can not save participants without assignments");
        }
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
