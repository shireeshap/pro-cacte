package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudyParticipantCrfIntegrationTest extends TestDataManager {

    private Study inValidStudy, studyWithStudyOrganizations;

    private StudySite nciStudySite;
    private Organization nci, duke;

    private StudySponsor studySponsor;
    private DataCoordinatingCenter dataCoordinatingCenter;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        OrganizationQuery query = new OrganizationQuery();
        query.setMaximumResults(10);
        List<Organization> organizations = new ArrayList<Organization>(organizationRepository.find(query));

        nci = organizations.get(0);
        duke = organizations.get(1);


        nciStudySite = new StudySite();
        nciStudySite.setOrganization(nci);

        studySponsor = new StudySponsor();
        studySponsor.setOrganization(nci);

        dataCoordinatingCenter = new DataCoordinatingCenter();
        dataCoordinatingCenter.setOrganization(nci);

        studyWithStudyOrganizations = Fixture.createStudy("study short title", "study long title", "assigned identifier");
        studyWithStudyOrganizations.setStudySponsor(studySponsor);
        studyWithStudyOrganizations.setDataCoordinatingCenter(dataCoordinatingCenter);
        studyWithStudyOrganizations.addStudySite(nciStudySite);

        studyWithStudyOrganizations = studyRepository.save(studyWithStudyOrganizations);

        assertNotNull(studyWithStudyOrganizations.getId());
        assertNotNull(studyWithStudyOrganizations.getId());
        assertEquals("must not create multiple study coordinating center", Integer.valueOf(3), Integer.valueOf(studyWithStudyOrganizations.getStudyOrganizations().size()));
        assertEquals("must not create multiple funding sponsor", Integer.valueOf(3), Integer.valueOf(studyWithStudyOrganizations.getStudyOrganizations().size()));

        ProCtcQuestion p = new ProCtcQuestion();
        //p.setQuestionText();
        // CrfPageItem crfItem1 = new CrfPageItem();
        // crfItem1.s


    }


    public void testFindStudySites() {
    }

    public void testStudyOrganizationQuery() {
    }


}