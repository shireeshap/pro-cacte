package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;

/**
 * @author Vinay Kumar
 * @crated Mar 11, 2009
 */
public class AbstractInstanceLevelAuthorizationIntegrationTest extends AbstractHibernateIntegrationTestCase {

    protected Study study1, study2;

    protected User user, anotherUser;

    protected ClinicalStaff anotherClinicalStaff;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        study1 = createStudy("-10002");
        study2 = createStudy("-1003");

        study1.getStudySites().get(0).setOrganization(wake);
        study2.getStudySites().get(0).setOrganization(wake);

        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);


        anotherClinicalStaff = Fixture.createClinicalStaffWithOrganization("Paul", "Williams", "-123456", wake);
        anotherClinicalStaff = clinicalStaffRepository.save(anotherClinicalStaff);
        commitAndStartNewTransaction();


    }


}
