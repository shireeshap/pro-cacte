package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudyOrganizationQuery extends AbstractQuery {

	private static String queryString = "SELECT o from StudyOrganization o order by o.id";

	public StudyOrganizationQuery() {

		super(queryString);
	}
	public StudyOrganizationQuery(String queryString) {

		super(queryString);
	}
}