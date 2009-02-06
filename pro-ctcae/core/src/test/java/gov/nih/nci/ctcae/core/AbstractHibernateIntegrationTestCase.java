package gov.nih.nci.ctcae.core;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.*;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import java.util.Date;

public abstract class AbstractHibernateIntegrationTestCase extends AbstractTransactionalDataSourceSpringContextTests {

    protected FinderRepository finderRepository;
    protected ProCtcTermRepository proCtcTermRepository;
    protected ClinicalStaffRepository clinicalStaffRepository;
    protected OrganizationRepository organizationRepository;
    protected ClinicalStaff defaultClinicalStaff;
    protected SiteClinicalStaff defaultSiteClinicalStaff;
    protected Organization defaultOrganization;

    protected Study defaultStudy;
    protected StudySite defaultStudySite;
    protected StudyRepository studyRepository;


    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml",

                "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml",
//                "classpath*:gov/nih/nci/ctcae/core/applicationContext-mail.xml",
                "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml",
                "classpath*:gov/nih/nci/ctcae/core/resourceContext-job.xml",
//                "classpath*:gov/nih/nci/ctcae/core/applicationContext-core-security.xml",
                "classpath*:" + "/*-context-test.xml"};
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

        defaultStudy = studyRepository.findById(-1001);
        assertNotNull("must find default study. Try running ant migrate-sample-date to migrate sample data", defaultStudy);
        defaultStudySite = defaultStudy.getStudySites().get(0);
        assertNotNull("must find default study site. Try running ant migrate-sample-date to migrate sample data", defaultStudySite);

        defaultClinicalStaff = clinicalStaffRepository.findById(-100);
        assertNotNull("must find default clinical staff. Try running ant migrate-sample-date to migrate sample data", defaultClinicalStaff);

        defaultSiteClinicalStaff = defaultClinicalStaff.getSiteClinicalStaffs().get(0);
        assertNotNull("must find default site clinical staff. Try running ant migrate-sample-date to migrate sample data", defaultSiteClinicalStaff);

    }


    @Override
    protected void onTearDownInTransaction() throws Exception {
        DataAuditInfo.setLocal(null);
        super.onTearDownInTransaction();


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
