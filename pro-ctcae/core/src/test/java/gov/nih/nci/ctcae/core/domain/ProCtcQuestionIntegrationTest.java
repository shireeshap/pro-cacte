package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcQuestionIntegrationTest extends TestDataManager {

    private ProCtcQuestionRepository proCtcQuestionRepository;

    private ProCtcQuestion proCtcQuestion, inValidproCtcQuestion;
    private ProCtcTerm proProCtcTerm;
    private ProCtc proCtc;
    private ArrayList<ProCtcValidValue> validValues = new ArrayList<ProCtcValidValue>();

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

//        saveCsv();
        proCtc = proCtcRepository.find(new ProCtcQuery()).iterator().next();
        assertNotNull(proCtc);

        proProCtcTerm = proCtcTermRepository.find(new ProCtcTermQuery()).iterator().next();
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

            fail("Expected CtcAeSystemException because all the fields are null");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testSavingNullQuestionProCtcTerm() {
        inValidproCtcQuestion = new ProCtcQuestion();
        try {
            inValidproCtcQuestion.setProCtcTerm(proProCtcTerm);
            inValidproCtcQuestion = proCtcQuestionRepository.save(inValidproCtcQuestion);

            fail("Expected CtcAeSystemException because question is null");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testSavingNullCtcTermProCtcTerm() {
        inValidproCtcQuestion = new ProCtcQuestion();
        try {
            inValidproCtcQuestion.setQuestionText("How is the pain?");
            inValidproCtcQuestion = proCtcQuestionRepository.save(inValidproCtcQuestion);

            fail("Expected CtcAeSystemException because proProCtcTerm is null");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testSavingNullProCtcProCtcTerm() {
        inValidproCtcQuestion = new ProCtcQuestion();
        try {
            inValidproCtcQuestion.setQuestionText("How is the pain?");
            inValidproCtcQuestion = proCtcQuestionRepository.save(inValidproCtcQuestion);
            fail("Expected CtcAeSystemException because proCtc is null");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testFindById() {
        ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();

        Collection<? extends ProCtcQuestion> proCtcQuestions = proCtcQuestionRepository.find(proCtcQuestionQuery);
        assertFalse(proCtcQuestions.isEmpty());
        proCtcQuestion = proCtcQuestions.iterator().next();
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
    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }


}
