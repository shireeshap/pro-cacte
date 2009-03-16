package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
public class OrganizationIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private Organization organization, inValidOrganization;


    private void saveOrganization() {
        organization = new Organization();
        organization.setName("National Cancer Institute");
        organization.setNciInstituteCode("NCI" + UUID.randomUUID());
        organization = organizationRepository.save(organization);
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {


    }

    public void testFindQueryPerformance() {


        List<Integer> ids = new ArrayList<Integer>();
        List<Integer> organizationids = jdbcTemplate.queryForList("select id from organizations", Integer.class);


        //first check for 10 organzations
        for (int i = 0; i < 21; i++) {
            ids.add(organizationids.get(i));
        }
        System.out.println("running for 10 ids");
        timeDifferenceForQueries(ids);

        ids.clear();

        for (int i = 0; i < 101; i++) {
            ids.add(organizationids.get(i));
        }

        System.out.println("running for 100 ids");
        timeDifferenceForQueries(ids);


        ids.clear();

        for (int i = 0; i < 1001; i++) {
            ids.add(organizationids.get(i));
        }
        System.out.println("running for 1000 ids");
        timeDifferenceForQueries(ids);

        ids.clear();
        for (int i = 0; i < 5001; i++) {
            ids.add(organizationids.get(i));
        }

        System.out.println("running for 5000 ids");
        timeDifferenceForQueries(ids);

    }

    private void timeDifferenceForQueries(List<Integer> ids) {
        String queryString = "";

        for (Integer id : ids) {
            queryString = id + "," + queryString;
        }
        queryString = queryString + ids.get(0);

        long startTime = System.currentTimeMillis();
        jdbcTemplate.queryForList(String.format("select id from organizations o where o.id in (%s)", queryString));
        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.print("time diff for  is:" + timeDiff + "milisecond .end of time diff");
    }

    public void testFindByName() {

        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByOrganizationName("N");

        Collection<? extends Organization> organizations = organizationRepository.find(organizationQuery);
        assertFalse(organizations.isEmpty());
        int size = jdbcTemplate.queryForInt("select distinct(count(*)) from organizations organizations where lower(organizations.name ) like '%n%'");

        assertEquals(size, organizations.size());


        for (Organization organization : organizations)

        {
            assertTrue(organization.getName().toLowerCase().contains("n"));
        }

    }

    public void testSingleThrowsException() {

        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByOrganizationName("N");
        try {
            organization = organizationRepository.findSingle(organizationQuery);
            fail("multiple results found for query");
        } catch (CtcAeSystemException e) {

        }

    }

    public void testFindSingle() {

        OrganizationQuery organizationQuery = new OrganizationQuery();

        organizationQuery.filterByOrganizationName("National");
        Collection<? extends Organization> organizations = organizationRepository.find(organizationQuery);
        assertFalse(organizations.isEmpty());

        organization = organizations.iterator().next();

        organizationQuery = new OrganizationQuery();
        organizationQuery.filterByNciCodeExactMatch(organization.getNciInstituteCode());
        organization = organizationRepository.findSingle(organizationQuery);

        assertNotNull(organization);
        assertNotNull(organization.getId());


    }

    public void testSingleReturnsNull() {

        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByNciInstituteCode("N");

        Collection<? extends Organization> organizations = organizationRepository.find(organizationQuery);
        assertFalse(organizations.isEmpty());

        organization = organizations.iterator().next();

        organizationQuery = new OrganizationQuery();
        organizationQuery.filterByNciCodeExactMatch(organization.getNciInstituteCode() + UUID.randomUUID());
        organization = organizationRepository.findSingle(organizationQuery);

        assertNull(organization);


    }

    public void testDelete() {

        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByNciInstituteCode("N");

        Collection<? extends Organization> organizations = organizationRepository.find(organizationQuery);
        assertFalse(organizations.isEmpty());

        organization = organizations.iterator().next();

        try {
            organizationRepository.delete(organization);
            fail("delete not supported");
        } catch (CtcAeSystemException e) {

        }

    }


    public void testSaveOrganization() {
        saveOrganization();

        assertNotNull(organization.getId());

    }

    public void testValidationExceptionForSavingInValidOrganization() {
        inValidOrganization = new Organization();

        try {
            inValidOrganization = organizationRepository.save(inValidOrganization);
        } catch (CtcAeSystemException e) {
            logger.info("expecting this");
        }

        try {
            inValidOrganization.setName("NCI");
            inValidOrganization = organizationRepository.save(inValidOrganization);
            fail();
        } catch (CtcAeSystemException e) {
            logger.info("expecting this..contact information and organization date can not be null");
        }

    }


    public void testFindById() {
        saveOrganization();

        Organization existingOrganization = organizationRepository.findById(organization.getId());
        assertEquals(organization.getName(), existingOrganization.getName());
        assertEquals(organization.getNciInstituteCode(), existingOrganization.getNciInstituteCode());

    }


    public void testFindByNCICode() {

        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByNciInstituteCode("N");

        Collection<? extends Organization> organizations = organizationRepository.find(organizationQuery);
        assertFalse(organizations.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from organizations organizations where lower(organizations.nci_institute_code ) like '%n%'");

        assertEquals(size, organizations.size());


        for (Organization organization : organizations)

        {
            assertTrue(organization.getNciInstituteCode().toLowerCase().contains("n"));
        }

    }


    public void testFindByOrganizationNameOrNciInstituteCode() {

        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByOrganizationNameOrNciInstituteCode("NCI");

        Collection<? extends Organization> organizations = organizationRepository.find(organizationQuery);
        assertFalse(organizations.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from organizations organizations where lower(organizations.nci_institute_code ) like '%nci%' or lower(organizations.name ) like '%nci%'");

        assertEquals(size, organizations.size());


        for (Organization organization : organizations)

        {
            assertTrue(organization.getNciInstituteCode().toLowerCase().contains("nci")
                    || organization.getName().toLowerCase().contains("nci"));
        }

    }

    public void testFindByNCICodeExactMatch() {
        saveOrganization();

        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByNciCodeExactMatch("NCI");

        Collection<? extends Organization> organizations = organizationRepository.find(organizationQuery);
        assertFalse(organizations.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from organizations organizations where organizations.nci_institute_code = 'NCI'");

        assertEquals(size, organizations.size());


        for (Organization organization : organizations)

        {
            assertEquals("NCI", organization.getNciInstituteCode());
        }

    }


}
