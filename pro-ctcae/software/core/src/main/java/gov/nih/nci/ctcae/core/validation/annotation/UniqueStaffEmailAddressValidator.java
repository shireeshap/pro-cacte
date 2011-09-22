package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Dec 20, 2010
 * Time: 2:16:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class UniqueStaffEmailAddressValidator extends AbstractValidator<UniqueStaffEmailAddress> {
    private String message;
    private ClinicalStaffRepository clinicalStaffRepository;
    private ParticipantRepository participantRepository;

    public boolean validateStaffEmail(String emailAddress, Integer staffId) {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByEmail(emailAddress);
        clinicalStaffQuery.excludeByStaffId(staffId);
        Collection<ClinicalStaff> clinicalStaffs = clinicalStaffRepository.find(clinicalStaffQuery);
        if (clinicalStaffs != null && !clinicalStaffs.isEmpty()) {
            return true;
        }

        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByEmail(emailAddress);
      //  participantQuery.excludeByParticipantId(participantID);
        Collection<Participant> participants = participantRepository.find(participantQuery);
        if(!CollectionUtils.isEmpty(participants)) {
            return true;
        }
        return false;
    }

    public void initialize(UniqueStaffEmailAddress parameters) {
        message = parameters.message();
    }

    public String message() {
        return message;
    }

    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

    @Required
    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
}
