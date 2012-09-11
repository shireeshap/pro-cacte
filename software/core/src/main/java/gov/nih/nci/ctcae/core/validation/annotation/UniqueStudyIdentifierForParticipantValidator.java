package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

/**
 * The Class UniqueStudyIdentifierForParticipantValidator.
 *
 * @author Reshma Koganti
 * @since  Dec 9, 2010
 */
public class UniqueStudyIdentifierForParticipantValidator extends AbstractValidator<UniqueStudyIdentifierForParticipant> {

    private String message;

    private ParticipantRepository participantRepository;

    /**
     * Validate unique participant identifier.
     * Study Participant Identifier has to be unique for a given study.
     *
     * @param studyId the study id
     * @param assignedIdentifier the assigned identifier
     * @param participantID the participant id
     * @return true, if successful
     */
    public boolean validateUniqueParticipantIdentifier(Integer studyId,String assignedIdentifier,Integer participantID) {
            ParticipantQuery participantQuery = new ParticipantQuery();
            participantQuery.filterByStudy(studyId);
            participantQuery.filterByStudyParticipantIdentifierExactMatch(assignedIdentifier);
            participantQuery.excludeByParticipantId(participantID);

            List<Participant> participants = (ArrayList<Participant>) participantRepository.find(participantQuery);
            if(!CollectionUtils.isEmpty(participants)){
                return true;
            }

        return false;
    }
   
    /**
     * Validate unique participant mrn.
     * Participant MRN(or assigned identifier) has to be unique for a site.
     *
     * @param siteId the site id
     * @param mrn the mrn
     * @param participantID the participant id
     * @return true, if successful
     */
    public boolean validateUniqueParticipantMrn(Integer siteId, String mrn, Integer participantID) {
	       ParticipantQuery participantQuery = new ParticipantQuery();
	       participantQuery.filterBySite(siteId);
	       participantQuery.filterByParticipantIdentifierExactMatch(mrn);
	       participantQuery.excludeByParticipantId(participantID);
	
	       List<Participant> participants = (ArrayList<Participant>) participantRepository.find(participantQuery);
	       if(!CollectionUtils.isEmpty(participants)){
	           return true;
	       }
	
	   return false;
	}


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

