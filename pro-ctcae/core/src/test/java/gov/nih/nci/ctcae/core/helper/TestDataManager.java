package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.csv.loader.CsvImporter;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.repository.*;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import java.io.IOException;
import java.util.*;

/**
 * @author Harsh Agarwal
 *         Date: Jun 5, 2009
 */
public class TestDataManager extends AbstractTransactionalDataSourceSpringContextTests {
    protected static StudyRepository studyRepository;
    protected UserRepository userRepository;
    protected static OrganizationRepository organizationRepository;
    protected static ClinicalStaffRepository clinicalStaffRepository;
    protected static OrganizationClinicalStaffRepository organizationClinicalStaffRepository;
    protected ProCtcTermRepository proCtcTermRepository;
    protected CtcTermRepository ctcTermRepository;
    protected ProCtcRepository proCtcRepository;
    protected CRFRepository crfRepository;

    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-setup.xml"
//            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core-security.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-test.xml"
    };
    private final String SYSTEM_ADMIN = "system_admin";
    public static final String DEFAULT_PASSWORD = "password";
    private static Study standardStudy;

    @Override
    protected String[] getConfigLocations() {
        return context;
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);
    }

    protected void createTestData() {
        insertAdminUser();
        login(SYSTEM_ADMIN);
        createClinicalStaff();
        createStudy();
        createCrf();
    }

    protected void deleteTestData() {
        login(SYSTEM_ADMIN);
        jdbcTemplate.execute("delete from CRF_PAGE_ITEM_DISPLAY_RULES");
        jdbcTemplate.execute("delete from study_participant_crf_items");
        jdbcTemplate.execute("delete from CRF_PAGE_ITEMS");
        jdbcTemplate.execute("delete from CRF_PAGES");
        jdbcTemplate.execute("delete from sp_crf_schedules");
        jdbcTemplate.execute("delete from study_participant_crfs");
        jdbcTemplate.execute("delete from crf_cycles");
        jdbcTemplate.execute("delete from crf_cycle_definitions");
        jdbcTemplate.execute("delete from crfs");
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
        ClinicalStaffTestHelper csth = new ClinicalStaffTestHelper(organizationClinicalStaffRepository, clinicalStaffRepository, organizationRepository);
        csth.createDefaultClinicalStaff();
        commitAndStartNewTransaction();
    }

    private void createStudy() {
        StudyTestHelper sth = new StudyTestHelper(studyRepository, organizationClinicalStaffRepository, clinicalStaffRepository, organizationRepository);
        standardStudy = sth.createStandardStudy();
        commitAndStartNewTransaction();
    }

    private void createCrf() {
        CrfTestHelper cth = new CrfTestHelper(crfRepository, proCtcTermRepository);
        cth.createTestForm();
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

    private void login(String userName) {
        User loadedUser = userRepository.loadUserByUsername(userName);
        assertNotNull("must find user", loadedUser);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loadedUser, DEFAULT_PASSWORD, loadedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    private void commitAndStartNewTransaction() {
        setComplete();
        endTransaction();
        startNewTransaction();
    }

    protected void saveCsv() throws IOException {
        String codeBase = (String) getApplicationContext().getBean("codebaseDirectory");
        assertNotNull("please define codebase.directory property in datasource.properties file. " +
                "This should property should point to directory where you have checked-out the code-base  of ctcae. " +
                "For ex:codebase.directory=/Users/saurabhagrawal/projects/pro-ctcae", codeBase);
        Collection<ProCtcTerm> ctcCollection = proCtcTermRepository.find(new ProCtcTermQuery());
        if (ctcCollection != null && !ctcCollection.isEmpty()) {
            return;
        }
        CsvImporter csvImporter = new CsvImporter();
        csvImporter.setCtcTermRepository(ctcTermRepository);
        String fileLocation = codeBase + "/core/src/main/java/gov/nih/nci/ctcae/core/csv/loader/ctcae_display_rules.csv";
        ProCtc proctc = csvImporter.readCsv(fileLocation);
        proCtcRepository.save(proctc);
        commitAndStartNewTransaction();
    }

    @Override
    protected void onTearDownInTransaction() throws Exception {
        DataAuditInfo.setLocal(null);
        super.onTearDownInTransaction();
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

    @Required
    public void setOrganizationClinicalStaffRepository(OrganizationClinicalStaffRepository organizationClinicalStaffRepository) {
        this.organizationClinicalStaffRepository = organizationClinicalStaffRepository;
    }

    @Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    @Required
    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

    @Required
    public void setProCtcRepository(ProCtcRepository proCtcRepository) {
        this.proCtcRepository = proCtcRepository;
    }

    @Required
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    public static Study getStandardStudy() {
        if (standardStudy == null) {
            StudyTestHelper sth = new StudyTestHelper(studyRepository, organizationClinicalStaffRepository, clinicalStaffRepository, organizationRepository);
            standardStudy = sth.getStandardStudy();
        }
        return standardStudy;
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

    protected final boolean isTestDataPresent() {

        List l = jdbcTemplate.queryForList("select s from studies s");
        if (l != null && l.size() > 0) {
            return true;
        }
        return false;
    }
}