package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Date: Oct 23, 2008
 */
public class ScheduleCrfAjaxFacade {
    private ParticipantRepository participantRepository;
    private StudyRepository studyRepository;

    public List<Study> matchStudies(String text, Integer participantId) {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesWithMatchingText(text);
        if (participantId != null) {
            studyQuery.filterByParticipant(participantId);
        }
        List<Study> studies = (List<Study>) studyRepository.find(studyQuery);
        return ObjectTools.reduceAll(studies, "id", "shortTitle", "assignedIdentifier");
    }

    public List<Participant> matchParticipants(String text, Integer studyId) {
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterParticipantsWithMatchingText(text);
        participantQuery.filterByStudy(studyId);
        List<Participant> participants = (List<Participant>) participantRepository.find(participantQuery);
        return ObjectTools.reduceAll(participants, "id", "firstName", "lastName");
    }

    @Required
    public void setParticipantRepository(
            ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void setStudyRepository(gov.nih.nci.ctcae.core.repository.StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}