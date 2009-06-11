package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.csv.loader.CsvImporter;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.repository.*;
import gov.nih.nci.ctcae.core.security.PrivilegeAuthorizationCheck;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.apache.commons.collections.map.ListOrderedMap;

import java.util.*;
import java.text.ParseException;

/**
 * @author Harsh Agarwal
 *         Date: Jun 5, 2009
 */
public class TestDataManager extends AbstractTransactionalDataSourceSpringContextTests {
    public static StudyRepository studyRepository;
    public static UserRepository userRepository;
    public static OrganizationRepository organizationRepository;
    public static ClinicalStaffRepository clinicalStaffRepository;
    public static OrganizationClinicalStaffRepository organizationClinicalStaffRepository;
    public static ProCtcTermRepository proCtcTermRepository;
    public static CtcTermRepository ctcTermRepository;
    public static ProCtcRepository proCtcRepository;
    public static CRFRepository crfRepository;
    public static ParticipantRepository participantRepository;
    public static StudyOrganizationRepository studyOrganizationRepository;
    public static StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    public static StudyOrganizationClinicalStaffRepository studyOrganizationClinicalStaffRepository;
    public static PrivilegeAuthorizationCheck privilegeAuthorizationCheck;
    public static ProCtcValidValueRepository proCtcValidValueRepository;
    public static StudyParticipantCrfRepository studyParticipantCrfRepository;
    public static StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;
    public static GenericRepository genericRepository;


    protected Study defaultStudy;
    protected String codeBase;
    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util-test.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-setup.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core-security.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-test.xml"
            , "classpath*:gov/nih/nci/ctcae/web/applicationContext-rules-jcr.xml"
//            , "classpath*:gov/nih/nci/ctcae/web/applicationContext-web-security.xml"
    };
    protected final String SYSTEM_ADMIN = "system_admin";
    public static final String DEFAULT_PASSWORD = "password";

    @Override
    protected void injectDependencies() throws Exception {
        super.injectDependencies();
        StudyTestHelper.initialize();
        OrganizationTestHelper.initialize();
        ClinicalStaffTestHelper.initialize();
        CrfTestHelper.inititalize();
        ParticipantTestHelper.initialize();
        codeBase = (String) getApplicationContext().getBean("codebaseDirectory");
    }

    @Override
    protected String[] getConfigLocations() {
        return context;
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);
        saveCsv(false);
        if (!isTestDataPresent()) {
            deleteAndCreateTestData();
        }
        login(SYSTEM_ADMIN);
        defaultStudy = StudyTestHelper.getDefaultStudy();
    }

    protected void deleteAndCreateTestData() {

        Long start = System.currentTimeMillis();
        deleteTestData();
        createTestData();
        Long end = System.currentTimeMillis();
        System.out.println("Time to refresh data - " + (end - start) / 1000 + " seconds");

    }

    protected void createTestData() {
        insertAdminUser();
        login(SYSTEM_ADMIN);
        createClinicalStaff();
        createStudy();
        try {
            createCrf();
            createParticipants();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        commitAndStartNewTransaction();
    }


    protected void deleteTestData() {
        insertAdminUser();
        login(SYSTEM_ADMIN);
        jdbcTemplate.execute("delete from CRF_PAGE_ITEM_DISPLAY_RULES");
        jdbcTemplate.execute("delete from study_participant_crf_items");
        jdbcTemplate.execute("delete from CRF_PAGE_ITEMS");
        jdbcTemplate.execute("delete from CRF_PAGES");
        jdbcTemplate.execute("delete from sp_crf_sch_added_questions");
        jdbcTemplate.execute("delete from sp_crf_added_questions");
        jdbcTemplate.execute("delete from sp_crf_schedules");
        jdbcTemplate.execute("delete from study_participant_crfs");
        jdbcTemplate.execute("delete from crf_cycles");
        jdbcTemplate.execute("delete from crf_cycle_definitions");
        jdbcTemplate.execute("delete from crfs");
        jdbcTemplate.execute("delete from study_participant_clinical_staffs");
        jdbcTemplate.execute("delete from study_organization_clinical_staffs");
        jdbcTemplate.execute("delete from study_participant_assignments");
        jdbcTemplate.execute("delete from study_organizations");
        jdbcTemplate.execute("delete from studies");
        jdbcTemplate.execute("delete from ORGANIZATION_CLINICAL_STAFFS");
        jdbcTemplate.execute("delete from CLINICAL_STAFFS");
        jdbcTemplate.execute("delete from user_roles");
        jdbcTemplate.execute("delete from study_participant_assignments");
        jdbcTemplate.execute("delete from participants");
        jdbcTemplate.execute("delete from USERS");
        jdbcTemplate.execute("delete from audit_event_values");
        jdbcTemplate.execute("delete from audit_events");
        commitAndStartNewTransaction();
    }

    private void createClinicalStaff() {
        ClinicalStaffTestHelper.createDefaultClinicalStaff();
        commitAndStartNewTransaction();
    }

    private void createStudy() {
        StudyTestHelper.createDefaultStudy();
        commitAndStartNewTransaction();
    }

    private void createParticipants() throws ParseException {
        ParticipantTestHelper.createDefaultParticipants();
        commitAndStartNewTransaction();
    }

    private void createCrf() throws ParseException {
        CrfTestHelper.createTestForm();
        commitAndStartNewTransaction();
    }

    private User insertAdminUser() {
        User admin = getAdminUser();
        if (admin == null) {
            admin = new User(SYSTEM_ADMIN, DEFAULT_PASSWORD, true, true, true, true);
            UserRole userRole = new UserRole();
            userRole.setRole(Role.ADMIN);
            admin.addUserRole(userRole);
            admin = userRepository.save(admin);
            commitAndStartNewTransaction();
        }
        return admin;
    }

    protected void login(String userName) {
        User loadedUser = userRepository.loadUserByUsername(userName);
        assertNotNull("must find user", loadedUser);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loadedUser, DEFAULT_PASSWORD, loadedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

//    protected void login(User user) {
//        login(user.getUsername());
//    }

    protected void commitAndStartNewTransaction() {
        setComplete();
        endTransaction();
        startNewTransaction();
    }

    protected void saveCsv(boolean force) throws Exception {
        if (force) {
            deleteTestData();
            deleteProCtcTerms();
        }
        if (isProCtcTermsLoaded()) {
            return;
        }
        assertNotNull("please define codebase.directory property in datasource.properties file. " +
                "This should property should point to directory where you have checked-out the code-base  of ctcae. " +
                "For ex:codebase.directory=/Users/saurabhagrawal/projects/pro-ctcae", codeBase);
        CsvImporter csvImporter = new CsvImporter();
        csvImporter.setCtcTermRepository(ctcTermRepository);
        String fileLocation = codeBase + "/core/src/main/java/gov/nih/nci/ctcae/core/csv/loader/ctcae_display_rules.csv";
        System.out.println("Codebase - " + codeBase + "; Reading csv file from  - " + fileLocation);
        ProCtc proctc = csvImporter.readCsv(fileLocation);
        proCtcRepository.save(proctc);
        commitAndStartNewTransaction();
        createTestData();
    }

    private void deleteProCtcTerms() {
        jdbcTemplate.execute("delete from question_display_rules");
        jdbcTemplate.execute("delete from pro_ctc_valid_values");
        jdbcTemplate.execute("delete from pro_ctc_questions");
        jdbcTemplate.execute("delete from pro_ctc_terms");
        jdbcTemplate.execute("delete from pro_ctc");
        commitAndStartNewTransaction();
    }

    private User getAdminUser() {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserName(SYSTEM_ADMIN);
        List<User> users = new ArrayList<User>(userRepository.find(userQuery));
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    protected final boolean isDataPresentInTable(String tableName) {
        List l = jdbcTemplate.queryForList("select count(1) from " + tableName + " t");
        Long lg = (Long) (((ListOrderedMap) l.get(0)).getValue(0));
        if (lg.equals(0L)) {
            return false;
        }
        return true;
    }

    protected final boolean isTestDataPresent() {
        return isDataPresentInTable("studies");
    }

    protected final boolean isProCtcTermsLoaded() {
        return isDataPresentInTable("pro_ctc_terms");
    }

    protected final boolean isMeddraTermsLoaded() {
        return isDataPresentInTable("meddra_llt");
    }

    @Override
    protected void onTearDownInTransaction() throws Exception {
        DataAuditInfo.setLocal(null);
        super.onTearDownInTransaction();
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        TestDataManager.studyRepository = studyRepository;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        TestDataManager.userRepository = userRepository;
    }

    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        TestDataManager.organizationRepository = organizationRepository;
    }

    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        TestDataManager.clinicalStaffRepository = clinicalStaffRepository;
    }

    @Required
    public void setOrganizationClinicalStaffRepository(OrganizationClinicalStaffRepository organizationClinicalStaffRepository) {
        TestDataManager.organizationClinicalStaffRepository = organizationClinicalStaffRepository;
    }

    @Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        TestDataManager.proCtcTermRepository = proCtcTermRepository;
    }

    @Required
    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        TestDataManager.ctcTermRepository = ctcTermRepository;
    }

    @Required
    public void setProCtcRepository(ProCtcRepository proCtcRepository) {
        TestDataManager.proCtcRepository = proCtcRepository;
    }

    @Required
    public void setCrfRepository(CRFRepository crfRepository) {
        TestDataManager.crfRepository = crfRepository;
    }

    @Required
    public void setParticipantRepository(ParticipantRepository participantRepository) {
        TestDataManager.participantRepository = participantRepository;
    }

    @Required
    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        TestDataManager.studyOrganizationRepository = studyOrganizationRepository;
    }

    @Required
    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        TestDataManager.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }

    @Required
    public void setStudyOrganizationClinicalStaffRepository(StudyOrganizationClinicalStaffRepository studyOrganizationClinicalStaffRepository) {
        TestDataManager.studyOrganizationClinicalStaffRepository = studyOrganizationClinicalStaffRepository;
    }

    @Required
    public void setPrivilegeAuthorizationCheck(PrivilegeAuthorizationCheck privilegeAuthorizationCheck) {
        TestDataManager.privilegeAuthorizationCheck = privilegeAuthorizationCheck;
    }

    @Required
    public static void setProCtcValidValueRepository(ProCtcValidValueRepository proCtcValidValueRepository) {
        TestDataManager.proCtcValidValueRepository = proCtcValidValueRepository;
    }

    @Required
    public void setStudyParticipantCrfRepository(StudyParticipantCrfRepository studyParticipantCrfRepository) {
        this.studyParticipantCrfRepository = studyParticipantCrfRepository;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        TestDataManager.genericRepository = genericRepository;
    }

    @Required
    public  void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        TestDataManager.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}