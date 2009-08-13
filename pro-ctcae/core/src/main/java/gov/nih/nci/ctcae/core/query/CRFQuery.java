package gov.nih.nci.ctcae.core.query;

import org.apache.commons.lang.StringUtils;

import java.util.List;

//
/**
 * The Class CRFQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CRFQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT o from CRF o order by o.id";

    /**
     * The Constant TITLE.
     */
    private static final String TITLE = "title";

    /**
     * The Constant CRFID.
     */
    private static final String CRFID = "crfId";

    /**
     * The Constant STUDYID.
     */
    private static final String STUDYID = "studyId";

    private static String CRF_IDS = "ids";

    /**
     * The Constant CRF_VERSION.
     */
    private static final String CRF_VERSION = "crfVersion";
//    private static final String NEXTVERSIONID = "nextVersionId";

    /**
     * Instantiates a new cRF query.
     */
    public CRFQuery() {

        super(queryString);
    }

    public void filterByCRFIds(List<Integer> crfIds) {

        andWhere("o.id in (:" + CRF_IDS + ")");
        setParameterList(CRF_IDS, crfIds);

    }


    /**
     * Instantiates a new cRF query.
     *
     * @param queryString the query string
     */
    public CRFQuery(String queryString) {
        super(queryString);
    }

    /**
     * Filter by title exact match.
     *
     * @param title the title
     */
    public void filterByTitleExactMatch(final String title) {
        if (!StringUtils.isBlank(title)) {
            andWhere("lower(o.title) = :" + TITLE);
            setParameter(TITLE, title.toLowerCase());
        }
    }

    /**
     * Filter by not having crf id.
     *
     * @param crfId the crf id
     */
    public void filterByNotHavingCrfId(final Integer crfId) {
        if (crfId != null) {
            andWhere("o.id != :" + CRFID);
            setParameter(CRFID, crfId);
        }
    }

    /**
     * Filter by study id.
     *
     * @param studyId the study id
     */
    public void filterByStudyId(final Integer studyId) {
        if (studyId != null) {
            andWhere("o.study.id = :" + STUDYID);
            setParameter(STUDYID, studyId);
        }

    }

    /**
     * Filter by null next version id.
     */
    public void filterByNullNextVersionId() {
        andWhere("o.childCrf.id is null");
    }

    /**
     * Filter by crf version.
     *
     * @param crfVersion the crf version
     */
    public void filterByCrfVersion(final String crfVersion) {
        andWhere("o.crfVersion =:" + CRF_VERSION);
        setParameter(CRF_VERSION, crfVersion);
    }

}


