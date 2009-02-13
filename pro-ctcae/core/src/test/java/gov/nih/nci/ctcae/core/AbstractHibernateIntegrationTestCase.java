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

        defaultStudy = studyRepository.findById(-1001);
        assertNotNull("must find default study. Try running ant migrate-sample-data to migrate sample data", defaultStudy);
        defaultStudySite = defaultStudy.getStudySites().get(0);
        assertNotNull("must find default study site. Try running ant migrate-sample-data to migrate sample data", defaultStudySite);

        defaultStudyParticipantAssignment = defaultStudySite.getStudyParticipantAssignments().get(0);
        assertNotNull("must find default study participant assignment. Try running ant migrate-sample-data to migrate sample data", defaultStudyParticipantAssignment);

        defaultClinicalStaff = clinicalStaffRepository.findById(-100);
        assertNotNull("must find default clinical staff. Try running ant migrate-sample-data to migrate sample data", defaultClinicalStaff);

        defaultClinicalStaffAssignment = defaultClinicalStaff.getClinicalStaffAssignments().get(0);
        assertNotNull("must find default site clinical staff. Try running ant migrate-sample-data to migrate sample data", defaultClinicalStaffAssignment);

    }


    protected void updateDefaultObjects() {

        defaultStudy = studyRepository.findById(defaultStudy.getId());
        defaultStudySite = defaultStudy.getStudySites().get(0);
        defaultStudyParticipantAssignment = defaultStudySite.getStudyParticipantAssignments().get(0);

        defaultStudyParticipantAssignment = defaultStudySite.getStudyParticipantAssignments().get(0);

        defaultClinicalStaff = clinicalStaffRepository.findById(-100);

        defaultClinicalStaffAssignment = defaultClinicalStaff.getClinicalStaffAssignments().get(0);

    }

    @Override
    protected void onTearDownInTransaction() throws Exception {
        DataAuditInfo.setLocal(null);
        super.onTearDownInTransaction();


    }


    protected ClinicalStaffAssignmentRole saveAndRetrieveClinicalStaffAssignmentRole() {

        defaultClinicalStaffAssignment.getClinicalStaffAssignmentRoles().clear();

        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);
        commitAndStartNewTransaction();

        defaultClinicalStaff = clinicalStaffRepository.findById(defaultClinicalStaff.getId());
        assertFalse("must have clinical staff assignment", defaultClinicalStaff.getClinicalStaffAssignments().isEmpty());

        defaultClinicalStaffAssignment = defaultClinicalStaff.getClinicalStaffAssignments().get(0);
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
        updateDefaultObjects();
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
