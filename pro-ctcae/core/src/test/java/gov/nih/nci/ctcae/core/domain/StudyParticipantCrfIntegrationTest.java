package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudyParticipantCrfIntegrationTest extends
        AbstractJpaIntegrationTestCase {

    private StudyRepository studyRepository;
    private Study inValidStudy, studyWithStudyOrganizations;

    private StudySite nciStudySite;
    private Organization nci, duke;
    private OrganizationRepository organizationRepository;

    private StudyFundingSponsor studyFundingSponsor;
    private StudyCoordinatingCenter studyCoordinatingCenter;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        nci = Fixture.createOrganization("National Cancer Institute", "NCI");


        nci = organizationRepository.save(nci);

        duke = Fixture.createOrganization("DUKE", "DUKE");

        duke = organizationRepository.save(duke);


        nciStudySite = new StudySite();
        nciStudySite.setOrganization(nci);

        studyFundingSponsor = new StudyFundingSponsor();
        studyFundingSponsor.setOrganization(nci);

        studyCoordinatingCenter = new StudyCoordinatingCenter();
        studyCoordinatingCenter.setOrganization(nci);

        studyWithStudyOrganizations = Fixture.createStudy("study short title", "study long title", "assigned identifier");
        studyWithStudyOrganizations.setStudyFundingSponsor(studyFundingSponsor);
        studyWithStudyOrganizations.setStudyCoordinatingCenter(studyCoordinatingCenter);
        studyWithStudyOrganizations.addStudySite(nciStudySite);

        studyWithStudyOrganizations = studyRepository.save(studyWithStudyOrganizations);

        assertNotNull(studyWithStudyOrganizations.getId());
        assertNotNull(studyWithStudyOrganizations.getId());
        assertEquals("must not create multiple study coordinating center", Integer.valueOf(3), Integer.valueOf(studyWithStudyOrganizations.getStudyOrganizations().size()));
        assertEquals("must not create multiple funding sponsor", Integer.valueOf(3), Integer.valueOf(studyWithStudyOrganizations.getStudyOrganizations().size()));

       // ProCtcQuestion p = new ProCtcQuestion();
       // p.s
       // CrfItem crfItem1 = new CrfItem();
       // crfItem1.s


    }


    public void testFindStudySites() {
    }

    public void testStudyOrganizationQuery() {
    }

    @Required
    public void setOrganizationRepository(
            OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }


    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

}