package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.InvestigatorQuery;
import gov.nih.nci.ctcae.core.repository.InvestigatorRepository;
import gov.nih.nci.ctcae.core.repository.JpaGenericRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Collection;

/**
 * @author mehul
 */

public class InvestigatorIntegrationTest extends AbstractJpaIntegrationTestCase {

    private InvestigatorRepository investigatorRepository;
    private Investigator investigator, inValidInvestigator;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        login();
      /*  investigatorRepository.setGenericRepository(new JpaGenericRepository<Investigator>());   */
        investigator = new Investigator();
        investigator.setFirstName("John");
        investigator.setLastName("Dow");
        investigator.setNciIdentifier("NCI 01");
        investigator = investigatorRepository.save(investigator);
    }

    public void testSaveInvestigator() {
        assertNotNull(investigator.getId());

    }

    public void testValidationExceptionForSavingInValidInvestigator() {
        inValidInvestigator = new Investigator();

        try {
            inValidInvestigator =  investigatorRepository.save(inValidInvestigator);
        } catch (DataIntegrityViolationException e) {
            logger.info("expecting this");
        }

        try {
            inValidInvestigator.setFirstName("John");
            inValidInvestigator = investigatorRepository.save(inValidInvestigator);
       } catch (JpaSystemException e) {
            fail();
            logger.info("expecting this.. last name and NCI code is missing");
        }
        inValidInvestigator= new Investigator();
        inValidInvestigator.setFirstName("John");
        inValidInvestigator.setLastName("Dow");
        inValidInvestigator.setNciIdentifier("NCI 1");
        inValidInvestigator = investigatorRepository.save(inValidInvestigator);
        assertNotNull(inValidInvestigator.getId());
    }

    public void testFindById() {

        Investigator existingInvestigator = investigatorRepository.findById(investigator.getId());
        assertEquals(investigator.getFirstName(), existingInvestigator.getFirstName());
        assertEquals(investigator.getLastName(), existingInvestigator.getLastName());
        assertEquals(investigator.getNciIdentifier(), existingInvestigator.getNciIdentifier());
    }

    public void testFindByFirstName() {

        InvestigatorQuery investigatorQuery = new InvestigatorQuery();
        investigatorQuery.filterByInvestigatorFirstName("J");

        Collection<? extends Investigator> investigators = investigatorRepository.find(investigatorQuery);
        assertFalse(investigators.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from investigators investigators where lower(investigators.first_name ) like '%j%'");
        assertEquals(size, investigators.size());

        for (Investigator investigator : investigators)
        {
            assertTrue(investigator.getFirstName().toLowerCase().contains("j"));
        }
    }

    public void testFindByLastName() {

        InvestigatorQuery investigatorQuery = new InvestigatorQuery();
        investigatorQuery.filterByInvestigatorLastName("D");

        Collection<? extends Investigator> investigators = investigatorRepository.find(investigatorQuery);
        assertFalse(investigators.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from investigators investigators where lower(investigators.last_name ) like '%d%'");
        assertEquals(size, investigators.size());

        for (Investigator investigator : investigators)
        {
            assertTrue(investigator.getLastName().toLowerCase().contains("d"));
        }

    }

     public void setInvestigatorRepository(InvestigatorRepository investigatorRepository) {
        this.investigatorRepository = investigatorRepository;
    }

}
