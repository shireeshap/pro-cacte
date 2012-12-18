package gov.nih.nci.ctcae.core.domain;

import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;
import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;

/*
Author: Amey
*/

public enum  QueryStrings implements CodedEnum<String>{
	
	PARTICIPANT_QUERY_STRING("SELECT p from Participant p order by p.id"), 
	PARTICIPANT_QUERY_STRING1("SELECT count(distinct p) from Participant p"),
	PARTICIPANT_QUERY_STRING2("SELECT p from Participant p"),
	STUDY_QUERY_STRING("Select distinct study from Study study order by study.shortTitle "),
	STUDY_QUERY_STRING1("SELECT count(distinct study) from Study study "),
	STUDY_QUERY_STRING2("SELECT distinct study from Study study "),
	STUDY_QUERY_STRING3("SELECT study from Study study left outer join study.studyOrganizations as so "),
	CRF_QUERY_STRING("SELECT o from CRF o order by o.id "),
	CRF_QUERY_STRING1("SELECT count(distinct o) from CRF o"),
	CRF_QUERY_STRING2("SELECT o from CRF o "),
	STAFF_QUERY_STRING("SELECT distinct cs from ClinicalStaff cs order by cs.id "),
	STAFF_QUERY_STRING1("SELECT count(distinct cs) from ClinicalStaff cs "),
	STAFF_QUERY_STRING2("SELECT distinct cs from ClinicalStaff cs ");
	
	private String queryString;
	//private String queryString;
	
	QueryStrings(String queString){
		this.queryString=queString;		
	}

	@Override
	public String getCode() {
		return getDisplayName();
	}

	@Override
	public String getDisplayName() {
		return queryString;
	}
}