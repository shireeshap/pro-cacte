package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcQuestionIntegrationTest extends AbstractHibernateIntegrationTestCase {

	private ProCtcQuestionRepository proCtcQuestionRepository;

	private ProCtcRepository proCtcRepository;
	private ProCtcTermRepository proCtcTermRepository;
	private ProCtcQuestion proCtcQuestion, inValidproCtcQuestion;
	private ProCtcTerm proProCtcTerm;
	private ProCtc proCtc;
	private ArrayList<ProCtcValidValue> validValues = new ArrayList<ProCtcValidValue>();

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();

		proCtc = proCtcRepository.find(new ProCtcQuery()).iterator().next();
		assertNotNull(proCtc);

		proProCtcTerm = proCtcTermRepository.findAndInitializeTerm(new ProCtcTermQuery()).iterator().next();
		assertNotNull(proProCtcTerm);

	}

	private void saveProCtcQuestion() {
		validValues = new ArrayList<ProCtcValidValue>();

		assertNotNull(validValues);

		proCtcQuestion = new ProCtcQuestion();
		proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.FREQUENCY);
		proCtcQuestion.setQuestionText("How is the pain?");
		proCtcQuestion.setProCtcTerm(proProCtcTerm);
		for (ProCtcValidValue validValue : validValues) {
			proCtcQuestion.addValidValue(validValue);
		}
		proCtcQuestion = proCtcQuestionRepository.save(proCtcQuestion);
	}

	public void testSaveproCtcTerm() {
		saveProCtcQuestion();

		assertNotNull(proCtcQuestion.getId());
	}

	public void testSavingNullProCtcTerm() {
		inValidproCtcQuestion = new ProCtcQuestion();

		try {
			inValidproCtcQuestion = proCtcQuestionRepository.save(inValidproCtcQuestion);

			fail("Expected DataIntegrityViolationException because all the fields are null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullQuestionProCtcTerm() {
		inValidproCtcQuestion = new ProCtcQuestion();
		try {
			inValidproCtcQuestion.setProCtcTerm(proProCtcTerm);
			inValidproCtcQuestion = proCtcQuestionRepository.save(inValidproCtcQuestion);

			fail("Expected DataIntegrityViolationException because question is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullCtcTermProCtcTerm() {
		inValidproCtcQuestion = new ProCtcQuestion();
		try {
			inValidproCtcQuestion.setQuestionText("How is the pain?");
			inValidproCtcQuestion = proCtcQuestionRepository.save(inValidproCtcQuestion);

			fail("Expected DataIntegrityViolationException because proProCtcTerm is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullProCtcProCtcTerm() {
		inValidproCtcQuestion = new ProCtcQuestion();
		try {
			inValidproCtcQuestion.setQuestionText("How is the pain?");
			inValidproCtcQuestion = proCtcQuestionRepository.save(inValidproCtcQuestion);
			fail("Expected DataIntegrityViolationException because proCtc is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testFindById() {
		ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();

		Collection<? extends ProCtcQuestion> proCtcTerms = proCtcQuestionRepository
			.find(proCtcQuestionQuery);
		assertFalse(proCtcTerms.isEmpty());
		proCtcQuestion = proCtcTerms.iterator().next();
		ProCtcQuestion existingproProCtcQuestion = proCtcQuestionRepository
			.findById(proCtcQuestion.getId());
		assertEquals(proCtcQuestion.getQuestionText(), existingproProCtcQuestion
			.getQuestionText());
		assertEquals(proCtcQuestion.getProCtcTerm(), existingproProCtcQuestion.getProCtcTerm());
		assertEquals(proCtcQuestion, existingproProCtcQuestion);

	}

	public void testFindByQuery() {

		ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();

		Collection<? extends ProCtcQuestion> proCtcTerms = proCtcQuestionRepository
			.find(proCtcQuestionQuery);
		assertFalse(proCtcTerms.isEmpty());
		int size = jdbcTemplate
			.queryForInt("select count(*) from pro_ctc_questions proProCtcQuestion");
		assertEquals(size, proCtcTerms.size());
	}

	@Required
	public void setProCtcTermRepository(
		ProCtcQuestionRepository proCtcQuestionRepository) {
		this.proCtcQuestionRepository = proCtcQuestionRepository;
	}

	@Required
	public void setProCtcRepository(ProCtcRepository proCtcRepository) {
		this.proCtcRepository = proCtcRepository;
	}

	@Required
	public void setCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
		this.proCtcTermRepository = proCtcTermRepository;
	}


}
