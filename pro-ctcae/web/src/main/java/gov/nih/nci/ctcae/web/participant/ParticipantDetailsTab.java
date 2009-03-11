package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.StudyOrganizationRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

//
/**
 * @author Harsh Agarwal
 * @crated Feb 19, 2009
 */
public class ParticipantDetailsTab extends SecuredTab<ParticipantCommand> {
    /**
     * The organization repository.
     */
    /**
     * The crf repository.
     */
    protected CRFRepository crfRepository;

    private StudyOrganizationRepository studyOrganizationRepository;

    /**
     * Instantiates a new participant details tab.
     */
    public ParticipantDetailsTab() {
        super("participant.tab.participant_details", "participant.tab.participant_details", "participant/createParticipant");
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_CREATE_PARTICIPANT;


    }

    @Override
    public void validate(ParticipantCommand command, Errors errors) {
        super.validate(command, errors);
        if (command.getStudySites() == null ||
                command.getStudySites().size() == 0) {
            if (command.getParticipant().getStudyParticipantAssignments() == null ||
                    command.getParticipant().getStudyParticipantAssignments().size() == 0) {
                errors.reject(
                        "studyId", "Please select at least one study.");
            }
        }
    }

    public Map<String, Object> referenceData(ParticipantCommand command) {
        HashMap<String, Object> referenceData = new HashMap<String, Object>();


        StudyOrganizationQuery query = new StudyOrganizationQuery();
        query.filterByStudySiteOnly();


        Collection<StudyOrganization> studySites = studyOrganizationRepository.find(query);

        List<Organization> organizationsHavingStudySite = new ArrayList();
        for (StudyOrganization studySite : studySites) {
            if (!organizationsHavingStudySite.contains(studySite.getOrganization())) {
                organizationsHavingStudySite.add(studySite.getOrganization());
            }
        }

        ListValues listValues = new ListValues();
        referenceData.put("genders", listValues.getGenderType());
        referenceData.put("ethnicities", listValues.getEthnicityType());
        referenceData.put("races", listValues.getRaceType());
        referenceData.put("organizationsHavingStudySite", ListValues.getOrganizationsHavingStudySite(organizationsHavingStudySite));
        return referenceData;
    }


    @Override
    public void postProcess(HttpServletRequest request, ParticipantCommand command, Errors errors) {
        command.apply(crfRepository, request);
        super.postProcess(request, command, errors);
    }

    /**
     * Sets the crf repository.
     *
     * @param crfRepository the new crf repository
     */
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }
}