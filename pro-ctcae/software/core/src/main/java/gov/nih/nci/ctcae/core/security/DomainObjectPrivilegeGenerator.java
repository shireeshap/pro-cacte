package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Vinay Kumar
 * @since Mar 10, 2009
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


    private Set<String> generatePrivilege(StudyParticipantAssignment studyParticipantAssignment) {
        Set<String> privileges = new HashSet<String>();

        privileges.add(generatePrivilege(studyParticipantAssignment.getStudySite().getStudy()));
        privileges.addAll(generatePrivilege(studyParticipantAssignment.getStudySite()));
        List<StudyParticipantCrf> studyParticipantCrfs = studyParticipantAssignment.getStudyParticipantCrfs();
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
            privileges.add(generatePrivilege(studyParticipantCrf));
        }

        return privileges;

    }

    private Set<String> generatePrivilege(OrganizationClinicalStaff organizationClinicalStaff) {
        Set<String> privileges = new HashSet<String>();

        privileges.add(generatePrivilege(organizationClinicalStaff.getOrganization()));

        return privileges;

    }


    private Set<String> generatePrivilege(Participant participant) {

        Set<String> privileges = new HashSet<String>();
        privileges.add(generatePrivilegeForPersistable(participant));
        List<StudyParticipantAssignment> studyParticipantAssignments = participant.getStudyParticipantAssignments();

        for (StudyParticipantAssignment studyParticipantAssignment : studyParticipantAssignments) {
            privileges.addAll(generatePrivilege(studyParticipantAssignment));
        }

        return privileges;
    }

    private Set<String> generatePrivilege(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        Set<String> privileges = new HashSet<String>();
        privileges.addAll(generatePrivilege(studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment()));

        return privileges;
    }

    private String generatePrivilege(StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion) {
        return generatePrivilege(studyParticipantCrfScheduleAddedQuestion.getStudyParticipantCrfSchedule().getStudyParticipantCrf());
    }

    private String generatePrivilege(StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion) {
        return generatePrivilege(studyParticipantCrfAddedQuestion.getStudyParticipantCrf());
    }

    private String generatePrivilege(StudyParticipantCrf studyParticipantCrf) {
        return generatePrivilegeForPersistable(studyParticipantCrf);
    }

    private Set<String> generatePrivilege(ClinicalStaff clinicalStaff) {

        Set<String> privileges = new HashSet<String>();

        List<OrganizationClinicalStaff> organizationClinicalStaffSet = clinicalStaff.getOrganizationClinicalStaffs();
        for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffSet) {
            privileges.addAll(generatePrivilege(organizationClinicalStaff));
        }
        return privileges;
    }

    private Set<String> generatePrivilege(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        Set<String> privileges = new HashSet<String>();

        StudyOrganization studyOrganization = studyOrganizationClinicalStaff.getStudyOrganization();
        if (studyOrganization instanceof LeadStudySite) {
            if (studyOrganizationClinicalStaff.getRole().equals(Role.LEAD_CRA) || studyOrganizationClinicalStaff.getRole().equals(Role.PI)) {
                privileges.add(generateGroupPrivilegeForStudyOrganization(studyOrganization));
                privileges.add(generateGroupPrivilegeForOrganizations());
                for(StudyOrganization so :studyOrganizationClinicalStaff.getStudyOrganization().getStudy().getStudyOrganizations()){
                    privileges.add(generatePrivilege(so.getOrganization()));    
                }
            }
        }
        privileges.add(generatePrivilege(studyOrganization.getStudy()));
        privileges.add(generatePrivilegeForPersistable(studyOrganization));

        return privileges;
    }

    private String generateGroupPrivilegeForOrganizations() {
        return Organization.class.getName() + ".GROUP";
    }

    private Set<String> generatePrivilege(StudyOrganization studyOrganization) {
        Set<String> privileges = new HashSet<String>();
        privileges.add(generatePrivilegeForPersistable(studyOrganization));
        privileges.add(generateGroupPrivilegeForStudyOrganization(studyOrganization));
        return privileges;

    }

    public Set<String> generatePrivilege(Persistable persistable) {

        Set<String> privileges = new HashSet<String>();

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
        if (persistable.getClass().isAssignableFrom(ClinicalStaff.class)) {
            privileges.addAll(generatePrivilege((ClinicalStaff) persistable));
        }
        if (persistable.getClass().isAssignableFrom(StudyParticipantCrfSchedule.class)) {
            privileges.addAll(generatePrivilege((StudyParticipantCrfSchedule) persistable));
        }
        if (persistable.getClass().isAssignableFrom(StudyParticipantCrfScheduleAddedQuestion.class)) {
            privileges.add(generatePrivilege((StudyParticipantCrfScheduleAddedQuestion) persistable));
        }
        if (persistable.getClass().isAssignableFrom(StudyParticipantCrfAddedQuestion.class)) {
            privileges.add(generatePrivilege((StudyParticipantCrfAddedQuestion) persistable));
        }
        if (persistable.getClass().isAssignableFrom(StudyParticipantCrf.class)) {
            privileges.add(generatePrivilege((StudyParticipantCrf) persistable));
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
