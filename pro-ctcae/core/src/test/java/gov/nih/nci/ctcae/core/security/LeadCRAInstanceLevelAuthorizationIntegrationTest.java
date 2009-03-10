package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import org.springframework.security.AccessDeniedException;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class LeadCRAInstanceLevelAuthorizationIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private User user, anotherUser;
    private Study study1, study2;
    protected ClinicalStaff anotherClinicalStaff;
    protected CRF defaultCRF;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        addLeadCRA(defaultOrganizationClinicalStaff, defaultStudy);
        user = defaultStudy.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        study1 = createStudy("-10002");
        study2 = createStudy("-1003");

        anotherClinicalStaff = Fixture.createClinicalStaffWithOrganization("Paul", "Williams", "-123456", defaultOrganization);
        anotherClinicalStaff = clinicalStaffRepository.save(anotherClinicalStaff);
        commitAndStartNewTransaction();

        addLeadCRA(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study1);
        addLeadCRA(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study2);

        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);
        commitAndStartNewTransaction();

        anotherUser = study1.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another lead cra also", anotherUser);


    }

    public void testStudyInstanceSecurity() throws Exception {
        login(user);

        Collection<Study> studies = studyRepository.find(new StudyQuery());
        assertFalse("must find studies", studies.isEmpty());
        assertEquals("must see one study only because this user is lead CRA on that study only", 1, studies.size());
        assertEquals("must see his own study only", defaultStudy, studies.iterator().next());


    }

    public void testCRFSecurityOnFind() throws Exception {
        login(anotherUser);
        createCRF(study1);
        createCRF(study1);

        login(user);
        defaultCRF = createCRF(defaultStudy);

        login(user);

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from crfs") >= 2);
        Collection<CRF> crfs = crfRepository.find(new CRFQuery());
        assertFalse("must find crfs", crfs.isEmpty());
        assertEquals("must see one crf only because this crf is created on user's study", 1, crfs.size());
        assertEquals("must see one crf only because this crf is created on user's study", defaultCRF, crfs.iterator().next());


    }

    public void testCRFSecurityOnFindMultiple() throws Exception {
        login(user);
        defaultCRF = createCRF(defaultStudy);

        login(anotherUser);
        CRF crf1 = createCRF(study1);
        CRF crf2 = createCRF(study1);


        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from crfs") >= 2);
        Collection<CRF> crfs = crfRepository.find(new CRFQuery());
        assertFalse("must find crfs", crfs.isEmpty());
        assertEquals("must see two crfs only because these crfs are created on user's study", 2, crfs.size());
        assertTrue("must see his own crf only", crfs.contains(crf1));
        assertTrue("must see his own crf only ", crfs.contains(crf2));


    }

    public void testCRFSecurityOnFindById() throws Exception {
        login(anotherUser);
        defaultCRF = createCRF(study1);

        CRF crf = crfRepository.findById(defaultCRF.getId());
        assertEquals("must see this crf  because this crf is created on user's study", defaultCRF, crf);

        login(user);
        try {
            crfRepository.findById(defaultCRF.getId());
            fail("must not see crfs for other studies");
        } catch (AccessDeniedException e) {
        }
    }

    public void testCRFSecurityOnFindSingle() throws Exception {
        login(anotherUser);
        defaultCRF = createCRF(study1);

        CRFQuery query = new CRFQuery();
        query.filterByTitleExactMatch(defaultCRF.getTitle());
        CRF crf = crfRepository.findSingle(query);
        assertEquals("must see this crf  because this crf is created on user's study", defaultCRF, crf);

        login(user);
        try {
            query = new CRFQuery();
            query.filterByTitleExactMatch(defaultCRF.getTitle());

            crfRepository.findSingle(query);
            fail("must not see crfs for other studies");
        } catch (AccessDeniedException e) {
        }
    }


    public void testCRFSecurityOnCreateAndEdit() throws Exception {

        login(user);
        CRF crf = createCRF(defaultStudy);


        crf.setStudy(study1);
        try {
            crf = crfRepository.save(crf);
            fail("must edit CRF for his own studies only");
        } catch (AccessDeniedException e) {

        }

        try {
            createCRF(study1);
            fail("must save CRF for his own studies only");
        } catch (AccessDeniedException e) {

        }


    }

    public void testCRFSecurityOnVersion() throws Exception {

        login(user);
        CRF crf = createCRF(defaultStudy);
        crf = crfRepository.versionCrf(crf);
        crf.setStudy(study1);
        try {
            crfRepository.versionCrf(crf);
            fail("must not version CRF on other studies");
        } catch (AccessDeniedException e) {

        }


    }

    public void testCRFSecurityOnCopy() throws Exception {

        login(user);
        CRF crf = createCRF(defaultStudy);
        crf = crfRepository.copy(crf);
        crf.setStudy(study1);
        try {
            crfRepository.copy(crf);
            fail("must not copy CRF on other studies");
        } catch (AccessDeniedException e) {

        }


    }

    public void testCRFSecurityOnRelease() throws Exception {

        login(user);
        CRF crf = createCRF(defaultStudy);
        crfRepository.updateStatusToReleased(crf);
        crf = crfRepository.findById(crf.getId());
        crf.setStudy(study1);
        try {
            crfRepository.updateStatusToReleased(crf);
            fail("must not update CRF on other studies");
        } catch (AccessDeniedException e) {

        }


    }

    private CRF createCRF(Study study) {

        Study savedStudy = studyRepository.findById(study.getId());
        assertEquals("must see his own study only", savedStudy, study);

        CRF crf = Fixture.createCrf();
        crf.setTitle("title" + UUID.randomUUID());
        crf.setStudy(savedStudy);
        crf = crfRepository.save(crf);

        assertNotNull("must save crf on his own study", crf);
        commitAndStartNewTransaction();
        return crf;
    }

    public void testStudyInstanceSecurityForCreateStudy() throws Exception {

        //this user can see two studies
        login(anotherUser);
        try {
            defaultStudy.setAssignedIdentifier("test");
            studyRepository.save(defaultStudy);
            fail("user must not edit other studies");
        } catch (AccessDeniedException e) {

        }

    }

    public void testStudyInstanceSecurityForMultipleStudies() throws Exception {

        //this user can see two studies
        login(anotherUser);
        Collection<Study> studies = studyRepository.find(new StudyQuery());
        assertFalse("must find studies", studies.isEmpty());
        assertEquals("must see one study only because this user is lead CRA on that study only", 2, studies.size());
        assertTrue("must see his own study only", studies.contains(study1));
        assertTrue("must see his own study only", studies.contains(study2));


    }

    public void testStudyInstanceSecurityByUsingFindById() throws Exception {

        //this user can see two studies
        login(anotherUser);
        Study study = studyRepository.findById(study1.getId());
        assertEquals("must see his own study only", study, study1);

        try {
            studyRepository.findById(defaultStudy.getId());
            fail("must see his own study only");
        } catch (AccessDeniedException e) {

        }

    }

    public void testStudyInstanceSecurityByUsingFindSingle() throws Exception {

        //this user can see two studies
        login(anotherUser);
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterByAssignedIdentifierExactMatch(study1.getAssignedIdentifier());
        Study study = studyRepository.findSingle(studyQuery);
        assertEquals("must see his own study only", study, study1);

        studyQuery = new StudyQuery();
        studyQuery.filterByAssignedIdentifierExactMatch(defaultStudy.getAssignedIdentifier());

        try {
            study = studyRepository.findSingle(studyQuery);
            fail("must see his own study only");
        } catch (AccessDeniedException e) {

        }


    }


}