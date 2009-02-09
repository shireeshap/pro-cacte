package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
public class StudyIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private Study inValidStudy, studyWithStudyOrganizations;

    private StudySite nciStudySite;
    private Organization nci, duke;

    private StudyFundingSponsor studyFundingSponsor;
    private StudyCoordinatingCenter studyCoordinatingCenter;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();    //To change body of overridden methods use File | Settings | File Templates.
        login();


        saveStudy();

        assertNotNull(studyWithStudyOrganizations.getId());
        assertEquals("must not create multiple study coordinating center", Integer.valueOf(3), Integer.valueOf(studyWithStudyOrganizations.getStudyOrganizations().size()));
        assertEquals("must not create multiple funding sponsor", Integer.valueOf(3), Integer.valueOf(studyWithStudyOrganizations.getStudyOrganizations().size()));


    }

    private void saveStudy() {
        OrganizationQuery query = new OrganizationQuery();
        query.setMaximumResults(10);
        List<Organization> organizations = new ArrayList<Organization>(organizationRepository.find(query));

        nci = organizations.get(0);
        duke = organizations.get(1);


        nciStudySite = new StudySite();
        nciStudySite.setOrganization(nci);

        studyFundingSponsor = new StudyFundingSponsor();
        studyFundingSponsor.setOrganization(nci);

        studyCoordinatingCenter = new StudyCoordinatingCenter();
        studyCoordinatingCenter.setOrganization(nci);

        studyWithStudyOrganizations = Fixture.createStudy("study short title", "study long title", "assigned identifier" + UUID.randomUUID());
        studyWithStudyOrganizations.setStudyFundingSponsor(studyFundingSponsor);
        studyWithStudyOrganizations.setStudyCoordinatingCenter(studyCoordinatingCenter);
        studyWithStudyOrganizations.addStudySite(nciStudySite);

        studyWithStudyOrganizations = studyRepository.save(studyWithStudyOrganizations);
    }

    public void testFindSite() {
        endTransaction();
        startNewTransaction();


        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesForStudySite(nciStudySite.getOrganization().getId());

        Collection<? extends Study> studies = studyRepository.find(studyQuery);
        //	assertFalse(studies.isEmpty());
//        int size = jdbcTemplate.queryForInt("select count(*) from studies studies where lower (studies.long_title) like '%s%' " +
//                "or lower(studies.short_title ) like '%s%' or lower(studies.assigned_identifier ) like '%s%'");
//
//        assertEquals(size, studies.size());


        for (Study study : studies)

        {
            List<StudySite> studySites = study.getStudySites();
            List<Organization> sites = new ArrayList<Organization>();
            for (StudySite studySite : studySites) {
                sites.add(studySite.getOrganization());
            }
            assertTrue("must contains study site", sites.contains(nciStudySite.getOrganization()));
        }

    }

    public void testFindOrganizationsForStudySites() {
        endTransaction();
        startNewTransaction();

        Collection<? extends Organization> organizations = organizationRepository.findOrganizationsForStudySites();
        //	assertFalse(organizations.isEmpty());


    }

    public void testAddStudyFundingSponsorInCreateStudy() {
        assertNotNull("must save funding sponsor", studyWithStudyOrganizations.getStudyFundingSponsor());
        assertNotNull(studyWithStudyOrganizations.getStudyFundingSponsor().getId());
        assertEquals(nci, studyWithStudyOrganizations.getStudyFundingSponsor().getOrganization());
        assertEquals("must not create multiple funding sponsor", Integer.valueOf(3), Integer.valueOf(studyWithStudyOrganizations.getStudyOrganizations().size()));

    }


    public void testAddStudyCoordinatingCenterInCreateStudy() {

        assertNotNull("must save coordinating center", studyWithStudyOrganizations.getStudyCoordinatingCenter());

        assertNotNull(studyWithStudyOrganizations.getStudyCoordinatingCenter().getId());
        assertEquals(nci, studyWithStudyOrganizations.getStudyCoordinatingCenter().getOrganization());
        assertEquals("must not create multiple study coordinating center", Integer.valueOf(3), Integer.valueOf(studyWithStudyOrganizations.getStudyOrganizations().size()));

    }

    public void testUpdateStudyCoordinatingCenter() {
        assertNotNull(studyWithStudyOrganizations.getStudyCoordinatingCenter().getId());
        assertEquals(nci, studyWithStudyOrganizations.getStudyCoordinatingCenter().getOrganization());

        studyWithStudyOrganizations.getStudyCoordinatingCenter().setOrganization(duke);
        studyWithStudyOrganizations = studyRepository.save(studyWithStudyOrganizations);
        assertNotNull(studyWithStudyOrganizations.getStudyCoordinatingCenter().getId());
        assertEquals(duke, studyWithStudyOrganizations.getStudyCoordinatingCenter().getOrganization());
        assertEquals("must not create multiple study coordinating center", Integer.valueOf(3), Integer.valueOf(studyWithStudyOrganizations.getStudyOrganizations().size()));


    }

    public void testUpdateStudyFundingSponsor() {
        assertNotNull(studyWithStudyOrganizations.getStudyFundingSponsor().getId());
        assertEquals(nci, studyWithStudyOrganizations.getStudyFundingSponsor().getOrganization());


        studyWithStudyOrganizations.getStudyFundingSponsor().setOrganization(duke);
        studyWithStudyOrganizations = studyRepository.save(studyWithStudyOrganizations);
        assertNotNull(studyWithStudyOrganizations.getStudyFundingSponsor().getId());
        assertEquals(duke, studyWithStudyOrganizations.getStudyFundingSponsor().getOrganization());
        assertEquals("must not create multiple study funding sponsor", Integer.valueOf(3), Integer.valueOf(studyWithStudyOrganizations.getStudyOrganizations().size()));


    }

    public void testAddStudySiteInCreateStudy() {
        assertFalse("must save study site", studyWithStudyOrganizations.getStudySites().isEmpty());
        assertEquals(Integer.valueOf(1), Integer.valueOf(studyWithStudyOrganizations.getStudySites().size()));
        StudyOrganization studyOrganization = studyWithStudyOrganizations.getStudySites().get(0);
        assertNotNull(studyOrganization.getId());
        assertEquals(nci, studyOrganization.getOrganization());
    }

    public void testAddStudySiteInEditStudy() {

        studyWithStudyOrganizations.addStudySite(nciStudySite);
        studyWithStudyOrganizations = studyRepository.save(studyWithStudyOrganizations);
        assertNotNull(studyWithStudyOrganizations.getId());
        assertFalse("must save study site", studyWithStudyOrganizations.getStudySites().isEmpty());
        assertEquals(Integer.valueOf(2), Integer.valueOf(studyWithStudyOrganizations.getStudySites().size()));
        StudyOrganization studyOrganization = studyWithStudyOrganizations.getStudySites().get(0);
        assertNotNull(studyOrganization.getId());
        assertEquals(nci, studyOrganization.getOrganization());


    }

    public void testDeleteStudySite() {

        studyWithStudyOrganizations.addStudySite(nciStudySite);
        studyWithStudyOrganizations = studyRepository.save(studyWithStudyOrganizations);
        assertNotNull(studyWithStudyOrganizations.getId());
        assertFalse("must save study site", studyWithStudyOrganizations.getStudySites().isEmpty());
        assertEquals(Integer.valueOf(2), Integer.valueOf(studyWithStudyOrganizations.getStudySites().size()));
        assertNotNull(studyWithStudyOrganizations.getStudySites().get(0).getId());

        studyWithStudyOrganizations.removeStudySite(studyWithStudyOrganizations.getStudySites().get(0));
        studyWithStudyOrganizations = studyRepository.save(studyWithStudyOrganizations);
        assertNotNull(studyWithStudyOrganizations.getId());
        assertEquals("must remove study site", Integer.valueOf(1), Integer.valueOf(studyWithStudyOrganizations.getStudySites().size()));


    }

    public void testSaveStudy() {

        assertNotNull(studyWithStudyOrganizations.getId());

    }

//    public void testValidationExceptionForSavingInValidStudy() {
//        inValidStudy = new Study();
//
//        try {
//            inValidStudy = studyRepository.save(inValidStudy);
//        } catch (DataIntegrityViolationException e) {
//            logger.info("expecting this");
//        }
//
//        try {
//            inValidStudy.setName("NCI");
//            inValidStudy = studyRepository.save(inValidStudy);
//        } catch (JpaSystemException e) {
//            fail();
//            logger.info("expecting this..contact information and study date can not be null");
//        }
//        inValidStudy.setNciInstituteCode("NCI");
//        studyRepository.save(inValidStudy);
//        inValidStudy = studyRepository.save(inValidStudy);
//        assertNotNull(inValidStudy.getId());
//
//    }


    public void testFindById() {

        Study existingStudy = studyRepository.findById(this.studyWithStudyOrganizations.getId());
        assertEquals(this.studyWithStudyOrganizations, existingStudy);

    }


    public void testFindByName() {

        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesByShortTitle("short");

        Collection<? extends Study> studies = studyRepository.find(studyQuery);
        assertFalse(studies.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from studies studies where lower(studies.short_title ) like '%short%'");

        assertEquals(size, studies.size());


        for (Study study : studies)

        {
            assertTrue(study.getShortTitle().toLowerCase().contains("short"));
        }

    }

    public void testFindByNCICode() {

        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesByLongTitle("long");

        Collection<? extends Study> studies = studyRepository.find(studyQuery);
        assertFalse(studies.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from studies studies where lower (studies.long_title)  like '%long%'");

        assertEquals(size, studies.size());


        for (Study study : studies)

        {
            assertTrue(study.getLongTitle().toLowerCase().contains("long"));
        }

    }

    public void testFindByNCICodeExactMatch() {

        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterByAssignedIdentifierExactMatch(studyWithStudyOrganizations.getAssignedIdentifier());

        Collection<? extends Study> studies = studyRepository.find(studyQuery);
        assertFalse(studies.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from studies studies where lower (studies.assigned_identifier)  = ?", new String[]{studyWithStudyOrganizations.getAssignedIdentifier()});

        assertEquals(size, studies.size());


        for (Study study : studies)

        {
            assertTrue(study.getAssignedIdentifier().toLowerCase().equals(studyWithStudyOrganizations.getAssignedIdentifier()));
        }

    }

    public void testFindByMatchingText() {

        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesWithMatchingText("s");

        Collection<? extends Study> studies = studyRepository.find(studyQuery);
        assertFalse(studies.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from studies studies where lower (studies.long_title) like '%s%' " +
                "or lower(studies.short_title ) like '%s%' or lower(studies.assigned_identifier ) like '%s%'");

        assertEquals(size, studies.size());


        for (Study study : studies)

        {
            assertTrue(study.getShortTitle().toLowerCase().contains("s")
                    || study.getLongTitle().toLowerCase().contains("s")
                    || study.getAssignedIdentifier().toLowerCase().contains("s"));
        }

    }


}