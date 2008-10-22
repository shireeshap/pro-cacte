package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 22, 2008
 */
public class StudySiteQuery extends StudyOrganizationQuery
{

	private static String queryString = "SELECT o from StudySite o order by o.id";

	public StudySiteQuery() {
		super(queryString);
	}

}