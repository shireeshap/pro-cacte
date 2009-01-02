package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CRFSelfQuery extends CRFQuery {

	private static String queryString = "select o from CRF parentCrf,CRF childCrf where childCrf.parentVersionId=parentCrf.id";
	private static final String PARENT_CRF_ID = "parentCrfId";


	public CRFSelfQuery() {

		super(queryString);
	}

	public void filterByNotHavingCrfId(final Integer crfId) {
		if (crfId != null) {
			andWhere("o.id = :" + PARENT_CRF_ID);
			setParameter(PARENT_CRF_ID, crfId);
		}
	}

}