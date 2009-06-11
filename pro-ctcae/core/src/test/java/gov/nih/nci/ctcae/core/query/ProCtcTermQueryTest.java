package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import junit.framework.TestCase;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

/**
 * @author Vinay Kumar
 */

public class ProCtcTermQueryTest extends TestCase {

    public void testFilterByCtcTermHavingQuestionsOnly() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcTermHavingQuestionsOnly();
        assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues inner join o.proCtcQuestions order by o.id", query.getQueryString());
    }

    public void testFilterByCtcCategoryId() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcCategoryId(1);
        assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues WHERE o.ctcTerm.category.id = :ctcCategoryId order by o.id", query.getQueryString());
    }

    public void testFilterByCtcTermId() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByCtcTermId(1);
        assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues WHERE o.ctcTerm.id = :ctcTermId order by o.id", query.getQueryString());
    }

    public void testFilterByTerm() {
        ProCtcTermQuery query = new ProCtcTermQuery();
        query.filterByTerm("test");
        assertEquals("SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues WHERE lower(o.term) = :symptom order by o.id", query.getQueryString());

    }

}