package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.*;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 10, 2009
 */
public class DomainObjectPrivilegeGenerator {

    public String generatePrivilege(Persistable persistable) {
        if (CRF.class.isAssignableFrom(persistable.getClass())) {
            CRF crf = (CRF) persistable;
            return generatePrivilege(crf.getStudy());


        }
        if (Study.class.isAssignableFrom(persistable.getClass())) {

            return generatePrivilegeForPersistable(persistable);
        }
        if (StudyOrganization.class.isAssignableFrom(persistable.getClass())) {
            StudyOrganization studyOrganization = (StudyOrganization) persistable;
            return generatePrivilegeForPersistable(studyOrganization.getStudy());
        }
        if (Participant.class.isAssignableFrom(persistable.getClass())) {
            Participant participant = (Participant) persistable;
            List<StudyParticipantAssignment> studyParticipantAssignments = participant.getStudyParticipantAssignments();
            String privilege = generatePrivilegeForPersistable(studyParticipantAssignments.get(0).getStudySite().getStudy());

            for (StudyParticipantAssignment studyParticipantAssignment : studyParticipantAssignments) {
                String privilegeString = generatePrivilegeForPersistable(studyParticipantAssignment.getStudySite().getStudy());
                if (!StringUtils.equals(privilege, privilegeString)) {
                    privilege = privilege + "-" + privilegeString;
                }
            }
            return privilege;
        }
        if (StudyParticipantAssignment.class.isAssignableFrom(persistable.getClass())) {
            StudyParticipantAssignment studyParticipantAssignment = (StudyParticipantAssignment) persistable;
            String privilege = generatePrivilegeForPersistable(studyParticipantAssignment.getStudySite().getStudy());
            return privilege;
        }
        return generatePrivilegeForPersistable(persistable);
    }

    private String generatePrivilegeForPersistable(Persistable persistable) {
        return persistable.getClass().getName() + "." + persistable.getId();
    }
}
