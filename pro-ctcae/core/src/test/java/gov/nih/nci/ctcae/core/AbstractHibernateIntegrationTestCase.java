package gov.nih.nci.ctcae.core;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.*;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.dao.DaoAuthenticationProvider;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Vinay Kumar
 * @crated Mar 9, 2009
 */
public class AbstractHibernateIntegrationTestCase extends AbstractTransactionalDataSourceSpringContextTests {
    protected ProCtcTermRepository proCtcTermRepository;
    protected ClinicalStaffRepository clinicalStaffRepository;
    protected OrganizationClinicalStaffRepository organizationClinicalStaffRepository;

    protected OrganizationRepository organizationRepository;
    protected ClinicalStaff defaultClinicalStaff;
    protected StudyParticipantAssignment defaultStudyParticipantAssignment;
    protected Organization defaultOrganization;
    protected Organization organization1, wake;
    protected Participant defaultParticipant;
    protected ParticipantRepository participantRepository;
    protected StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    protected Study defaultStudy;
    protected StudySite defaultStudySite;
    protected StudySite studySite1;
    protected StudyRepository studyRepository;
    protected GenericRepository genericRepository;
    protected UserRepository userRepository;
    protected StudyOrganizationRepository studyOrganizationRepository;
    public DaoAuthenticationProvider daoAuthenticationProvider;
    protected CRFRepository crfRepository;
    protected User defaultUser;
    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml",

            "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml",
//                "classpath*:gov/nih/nci/ctcae/core/applicationContext-mail.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml",
            "classpath*:gov/nih/nci/ctcae/core/resourceContext-job.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-core-security.xml",
            "classpath*:" + "/*-context-test.xml"};
    protected OrganizationClinicalStaff defaultOrganizationClinicalStaff;
    protected StudyOrganizationClinicalStaff studyOrganizationClinicalStaff;
    protected FundingSponsor fundingSponsor;
    protected LeadStudySite leadStudySite;

    @Override
    protected String[] getConfigLocations() {
        return context;
    }


    protected void login(User user) {
        User loadedUser = userRepository.loadUserByUsername(user.getUsername());
        assertNotNull("must find user", loadedUser);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loadedUser, Fixture.DEFAULT_PASSWORD, loadedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);


    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        deleteData();

        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);


        defaultClinicalStaff = Fixture.createClinicalStaff("Angello", "Williams", "-1234");
        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);
        commitAndStartNewTransaction();
        assertNotNull("must find default clinical staff. ", defaultClinicalStaff);

        defaultUser = defaultClinicalStaff.getUser();
        assertNotNull("must find user. ", defaultUser);

        //this is just the hack  for creating CCA (or first system user)
        User loadedUser = userRepository.loadUserByUsername(defaultUser.getUsername());
        assertNotNull("must find user", loadedUser);
        GrantedAuthority[] authorities = new GrantedAuthority[]{
                new GrantedAuthorityImpl("PRIVILEGE_CREATE_STUDY"), new GrantedAuthorityImpl("PRIVILEGE_SEARCH_STUDY"),
                new GrantedAuthorityImpl("PRIVILEGE_CREATE_PARTICIPANT"), new GrantedAuthorityImpl("PRIVILEGE_SEARCH_PARTICIPANT"),
                new GrantedAuthorityImpl("PRIVILEGE_CREATE_FORM"), new GrantedAuthorityImpl("PRIVILEGE_MANAGE_FORM"), new GrantedAuthorityImpl("PRIVILEGE_RELEASE_FORM"),
                new GrantedAuthorityImpl("PRIVILEGE_CREATE_CLINICAL_STAFF"), new GrantedAuthorityImpl("PRIVILEGE_SEARCH_CLINICAL_STAFF"),
                new GrantedAuthorityImpl("gov.nih.nci.ctcae.core.domain.Study.GROUP"),
                new GrantedAuthorityImpl("gov.nih.nci.ctcae.core.domain.Organization.GROUP"),
                new GrantedAuthorityImpl("gov.nih.nci.ctcae.core.domain.CRF.GROUP"),
                new GrantedAuthorityImpl("gov.nih.nci.ctcae.core.domain.Participant.GROUP")
        };
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loadedUser, Fixture.DEFAULT_PASSWORD, authorities);
        SecurityContextHolder.getContext().setAuthentication(token);


        defaultOrganization = organizationRepository.findById(105555);
        organization1 = organizationRepository.findById(104880);
        wake = organizationRepository.findById(104878);
        assertNotNull("must find organization", wake);


        defaultOrganizationClinicalStaff = new OrganizationClinicalStaff();
        defaultOrganizationClinicalStaff.setOrganization(defaultOrganization);
        defaultClinicalStaff.addOrganizationClinicalStaff(defaultOrganizationClinicalStaff);
        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);
        commitAndStartNewTransaction();

        defaultOrganizationClinicalStaff = defaultClinicalStaff.getOrganizationClinicalStaffs().get(0);
        assertNotNull("must find default clinical staff. ", defaultOrganizationClinicalStaff);


        defaultStudy = createStudy("-10001");


    }

    protected Study createStudy(final String assignedIdentifier) {
        Study defaultStudy = new Study();
        defaultStudy.setShortTitle(assignedIdentifier + "A Phase 2 Study of Suberoylanilide Hydroxamic Acid (SAHA) in Acute Myeloid Leukemia (AML)");
        defaultStudy.setLongTitle("A Phase 2 Study of Suberoylanilide Hydroxamic Acid (SAHA) in Acute Myeloid Leukemia (AML)");
        defaultStudy.setAssignedIdentifier(assignedIdentifier);

        DataCoordinatingCenter dataCoordinatingCenter = new DataCoordinatingCenter();
        dataCoordinatingCenter.setOrganization(defaultOrganization);
        StudySponsor studySponsor = new StudySponsor();

        studySponsor.setOrganization(defaultOrganization);
        defaultStudySite = new StudySite();
        defaultStudySite.setOrganization(defaultOrganization);

        studySite1 = new StudySite();
        studySite1.setOrganization(organization1);

        defaultStudy.addStudySite(defaultStudySite);
        defaultStudy.addStudySite(studySite1);

        fundingSponsor = new FundingSponsor();
        fundingSponsor.setOrganization(defaultOrganization);

        defaultStudy.setFundingSponsor(fundingSponsor);
        leadStudySite = new LeadStudySite();
        leadStudySite.setOrganization(defaultOrganization);
        defaultStudy.setLeadStudySite(leadStudySite);
        defaultStudy.setDataCoordinatingCenter(dataCoordinatingCenter);
        defaultStudy.setStudySponsor(studySponsor);
        defaultStudy = studyRepository.save(defaultStudy);
        assertNotNull("must find default study. ", defaultStudy);
        defaultStudySite = defaultStudy.getStudySites().get(0);
        assertNotNull("must find default study site. ", defaultStudySite);

        commitAndStartNewTransaction();

        return defaultStudy;
    }


    private void deleteData() {
        jdbcTemplate.execute("delete from CRF_PAGE_ITEM_DISPLAY_RULES");
        jdbcTemplate.execute("delete from CRF_PAGE_ITEMS");
        jdbcTemplate.execute("delete from CRF_PAGES");
        jdbcTemplate.execute("delete from sp_crf_schedules");
        jdbcTemplate.execute("delete from study_participant_crfs");
        jdbcTemplate.execute("delete from crf_cycle_definitions");
        jdbcTemplate.execute("delete from crfs");
        jdbcTemplate.execute("delete from study_organization_clinical_staffs");
        jdbcTemplate.execute("delete from study_participant_assignments");
        jdbcTemplate.execute("delete from study_organizations");
        jdbcTemplate.execute("delete from studies");
        jdbcTemplate.execute("delete from ORGANIZATION_CLINICAL_STAFFS");
        jdbcTemplate.execute("delete from CLINICAL_STAFFS");
        jdbcTemplate.execute("delete from USERS");
        jdbcTemplate.execute("delete from study_participant_assignments");
        jdbcTemplate.execute("delete from participants");
        commitAndStartNewTransaction();
    }

    @Override
    protected void onTearDownInTransaction() throws Exception {

        DataAuditInfo.setLocal(null);
        super.onTearDownInTransaction();


    }

    protected User createUser(final String userName, final String password, final boolean enabled,
                              final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
                              final GrantedAuthority[] authorities) {


        User userDetails = new User(userName, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked);
        userDetails = userRepository.save(userDetails);

        commitAndStartNewTransaction();
        return userDetails;

    }

    protected void insertDefaultUsers() throws Exception {
        ClinicalStaff clinicalStaff = Fixture.createClinicalStaffWithOrganization("Bob", "Williams", "-12345", defaultOrganization);
        clinicalStaff = clinicalStaffRepository.save(clinicalStaff);

        ClinicalStaff odcClinicalStaff = Fixture.createClinicalStaffWithOrganization("John", "Williams", "-12345", defaultOrganization);
        odcClinicalStaff = clinicalStaffRepository.save(odcClinicalStaff);

        ClinicalStaff ccaClinicalStaff = Fixture.createClinicalStaffWithOrganization("Steve", "Williams", "-12345", defaultOrganization);
        ccaClinicalStaff = clinicalStaffRepository.save(ccaClinicalStaff);

        ClinicalStaff sitePIClinicalStaff = Fixture.createClinicalStaffWithOrganization("Donna", "Williams", "-12345", defaultOrganization);
        sitePIClinicalStaff = clinicalStaffRepository.save(sitePIClinicalStaff);

        ClinicalStaff siteCRAClinicalStaff = Fixture.createClinicalStaffWithOrganization("George", "Williams", "-12345", defaultOrganization);
        siteCRAClinicalStaff = clinicalStaffRepository.save(siteCRAClinicalStaff);

        commitAndStartNewTransaction();
        assertNotNull("must find default clinical staff. ", defaultClinicalStaff);
        assertNotNull("must find default clinical staff. ", clinicalStaff);
        assertNotNull("must find default clinical staff. ", ccaClinicalStaff);
        assertNotNull("must find default clinical staff. ", sitePIClinicalStaff);
        assertNotNull("must find default clinical staff. ", odcClinicalStaff);
        assertNotNull("must find default clinical staff. ", siteCRAClinicalStaff);

        studyOrganizationClinicalStaff = addLeadCRA(defaultOrganizationClinicalStaff, defaultStudy);

        addPI(clinicalStaff.getOrganizationClinicalStaffs().get(0), defaultStudy);


        StudyOrganizationClinicalStaff odc = addODC(defaultOrganizationClinicalStaff, defaultStudy);
        addStudyOrganizationClinicalStaff(odc);

        StudyOrganizationClinicalStaff cca = new StudyOrganizationClinicalStaff();
        cca.setRole(Role.CCA);
        cca.setOrganizationClinicalStaff(ccaClinicalStaff.getOrganizationClinicalStaffs().get(0));
        defaultStudySite.addOrUpdateStudyOrganizationClinicalStaff(cca);

        addStudyOrganizationClinicalStaff(cca);

        StudyOrganizationClinicalStaff sitePI = new StudyOrganizationClinicalStaff();
        sitePI.setRole(Role.SITE_PI);
        sitePI.setOrganizationClinicalStaff(sitePIClinicalStaff.getOrganizationClinicalStaffs().get(0));
        defaultStudySite.addOrUpdateStudyOrganizationClinicalStaff(sitePI);

        addStudyOrganizationClinicalStaff(sitePI);

        StudyOrganizationClinicalStaff siteCRA = addSiteCRA(siteCRAClinicalStaff.getOrganizationClinicalStaffs().get(0), defaultStudy);
        addStudyOrganizationClinicalStaff(siteCRA);


        login(defaultUser);

        defaultParticipant = Fixture.createParticipant("Bruce", "Tanner", "P002");
        defaultStudyParticipantAssignment = new StudyParticipantAssignment();
        defaultStudyParticipantAssignment.setStudyParticipantIdentifier("1234");
        defaultStudyParticipantAssignment.setStudySite(defaultStudySite);
        defaultParticipant.addStudyParticipantAssignment(defaultStudyParticipantAssignment);
        defaultParticipant = participantRepository.save(defaultParticipant);

        commitAndStartNewTransaction();


        defaultStudyParticipantAssignment = defaultParticipant.getStudyParticipantAssignments().get(0);
        assertNotNull("must find default study participant assignment. ", defaultStudyParticipantAssignment);


        addResearchNurseAndTreatingPhysician();
    }

    protected StudyOrganizationClinicalStaff addSiteCRA(OrganizationClinicalStaff organizationClinicalStaff, Study study) {
        StudyOrganizationClinicalStaff siteCRA = new StudyOrganizationClinicalStaff();
        siteCRA.setRole(Role.SITE_CRA);
        siteCRA.setOrganizationClinicalStaff(organizationClinicalStaff);
        study.getStudySites().get(0).addOrUpdateStudyOrganizationClinicalStaff(siteCRA);
        return siteCRA;
    }

    protected StudyOrganizationClinicalStaff addPI(OrganizationClinicalStaff organizationClinicalStaff, Study study) {
        StudyOrganizationClinicalStaff pi = new StudyOrganizationClinicalStaff();
        pi.setRole(Role.PI);
        pi.setOrganizationClinicalStaff(organizationClinicalStaff);
        study.getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(pi);
        addStudyOrganizationClinicalStaff(pi);
        return pi;
    }

    protected StudyOrganizationClinicalStaff addLeadCRA(OrganizationClinicalStaff organizationClinicalStaff, Study study) {
        StudyOrganizationClinicalStaff studyOrganizationClinicalStaff = new StudyOrganizationClinicalStaff();
        studyOrganizationClinicalStaff.setRole(Role.LEAD_CRA);
        studyOrganizationClinicalStaff.setOrganizationClinicalStaff(organizationClinicalStaff);

        study.getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
        addStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
        return studyOrganizationClinicalStaff;
    }

    protected StudyOrganizationClinicalStaff addTreatingPhysicanOrResearchNurse(OrganizationClinicalStaff organizationClinicalStaff, StudySite studySite, final Role role) {
        StudyOrganizationClinicalStaff studyOrganizationClinicalStaff = new StudyOrganizationClinicalStaff();
        studyOrganizationClinicalStaff.setRole(role);
        studyOrganizationClinicalStaff.setOrganizationClinicalStaff(organizationClinicalStaff);

        studySite.addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
        return studyOrganizationClinicalStaff;
    }

    protected StudyOrganizationClinicalStaff addODC(OrganizationClinicalStaff organizationClinicalStaff, Study study) {
        StudyOrganizationClinicalStaff studyOrganizationClinicalStaff = new StudyOrganizationClinicalStaff();
        studyOrganizationClinicalStaff.setRole(Role.ODC);
        studyOrganizationClinicalStaff.setOrganizationClinicalStaff(organizationClinicalStaff);

        study.getDataCoordinatingCenter().addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
        return studyOrganizationClinicalStaff;
    }

    private void addResearchNurseAndTreatingPhysician() {
        ClinicalStaff c = Fixture.createClinicalStaffWithOrganization("Brian", "Kirchner", "-1235", defaultOrganization);
        c = clinicalStaffRepository.save(c);
        defaultStudy.getStudySites().get(0).addOrUpdateStudyOrganizationClinicalStaff(Fixture.createStudyOrganizationClinicalStaff(c, Role.NURSE, RoleStatus.ACTIVE, new Date(), defaultStudySite));
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
        defaultStudy.getStudySites().get(0).addOrUpdateStudyOrganizationClinicalStaff(Fixture.createStudyOrganizationClinicalStaff(c, Role.NURSE, RoleStatus.ACTIVE, new Date(), defaultStudySite));
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

    protected StudyOrganizationClinicalStaff addStudyOrganizationClinicalStaff(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {


        defaultStudy = studyRepository.save(defaultStudy);
        commitAndStartNewTransaction();

        List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs = defaultStudy.getAllStudyOrganizationClinicalStaffs();


        defaultStudySite = defaultStudy.getStudySites().get(0);
        assertFalse("must save study clinical staff", studyOrganizationClinicalStaffs.isEmpty());

        studyOrganizationClinicalStaff = defaultStudy.getStudyOrganizationClinicalStaffByRole(studyOrganizationClinicalStaff.getRole());

        assertNotNull("must save study clinical staff", studyOrganizationClinicalStaff);
        assertNotNull("must save study clinical staff", studyOrganizationClinicalStaff.getId());


        return studyOrganizationClinicalStaff;
    }

    protected Participant createParticipant(String firstName, final StudySite studySite) {
        Participant participant = Fixture.createParticipant(firstName, "Doe", "12345");
        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(studySite);
        studyParticipantAssignment.setStudyParticipantIdentifier("-12345");
        participant.addStudyParticipantAssignment(studyParticipantAssignment);
        participant = participantRepository.save(participant);
        commitAndStartNewTransaction();
        return participant;
    }

    protected CRF createCRF(Study study) throws ParseException {

        Study savedStudy = studyRepository.findById(study.getId());
        assertEquals("must see his own study only", savedStudy, study);

        CRF crf = Fixture.createCrf();
        crf.setTitle("title" + UUID.randomUUID());
        crf.setStudy(savedStudy);
        crf.setEffectiveStartDate(new Date());

        CRFCycleDefinition crfCycleDefinition = new CRFCycleDefinition();
        crfCycleDefinition.setCycleLength(21);
        crfCycleDefinition.setCycleLengthUnit("Days");
        crfCycleDefinition.setOrder(1);
        crfCycleDefinition.setRepeatTimes("2");

        CRFCycle crfCycle1 = new CRFCycle();
        crfCycle1.setCycleDays(",1,5,8,11");
        crfCycle1.setOrder(1);

        crfCycleDefinition.addCrfCycle(crfCycle1);

        CRFCycle crfCycle2 = new CRFCycle();
        crfCycle2.setCycleDays(",4,7,11,18");
        crfCycle2.setOrder(2);

        crfCycleDefinition.addCrfCycle(crfCycle2);

        crf.addCrfCycleDefinition(crfCycleDefinition);

        crf = crfRepository.save(crf);
        crfRepository.updateStatusToReleased(crf);

        assertNotNull("must save crf on his own study", crf);
        commitAndStartNewTransaction();
        return crf;
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

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Required
    public void setDaoAuthenticationProvider(final DaoAuthenticationProvider daoAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @Required
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    @Required
    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }


    @Required
    public void setOrganizationClinicalStaffRepository(OrganizationClinicalStaffRepository organizationClinicalStaffRepository) {

        this.organizationClinicalStaffRepository = organizationClinicalStaffRepository;
    }

    @Required
    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
