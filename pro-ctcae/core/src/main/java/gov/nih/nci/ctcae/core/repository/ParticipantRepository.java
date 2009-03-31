package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

//
/**
 * The Class ParticipantRepository.
 *
 * @author mehul
 */

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)

public class ParticipantRepository implements Repository<Participant, ParticipantQuery> {

    private GenericRepository genericRepository;
    private UserRepository userRepository;

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
        User user = participant.getUser();
        if (user != null) {
            UserRole userRole = new UserRole();
            userRole.setRole(Role.PARTICIPANT);
            user.addUserRole(userRole);
            user = userRepository.save(user);
            participant = genericRepository.save(participant);

        } else {
            throw new CtcAeSystemException("can not save participant without user");
        }

        for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
            studyParticipantAssignment.getStudyParticipantCrfs();
            studyParticipantAssignment.getStudyParticipantClinicalStaffs();
        }
        return participant;
    }

    public List<Participant> findByStudySiteId(String text, Integer studySiteId) {
        ParticipantQuery query = new ParticipantQuery();
        query.filterParticipantsWithMatchingText(text);
        query.filterByStudySite(studySiteId);
        return (List<Participant>) find(query);

    }

    public List<Participant> findByStudyId(String text, Integer studyId) {
        ParticipantQuery query = new ParticipantQuery();
        query.filterParticipantsWithMatchingText(text);
        query.filterByStudy(studyId);
        return (List<Participant>) find(query);

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

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
