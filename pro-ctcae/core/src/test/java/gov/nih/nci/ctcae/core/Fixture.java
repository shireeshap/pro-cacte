package gov.nih.nci.ctcae.core;

import gov.nih.nci.ctcae.core.domain.*;

/**
 * @author Vinay Kumar
 */
public class Fixture {


    public static Organization createOrganization(String name, String nciCode) {

        Organization organization = new Organization();
        organization.setName(name);
        organization.setNciInstituteCode(nciCode);
        return organization;

    }

    public static CRF createCrf() {
        CRF crf = new CRF();
        crf.setTitle("Cancer CRF");
        crf.setDescription("Case Report Form for Cancer Patients");
        crf.setStatus(CrfStatus.DRAFT);
        crf.setCrfVersion("1.0");
        return crf;
    }


    public static CrfPageItemDisplayRule createCrfPageItemDisplayRules(final Integer id, final ProCtcValidValue proCtcValidValue) {
        CrfPageItemDisplayRule crfPageItemDisplayRule = new CrfPageItemDisplayRule();
        crfPageItemDisplayRule.setId(id);
        crfPageItemDisplayRule.setProCtcValidValue(proCtcValidValue);
        return crfPageItemDisplayRule;


    }

    public static Study createStudy(final String shortTitle, final String longTitle, final String assignedIdentifier) {
        Study study = new Study();
        study.setShortTitle(shortTitle);
        study.setLongTitle(longTitle);
        study.setAssignedIdentifier(assignedIdentifier);
        return study;
    }

    public static Study createStudyWithStudySite(final String shortTitle, final String longTitle, final String assignedIdentifier, final Organization organization) {
        Study study = createStudy(shortTitle, longTitle, assignedIdentifier);
        StudySite studySite = new StudySite();
        studySite.setOrganization(organization);
        study.addStudySite(studySite);
        return study;
    }

    public static ClinicalStaff createClinicalStaff(final String firstName, final String lastName, final String nciIdentifier) {
        ClinicalStaff clinicalStaff = new ClinicalStaff();
        clinicalStaff.setFirstName(firstName);
        clinicalStaff.setLastName(lastName);
        clinicalStaff.setNciIdentifier(nciIdentifier);
        return clinicalStaff;
    }

    public static Participant createParticipant(final String firstName, final String lastName, final String identifier) {
        Participant participant = new Participant();
        participant.setFirstName(firstName);
        participant.setLastName(lastName);
        participant.setAssignedIdentifier(identifier);
        return participant;
    }

    public static Participant createParticipantWithStudyAssignment(final String firstName, final String lastName, final String identifier, final StudySite studySite) {
        Participant participant = createParticipant(firstName, lastName, identifier);
        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(studySite);
        studyParticipantAssignment.setStudyParticipantIdentifier("SPI");
        participant.addStudyParticipantAssignment(studyParticipantAssignment);
        return participant;
    }

    public static CRF createCrf(final String title, final CrfStatus crfStatus, final String crfVersion) {
        CRF crf = new CRF();
        crf.setTitle(title);
        crf.setStatus(crfStatus);
        crf.setCrfVersion(crfVersion);
        return crf;
    }


}
