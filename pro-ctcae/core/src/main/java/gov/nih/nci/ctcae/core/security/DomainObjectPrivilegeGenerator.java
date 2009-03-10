package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.*;

/**
 * @author Vinay Kumar
 * @crated Mar 10, 2009
 */
public class DomainObjectPrivilegeGenerator {

    public String generatePrivilege(Persistable persistable) {
        if (CRF.class.isAssignableFrom(persistable.getClass())) {
            CRF crf = (CRF) persistable;
            return generatePrivilege(crf.getStudy());


        } else if (Study.class.isAssignableFrom(persistable.getClass())) {

            return generatePrivilegeForPersistable(persistable);
        } else if (StudyOrganization.class.isAssignableFrom(persistable.getClass())) {
            StudyOrganization studyOrganization = (StudyOrganization) persistable;
            return generatePrivilegeForPersistable(studyOrganization.getStudy());
        } else if (Participant.class.isAssignableFrom(persistable.getClass())) {
            Participant participant = (Participant) persistable;
            String privilege = "no assignment found for this participant";
            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
                privilege = generatePrivilegeForPersistable(studyParticipantAssignment.getStudySite().getStudy());
            }
            return privilege;
        }
        return generatePrivilegeForPersistable(persistable);
    }

    private String generatePrivilegeForPersistable(Persistable persistable) {
        return persistable.getClass().getName() + "." + persistable.getId();
    }
}
