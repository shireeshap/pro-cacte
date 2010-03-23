package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 */

public class ProCtcTermQueryTest extends TestCase {

    public void testFilterByCtcTermHavingQuestionsOnly() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcTermHavingQuestionsOnly();
        assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues inner join o.proCtcQuestions WHERE o.ctcTerm.category.ctc.name = :ctcName order by o.term", query.getQueryString());
    }

    public void testFilterByCtcCategoryId() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcCategoryId(1);
        assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues WHERE o.ctcTerm.category.id = :ctcCategoryId AND o.ctcTerm.category.ctc.name = :ctcName order by o.term", query.getQueryString());
    }

    public void testFilterByCtcTermId() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcTermId(1);
        assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues WHERE o.ctcTerm.category.ctc.name = :ctcName AND o.ctcTerm.id = :ctcTermId order by o.term", query.getQueryString());
    }

    public void testFilterByTerm() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByTerm("test");
        assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues WHERE o.ctcTerm.category.ctc.name = :ctcName AND lower(o.term) = :symptom order by o.term", query.getQueryString());

    }

}