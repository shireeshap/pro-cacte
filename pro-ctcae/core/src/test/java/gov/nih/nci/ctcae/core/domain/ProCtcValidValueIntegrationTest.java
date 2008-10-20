package gov.nih.nci.ctcae.core.domain;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.CtcTermQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcValidValueQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcValidValueRepository;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcValidValueIntegrationTest extends
		AbstractJpaIntegrationTestCase {

	private ProCtcValidValueRepository proCtcValidValueRepository;
	private ProCtcValidValue proCtcValidValue, inValidProCtcValidValue;
	private ProCtcRepository proCtcRepository;
	private CtcTermRepository ctcTermRepository;
	private ProCtcTermRepository proCtcTermRepository;

	private ProCtcTerm proCtcTerm;
	private CtcTerm ctcTerm;
	private ProCtc proCtc;
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		
		proCtc = proCtcRepository.find(new ProCtcQuery()).iterator().next();
		assertNotNull(proCtc);

		ctcTerm = ctcTermRepository.find(new CtcTermQuery()).iterator().next();
		assertNotNull(ctcTerm);
		
		proCtcTerm = new ProCtcTerm();
		proCtcTerm.setQuestionText("How is the pain?");
		proCtcTerm.setCtcTerm(ctcTerm);
		proCtcTerm.setProCtc(proCtc);
		
		proCtcTermRepository.save(proCtcTerm);
		
		proCtcValidValue = new ProCtcValidValue();
		proCtcValidValue.setValue("TestValidValue");
		proCtcValidValue.setProCtcTerm(proCtcTerm);
		proCtcValidValue = proCtcValidValueRepository.save(proCtcValidValue);

	}

	public void testSaveProCtcValidValue() {
		assertNotNull(proCtcValidValue.getId());
	}

	public void testSavingNullProCtcValidValue() {
		inValidProCtcValidValue = new ProCtcValidValue();

		try {
			inValidProCtcValidValue = proCtcValidValueRepository
					.save(inValidProCtcValidValue);
			fail("Expected DataIntegrityViolationException because value is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testFindById() {

		ProCtcValidValue existingProCtcValidValue = proCtcValidValueRepository
				.findById(proCtcValidValue.getId());
		assertEquals(proCtcValidValue.getValue(), existingProCtcValidValue
				.getValue());
		assertEquals(proCtcValidValue, existingProCtcValidValue);
	}

	public void testFindByQuery() {

		ProCtcValidValueQuery proCtcValidValueQuery = new ProCtcValidValueQuery();

		Collection<? extends ProCtcValidValue> ProCtcValidValues = proCtcValidValueRepository
				.find(proCtcValidValueQuery);
		assertFalse(ProCtcValidValues.isEmpty());
		int size = jdbcTemplate
				.queryForInt("select count(*) from PRO_CTC_VALID_VALUES ProCtcValidValue");
		assertEquals(size, ProCtcValidValues.size());
	}

	@Required
	public void setProCtcValidValueRepository(
			ProCtcValidValueRepository ProCtcValidValueRepository) {
		this.proCtcValidValueRepository = ProCtcValidValueRepository;
	}
	
	public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
		this.proCtcTermRepository = proCtcTermRepository;
	}

	public void setProCtcRepository(ProCtcRepository proCtcRepository) {
		this.proCtcRepository = proCtcRepository;
	}

	public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
		this.ctcTermRepository = ctcTermRepository;
	}
}
