package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Dec 30, 2010
 * Time: 8:33:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class UniqueParticipantUserNumberValidator extends AbstractValidator<UniqueParticipantUserNumber>{
    private String message;
    private GenericRepository genericRepository;
    public boolean validateUserNumber(String userNumber, Integer participantID){
     //   Integer userNum = Integer.parseInt(userNumber);
        ParticipantQuery participantQuery = new ParticipantQuery(false);
        participantQuery.filterByUserNumber(userNumber);
        participantQuery.excludeByParticipantId(participantID);

        Collection<Participant> participants = genericRepository.find(participantQuery);
        if(!CollectionUtils.isEmpty(participants)) {
            return true;
        }
        return false;
    }

     public boolean validatePhoneNumber(String phoneNumber, Integer participantID){
        ParticipantQuery participantQuery = new ParticipantQuery(false);
        participantQuery.excludeByParticipantId(participantID);
        participantQuery.filterByPhoneNumber(phoneNumber);

        Collection<Participant> participants = genericRepository.find(participantQuery);
        if(!CollectionUtils.isEmpty(participants)) {
            return true;
        }
        return false;
    }

    public void initialize(UniqueParticipantUserNumber parameters) {
        message = parameters.message();
    }

    public String message() {
        return message;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
