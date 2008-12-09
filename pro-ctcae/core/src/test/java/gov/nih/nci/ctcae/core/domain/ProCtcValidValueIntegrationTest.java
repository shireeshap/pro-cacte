package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.query.ProCtcValidValueQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcValidValueRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcValidValueIntegrationTest extends
	AbstractHibernateIntegrationTestCase {

	private ProCtcValidValueRepository proCtcValidValueRepository;
	private ProCtcValidValue proCtcValidValue, inValidProCtcValidValue;
	private ProCtcRepository proCtcRepository;
	private ProCtcTermRepository proCtcTermRepository;
	private ProCtcQuestionRepository proCtcQuestionRepository;

	private ProCtcQuestion proCtcQuestion;
	private ProCtcTerm proProCtcTerm;
	private ProCtc proCtc;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();

		proCtc = proCtcRepository.find(new ProCtcQuery()).iterator().next();
		assertNotNull(proCtc);

		proProCtcTerm = proCtcTermRepository.findAndInitializeTerm(new ProCtcTermQuery()).iterator().next();
		assertNotNull(proProCtcTerm);

		proCtcQuestion = new ProCtcQuestion();
		proCtcQuestion.setQuestionText("How is the pain?");
		proCtcQuestion.setProCtcTerm(proProCtcTerm);
		proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);

		proCtcQuestionRepository.save(proCtcQuestion);

		proCtcValidValue = new ProCtcValidValue();
		proCtcValidValue.setValue(1);
		proCtcValidValue.setDisplayName("test");
		proCtcValidValue.setProCtcTerm(proCtcQuestion);
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

	public void setProCtcTermRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
		this.proCtcQuestionRepository = proCtcQuestionRepository;
	}

	public void setProCtcRepository(ProCtcRepository proCtcRepository) {
		this.proCtcRepository = proCtcRepository;
	}

	public void setCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
		this.proCtcTermRepository = proCtcTermRepository;
	}
}
