package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 10, 2009
 */
public class DomainObjectPrivilegeGenerator {

    private String generatePrivilege(CRF crf) {
        return generatePrivilege(crf.getStudy());


    }

    private String generatePrivilege(Study study) {
        return generatePrivilegeForPersistable(study);


    }

    private String generatePrivilege(Organization organization) {
        return generatePrivilegeForPersistable(organization);


    }


    private List<String> generatePrivilege(StudyParticipantAssignment studyParticipantAssignment) {
        List<String> privileges = new ArrayList<String>();

        privileges.add(generatePrivilege(studyParticipantAssignment.getStudySite().getStudy()));
        privileges.addAll(generatePrivilege(studyParticipantAssignment.getStudySite()));

        return privileges;

    }

    private List<String> generatePrivilege(Participant participant) {

        List<String> privileges = new ArrayList<String>();

        List<StudyParticipantAssignment> studyParticipantAssignments = participant.getStudyParticipantAssignments();
        privileges.addAll(generatePrivilege(studyParticipantAssignments.get(0)));
        return privileges;
    }

    private List<String> generatePrivilege(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        List<String> privileges = new ArrayList<String>();

        StudyOrganization studyOrganization = studyOrganizationClinicalStaff.getStudyOrganization();
        if (studyOrganization instanceof LeadStudySite) {
            privileges.add(generateGroupPrivilegeForStudyOrganization(studyOrganization));

        }
        privileges.add(generatePrivilege(studyOrganization.getStudy()));
        privileges.add(generatePrivilegeForPersistable(studyOrganization));

        return privileges;
    }

    private List<String> generatePrivilege(StudyOrganization studyOrganization) {
        List<String> privileges = new ArrayList<String>();
        privileges.add(generatePrivilegeForPersistable(studyOrganization));
        privileges.add(generateGroupPrivilegeForStudyOrganization(studyOrganization));
        return privileges;

    }

    public List<String> generatePrivilege(Persistable persistable) {

        List<String> privileges = new ArrayList<String>();

        if (persistable.getClass().isAssignableFrom(Study.class)) {
            privileges.add(generatePrivilege((Study) persistable));
        }
        if (persistable.getClass().isAssignableFrom(Organization.class)) {
            privileges.add(generatePrivilege((Organization) persistable));
        }
        if (persistable.getClass().isAssignableFrom(Participant.class)) {
            privileges.addAll(generatePrivilege((Participant) persistable));
        }
        if (persistable.getClass().isAssignableFrom(StudyParticipantAssignment.class)) {
            privileges.addAll(generatePrivilege((StudyParticipantAssignment) persistable));
        }
        if (StudyOrganization.class.isAssignableFrom(persistable.getClass())) {
            privileges.addAll(generatePrivilege((StudyOrganization) persistable));
        }
        if (persistable.getClass().isAssignableFrom(StudyOrganizationClinicalStaff.class)) {
            privileges.addAll(generatePrivilege((StudyOrganizationClinicalStaff) persistable));
        }
        if (persistable.getClass().isAssignableFrom(CRF.class)) {
            privileges.add(generatePrivilege((CRF) persistable));
        }
        return privileges;
    }


    private String generatePrivilegeForPersistable(Persistable persistable) {
        return persistable.getClass().getName() + "." + persistable.getId();
    }


    private String generateGroupPrivilegeForStudyOrganization(StudyOrganization studyOrganization) {
        return StudyOrganization.class.getName() + ".Study." + studyOrganization.getStudy().getId();
    }
}
