package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author vinay Kumar
 * @crated Dec 15, 2008
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