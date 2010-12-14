package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;

//
/**
 * The Class UniqueStudyIdentifierForParticipantValidator.
 *
 * @author Reshma Koganti
 * @since  Dec 9, 2010
 */
public class UniqueStudyIdentifierForParticipantValidator extends AbstractValidator<UniqueStudyIdentifierForParticipant> {

    /**
     * The message.
     */
    private String message;

    /**
     * The study repository.
     */
    private StudyRepository studyRepository;
    private ParticipantRepository participantRepository;

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object)
     */
    public boolean validate(Integer studyId,String assignedIdentifier) {
            ParticipantQuery participantQuery = new ParticipantQuery();
            participantQuery.filterByStudy(studyId);
            Collection<Participant> participants = participantRepository.find(participantQuery);
            if(participants != null || !participants.isEmpty()){
                for(Participant participant:participants){
                    if(participant.getStudyParticipantIdentifier().equals(assignedIdentifier)){
                        return true;
                    }
                    else
                        return false;
                }
            }
//            return (studies == null || studies.isEmpty()) ? true : false;
        return true;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#initialize(java.lang.annotation.Annotation)
     */
    public void initialize(UniqueStudyIdentifierForParticipant parameters) {
         message = parameters.message();
    }

    public String message() {
        return message;
    }

    /**
     * Sets the study repository.
     *
     * @param studyRepository the new study repository
     */
    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Required
    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
}

