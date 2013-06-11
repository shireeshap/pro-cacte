package gov.nih.nci.ctcae.core.query;

import java.util.List;

import org.apache.commons.lang.StringUtils;

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
    private static String queryString1 = "SELECT count(distinct o) from CRF o";
    private static String queryString2 = "SELECT distinct o from CRF o";

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

    private static String IS_HIDDEN = "hidden";
    private static final String SHORT_TITLE = "shortTitle";
    private static final String USERNAME = "username";
    /**
     * The Constant CRF_VERSION.
     */
    private static final String CRF_VERSION = "crfVersion";

    /**
     * Instantiates a new cRF query.
     */
    public CRFQuery() {

        super(queryString);
    }

    public CRFQuery(boolean count) {
        super(queryString1);
    }

    public CRFQuery(boolean sort, boolean count) {
        super(queryString2);
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
    public void filterByTest() {

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

    public void filterByHidden(final Boolean hidden) {
        andWhere("o.hidden =:" + IS_HIDDEN);
        setParameter(IS_HIDDEN, hidden);
    }

    public void filterByHiddenKey(final Boolean hidden, String key) {
        andWhere("o.hidden =:" + IS_HIDDEN+key);
        setParameter(IS_HIDDEN+key, hidden);
    }

    public void filterByAll(String text, String key) {
        String searchString = text != null && StringUtils.isNotBlank(text) ? "%" + StringUtils.trim(StringUtils.lowerCase(text)) + "%" : null;
        andWhere(String.format("(lower(o.title) LIKE :%s " +
        "or lower(o.study.shortTitle) LIKE :%s and o.childCrf.id is null)", TITLE+key, SHORT_TITLE+key));
        setParameter(TITLE+key, searchString);
        setParameter(SHORT_TITLE+key, searchString);
    }

    public void setLeftJoin() {
        leftJoin("o.study as study " +
                "left outer join study.studyOrganizations as so " +
                "left outer join so.studyOrganizationClinicalStaffs as socs " +
                "left outer join socs.organizationClinicalStaff as oc " +
                "left outer join oc.clinicalStaff as cs " +
                "left outer join cs.user as user");
    }

    public void filterByUsername(final String userName) {
        setLeftJoin();
        andWhere("user.username = :" + USERNAME);
        setParameter(USERNAME, userName);
    }

}


