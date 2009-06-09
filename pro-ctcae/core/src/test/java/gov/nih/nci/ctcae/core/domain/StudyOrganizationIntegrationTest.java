package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudyOrganizationIntegrationTest extends TestDataManager {

    private Organization organization, organization1;
    private Study study;
    private StudySite studySite;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        OrganizationQuery query = new OrganizationQuery();
        query.setMaximumResults(10);
        List<Organization> organizations = new ArrayList<Organization>(organizationRepository.find(query));

        organization = organizations.get(0);
        organization1 = organizations.get(1);

        study = createStudy(0);

        assertNotNull(organization);
        assertNotNull(study);

        studySite = new StudySite();

        studySite.setOrganization(organization);
        studySite.setStudy(study);
//        studyOrganizationRepository.save(studySite);

    }


    public void testFindStudySites() {

//        List<Study> studyList = new ArrayList<Study>();
//
//        for (int i = 0; i < 10; i++) {
//            Study study2 = createStudy(i);
//            studyList.add(study2);
//        }
//
//        for (int i = 0; i < 5; i++) {
//            createOrganization(i);
//        }
//
//        ArrayList<Organization> organizations = new ArrayList<Organization>(
//                organizationRepository.find(new OrganizationQuery()));
//
//        ArrayList<Study> studies = new ArrayList<Study>(studyRepository
//                .find(new StudyQuery()));
//
//        int i = 0;
//        int j = 0;
//        for (Organization organization : organizations) {
//            i++;
//            j = 0;
//            for (Study study : studies) {
//                j++;
//                StudySite studySite = new StudySite();
//                studySite.setStudy(study);
//                studySite.setOrganization(organization);
////                studyOrganizationRepository.save(studySite);
//                if (j > 2 * i)
//                    break;
//            }
//        }
//
//        for (i = 6; i < 10; i++) {
//            createOrganization(i);
//        }
//
//        setComplete();
//        endTransaction();
//        startNewTransaction();
//        organizations = organizationRepository.findOrganizationsForStudySites();
//
//        OrganizationQuery oQuery = new OrganizationQuery();
//        oQuery.filterByNciCodeExactMatch("OrganizationNCI0");
//        assertEquals(true, organizations.contains(organizationRepository.findSingle(oQuery)));
//
//        oQuery = new OrganizationQuery();
//        oQuery.filterByNciCodeExactMatch("OrganizationNCI1");
//        assertEquals(true, organizations.contains(organizationRepository.findSingle(oQuery)));
//
//        oQuery = new OrganizationQuery();
//        oQuery.filterByNciCodeExactMatch("OrganizationNCI2");
//        assertEquals(true, organizations.contains(organizationRepository.findSingle(oQuery)));
//
//        oQuery = new OrganizationQuery();
//        oQuery.filterByNciCodeExactMatch("OrganizationNCI3");
//        assertEquals(true, organizations.contains(organizationRepository.findSingle(oQuery)));
//
//        oQuery = new OrganizationQuery();
//        oQuery.filterByNciCodeExactMatch("OrganizationNCI4");
//        assertEquals(true, organizations.contains(organizationRepository.findSingle(oQuery)));
//
//        oQuery = new OrganizationQuery();
//        oQuery.filterByNciCodeExactMatch("OrganizationNCI1");
//        assertEquals(true, organizations.contains(organizationRepository.findSingle(oQuery)));
//
//        oQuery = new OrganizationQuery();
//        oQuery.filterByNciCodeExactMatch("OrganizationNCI2");
//        assertEquals(true, organizations.contains(organizationRepository.findSingle(oQuery)));
//        oQuery = new OrganizationQuery();
//        oQuery.filterByNciCodeExactMatch("OrganizationNCI5");
//        assertEquals(false, organizations.contains(organizationRepository.findSingle(oQuery)));
//
//        oQuery = new OrganizationQuery();
//        oQuery.filterByNciCodeExactMatch("OrganizationNCI6");
//        assertEquals(false, organizations.contains(organizationRepository.findSingle(oQuery)));
//
//        for (Study study2 : studyList) {
//            studyRepository.delete(study2);
//        }
    }

    public void testStudyOrganizationQuery() {
        StudyOrganizationQuery query = new StudyOrganizationQuery();
        query.filterByOrganizationId(organization.getId());
        query.filterByStudyId(study.getId());
        query.filterByStudySiteAndLeadSiteOnly();

        System.out.println(organization.getId());
        System.out.println(study.getId());
//        ArrayList<StudyOrganization> studySites = (ArrayList<StudyOrganization>) studyOrganizationRepository.find(query);
//        assertEquals(1, studySites.size());
//
//        StudySite studySite = (StudySite) studySites.get(0);
//
//        assertEquals(studySite.getOrganization(), organization);
//        assertEquals(studySite.getStudy(), study);

    }

    private Study createStudy(int number) {
        Study study = new Study();
        study.setDescription("StudyDesc" + number);
        study.setLongTitle("StudyDesc" + number);
        study.setShortTitle("StudyDesc" + number);
        study.setAssignedIdentifier("StudyIdentifier" + number);
        return studyRepository.save(study);

    }

    private Organization createOrganization(int number) {
        Organization organization = new Organization();
        organization.setName("OrganizationName" + number);
        organization.setNciInstituteCode("OrganizationNCI" + number);
        return organizationRepository.save(organization);
    }


}
