package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;
import java.util.Date;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcIntegrationTest extends AbstractHibernateIntegrationTestCase {

	private ProCtcRepository proCtcRepository;
	private ProCtc proCtc, inValidProCtc;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		proCtc = new ProCtc();
		proCtc.setProCtcVersion("2.0");
		proCtc.setReleaseDate(new Date());
		proCtc = proCtcRepository.save(proCtc);

	}

	public void testSaveProCtc() {
		assertNotNull(proCtc.getId());
	}

	public void testSavingNullProCtc() {
		inValidProCtc = new ProCtc();

		try {
			inValidProCtc = proCtcRepository.save(inValidProCtc);
			fail("Expected DataIntegrityViolationException because title, status and formVersion are null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullVersionProCtc() {
		inValidProCtc = new ProCtc();
		try {
			inValidProCtc.setReleaseDate(new Date());
			inValidProCtc = proCtcRepository.save(inValidProCtc);
			fail("Expected DataIntegrityViolationException because title is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullReleaseDateProCtc() {
		inValidProCtc = new ProCtc();
		try {
			inValidProCtc.setProCtcVersion("2.0");
			inValidProCtc = proCtcRepository.save(inValidProCtc);
			fail("Expected DataIntegrityViolationException because status is null");
		} catch (DataIntegrityViolationException e) {
		}

	}

	public void testFindById() {

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
		proCtcQuery.filterByProCtcVersion("2.0");

		Collection<? extends ProCtc> proCtcs = proCtcRepository
			.find(proCtcQuery);
		assertFalse(proCtcs.isEmpty());
		int size = jdbcTemplate
			.queryForInt("select count(*) from PRO_CTC proCtc where proCtc.pro_ctc_version = '2.0'");

		assertEquals(size, proCtcs.size());
		assertEquals(1, proCtcs.size());
		for (ProCtc proCtc : proCtcs) {
			assertEquals("2.0", proCtc.getProCtcVersion());
		}

	}

	@Required
	public void setProCtcRepository(ProCtcRepository proCtcRepository) {
		this.proCtcRepository = proCtcRepository;
	}
}
