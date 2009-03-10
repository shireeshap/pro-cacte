package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.StudyQuery;

import java.util.Collection;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class LeadCRAInstanceLevelAuthorizationIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private User user, anotherLeadCRA;
    private Study study1, study2;
    protected ClinicalStaff anotherClinicalStaff;

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

        anotherLeadCRA = study1.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another lead cra also", anotherLeadCRA);


    }

    public void testStudyInstanceSecurity() throws Exception {
        login(user);

        Collection<Study> studies = studyRepository.find(new StudyQuery());
        assertFalse("must find studies", studies.isEmpty());
        assertEquals("must see one study only because this user is lead CRA on that study only", 1, studies.size());
        assertEquals("must see his own study only", defaultStudy, studies.iterator().next());


    }

    public void testStudyInstanceSecurityForMultipleStudies() throws Exception {

        //this user can see two studies
        login(anotherLeadCRA);
        Collection<Study> studies = studyRepository.find(new StudyQuery());
        assertFalse("must find studies", studies.isEmpty());
        assertEquals("must see one study only because this user is lead CRA on that study only", 2, studies.size());
        assertTrue("must see his own study only", studies.contains(study1));
        assertTrue("must see his own study only", studies.contains(study2));


    }


}