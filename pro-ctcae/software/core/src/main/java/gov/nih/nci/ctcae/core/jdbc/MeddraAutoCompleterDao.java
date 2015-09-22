package gov.nih.nci.ctcae.core.jdbc;

import gov.nih.nci.ctcae.core.jdbc.support.MatchingMeddraTermsCallBackHandler;
import gov.nih.nci.ctcae.core.jdbc.support.MeddraAutoCompleterWrapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

public class MeddraAutoCompleterDao {
    JdbcTemplate jdbcTemplate;
    private final static String ENGLISH = "en";

    @SuppressWarnings("unchecked")
    public List<MeddraAutoCompleterWrapper> getMatchingMeddraTerms(String searchString, String language, Integer maxDistance, Integer soundexRank) {
        Boolean includeFuzzy = maxDistance > 3;
        String fetchMatchingMeddraTerms;
        if (ENGLISH.equals(language)) {
            fetchMatchingMeddraTerms = "\n" +
                    "SELECT mlltv.meddra_term_english " +
                    ",  difference(lower(mlltv.meddra_term_english), '" + searchString + "') as soundex_rank, levenshtein(lower(mlltv.meddra_term_english), '" + searchString + "') as distance\n" +
                    " FROM meddra_llt_vocab mlltv \n" +
                    " inner join meddra_llt mllt on mllt.id = mlltv.meddra_llt_id \n" +
                    "where ( \n" +
                    " lower( mlltv.meddra_term_english ) ilike '%" + searchString + "%' \n" +
                    (includeFuzzy ? " or difference(lower(mlltv.meddra_term_english), '" + searchString + "') >= " + soundexRank + " " : "") +
                    (includeFuzzy ? " or ( length(meddra_term_english) <256  AND levenshtein(lower(mlltv.meddra_term_english), '" + searchString + "') <= " + maxDistance + " ) " : "") +
                    ")\n" +
                    " AND mllt.currency = 'Y' \n" +

                    "UNION\n" +

                    "SELECT ctcv.term_english as meddra_term_english" +
                    ", difference(lower(ctcv.term_english), '" + searchString + "') as soundex_rank, levenshtein(lower(ctcv.term_english), '" + searchString + "') as distance\n" +
                    " FROM ctc_terms_vocab ctcv \n" +
                    "where \n" +
                    " lower(ctcv.term_english) ilike '%" + searchString + "%' \n" +
                    (includeFuzzy ? " or difference(lower(ctcv.term_english), '" + searchString + "') >= " + soundexRank + " " : "") +
                    (includeFuzzy ? " or ( length(ctcv.term_english) <256  AND levenshtein(lower(ctcv.term_english), '" + searchString + "') <= " + maxDistance + " )" : "") +

                    "UNION\n" +

                    "SELECT ptctv.term_english  as meddra_term_english" +
                    ", difference(lower(ptctv.term_english), '" + searchString + "') as soundex_rank, levenshtein(lower(ptctv.term_english), '" + searchString + "') as distance\n" +
                    "FROM PRO_CTC_TERMS_VOCAB ptctv \n" +
                    " left join pro_ctc_terms pctc on pctc.id = ptctv.pro_ctc_terms_id\n" +
                    " left join ctc_terms ctc on pctc.ctc_term_id = ctc.id  \n" +
                    " left join CTC_CATEGORIES ctct on ctc.category_id = ctct.id\n" +
                    " left join CATEGORY_TERM_SET categoryTerm on categoryTerm.category_id = ctct.id\n" +
                    "where ( \n" +
                    " lower(ptctv.term_english) ilike '%" + searchString + "%' \n" +
                    (includeFuzzy ? " or difference(lower(ptctv.term_english), '" + searchString + "') >= " + soundexRank + " " : "") +
                    (includeFuzzy ? " or ( length(term_english) <256  AND levenshtein(lower(ptctv.term_english), '" + searchString + "') <= " + maxDistance + " )" : "") +
                    " ) " +
//                " --and ctct.name = :categoryName     \n" +
                    "order by distance asc, soundex_rank desc ;";

        } else {
            fetchMatchingMeddraTerms = "\n" +
                    "SELECT mlltv.meddra_term_spanish " +
                    ",  difference(lower(mlltv.meddra_term_spanish), '" + searchString + "') as soundex_rank, levenshtein(lower(mlltv.meddra_term_spanish), '" + searchString + "') as distance\n" +
                    " FROM meddra_llt_vocab mlltv \n" +
                    " inner join meddra_llt mllt on mllt.id = mlltv.meddra_llt_id \n" +
                    "where ( \n" +
                    " lower( mlltv.meddra_term_spanish ) ilike '%" + searchString + "%' \n" +
                    (includeFuzzy ? " or difference(lower(mlltv.meddra_term_spanish), '" + searchString + "') >= " + soundexRank + " " : "") +
                    (includeFuzzy ? " or ( length(meddra_term_spanish) <256  AND levenshtein(lower(mlltv.meddra_term_spanish), '" + searchString + "') <= " + maxDistance + " ) " : "") +
                    ")\n" +
                    " AND mllt.participant_added IS NOT TRUE\n" +

                    "UNION\n" +

                    "SELECT ctcv.term_spanish as meddra_term_spanish" +
                    ", difference(lower(ctcv.term_spanish), '" + searchString + "') as soundex_rank, levenshtein(lower(ctcv.term_spanish), '" + searchString + "') as distance\n" +
                    " FROM ctc_terms_vocab ctcv \n" +
                    "where \n" +
                    " lower(ctcv.term_spanish) ilike '%" + searchString + "%' \n" +
                    (includeFuzzy ? " or difference(lower(ctcv.term_spanish), '" + searchString + "') >= " + soundexRank + " " : "") +
                    (includeFuzzy ? " or ( length(ctcv.term_spanish) <256  AND levenshtein(lower(ctcv.term_spanish), '" + searchString + "') <= " + maxDistance + " )" : "") +

                    "UNION\n" +

                    "SELECT ptctv.term_spanish  as meddra_term_spanish" +
                    ", difference(lower(ptctv.term_spanish), '" + searchString + "') as soundex_rank, levenshtein(lower(ptctv.term_spanish), '" + searchString + "') as distance\n" +
                    "FROM PRO_CTC_TERMS_VOCAB ptctv \n" +
                    " left join pro_ctc_terms pctc on pctc.id = ptctv.pro_ctc_terms_id\n" +
                    " left join ctc_terms ctc on pctc.ctc_term_id = ctc.id  \n" +
                    " left join CTC_CATEGORIES ctct on ctc.category_id = ctct.id\n" +
                    " left join CATEGORY_TERM_SET categoryTerm on categoryTerm.category_id = ctct.id\n" +
                    "where ( \n" +
                    " lower(ptctv.term_spanish) ilike '%" + searchString + "%' \n" +
                    (includeFuzzy ? " or difference(lower(ptctv.term_spanish), '" + searchString + "') >= " + soundexRank + " " : "") +
                    (includeFuzzy ? " or ( length(term_spanish) <256  AND levenshtein(lower(ptctv.term_spanish), '" + searchString + "') <= " + maxDistance + " )" : "") +
                    " ) " +
//                " --and ctct.name = :categoryName     \n" +
                    "order by distance asc, soundex_rank desc ;";
        }

        List<MeddraAutoCompleterWrapper> result = new ArrayList<MeddraAutoCompleterWrapper>();
        jdbcTemplate.query(fetchMatchingMeddraTerms, new MatchingMeddraTermsCallBackHandler(language, result));
        return result;
    }

    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}