package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author vinay Kumar
 * @since Dec 15, 2008
 */
public class AbstractQueryTest extends TestCase {

	public void testQueryConstructor() throws Exception {
		AbstractQuery query = new TestQuery();
		assertEquals("wrong parsing for constructor",
			"SELECT distinct(o) from Organization o order by o.id", query
				.getQueryString());

	}

	public void testLeftJoinFeth() throws Exception {
		TestQuery query = new TestQuery();
		query.leftJoinFeth();
		assertEquals(
			"SELECT distinct(o) from Organization o left join fetch Study order by o.id",
			query.getQueryString());


	}

	public void testJoin() throws Exception {
		TestQuery query = new TestQuery();
		query.Join();
		assertEquals(
			"SELECT distinct(o) from Organization o join Participant order by o.id",
			query.getQueryString());


	}

	public void testJoinMultipleTimes() throws Exception {
		TestQuery query = new TestQuery();
		query.Join();
		query.Join();
		assertEquals(
			"SELECT distinct(o) from Organization o join Participant order by o.id",
			query.getQueryString());


	}

	public void testGetMaximumResults() throws Exception {
		TestQuery query = new TestQuery();
		query.setMaximumResults(30);
		assertEquals(Integer.valueOf(30), query.getMaximumResults());


	}


	private class TestQuery extends AbstractQuery {


		public TestQuery() {

			super("SELECT distinct(o) from Organization o order by o.id");
		}

		public void leftJoinFeth() {
			leftJoinFetch("Study");

		}

		public void Join() {
			join("Participant");

		}
	}

}