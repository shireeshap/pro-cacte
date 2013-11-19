package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 */

public class ProCtcTermQueryTest extends TestCase {

    public void testFilterByCtcTermHavingQuestionsOnly() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcTermHavingQuestionsOnly();
        assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues left join o.ctcTerm.categoryTermSets as categoryTerm inner join o.proCtcQuestions WHERE " +
        		" categoryTerm.category.ctc.name = :ctcName AND  categoryTerm.category.name not in ('EQ5D-5L', 'EQ5D-3L') ", query.getQueryString());
    }

    public void testFilterByCtcCategoryId() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcCategoryId(1);
		assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues left join o.ctcTerm.categoryTermSets as categoryTerm WHERE " +
				" categoryTerm.category.ctc.name = :ctcName AND categoryTerm.category.id = :ctcCategoryId AND  categoryTerm.category.name not in ('EQ5D-5L', 'EQ5D-3L') ",query.getQueryString());
    }

    public void testFilterByCtcTermId() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcTermId(1);
        assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues left join o.ctcTerm.categoryTermSets as categoryTerm WHERE" +
        		" o.ctcTerm.id = :ctcTermId AND  categoryTerm.category.ctc.name = :ctcName AND  categoryTerm.category.name not in ('EQ5D-5L', 'EQ5D-3L') ", query.getQueryString());
    }

    public void testFilterByTerm() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByTerm("test");

        assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues left join o.ctcTerm.categoryTermSets as categoryTerm WHERE " +
        		" categoryTerm.category.ctc.name = :ctcName AND lower(o.proCtcTermVocab.termEnglish) = :symptom AND  categoryTerm.category.name not in ('EQ5D-5L', 'EQ5D-3L') ", query.getQueryString());

    }

}