package gov.nih.nci.ctcae.core.repository.secured;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.StudyMode;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantMode;
import gov.nih.nci.ctcae.core.domain.StudyParticipantReportingModeHistory;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.Repository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.security.SecurityHelper;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    public Long findWithCount(ParticipantQuery query) {
        return genericRepository.findWithCount(query);
    }

    private void initialzeParticipant(Participant participant) {
        for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
            StudyOrganization studyOrganization = studyParticipantAssignment.getStudySite();
            studyOrganization.getStudy();
            studyOrganization.getOrganization();
            studyParticipantAssignment.getParticipant();
            studyParticipantAssignment.getStudyParticipantCrfs();
            studyParticipantAssignment.getStudyParticipantClinicalStaffs();
            for (StudyParticipantMode studyParticipantMode : studyParticipantAssignment.getStudyParticipantModes()) {
                studyParticipantMode.getMode();
            }
            for (StudyMode studyMode : studyParticipantAssignment.getStudySite().getStudy().getStudyModes()) {
                studyMode.getMode();
            }
            for (StudyParticipantReportingModeHistory studyParticipantReportingModeHistory : studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems()) {
                studyParticipantReportingModeHistory.getMode();
            }
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
            user.setNumberOfAttempts(0);
            user.setAccountNonLocked(true);
            user = userRepository.save(user);
            participant = genericRepository.save(participant);

        } else {
            throw new CtcAeSystemException("can not save participant without user");
        }

        initialzeParticipant(participant);

        participant.getUser().setConfirmPassword(participant.getUser().getPassword());
        participant.setConfirmPinNumber(participant.getPinNumber());
        return participant;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveOrUpdate(Participant participant) {
        if (participant.getStudyParticipantAssignments().isEmpty()) {
            throw new CtcAeSystemException("can not save participants without assignments");
        }
        User user = participant.getUser();
        if (participant.getId() == null) {
            //create flow
            if (user == null) throw new CtcAeSystemException("can not save participant without user");
            UserRole userRole = new UserRole();
            userRole.setRole(Role.PARTICIPANT);
            user.addUserRole(userRole);
            user.setNumberOfAttempts(0);
            user.setAccountNonLocked(true);
        }

        userRepository.saveOrUpdate(participant.getUser(), participant.getPassword());
        genericRepository.saveOrUpdate(participant);
        participant.getUser().setConfirmPassword(participant.getUser().getPassword());

    }

    public List<Participant> findByStudySiteId(String text, Integer studySiteId) {
        boolean isLeadStaff = SecurityHelper.isUserSiteIndependent();
        ParticipantQuery query;
        if (isLeadStaff) {
            query = new ParticipantQuery();
        } else {
            query = new ParticipantQuery(true);
        }
        query.filterParticipantsWithMatchingText(text);
        query.filterByStudySite(studySiteId);
        return (List<Participant>) find(query);
    }

    public List<Participant> findByStudyId(String text, Integer studyId) {
        boolean isLeadStaff = SecurityHelper.isUserSiteIndependent();
        ParticipantQuery query;
        if (isLeadStaff) {
            query = new ParticipantQuery();
        } else {
            query = new ParticipantQuery(true);
        }
        query.filterParticipantsWithMatchingText(text);
        query.filterByStudy(studyId);
        return (List<Participant>) find(query);
    }


    public void delete(Participant participant) {
        if (participant != null) {
            genericRepository.delete(participant);
        }
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
