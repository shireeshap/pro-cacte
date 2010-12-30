package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import org.springframework.beans.factory.annotation.Required;

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
    private ParticipantRepository participantRepository;
    public boolean validateUserNumber(String userNumber, String participantID){
        Integer userNum = Integer.parseInt(userNumber);
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByUserNumber(userNum);
        Collection<Participant> participants = participantRepository.find(participantQuery);
        boolean flag = false;
        if (participants != null && !participants.isEmpty()) {
            if(participantID ==null || participantID.equals("")){
                return true;
            }
            else{
                for(Participant participant:participants){
                    if(participant.getId().equals(Integer.parseInt(participantID))){
                        flag = true;
                    }
                }
                if(!flag){
                    return true;
                }
            }
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
    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
}
