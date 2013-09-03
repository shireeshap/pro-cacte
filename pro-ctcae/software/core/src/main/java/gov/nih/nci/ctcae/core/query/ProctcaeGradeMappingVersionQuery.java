package gov.nih.nci.ctcae.core.query;


public class ProctcaeGradeMappingVersionQuery extends AbstractQuery{
	private static String queryString = "select gmv from GradeMappingVersion gmv ";
	private static final String VERSION = "version";
	private static String DEFAULT_VERSION = "v1.0";

	public ProctcaeGradeMappingVersionQuery() {
		super(queryString);
	}
	
	public void filterByVersion(String version){
		andWhere( "gmv.version := " + VERSION);
		setParameter(VERSION, DEFAULT_VERSION);
	}
}
