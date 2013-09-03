package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

public class ProctcaeGradeMappingVersionQueryTest extends TestCase{
	
	private static String VERSION = "version";
	private static String VERSION_NUMBER = "v1.0";
	
	public void testQueryConstructor(){
		ProctcaeGradeMappingVersionQuery query = new ProctcaeGradeMappingVersionQuery();
		assertEquals(query.getQueryString(), "select gmv from ProctcaeGradeMappingVersion gmv");
	}
	
	public void testFilterByVersion(){
		ProctcaeGradeMappingVersionQuery query = new ProctcaeGradeMappingVersionQuery();
		query.filterByVersion(VERSION_NUMBER);
		assertEquals(query.getQueryString(), "select gmv from ProctcaeGradeMappingVersion gmv WHERE gmv.version := version");
		assertEquals(query.getParameterMap().get(VERSION), VERSION_NUMBER);
	}
}
