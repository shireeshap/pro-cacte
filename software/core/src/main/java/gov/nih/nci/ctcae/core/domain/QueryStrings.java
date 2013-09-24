package gov.nih.nci.ctcae.core.domain;

import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;
import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;

/*
Author: Amey
*/

public enum  QueryStrings implements CodedEnum<String>{
	
	PARTICIPANT_QUERY_BASIC("SELECT p from Participant p order by p.id"), 
	PARTICIPANT_QUERY_COUNT("SELECT count(distinct p) from Participant p"),
	PARTICIPANT_QUERY_WITH_JOINS("SELECT p from Participant p"),
	
	STUDY_QUERY_BASIC("Select distinct study from Study study order by study.shortTitle "),
	STUDY_QUERY_COUNT("SELECT count(distinct study) from Study study "),
	STUDY_QUERY_SORTBY_FIELDS("SELECT distinct study from Study study "),
	STUDY_QUERY_SORTBY_FSP_DCC("SELECT study from Study study left outer join study.studyOrganizations as so "),
	
	CRF_QUERY_BASIC("SELECT o from CRF o order by o.id "),
	CRF_QUERY_COUNT("SELECT count(distinct o) from CRF o"),
	CRF_QUERY_SORTBY_FIELDS("SELECT o from CRF o "),
	
	STAFF_QUERY_BASIC("SELECT distinct cs from ClinicalStaff cs order by cs.id "),
	STAFF_QUERY_COUNT("SELECT count(distinct cs) from ClinicalStaff cs "),
	STAFF_QUERY_SORTBY_FIELDS("SELECT distinct cs from ClinicalStaff cs "),
    
	USER_QUERY_BASIC("SELECT user from User user order by user.id"),
    UN_QUERY_BASIC("SELECT un from UserNotification un"),
    UN_QUERY_COUNT("SELECT count(distinct un) from UserNotification un"),
    
    SOCS_QUERY_COUNT("SELECT count(*) from StudyOrganizationClinicalStaff socs"),
    SOCS_QUERY_BASIC("SELECT socs from StudyOrganizationClinicalStaff socs order by socs.organizationClinicalStaff.clinicalStaff.firstName"),
   
    OCS_QUERY_BASIC("SELECT scs from OrganizationClinicalStaff scs order by scs.clinicalStaff.firstName"),
    
    ORGANIZATION_QUERY_BASIC("SELECT distinct(o) from Organization o order by o.id "),
	ORGANIZATION_QUERY_FILTER_STUDYSITES("SELECT distinct(o) from Organization o "),
	
	SO_QUERY_BASIC("SELECT o from StudyOrganization o order by o.organization.name ");
	
	
	
	private String queryString;
	
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