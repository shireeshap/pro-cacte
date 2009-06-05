package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;

import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcTermIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private ProCtcTerm proProCtcTerm;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
//        saveCsv();

    }

    public void testDeleteNotSupported() {
        try {
            proCtcTermRepository.delete(new ProCtcTerm());
            fail("Expecting UnsupportedOperationException: delete is not supported for ProCtcTerm.");
        } catch (CtcAeSystemException e) {
        }
    }

    public void testFindAndInitialize() {
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository.find(proCtcTermQuery);
        ProCtcTerm firstProProCtcTerm = ctcTerms.iterator().next();

        proProCtcTerm = proCtcTermRepository.findById(firstProProCtcTerm.getId());
        assertEquals(proProCtcTerm.getCtcTerm().getCtepCode(), firstProProCtcTerm.getCtcTerm().getCtepCode());
        assertEquals(proProCtcTerm.getCtcTerm().getCtepTerm(), firstProProCtcTerm.getCtcTerm().getCtepTerm());
        assertEquals(proProCtcTerm.getCtcTerm().getSelect(), firstProProCtcTerm.getCtcTerm().getSelect());
        assertEquals(proProCtcTerm.getTerm(), firstProProCtcTerm.getTerm());
        assertEquals(proProCtcTerm, firstProProCtcTerm);
    }

    public void testFindAndInitializeById() {
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
                .find(proCtcTermQuery);
        ProCtcTerm firstProProCtcTerm = ctcTerms.iterator().next();


        ProCtcTerm proCtcTerm = proCtcTermRepository.findById(firstProProCtcTerm.getId());
        assertEquals(proCtcTerm, firstProProCtcTerm);
    }

    public void testFilterByCtcTermHavingQuestionsOnly() {
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        proCtcTermQuery.filterByCtcTermHavingQuestionsOnly();
        Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
                .find(proCtcTermQuery);

        assertFalse("must find atleast one ctc term", ctcTerms.isEmpty());
        for (ProCtcTerm proCtcTerm : ctcTerms) {
            assertFalse("must find atleast one question for each term", proCtcTerm.getProCtcQuestions().isEmpty());

        }
    }

    public void testFindByQuery() {

        int size = jdbcTemplate
                .queryForInt("select count(*) from PRO_CTC_TERMS");
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        proCtcTermQuery.setMaximumResults(size + 1000);
        Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
                .find(proCtcTermQuery);

        assertFalse(ctcTerms.isEmpty());
        assertEquals(size, ctcTerms.size());
    }


}
