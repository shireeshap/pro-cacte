package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;

import java.util.Collection;
import java.util.Date;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private ProCtc proCtc, inValidProCtc;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
//        saveCsv();

    }

    private void saveProCtc() {
        proCtc = new ProCtc();
        proCtc.setProCtcVersion("2.0");
        proCtc.setReleaseDate(new Date());
        proCtc = proCtcRepository.save(proCtc);
    }

    public void testSaveProCtc() {
        saveProCtc();
        assertNotNull(proCtc.getId());
    }

    public void testSavingNullProCtc() {
        inValidProCtc = new ProCtc();

        try {
            inValidProCtc = proCtcRepository.save(inValidProCtc);
            fail("Expected CtcAeSystemException because title, status and formVersion are null");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testSavingNullVersionProCtc() {
        inValidProCtc = new ProCtc();
        try {
            inValidProCtc.setReleaseDate(new Date());
            inValidProCtc = proCtcRepository.save(inValidProCtc);
            fail("Expected CtcAeSystemException because title is null");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testSavingNullReleaseDateProCtc() {
        inValidProCtc = new ProCtc();
        try {
            inValidProCtc.setProCtcVersion("2.0");
            inValidProCtc = proCtcRepository.save(inValidProCtc);
            fail("Expected CtcAeSystemException because status is null");
        } catch (CtcAeSystemException e) {
        }

    }

    public void testFindById() {
        ProCtcQuery proCtcQuery = new ProCtcQuery();

        Collection<? extends ProCtc> proCtcs = proCtcRepository
                .find(proCtcQuery);
        assertFalse(proCtcs.isEmpty());

        proCtc = proCtcs.iterator().next();
        ProCtc existingProCtc = proCtcRepository.findById(proCtc.getId());
        assertEquals(proCtc.getProCtcVersion(), existingProCtc
                .getProCtcVersion());
        assertEquals(proCtc.getReleaseDate(), existingProCtc.getReleaseDate());
        assertEquals(proCtc, existingProCtc);
    }

    public void testFindByQuery() {

        ProCtcQuery proCtcQuery = new ProCtcQuery();

        Collection<? extends ProCtc> proCtcs = proCtcRepository
                .find(proCtcQuery);
        assertFalse(proCtcs.isEmpty());
        int size = jdbcTemplate
                .queryForInt("select count(*) from PRO_CTC proCtc");
        assertEquals(size, proCtcs.size());
    }

    public void testFindByProCtcVersion() {
        ProCtcQuery proCtcQuery = new ProCtcQuery();
        proCtcQuery.filterByProCtcVersion("1.0");

        Collection<? extends ProCtc> proCtcs = proCtcRepository
                .find(proCtcQuery);
        assertFalse(proCtcs.isEmpty());
        int size = jdbcTemplate
                .queryForInt("select count(*) from PRO_CTC proCtc where proCtc.pro_ctc_version = '1.0'");

        assertEquals(size, proCtcs.size());
        assertEquals(1, proCtcs.size());
        for (ProCtc proCtc : proCtcs) {
            assertEquals("1.0", proCtc.getProCtcVersion());
        }

    }


}
