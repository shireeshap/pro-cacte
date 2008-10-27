package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.query.ResearchStaffQuery;
import gov.nih.nci.ctcae.core.repository.ResearchStaffRepository;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Collection;

/**
 * @author Mehul Gulati
 * Date: Oct 27, 2008
 * */
public class ResearchStaffIntegrationTest extends AbstractJpaIntegrationTestCase {

    private ResearchStaffRepository researchStaffRepository;
    private ResearchStaff researchStaff, inValidResearchStaff;
    private Organization nci;
    private OrganizationRepository organizationRepository;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        login();

        researchStaff = new ResearchStaff();
        researchStaff.setFirstName("john");
        researchStaff.setLastName("dow");
        researchStaff.setResearcherID("ID 4");
        nci = Fixture.createOrganization("National Cancer Institute", "NCI");
        nci = organizationRepository.save(nci);

        researchStaff.setOrganization(nci);
        researchStaff = researchStaffRepository.save(researchStaff);
    }

    public void testSaveResearchStaff() {
        assertNotNull(researchStaff.getId());
    }

    public void testValidationExceptionForSavingInValidResearchStaff() {
        inValidResearchStaff = new ResearchStaff();

        try {
            inValidResearchStaff = researchStaffRepository.save(inValidResearchStaff);
        } catch (DataIntegrityViolationException e) {
            logger.info("expecting this");
        }
        try {
            inValidResearchStaff.setFirstName("john");
            inValidResearchStaff = researchStaffRepository.save(inValidResearchStaff);
        } catch (JpaSystemException e) {
            fail();
            logger.info("expecting this.. last name and Research ID are missing");
        }

        inValidResearchStaff = new ResearchStaff();
        inValidResearchStaff.setFirstName("john");
        inValidResearchStaff.setLastName("dow");
        inValidResearchStaff.setResearcherID("ID 4");
        inValidResearchStaff = researchStaffRepository.save(inValidResearchStaff);
        assertNotNull(inValidResearchStaff.getId());
    }

    public void testFindById() {
        ResearchStaff existingResearchStaff = researchStaffRepository.findById(researchStaff.getId());
        assertEquals(researchStaff.getFirstName(), existingResearchStaff.getFirstName());
        assertEquals(researchStaff.getLastName(), existingResearchStaff.getLastName());
        assertEquals(researchStaff.getResearcherID(), existingResearchStaff.getResearcherID());
    }

    public void testFindByFirstName() {

        ResearchStaffQuery researchStaffQuery = new ResearchStaffQuery();
        researchStaffQuery.filterByResearchStaffFirstName("john");

        Collection<? extends ResearchStaff> researchStaffs = researchStaffRepository.find(researchStaffQuery);
        assertFalse(researchStaffs.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from Research_Staff researchStaff where lower(researchStaff.first_name ) like '%j%'");
        assertEquals(size, researchStaffs.size());

        for (ResearchStaff researchStaff : researchStaffs)
        {
            assertTrue(researchStaff.getFirstName().toLowerCase().contains("j"));
        }
    }

    public void testFindByLastName() {

           ResearchStaffQuery researchStaffQuery = new ResearchStaffQuery();
           researchStaffQuery.filterByResearchStaffLastName("dow");

           Collection<? extends ResearchStaff> researchStaffs = researchStaffRepository.find(researchStaffQuery);
           assertFalse(researchStaffs.isEmpty());
           int size = jdbcTemplate.queryForInt("select count(*) from Research_Staff researchStaff where lower(researchStaff.last_name ) like '%d%'");
           assertEquals(size, researchStaffs.size());

           for (ResearchStaff researchStaff : researchStaffs)
           {
               assertTrue(researchStaff.getLastName().toLowerCase().contains("d"));
           }
       }


    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public void setResearchStaffRepository(ResearchStaffRepository researchStaffRepository) {
        this.researchStaffRepository = researchStaffRepository;
    }
}
