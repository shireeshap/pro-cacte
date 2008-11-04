package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudyOrganizationIntegrationTest extends
        AbstractJpaIntegrationTestCase {

    private OrganizationRepository organizationRepository;
    private StudyRepository studyRepository;
    private Organization organization, organization1;
    private Study study;
    private StudySite studySite;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        organization = createOrganization(0);
        organization1 = createOrganization(1);
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
//            Study study1 = createStudy(i);
//            studyList.add(study1);
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
//        for (Study study1 : studyList) {
//            studyRepository.delete(study1);
//        }
    }

    public void testStudyOrganizationQuery() {
        StudyOrganizationQuery query = new StudyOrganizationQuery();
        query.filterByOrganizationId(organization.getId());
        query.filterByStudyId(study.getId());
        query.filterByStudySiteOnly();

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
