package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Collection;

/**
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
public class OrganizationIntegrationTest extends AbstractJpaIntegrationTestCase {

    private OrganizationRepository organizationRepository;
    private Organization organization, inValidOrganization;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();    //To change body of overridden methods use File | Settings | File Templates.
        login();

        organization = new Organization();
        organization.setName("National Cancer Institute");
        organization.setNciInstituteCode("NCI");
        organization = organizationRepository.save(organization);

    }


    public void testSaveOrganization() {

        assertNotNull(organization.getId());

    }

    public void testValidationExceptionForSavingInValidOrganization() {
        inValidOrganization = new Organization();

        try {
            inValidOrganization = organizationRepository.save(inValidOrganization);
        } catch (DataIntegrityViolationException e) {
            logger.info("expecting this");
        }

        try {
            inValidOrganization.setName("NCI");
            inValidOrganization = organizationRepository.save(inValidOrganization);
        } catch (JpaSystemException e) {
            fail();
            logger.info("expecting this..contact information and organization date can not be null");
        }
        inValidOrganization.setNciInstituteCode("NCI");
        organizationRepository.save(inValidOrganization);
        inValidOrganization = organizationRepository.save(inValidOrganization);
        assertNotNull(inValidOrganization.getId());

    }


    public void testFindById() {

        Organization existingOrganization = organizationRepository.findById(organization.getId());
        assertEquals(organization.getName(), existingOrganization.getName());
        assertEquals(organization.getNciInstituteCode(), existingOrganization.getNciInstituteCode());

    }


    public void testFindByName() {

        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByOrganizationName("N");

        Collection<? extends Organization> organizations = organizationRepository.find(organizationQuery);
        assertFalse(organizations.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from organizations organizations where lower(organizations.name ) like '%n%'");

        assertEquals(size, organizations.size());


        for (Organization organization : organizations)

        {
            assertTrue(organization.getName().toLowerCase().contains("n"));
        }

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
        int size = jdbcTemplate.queryForInt("select count(*) from organizations organizations where lower(organizations.nci_institute_code ) like '%nci%'");

        assertEquals(size, organizations.size());


        for (Organization organization : organizations)

        {
            assertTrue(organization.getNciInstituteCode().toLowerCase().contains("nci")
                    || organization.getNciInstituteCode().toLowerCase().contains("nci"));
        }

    }

    public void testFindByNCICodeExactMatch() {

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


    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }
}
