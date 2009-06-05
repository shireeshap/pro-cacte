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
 * Date: Jun 5, 2009
 */
public class GenerateTestDataTest extends AbstractTransactionalDataSourceSpringContextTests {
    protected StudyRepository studyRepository;
    protected UserRepository userRepository;
    protected OrganizationRepository organizationRepository;
    protected ClinicalStaffRepository clinicalStaffRepository;
    protected OrganizationClinicalStaffRepository organizationClinicalStaffRepository;
    protected ProCtcTermRepository proCtcTermRepository;
    protected CtcTermRepository ctcTermRepository;
    protected ProCtcRepository proCtcRepository;

    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util-test.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-setup.xml"
//            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core-security.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-test.xml"
    };
    private final String SYSTEM_ADMIN = "system_admin";
    public static final String DEFAULT_PASSWORD = "password";


    @Override
    protected String[] getConfigLocations() {
        return context;
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);
        User admin = insertAdminUser();
        login(admin);
    }

    public void testCreateClinicalStaff() {
        ClinicalStaffTestHelper csth = new ClinicalStaffTestHelper(organizationClinicalStaffRepository, clinicalStaffRepository, organizationRepository);
        csth.createDefaultClinicalStaff();
        commitAndStartNewTransaction();
    }

    public void testCreateStudy() {
        StudyTestHelper sth = new StudyTestHelper(studyRepository, organizationClinicalStaffRepository, clinicalStaffRepository, organizationRepository);
        sth.createStandardStudy();
        commitAndStartNewTransaction();
    }

    public void testCreateCrf() {
        StudyTestHelper sth = new StudyTestHelper(studyRepository, organizationClinicalStaffRepository, clinicalStaffRepository, organizationRepository);
        sth.createStandardStudy();
        commitAndStartNewTransaction();
    }

    private User insertAdminUser() {
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserName(SYSTEM_ADMIN);
        List<User> users = new ArrayList<User>(userRepository.find(userQuery));
        User admin;
        if (users.isEmpty()) {
            admin = new User(SYSTEM_ADMIN, DEFAULT_PASSWORD, true, true, true, true);
            UserRole userRole = new UserRole();
            userRole.setRole(Role.ADMIN);
            admin.addUserRole(userRole);
            admin = userRepository.save(admin);
            commitAndStartNewTransaction();
        } else {
            admin = users.get(0);
        }
        return admin;
    }

    private void login(User user) {
        User loadedUser = userRepository.loadUserByUsername(user.getUsername());
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
}