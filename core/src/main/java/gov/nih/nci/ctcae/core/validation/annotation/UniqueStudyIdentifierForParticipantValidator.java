package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
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
    private ParticipantRepository participantRepository;
    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object)
     */
   public boolean validateUniqueParticipantIdentifier(Integer studyId,String assignedIdentifier,Integer participantID) {
            ParticipantQuery participantQuery = new ParticipantQuery();
            participantQuery.filterByStudy(studyId);
            Collection<Participant> participants = participantRepository.find(participantQuery);
            if(participants != null && !participants.isEmpty() && participants.size()>0){
                for(Participant participant:participants){
                    if(participant.getStudyParticipantIdentifier().equals(assignedIdentifier)){
                        if(participantID == null || participantID.equals("")){
							return true;
						} else if(participant.getId()==participantID){
							return false;
						}
                    }
                    else
                        return false;
                }
            }
        return false;
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

    @Required
    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

}

