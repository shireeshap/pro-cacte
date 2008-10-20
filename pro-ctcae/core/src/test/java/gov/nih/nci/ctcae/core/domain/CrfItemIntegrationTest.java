package gov.nih.nci.ctcae.core.domain;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.CrfItemQuery;
import gov.nih.nci.ctcae.core.query.CtcTermQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.CrfItemRepository;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CrfItemIntegrationTest extends AbstractJpaIntegrationTestCase {

	public void setCrfItemRepository(CrfItemRepository crfItemRepository) {
		this.crfItemRepository = crfItemRepository;
	}

	private CrfItemRepository crfItemRepository;
	private CRFRepository crfRepository;
	private ProCtcRepository proCtcRepository;
	private CtcTermRepository ctcTermRepository;
	private ProCtcTermRepository proCtcTermRepository;
	private CRF crf;
	private ProCtcTerm proCtcTerm;
	private ProCtc proCtc;
	private CtcTerm ctcTerm;
	private CrfItem crfItem, invalidCrfItem;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();

		crf = new CRF();
		crf.setTitle("Cancer CRF");
		crf.setDescription("Case Report Form for Cancer Patients");
		crf.setStatus("DRAFT");
		crf.setCrfVersion("1.0");
		crf = crfRepository.save(crf);

		proCtc = proCtcRepository.find(new ProCtcQuery()).iterator().next();
		assertNotNull(proCtc);

		ctcTerm = ctcTermRepository.find(new CtcTermQuery()).iterator().next();
		assertNotNull(ctcTerm);


		proCtcTerm = new ProCtcTerm();
		proCtcTerm.setQuestionText("How is the pain?");
		proCtcTerm.setCtcTerm(ctcTerm);
		proCtcTerm.setProCtc(proCtc);
		proCtcTerm.addValidValue(new ProCtcValidValue("High"));
		proCtcTerm.addValidValue(new ProCtcValidValue("Low"));
		proCtcTerm.addValidValue(new ProCtcValidValue("Severe"));
		proCtcTerm.addValidValue(new ProCtcValidValue("Very High"));

		proCtcTermRepository.save(proCtcTerm);

		crfItem = new CrfItem();
		crfItem.setCRF(crf);
		crfItem.setProCtcTerm(proCtcTerm);
		crfItem.setDisplayOrder(1);
		crfItemRepository.save(crfItem);
		crfItemRepository.find(new CrfItemQuery());
	}

	public void testSaveCrfItem() {
		assertNotNull(crfItem.getId());
	}

	public void testSavingNullCrfItem() {
		invalidCrfItem = new CrfItem();

		try {
			invalidCrfItem = crfItemRepository.save(invalidCrfItem);
			crfItemRepository.find(new CrfItemQuery());
			fail("Expected DataIntegrityViolationException because title, status and formVersion are null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullCRFCrfItem() {
		invalidCrfItem = new CrfItem();
		try {
			invalidCrfItem.setProCtcTerm(proCtcTerm);
			invalidCrfItem.setDisplayOrder(1);
			invalidCrfItem = crfItemRepository.save(invalidCrfItem);
			crfItemRepository.find(new CrfItemQuery());
			fail("Expected DataIntegrityViolationException because CRF is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullProCtcTermCrfItem() {
		invalidCrfItem = new CrfItem();
		try {
			invalidCrfItem.setCRF(crf);
			invalidCrfItem.setDisplayOrder(1);
			invalidCrfItem = crfItemRepository.save(invalidCrfItem);
			crfItemRepository.find(new CrfItemQuery());
			fail("Expected DataIntegrityViolationException because ProCtcTerm is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullDisplayOrderCrfItem() {
		invalidCrfItem = new CrfItem();
		try {
			invalidCrfItem.setCRF(crf);
			invalidCrfItem.setProCtcTerm(proCtcTerm);
			invalidCrfItem = crfItemRepository.save(invalidCrfItem);
			crfItemRepository.find(new CrfItemQuery());
			fail("Expected DataIntegrityViolationException because DisplayOrder is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testFindById() {

		CrfItem existingCrfItem = crfItemRepository.findById(crfItem.getId());
		assertEquals(crfItem.getDisplayOrder(), existingCrfItem
				.getDisplayOrder());
		assertEquals(crfItem.getProCtcTerm(), existingCrfItem.getProCtcTerm());
		assertEquals(crfItem.getCRF(), existingCrfItem.getCRF());
		assertEquals(crfItem, existingCrfItem);
	}

	public void testFindByQuery() {

		CrfItemQuery crfItemQuery = new CrfItemQuery();

		Collection<? extends CrfItem> crfItems = crfItemRepository
				.find(crfItemQuery);
		assertFalse(crfItems.isEmpty());
		int size = jdbcTemplate
				.queryForInt("select count(*) from CRF_ITEMS crfItem");
		assertEquals(size, crfItems.size());
	}

	@Required
	public void setCRFRepository(CRFRepository crfRepository) {
		this.crfRepository = crfRepository;
	}

	public void setProCtcTermRepository(
			ProCtcTermRepository proCtcTermRepository) {
		this.proCtcTermRepository = proCtcTermRepository;
	}

	public void setProCtcRepository(ProCtcRepository proCtcRepository) {
		this.proCtcRepository = proCtcRepository;
	}

	public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
		this.ctcTermRepository = ctcTermRepository;
	}
}
