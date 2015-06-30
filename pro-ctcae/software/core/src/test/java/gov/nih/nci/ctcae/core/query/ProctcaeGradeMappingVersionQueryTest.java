package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

public class ProctcaeGradeMappingVersionQueryTest extends TestCase{
	
	private static String VERSION = "version";
	private static String VERSION_NUMBER = "v4.0";
	
	public void testQueryConstructor(){
		ProctcaeGradeMappingVersionQuery query = new ProctcaeGradeMappingVersionQuery();
		assertEquals(query.getQueryString(), "select gmv from ProctcaeGradeMappingVersion gmv");
	}
	
	public void testFilterByVersion(){
		ProctcaeGradeMappingVersionQuery query = new ProctcaeGradeMappingVersionQuery();
		query.filterByDefaultVersion();
		assertEquals("select gmv from ProctcaeGradeMappingVersion gmv WHERE lower(gmv.version) = :version", query.getQueryString());
		assertEquals(query.getParameterMap().get(VERSION), VERSION_NUMBER);
	}
}
