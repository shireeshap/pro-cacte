package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import org.aspectj.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

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
            participantQuery.filterByStudyParticipantIdentifier(assignedIdentifier);
            participantQuery.excludeByParticipantId(participantID);

            Collection<Participant> participants = participantRepository.find(participantQuery);
            if(!CollectionUtils.isEmpty(participants)){
                return true;
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

