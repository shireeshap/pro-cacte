package gov.nih.nci.ctcae.core.query;


public class ProctcaeGradeMappingVersionQuery extends AbstractQuery{
	private static String queryString = "select gmv from ProctcaeGradeMappingVersion gmv ";
	private static final String VERSION = "version";
    /* Note: Please do update the references present in StudyParticipantCrfGradesCreator.java
	 * Current release is using v1.0
	 * Next release will use v4.0
	 */ 
	public static String VERSION_NUMBER = "v4.0";

	public ProctcaeGradeMappingVersionQuery() {
		super(queryString);
	}
	
	public void filterByVersion(String version){
		andWhere( "gmv.version = :" + VERSION);
		setParameter(VERSION, version);
	}
	
	public void filterByDefaultVersion(){
		andWhere( "lower(gmv.version) = :" + VERSION);
		setParameter(VERSION, VERSION_NUMBER);
	}
}
