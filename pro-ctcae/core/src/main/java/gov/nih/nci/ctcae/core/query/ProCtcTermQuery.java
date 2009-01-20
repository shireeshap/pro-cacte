package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcTermQuery extends AbstractQuery {

    private static String queryString = "SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues order by o.id ";

    public ProCtcTermQuery() {

        super(queryString);
    }

    public void filterByCtcTermHavingQuestionsOnly() {
        innerJoin("o.proCtcQuestions");
    }
}
