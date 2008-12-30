package gov.nih.nci.ctcae.core.query;

import org.apache.commons.lang.StringUtils;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CRFQuery extends AbstractQuery {

	private static String queryString = "SELECT o from CRF o order by o.id";
	private static final String TITLE = "title";
	private static final String CRFID = "crfId";
    private static final String STUDYID = "studyId";

    public CRFQuery() {

		super(queryString);
	}

	public void filterByTitleExactMatch(final String title) {
		if (!StringUtils.isBlank(title)) {
			andWhere("lower(o.title) = :" + TITLE);
			setParameter(TITLE, title.toLowerCase());
		}
	}

	public void filterByNotHavingCrfId(final Integer crfId) {
		if (crfId != null) {
			andWhere("o.id != :" + CRFID);
			setParameter(CRFID, crfId);
		}
	}

    public void filterByStudyId(final Integer studyId) {
        if (studyId != null) {
            andWhere("o.studyCrf.study.id = :" + STUDYID);
            setParameter(STUDYID, studyId);
        }

    }
}
