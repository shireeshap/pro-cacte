package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CRFIntegrationTest extends AbstractHibernateIntegrationTestCase {

	private CRFRepository crfRepository;
	private CRF crf, inValidCRF;
	private String title = "Cancer CRF" + UUID.randomUUID().toString();

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();

		crf = new CRF();
		crf.setTitle(title);
		crf.setDescription("Case Report Form for Cancer Patients");
		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");
		crf = crfRepository.save(crf);
	}

	public void testSaveCRF() {
		assertNotNull(crf.getId());
	}

	public void testUpdateStatusOfCRF() {
		assertNotNull(crf.getId());
		crfRepository.updateStatusToReleased(crf);
		assertEquals(CrfStatus.RELEASED, crf.getStatus());
	}

	public void testUniqueCrfTitle() {
		inValidCRF = new CRF();
		crf.setTitle(title);
		crf.setDescription("Case Report Form for Cancer Patients");
		crf.setStatus(CrfStatus.DRAFT);
		crf.setCrfVersion("1.0");

		try {
			inValidCRF = crfRepository.save(inValidCRF);
			fail("Expected DataIntegrityViolationException because title is unique");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullCRF() {
		inValidCRF = new CRF();

		try {
			inValidCRF = crfRepository.save(inValidCRF);
			crfRepository.find(new CRFQuery());
			fail("Expected DataIntegrityViolationException because title, status and formVersion are null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullTitleCRF() {
		inValidCRF = new CRF();
		try {
			inValidCRF.setStatus(CrfStatus.DRAFT);
			inValidCRF.setCrfVersion("1.0");
			inValidCRF = crfRepository.save(inValidCRF);
			crfRepository.find(new CRFQuery());
			fail("Expected DataIntegrityViolationException because title is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullStatusCRF() {
		inValidCRF = new CRF();
		try {
			inValidCRF.setTitle("Cancer CRF");
			inValidCRF.setStatus(null);
			inValidCRF.setCrfVersion("1.0");
			inValidCRF = crfRepository.save(inValidCRF);
			fail("Expected DataIntegrityViolationException because status is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullVersionCRF() {
		inValidCRF = new CRF();
		try {
			inValidCRF.setTitle(title);
			inValidCRF.setStatus(CrfStatus.DRAFT);
			crfRepository.save(inValidCRF);
			crfRepository.find(new CRFQuery());
			fail("Expected DataIntegrityViolationException because formVersion is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testFindById() {

		CRF existingCrf = crfRepository.findById(crf.getId());
		assertEquals(crf.getTitle(), existingCrf.getTitle());
		assertEquals(crf.getStatus(), existingCrf.getStatus());
		assertEquals(crf.getCrfVersion(), existingCrf.getCrfVersion());
		assertEquals(crf.getDescription(), existingCrf.getDescription());

	}


	public void testFindByQuery() {

		CRFQuery crfQuery = new CRFQuery();

		Collection<? extends CRF> crfs = crfRepository.find(crfQuery);
		assertFalse(crfs.isEmpty());
		int size = jdbcTemplate.queryForInt("select count(*) from CRFS crf");
		assertEquals(size, crfs.size());
	}

	public void testFindByTitleExactMatchQuery() {

		CRFQuery crfQuery = new CRFQuery();
		crfQuery.filterByTitleExactMatch(crf.getTitle());
		Collection<? extends CRF> crfs = crfRepository.find(crfQuery);
		assertFalse(crfs.isEmpty());
		int size = jdbcTemplate.queryForInt("select count(*) from CRFS crf where lower(crf.title)=?", new String[]{crf.getTitle().toLowerCase()});
		assertEquals(size, crfs.size());
		assertEquals("title must be unique", Integer.valueOf(1), Integer.valueOf(size));


	}

	public void testFilterByNotHavingCrfId() {

		CRFQuery crfQuery = new CRFQuery();
		crfQuery.filterByNotHavingCrfId(crf.getId());
		Collection<? extends CRF> crfs = crfRepository.find(crfQuery);
		int size = jdbcTemplate.queryForInt("select count(*) from CRFS crf where crf.id !=?", new Integer[]{crf.getId()});
		assertEquals(size, crfs.size());


	}

	@Required
	public void setCRFRepository(CRFRepository crfRepository) {
		this.crfRepository = crfRepository;
	}

}
