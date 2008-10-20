package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.CtcTermQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.query.ProCtcValidValueQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcValidValueRepository;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcTermIntegrationTest extends AbstractJpaIntegrationTestCase {

	private ProCtcTermRepository proCtcTermRepository;

	private ProCtcRepository proCtcRepository;
	private CtcTermRepository ctcTermRepository;
	private ProCtcValidValueRepository proCtcValidValueRepository;
	private ProCtcTerm proCtcTerm, inValidproCtcTerm;
	private CtcTerm ctcTerm;
	private ProCtc proCtc;
	private ArrayList<ProCtcValidValue> validValues = new ArrayList<ProCtcValidValue>();

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();

		proCtc = proCtcRepository.find(new ProCtcQuery()).iterator().next();
		assertNotNull(proCtc);

		ctcTerm = ctcTermRepository.find(new CtcTermQuery()).iterator().next();
		assertNotNull(ctcTerm);

		ProCtcValidValueQuery validValueQuery = new ProCtcValidValueQuery();
		validValueQuery.setMaximumResults(4);
		validValues = new ArrayList<ProCtcValidValue>(
				proCtcValidValueRepository.find(validValueQuery));
		assertNotNull(validValues);

		proCtcTerm = new ProCtcTerm();
		proCtcTerm.setQuestionText("How is the pain?");
		proCtcTerm.setCtcTerm(ctcTerm);
		proCtcTerm.setProCtc(proCtc);
		for(ProCtcValidValue validValue : validValues){
			proCtcTerm.addValidValue(validValue);
		}
		proCtcTerm = proCtcTermRepository.save(proCtcTerm);
	}

	public void testSaveproCtcTerm() {
		assertNotNull(proCtcTerm.getId());
	}

	public void testSavingNullProCtcTerm() {
		inValidproCtcTerm = new ProCtcTerm();

		try {
			inValidproCtcTerm = proCtcTermRepository.save(inValidproCtcTerm);
			fail("Expected DataIntegrityViolationException because all the fields are null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullQuestionProCtcTerm() {
		inValidproCtcTerm = new ProCtcTerm();
		try {
			inValidproCtcTerm.setCtcTerm(ctcTerm);
			inValidproCtcTerm.setProCtc(proCtc);
			inValidproCtcTerm = proCtcTermRepository.save(inValidproCtcTerm);
			fail("Expected DataIntegrityViolationException because question is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullCtcTermProCtcTerm() {
		inValidproCtcTerm = new ProCtcTerm();
		try {
			inValidproCtcTerm.setQuestionText("How is the pain?");
			inValidproCtcTerm.setProCtc(proCtc);
			inValidproCtcTerm = proCtcTermRepository.save(inValidproCtcTerm);
			proCtcTermRepository.find(new ProCtcTermQuery());
			fail("Expected DataIntegrityViolationException because ctcTerm is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testSavingNullProCtcProCtcTerm() {
		inValidproCtcTerm = new ProCtcTerm();
		try {
			inValidproCtcTerm.setQuestionText("How is the pain?");
			inValidproCtcTerm.setCtcTerm(ctcTerm);
			inValidproCtcTerm = proCtcTermRepository.save(inValidproCtcTerm);
			proCtcTermRepository.find(new ProCtcTermQuery());
			fail("Expected DataIntegrityViolationException because proCtc is null");
		} catch (DataIntegrityViolationException e) {
		}
	}

	public void testFindById() {

		ProCtcTerm existingproCtcTerm = proCtcTermRepository
				.findById(proCtcTerm.getId());
		assertEquals(proCtcTerm.getQuestionText(), existingproCtcTerm
				.getQuestionText());
		assertEquals(proCtcTerm.getCtcTerm(), existingproCtcTerm.getCtcTerm());
		assertEquals(proCtcTerm.getProCtc(), existingproCtcTerm.getProCtc());
		assertEquals(proCtcTerm, existingproCtcTerm);

	}

	public void testFindByQuery() {

		ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();

		Collection<? extends ProCtcTerm> proCtcTerms = proCtcTermRepository
				.find(proCtcTermQuery);
		assertFalse(proCtcTerms.isEmpty());
		int size = jdbcTemplate
				.queryForInt("select count(*) from pro_ctc_terms proCtcTerm");
		assertEquals(size, proCtcTerms.size());
	}

	@Required
	public void setProCtcTermRepository(
			ProCtcTermRepository proCtcTermRepository) {
		this.proCtcTermRepository = proCtcTermRepository;
	}

	@Required
	public void setProCtcRepository(ProCtcRepository proCtcRepository) {
		this.proCtcRepository = proCtcRepository;
	}

	@Required
	public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
		this.ctcTermRepository = ctcTermRepository;
	}

	@Required
	public void setProCtcValidValueRepository(
			ProCtcValidValueRepository proCtcValidValueRepository) {
		this.proCtcValidValueRepository = proCtcValidValueRepository;
	}
}
