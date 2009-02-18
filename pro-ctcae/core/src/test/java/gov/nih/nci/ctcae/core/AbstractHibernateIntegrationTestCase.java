package gov.nih.nci.ctcae.core;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.*;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import java.util.Date;
import java.util.List;

public abstract class AbstractHibernateIntegrationTestCase extends AbstractTransactionalDataSourceSpringContextTests {

    protected FinderRepository finderRepository;
    protected ProCtcTermRepository proCtcTermRepository;
    protected ClinicalStaffRepository clinicalStaffRepository;
    protected OrganizationRepository organizationRepository;
    protected ClinicalStaff defaultClinicalStaff;
    protected ClinicalStaffAssignment defaultClinicalStaffAssignment;
    protected StudyParticipantAssignment defaultStudyParticipantAssignment;
    protected Organization defaultOrganization;
    protected Participant defaultParticipant;
    protected ParticipantRepository participantRepository;
    protected Study defaultStudy;
    protected StudySite defaultStudySite;
    protected StudyRepository studyRepository;

    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml",

            "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml",
//                "classpath*:gov/nih/nci/ctcae/core/applicationContext-mail.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml",
            "classpath*:gov/nih/nci/ctcae/core/resourceContext-job.xml",
//                "classpath*:gov/nih/nci/ctcae/core/applicationContext-core-security.xml",
            "classpath*:" + "/*-context-test.xml"};
    protected SiteClinicalStaff defaultSiteClinicalStaff;
    private Roles roles;


    @Override
    protected String[] getConfigLocations() {
        return context;
    }


    protected void login() {
//        user = userRepository.loadUserByUsername("saurabh1@abc.com");
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "password");
//
//        SecurityContextHolder.getContext().setAuthentication(token);


    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);

        defaultOrganization = organizationRepository.findById(105051);

        defaultStudy = new Study();
        defaultStudy.setShortTitle("A Phase 2 Study of Suberoylanilide Hydroxamic Acid (SAHA) in Acute Myeloid Leukemia (AML)");
        defaultStudy.setLongTitle("A Phase 2 Study of Suberoylanilide Hydroxamic Acid (SAHA) in Acute Myeloid Leukemia (AML)");
        defaultStudy.setAssignedIdentifier("-10001");

        defaultStudySite = new StudySite();
        defaultStudySite.setOrganization(defaultOrganization);

        defaultStudy.addStudySite(defaultStudySite);
        defaultStudy = studyRepository.save(defaultStudy);
        assertNotNull("must find default study. ", defaultStudy);
        defaultStudySite = defaultStudy.getStudySites().get(0);
        assertNotNull("must find default study site. ", defaultStudySite);

        commitAndStartNewTransaction();


        defaultParticipant = Fixture.createParticipant("Bruce", "Tanner", "P002");
        defaultStudyParticipantAssignment = new StudyParticipantAssignment();
        defaultStudyParticipantAssignment.setStudyParticipantIdentifier("1234");
        defaultStudyParticipantAssignment.setStudySite(defaultStudySite);
        defaultParticipant.addStudyParticipantAssignment(defaultStudyParticipantAssignment);
        defaultParticipant = participantRepository.save(defaultParticipant);

        commitAndStartNewTransaction();


        defaultStudyParticipantAssignment = defaultParticipant.getStudyParticipantAssignments().get(0);
        assertNotNull("must find default study participant assignment. ", defaultStudyParticipantAssignment);

        defaultClinicalStaff = new ClinicalStaff();
        defaultClinicalStaff.setFirstName("Bruce");
        defaultClinicalStaff.setLastName("Tanner");
        defaultClinicalStaff.setNciIdentifier("-1234");
        defaultSiteClinicalStaff = new SiteClinicalStaff();
        defaultSiteClinicalStaff.setOrganization(defaultOrganization);
        defaultClinicalStaff.addSiteClinicalStaff(defaultSiteClinicalStaff);

        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);

        commitAndStartNewTransaction();
        assertNotNull("must find default clinical staff. ", defaultClinicalStaff);

        defaultSiteClinicalStaff = defaultClinicalStaff.getSiteClinicalStaffs().get(0);
        assertNotNull("must find default clinical staff. ", defaultSiteClinicalStaff);


    }


    protected void updateDefaultObjects() {

//        defaultStudy = studyRepository.findById(defaultStudy.getId());
//        defaultStudySite = defaultStudy.getStudySites().get(0);
//        defaultStudyParticipantAssignment = defaultStudySite.getStudyParticipantAssignments().get(0);
//
//        defaultStudyParticipantAssignment = defaultStudySite.getStudyParticipantAssignments().get(0);
//
//        defaultClinicalStaff = clinicalStaffRepository.findById(-100);


    }

    @Override
    protected void onTearDownInTransaction() throws Exception {
        DataAuditInfo.setLocal(null);
        super.onTearDownInTransaction();

        deleteFromTables(new String[]{"study_participant_assignments", "study_organizations", "studies", "SITE_CLINICAL_STAFFS", "CLINICAL_STAFFS"});
    }


    protected ClinicalStaffAssignmentRole saveAndRetrieveClinicalStaffAssignmentRole() {

        defaultClinicalStaffAssignment.getClinicalStaffAssignmentRoles().clear();

        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);
        commitAndStartNewTransaction();

        defaultClinicalStaff = clinicalStaffRepository.findById(defaultClinicalStaff.getId());
//        assertFalse("must have clinical staff assignment", defaultClinicalStaff.getClinicalStaffAssignments().isEmpty());
//
//        defaultClinicalStaffAssignment = defaultClinicalStaff.getClinicalStaffAssignments().get(0);
        assertTrue("must remove clinical staff assignment role", defaultClinicalStaffAssignment.getClinicalStaffAssignmentRoles().isEmpty());

        ClinicalStaffAssignmentRole clinicalStaffAssignmentRole = new ClinicalStaffAssignmentRole();

        defaultClinicalStaffAssignment.addClinicalStaffAssignmentRole(clinicalStaffAssignmentRole);

        defaultClinicalStaffAssignment.setDomainObjectClass(Organization.class.getName());
        defaultClinicalStaffAssignment.setDomainObjectId(defaultOrganization.getId());

        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);
        commitAndStartNewTransaction();
        defaultClinicalStaff = clinicalStaffRepository.findById(defaultClinicalStaff.getId());

        List<ClinicalStaffAssignmentRole> clinicalStaffAssignmentRoles = defaultClinicalStaffAssignment.getClinicalStaffAssignmentRoles();

        clinicalStaffAssignmentRole = clinicalStaffAssignmentRoles.get(0);
        assertFalse("must save site clinical staff role", clinicalStaffAssignmentRoles.isEmpty());


        assertNotNull("must save site clinical staff role", clinicalStaffAssignmentRole.getId());

        assertEquals("clinical staff must be same", defaultClinicalStaffAssignment, clinicalStaffAssignmentRole.getClinicalStaffAssignment());
        return clinicalStaffAssignmentRole;
    }

    protected void commitAndStartNewTransaction() {
        setComplete();
        endTransaction();
        startNewTransaction();
    }

    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

    @Required
    public void setFinderRepository(final FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    @Required
    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }
}
