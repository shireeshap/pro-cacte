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
    protected StudyParticipantAssignment defaultStudyParticipantAssignment;
    protected Organization defaultOrganization, organization1;
    protected Participant defaultParticipant;
    protected ParticipantRepository participantRepository;
    protected Study defaultStudy;
    protected StudySite defaultStudySite, studySite1;
    protected StudyRepository studyRepository;

    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml",

            "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml",
//                "classpath*:gov/nih/nci/ctcae/core/applicationContext-mail.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml",
            "classpath*:gov/nih/nci/ctcae/core/resourceContext-job.xml",
//                "classpath*:gov/nih/nci/ctcae/core/applicationContext-core-security.xml",
            "classpath*:" + "/*-context-test.xml"};
    protected OrganizationClinicalStaff defaultOrganizationClinicalStaff;
    protected Role PI, ODC, LEAD_CRA;


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

        deleteData();

        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);

        PI = Role.PI;
        ODC = Role.ODC;
        LEAD_CRA = Role.LEAD_CRA;
        defaultOrganization = organizationRepository.findById(105051);
        organization1 = organizationRepository.findById(100010);

        defaultStudy = new Study();
        defaultStudy.setShortTitle("A Phase 2 Study of Suberoylanilide Hydroxamic Acid (SAHA) in Acute Myeloid Leukemia (AML)");
        defaultStudy.setLongTitle("A Phase 2 Study of Suberoylanilide Hydroxamic Acid (SAHA) in Acute Myeloid Leukemia (AML)");
        defaultStudy.setAssignedIdentifier("-10001");

        StudyCoordinatingCenter studyCoordinatingCenter = new StudyCoordinatingCenter();
        studyCoordinatingCenter.setOrganization(defaultOrganization);
        StudyFundingSponsor studyFundingSponsor = new StudyFundingSponsor();

        studyFundingSponsor.setOrganization(defaultOrganization);
        defaultStudySite = new StudySite();
        defaultStudySite.setOrganization(defaultOrganization);

        studySite1 = new StudySite();
        studySite1.setOrganization(organization1);

        defaultStudy.addStudySite(defaultStudySite);
        defaultStudy.addStudySite(studySite1);

        defaultStudy.setStudyCoordinatingCenter(studyCoordinatingCenter);
        defaultStudy.setStudyFundingSponsor(studyFundingSponsor);
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

        defaultClinicalStaff = Fixture.createClinicalStaffWithOrganization("Angello", "Williams", "-1234", defaultOrganization);
        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);

        commitAndStartNewTransaction();
        assertNotNull("must find default clinical staff. ", defaultClinicalStaff);

        defaultOrganizationClinicalStaff = defaultClinicalStaff.getOrganizationClinicalStaffs().get(0);
        assertNotNull("must find default clinical staff. ", defaultOrganizationClinicalStaff);

        addResearchNurseAndTreatingPhysician();

    }

    private void addResearchNurseAndTreatingPhysician() {
        ClinicalStaff c = Fixture.createClinicalStaffWithOrganization("Brian", "Kirchner", "-1235", defaultOrganization);
        c = clinicalStaffRepository.save(c);
        defaultStudy.getStudySites().get(0).addOrUpdateStudyOrganizationClinicalStaff(Fixture.createStudyOrganizationClinicalStaff(c, Role.RESEARCH_NURSE, RoleStatus.ACTIVE, new Date(), defaultStudySite));
        commitAndStartNewTransaction();

        c = Fixture.createClinicalStaffWithOrganization("Joshua", "Hennagir", "-1236", defaultOrganization);
        c = clinicalStaffRepository.save(c);
        defaultStudy.getStudySites().get(0).addOrUpdateStudyOrganizationClinicalStaff(Fixture.createStudyOrganizationClinicalStaff(c, Role.TREATING_PHYSICIAN, RoleStatus.ACTIVE, new Date(), defaultStudySite));
        commitAndStartNewTransaction();

        c = Fixture.createClinicalStaffWithOrganization("Paul", "Kewitch", "-1237", defaultOrganization);
        clinicalStaffRepository.save(c);
        commitAndStartNewTransaction();

        c = Fixture.createClinicalStaffWithOrganization("Laura", "Jones", "-1238", defaultOrganization);
        c = clinicalStaffRepository.save(c);
        defaultStudy.getStudySites().get(0).addOrUpdateStudyOrganizationClinicalStaff(Fixture.createStudyOrganizationClinicalStaff(c, Role.RESEARCH_NURSE, RoleStatus.ACTIVE, new Date(), defaultStudySite));
        commitAndStartNewTransaction();

        c = Fixture.createClinicalStaffWithOrganization("Kerry", "Bueckers", "-1239", organization1);
        c = clinicalStaffRepository.save(c);
        defaultStudy.getStudySites().get(1).addOrUpdateStudyOrganizationClinicalStaff(Fixture.createStudyOrganizationClinicalStaff(c, Role.TREATING_PHYSICIAN, RoleStatus.ACTIVE, new Date(), studySite1));
        commitAndStartNewTransaction();

        c = Fixture.createClinicalStaffWithOrganization("Diane", "Opland", "-1240", organization1);
        clinicalStaffRepository.save(c);

        studyRepository.save(defaultStudy);
        commitAndStartNewTransaction();

    }

    private void deleteData() {
        jdbcTemplate.execute("delete from CRF_PAGE_ITEM_DISPLAY_RULES");
        jdbcTemplate.execute("delete from CRF_PAGE_ITEMS");
        jdbcTemplate.execute("delete from CRF_PAGES");
        jdbcTemplate.execute("delete from crfs");
        jdbcTemplate.execute("delete from study_organization_clinical_staffs");
        jdbcTemplate.execute("delete from study_participant_assignments");
        jdbcTemplate.execute("delete from study_organizations");
        jdbcTemplate.execute("delete from studies");
        jdbcTemplate.execute("delete from ORGANIZATION_CLINICAL_STAFFS");
        jdbcTemplate.execute("delete from CLINICAL_STAFFS");
        commitAndStartNewTransaction();
    }


    @Override
    protected void onTearDownInTransaction() throws Exception {

        DataAuditInfo.setLocal(null);
        super.onTearDownInTransaction();


    }


    protected StudyOrganizationClinicalStaff addStudyOrganizationClinicalStaff(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        defaultStudySite.addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);

        defaultStudy = studyRepository.save(defaultStudy);
        commitAndStartNewTransaction();

        defaultStudySite = defaultStudy.getStudySites().get(0);
        List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs = defaultStudySite.getStudyOrganizationClinicalStaffs();


        assertFalse("must save study clinical staff", studyOrganizationClinicalStaffs.isEmpty());
        studyOrganizationClinicalStaff = studyOrganizationClinicalStaffs.get(0);
        assertNotNull("must save study clinical staff", studyOrganizationClinicalStaff.getId());
        assertEquals("study site must be same", studyOrganizationClinicalStaff.getStudyOrganization(), defaultStudySite);


        assertEquals("site clinical staff  must be same", defaultOrganizationClinicalStaff, studyOrganizationClinicalStaff.getOrganizationClinicalStaff());
        return studyOrganizationClinicalStaff;
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
