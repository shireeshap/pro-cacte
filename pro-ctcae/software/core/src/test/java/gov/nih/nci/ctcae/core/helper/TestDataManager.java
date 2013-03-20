package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.csv.loader.ProCtcTermsImporterV4;
import gov.nih.nci.ctcae.core.dao.LowLevelTermDao;
import gov.nih.nci.ctcae.core.dao.MeddraVersionDao;
import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.IvrsCallHistoryRepository;
import gov.nih.nci.ctcae.core.repository.IvrsScheduleRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcValidValueRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCRFScheduleSymptomRecordRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.security.PrivilegeAuthorizationCheck;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.CombinationValidatorTest;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

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
    public static UserNameAndPasswordValidator userNameAndPasswordValidator;
    public static IvrsScheduleRepository ivrsScheduleRepository;
    public static IvrsCallHistoryRepository ivrsCallHistoryRepository;
    public static StudyParticipantCRFScheduleSymptomRecordRepository studyParticipantCRFScheduleSymptomRecordRepository;
    public static LowLevelTermDao lowLevelTermDao; 
    public static MeddraVersionDao meddraVersionDao;
    public static HibernateTemplate hibernateTemplate;
    
    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml"
            , "classpath*:gov/nih/nci/ctcae/core/testapplicationContext-util.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-setup.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core-security.xml"

    };
    protected final String SYSTEM_ADMIN = "system_admin";
    public static final String DEFAULT_PASSWORD = "Password@2";


    @Override
    protected void injectDependencies() throws Exception {
        super.injectDependencies();
        StudyTestHelper.initialize();
        OrganizationTestHelper.initialize();
        ClinicalStaffTestHelper.initialize();
        CrfTestHelper.inititalize();
        ParticipantTestHelper.initialize();
    }

    @Override
    protected String[] getConfigLocations() {
        return context;
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
    	System.out.println("Starting onSetUpInTransaction TestDataManager");
        super.onSetUpInTransaction();
        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);
        login(SYSTEM_ADMIN);
        saveCsv(false);
        if (!isTestDataPresent()) {
            deleteAndCreateTestData();
        } else {
        	System.out.println("test data present:  onSetUpInTransaction TestDataManager");
        }
        commitAndStartNewTransaction();
        System.out.println("Ending onSetUpInTransaction TestDataManager");
    }

    protected void deleteAndCreateTestData() {
        System.out.println("Refreshing data...");
        Long start = System.currentTimeMillis();
        deleteTestData();
        try {
            createTestData();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        Long end = System.currentTimeMillis();
        System.out.println("Data Refreshed (" + (end - start) / 1000 + " seconds)");

    }

    protected void createTestData() throws Exception {
        System.out.println("  Creating data...");
        long start = System.currentTimeMillis();
        CrfTestHelper.setTestDataManager(this);
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
        long end = System.currentTimeMillis();
        System.out.println("  Data created (" + (end - start) / 1000 + " seconds)");
    }

    protected void deleteAdminUser() {
        UserQuery uq = new UserQuery();
        uq.filterByUserName(SYSTEM_ADMIN);
        userRepository.delete(userRepository.findSingle(uq));
        commitAndStartNewTransaction();
    }

    protected void deleteTestData() {

        System.out.println("  Deleting data...");
        long start = System.currentTimeMillis();
        login(SYSTEM_ADMIN);

        deleteUsingJdbcTemplate();

        commitAndStartNewTransaction();
        long end = System.currentTimeMillis();
        System.out.println("  Data deleted (" + (end - start) / 1000 + " seconds)");
    }

    private void createClinicalStaff() {
        ClinicalStaffTestHelper.createDefaultClinicalStaff();
        commitAndStartNewTransaction();
    }

    private void createStudy() {
        StudyTestHelper.createDefaultStudy();
        StudyTestHelper.createSecondaryStudy();
        commitAndStartNewTransaction();
    }

    private void createParticipants() throws Exception {
        ParticipantTestHelper.createDefaultParticipants();
        commitAndStartNewTransaction();
    }

    private void createCrf() throws Exception {
        CrfTestHelper.createTestForm();
        commitAndStartNewTransaction();
    }

    protected User insertAdminUser() {
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
        userRepository.setCheckAccountLockout(false);
        if (userName.equals(SYSTEM_ADMIN)) {
            insertAdminUser();
        }
        User loadedUser = userRepository.loadUserByUsername(userName);
        if (userName.equals(SYSTEM_ADMIN)) {
            ArrayList<GrantedAuthority> list = new ArrayList(Arrays.asList(loadedUser.getAuthorities()));
            list.add(new GrantedAuthorityImpl("gov.nih.nci.ctcae.core.domain.Study.GROUP"));
            list.add(new GrantedAuthorityImpl("gov.nih.nci.ctcae.core.domain.Participant.GROUP"));
            list.add(new GrantedAuthorityImpl("gov.nih.nci.ctcae.core.domain.CRF.GROUP"));
            list.add(new GrantedAuthorityImpl("gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff.GROUP"));
            list.add(new GrantedAuthorityImpl("gov.nih.nci.ctcae.core.domain.ClinicalStaff.GROUP"));
            list.add(new GrantedAuthorityImpl("gov.nih.nci.ctcae.core.domain.StudyOrganization.GROUP"));
            loadedUser.setGrantedAuthorities(list.toArray(new GrantedAuthority[]{}));
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loadedUser, DEFAULT_PASSWORD, loadedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

//    protected void login(User user) {
//        login(user.getUsername());
//    }

    public void commitAndStartNewTransaction() {
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
        System.out.println("  Loading ProctcTerms...");
        long start = System.currentTimeMillis();
        ProCtcTermsImporterV4 csvImporter = new ProCtcTermsImporterV4();
        csvImporter.setCtcTermRepository(ctcTermRepository);
        ProCtc proctc = csvImporter.loadProCtcTerms(true);
        proCtcRepository.save(proctc);
        commitAndStartNewTransaction();
        long end = System.currentTimeMillis();
        System.out.println("  ProctcTerms loaded (" + (end - start) / 1000 + " seconds)");
        if (force) {
            createTestData();
        }
    }

    protected void deleteProCtcTermsInTestData() {
		deleteTestData();
		deleteProCtcTerms();
	}

	
	protected void deleteLowLevelTermInTestData(){
		deleteLowLevelTerms();
	}
	
	private void deleteLowLevelTerms(){
		System.out.println("  Deleting LowLevelTerms...");
		long start = System.currentTimeMillis();
		
		jdbcTemplate.execute("delete from meddra_llt_vocab");
		jdbcTemplate.execute("delete from meddra_llt");
		commitAndStartNewTransaction();
		long end = System.currentTimeMillis();
		System.out.println(" LowLevelTerms deleted (" + (end - start) / 1000 
				+ " seconds");
	}
	
	private void deleteProCtcTerms() {
		System.out.println("  Deleting ProCtcTerms...");
		long start = System.currentTimeMillis();

		jdbcTemplate.execute("delete from question_display_rules");
		jdbcTemplate.execute("delete from pro_ctc_valid_values_vocab");
		jdbcTemplate.execute("delete from pro_ctc_valid_values");
		jdbcTemplate.execute("delete from pro_ctc_questions_vocab");
		jdbcTemplate.execute("delete from pro_ctc_questions");
		jdbcTemplate.execute("delete from pro_ctc_terms_vocab");
		jdbcTemplate.execute("delete from pro_ctc_terms");
		jdbcTemplate.execute("delete from pro_ctc");
		commitAndStartNewTransaction();
		long end = System.currentTimeMillis();
		System.out.println("  ProCtcTerms deleted (" + (end - start) / 1000
				+ " seconds)");
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
    
    protected final ProCtcTerm getDefaultProCtcTerm(){
    	ProCtcTermQuery pQuery = new ProCtcTermQuery();
    	pQuery.filterByTerm("Aching muscles");
    	return proCtcTermRepository.findSingle(pQuery);
    }
    
    protected final CRFPage getDefaultCrfPage(){
    	CRFPage crfPage = new CRFPage();
    	crfPage.setProCtcTerm(getDefaultProCtcTerm());
    	return crfPage;
    }

    protected final boolean isDataPresentInTable(String tableName) {
        return countRowsInTable(tableName) != 0 ;
    }

    protected boolean isTestDataPresent() {
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
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        TestDataManager.hibernateTemplate = hibernateTemplate;
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
    public void setLowLevelTermDao(LowLevelTermDao lowLevelTermDao) {
        TestDataManager.lowLevelTermDao = lowLevelTermDao;
    }
    
    @Required
    public void setMeddraVersionDao(MeddraVersionDao meddraVersionDao) {
        TestDataManager.meddraVersionDao = meddraVersionDao;
    }
    
    @Required
    public static void setProCtcValidValueRepository(ProCtcValidValueRepository proCtcValidValueRepository) {
        TestDataManager.proCtcValidValueRepository = proCtcValidValueRepository;
    }

    @Required
    public void setStudyParticipantCrfRepository(StudyParticipantCrfRepository studyParticipantCrfRepository) {
        TestDataManager.studyParticipantCrfRepository = studyParticipantCrfRepository;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        TestDataManager.genericRepository = genericRepository;
    }

    @Required
    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        TestDataManager.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }


    @Required
    public void setUserNameAndPasswordValidator(UserNameAndPasswordValidator userNameAndPasswordValidator) {
        TestDataManager.userNameAndPasswordValidator = userNameAndPasswordValidator;
    }

    @Required
    public void setIvrsScheduleRepository(IvrsScheduleRepository ivrsScheduleRepository) {
        TestDataManager.ivrsScheduleRepository = ivrsScheduleRepository;
    }
    
    @Required
    public void setIvrsCallHistoryRepository(IvrsCallHistoryRepository ivrsCallHistoryRepository) {
        TestDataManager.ivrsCallHistoryRepository = ivrsCallHistoryRepository;
    }
    
    @Required
    public void setStudyParticipantCRFScheduleSymptomRecordRepository(StudyParticipantCRFScheduleSymptomRecordRepository 
    		studyParticipantCRFScheduleSymptomRecordRepository) {
        TestDataManager.studyParticipantCRFScheduleSymptomRecordRepository = studyParticipantCRFScheduleSymptomRecordRepository;
    }
    
//    private void deleteUsingHibernate(){
//                 StudyQuery query = new StudyQuery();
//        List<Study> studies = (List) studyRepository.find(query);
//        for (Study study : studies) {
//            studyRepository.delete(study);
//        }
//
//        ParticipantQuery pQuery = new ParticipantQuery();
//        List<Participant> participants = (List) participantRepository.find(pQuery);
//        for (Participant p : participants) {
//            participantRepository.delete(p);
//        }
//
//        ClinicalStaffQuery csQuery = new ClinicalStaffQuery();
//        List<ClinicalStaff> clinicalStaffs = (List) clinicalStaffRepository.find(csQuery);
//        for (ClinicalStaff cs : clinicalStaffs) {
//            clinicalStaffRepository.delete(cs);
//        }
//
//        UserQuery uQuery = new UserQuery();
//        List<User> users = (List) userRepository.find(uQuery);
//        for (User u : users) {
//            userRepository.delete(u);
//        }
//    }

    private void deleteUsingJdbcTemplate() {
        jdbcTemplate.execute("delete from user_notifications");
        jdbcTemplate.execute("delete from notifications");
        jdbcTemplate.execute("delete from CRF_PAGE_ITEM_DISPLAY_RULES");
        jdbcTemplate.execute("delete from study_participant_crf_items");
        jdbcTemplate.execute("delete from CRF_PAGE_ITEMS");
        jdbcTemplate.execute("delete from CRF_PAGES");
        jdbcTemplate.execute("delete from sp_crf_sch_added_questions");
        jdbcTemplate.execute("delete from sp_crf_added_questions");
        jdbcTemplate.execute("delete from sp_crf_schedule_notif");
        jdbcTemplate.execute("delete from sp_crf_schedules");
        jdbcTemplate.execute("delete from study_participant_crfs");
        jdbcTemplate.execute("delete from crf_calendars");
        jdbcTemplate.execute("delete from crf_cycles");
        jdbcTemplate.execute("delete from crf_cycle_definitions");
        jdbcTemplate.execute("delete from form_arm_schedules");
        jdbcTemplate.execute("delete from site_crf_notification_rules");
        jdbcTemplate.execute("delete from crf_notification_rules");
        jdbcTemplate.execute("delete from crfs");
        jdbcTemplate.execute("delete from study_par_clinical_staffs");
        jdbcTemplate.execute("delete from study_org_clinical_staffs");
        jdbcTemplate.execute("delete from sp_reporting_mode_hist");
        jdbcTemplate.execute("delete from study_participant_assignments");
        jdbcTemplate.execute("delete from study_organizations");
        jdbcTemplate.execute("delete from arms");
        jdbcTemplate.execute("delete from studies");
        jdbcTemplate.execute("delete from ORGANIZATION_CLINICAL_STAFFS");
        jdbcTemplate.execute("delete from CLINICAL_STAFFS");
        jdbcTemplate.execute("delete from user_roles");
        jdbcTemplate.execute("delete from study_participant_assignments");
        jdbcTemplate.execute("delete from participants");
        jdbcTemplate.execute("delete from USERS");
        jdbcTemplate.execute("delete from audit_event_values");
        jdbcTemplate.execute("delete from audit_events");
    }

}