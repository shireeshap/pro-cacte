package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.security.ApplicationSecurityManager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * The Class ParticipantQuery.
 *
 * @author mehul
 */

public class ParticipantQuery extends SecuredQuery<Organization> {
	
    /**
     * The FIRST name.
     */
    private static String FIRST_NAME = "firstName";

    /**
     * The LAST name.
     */
    private static String LAST_NAME = "lastName";

    /**
     * The IDENTIFIER.
     */
    private static String IDENTIFIER = "assignedIdentifier";

    /**
     * The Constant STUDY_ID.
     */
    private static final String STUDY_ID = "studyId";
    private static final String STUDY_SITE_ID = "studySiteId";
    private static final String ORGANIZATION_ID = "siteId";
    private static final String USERNAME = "username";
    private static final String STUDY_PARTICIPANT_IDENTIFIER = "studyParticipantIdentifier";
    private static String EMAIL = "emailAddress";
    private static String USERNUMBER = "userNumber";
    private static final String STUDY_SITE = "studySite";
    private static final String LEAD_SITE = "leadSite";
    private static final String SHORT_TITLE = "shortTitle";
    private static final String NAME = "name";
    
    private boolean isStudySiteLevel = false;

    /** Instantiates a new participant query.
     */
    public ParticipantQuery(boolean secure) {
        super(QueryStrings.PARTICIPANT_QUERY_STRING.getCode(), secure);
        if (secure) {
            leftJoinStudySites();
        }
    }

    public ParticipantQuery(boolean count, boolean secure) {
        super(QueryStrings.PARTICIPANT_QUERY_STRING1.getCode(), secure);
    }

    public ParticipantQuery(boolean sort, boolean count, boolean secure) {
        super(QueryStrings.PARTICIPANT_QUERY_STRING2.getCode(), secure);
        if (secure) {
            leftJoinStudySites();
        }
    }
    
    public ParticipantQuery(boolean count, Role role, boolean secure) {
    	super(QueryStrings.PARTICIPANT_QUERY_STRING1.getCode(), false);	
       
    	User currentLoggedInUser = ApplicationSecurityManager.getCurrentLoggedInUser();
        List<Integer> objectIds = new ArrayList<Integer>();
        if (secure) {
            leftJoinStudySites();
            if(role.equals(Role.LEAD_CRA) || role.equals(Role.PI)){
                objectIds = currentLoggedInUser.findAccessibleObjectIds(Study.class);

            } else if(role.equals(Role.SITE_CRA) || role.equals(Role.SITE_PI) || role.equals(Role.NURSE) || role.equals(Role.TREATING_PHYSICIAN)){
            	isStudySiteLevel = true;
                objectIds = currentLoggedInUser.findAccessibleObjectIds(StudyOrganization.class);
            }
        }
        filterByObjectIds(objectIds);
    }

    public ParticipantQuery(Role role, boolean secure, String sortField) {
        super(QueryStrings.PARTICIPANT_QUERY_STRING2.getCode(), false);
       
        User currentLoggedInUser = ApplicationSecurityManager.getCurrentLoggedInUser();
        List<Integer> objectIds = new ArrayList<Integer>();
        if (secure) {
        	if(sortField.compareToIgnoreCase("organizationName")==0){
        		leftJoinForSortBySite();
        	}else{        	
        		leftJoinStudySites();
        	}
            if(role.equals(Role.LEAD_CRA) || role.equals(Role.PI)){
                objectIds = currentLoggedInUser.findAccessibleObjectIds(Study.class);

            } else if(role.equals(Role.SITE_CRA) || role.equals(Role.SITE_PI) || role.equals(Role.NURSE) || role.equals(Role.TREATING_PHYSICIAN)){
            	isStudySiteLevel = true;
                objectIds = currentLoggedInUser.findAccessibleObjectIds(StudyOrganization.class);
            }
        }
        filterByObjectIds(objectIds);
    }

    /**
     * Filter by participant first name.
     *
     * @param firstName the first name
     */
    public void filterByParticipantFirstName(final String firstName) {
        String searchString = "%" + firstName.toLowerCase() + "%";
        andWhere("lower(p.firstName) LIKE :" + FIRST_NAME);
        setParameter(FIRST_NAME, searchString);
    }

    /**
     * Filter by participant last name.
     *
     * @param lastName the last name
     */
    public void filterByParticipantLastName(final String lastName) {
        String searchString = "%" + lastName.toLowerCase() + "%";
        andWhere("lower(p.lastName) LIKE :" + LAST_NAME);
        setParameter(LAST_NAME, searchString);
    }

    
    /**
     * Filter by participant identifier(also referred to as assigned identifier/MRN).
     *
     * @param identifier the identifier
     */
    public void filterByParticipantIdentifier(final String identifier) {
        String searchString = "%" + identifier.toLowerCase() + "%";
        andWhere("lower(p.assignedIdentifier) LIKE :" + IDENTIFIER);
        setParameter(IDENTIFIER, searchString);
    }
    

    /**
     * Filter by participant identifier exact match.
     *
     * @param identifier the identifier
     */
    public void filterByParticipantIdentifierExactMatch(final String identifier) {
        String searchString = identifier.toLowerCase();
        andWhere("lower(p.assignedIdentifier) = :" + IDENTIFIER);
        setParameter(IDENTIFIER, searchString);
    }




    /**
     * Filter participants with matching text.
     *
     * @param text the text
     */
    public void filterParticipantsWithMatchingText(String text) {

        if (text != null) {

            String firstSearchString = null;
            String secondSearchString = null;

            String[] searchStrings = StringUtils.split(text);

            if (searchStrings.length == 2) {
                firstSearchString = searchStrings[0];
                secondSearchString = searchStrings[1];
                andWhere(String.format("(( lower(p.firstName) = :%s AND lower(p.lastName) like :%s) or " +
                        "(lower(p.lastName) = :%s AND lower(p.firstName) like :%s)) ", "FIRST_TOKEN", "SECOND_TOKEN", "THIRD_TOKEN", "FOURTH_TOKEN"));
                setParameter("FIRST_TOKEN", StringUtils.lowerCase(firstSearchString));
                setParameter("SECOND_TOKEN", String.format("%s%s", StringUtils.lowerCase(secondSearchString), "%"));
                setParameter("THIRD_TOKEN", StringUtils.lowerCase(firstSearchString));
                setParameter("FOURTH_TOKEN", String.format("%s%s", StringUtils.lowerCase(secondSearchString), "%"));
                return;
            } else {

                String searchString = text != null && StringUtils.isNotBlank(text) ? "%" + StringUtils.trim(StringUtils.lowerCase(text)) + "%" : null;
                // String searchString = text != null && StringUtils.isNotBlank(text) ? "%" +StringUtils.trim(text) + "%" : null;

                andWhere(String.format("(lower(p.firstName) LIKE :%s " +
                        "or lower(p.lastName) LIKE :%s " +
                        "or lower(p.assignedIdentifier) LIKE :%s " +
                        "or lower(p.studyParticipantAssignments.studyParticipantIdentifier) LIKE :%s )", FIRST_NAME, LAST_NAME, IDENTIFIER, STUDY_PARTICIPANT_IDENTIFIER));
                setParameter(IDENTIFIER, searchString);
                setParameter(FIRST_NAME, searchString);
                setParameter(LAST_NAME, searchString);
                setParameter(STUDY_PARTICIPANT_IDENTIFIER, searchString);
            }
        }
    }

    public void setLeftJoin() {
        leftJoin("p.studyParticipantAssignments as spa join spa.studySite as ss join ss.study as study");
        leftJoin("p.studyParticipantAssignments as spa join spa.studySite as ss ");

    }
    
    public void filterByAll(String text, String key) {
        String searchString = text != null && StringUtils.isNotBlank(text) ? "%" + StringUtils.trim(StringUtils.lowerCase(text)) + "%" : null;

        andWhere(String.format("(lower(p.firstName) LIKE :%s " +
                "or lower(p.lastName) LIKE :%s " +
                "or lower(p.assignedIdentifier) LIKE :%s " +
                "or lower(p.studyParticipantAssignments.studyParticipantIdentifier) LIKE :%s " +
                "or lower(study.shortTitle) LIKE :%s " +
                "or lower(ss.organization.name) LIKE :%s )", FIRST_NAME+key, LAST_NAME+key, IDENTIFIER+key, STUDY_PARTICIPANT_IDENTIFIER+key, SHORT_TITLE+key, NAME+key));
        setParameter(IDENTIFIER+key, searchString);
        setParameter(FIRST_NAME+key, searchString);
        setParameter(LAST_NAME+key, searchString);
        setParameter(STUDY_PARTICIPANT_IDENTIFIER+key, searchString);
        setParameter(SHORT_TITLE+key, searchString);
        setParameter(NAME+key, searchString);
    }

    /**
     * Filter by study.
     *
     * @param studyId the study id
     */
    public void filterByStudy(Integer studyId) {
        if (studyId != null) {

            leftJoin("p.studyParticipantAssignments as spa join spa.studySite as ss join ss.study as study");
            andWhere("study.id =:" + STUDY_ID);
            setParameter(STUDY_ID, studyId);
        }
    }

    public void filterByStudySite(Integer studySiteId) {
        if (studySiteId != null) {
            leftJoin("p.studyParticipantAssignments as spa join spa.studySite as ss ");
            andWhere("ss.id =:" + STUDY_SITE_ID);
            setParameter(STUDY_SITE_ID, studySiteId);
        }
    }

    public void filterBySite(Integer siteId) {
        if (siteId != null) {
            leftJoinStudySites();
            andWhere("ss.organization.id = :" + ORGANIZATION_ID);
            andWhere(String.format("(ss.class = :%s or ss.class = :%s )", STUDY_SITE, LEAD_SITE));
            setParameter(ORGANIZATION_ID, siteId);
            setParameter(STUDY_SITE, "SST");
            setParameter(LEAD_SITE, "LSS");
        }
    }

    public void leftJoinStudySites() {
        leftJoin("p.studyParticipantAssignments as spa join spa.studySite as ss join ss.study as study ");
    }

    public void leftJoinForSortBySite(){
    	leftJoin(" p.studyParticipantAssignments as spa left join spa.studySite as ss ");   	
    }    
    
    public void filterByUsername(String username) {
        if (username != null) {
            andWhere("p.user.username =:" + USERNAME);
            setParameter(USERNAME, username.toLowerCase());
        }
    }

    public void filterByStudyParticipantIdentifier(String spIdentifier) {
        if (spIdentifier != null) {
            leftJoin("p.studyParticipantAssignments as spa");
            andWhere("lower(spa.studyParticipantIdentifier) LIKE :" + STUDY_PARTICIPANT_IDENTIFIER);
            setParameter(STUDY_PARTICIPANT_IDENTIFIER, spIdentifier.toLowerCase());
        }
    }

    public void filterByStudyParticipantIdentifierExactMatch(String spIdentifier) {
        if (spIdentifier != null) {
            leftJoin("p.studyParticipantAssignments as spa");
            andWhere("lower(spa.studyParticipantIdentifier) =:" + STUDY_PARTICIPANT_IDENTIFIER);
            setParameter(STUDY_PARTICIPANT_IDENTIFIER, spIdentifier.toLowerCase());
        }
    }

    /**
     * Filter by study.
     * <p/>
     * //     * @param studyParticipantIdentifier the study Participant Identifier
     */
    public void filterByStudyParticipantAssignmentId(Integer studyParticipantAssignmentId) {
        if (studyParticipantAssignmentId != null) {
            leftJoin("p.studyParticipantAssignments as spa");
            andWhere("spa.id = :id");
            setParameter("id", studyParticipantAssignmentId);
        }
    }

    /**
     * Filter by participant email address.
     *
     * @param email the email
     */
    public void filterByEmail(final String email) {
        String searchString = email.toLowerCase();
        andWhere("lower(p.emailAddress) LIKE :" + EMAIL);
        setParameter(EMAIL, searchString);
    }

    /**
     * Filter by participant user number.
     *
     * @param userNumber the userNumber
     */
    public void filterByUserNumber(final String userNumber) {
        if (userNumber != null) {
            andWhere("p.userNumber =:" + USERNUMBER);
            setParameter(USERNUMBER, userNumber);
        }
    }

    /**
     * Filter by participant phone number.
     *
     * @param phoneNumber the userNumber
     */
    public void filterByPhoneNumber(final String phoneNumber) {
        if (phoneNumber != null) {
            String plainPhoneNumber = phoneNumber.replaceAll("-", "");
            String phoneString = "('"+phoneNumber + "','"+ plainPhoneNumber + "')";
            andWhere("p.phoneNumber IN " + phoneString);
         //   setParameter(PHONENUMBER, phoneString);
        }
    }

    /**
     * Filter by excludes existing participant .
     *
     * @param participantId the Db identifier
     */
    public void excludeByParticipantId(final Integer participantId) {
        if (participantId != null) {
            andWhere("p.id <> :id");
            setParameter("id", participantId);
        }
    }

    public void setLeftJoinForUserName() {
        leftJoin("p.studyParticipantAssignments as spa " +
                "left outer join spa.studySite as ss join ss.study as study " +
                "left outer join ss.study as study " +
                "left outer join study.studyOrganizations as so " +
                "left outer join so.studyOrganizationClinicalStaffs as socs " +
                "left outer join socs.organizationClinicalStaff as oc " +
                "left outer join oc.clinicalStaff as cs " +
                "left outer join cs.user as user");
        }

    public void filterByStaffUsername(final String userName) {
        setLeftJoinForUserName();
        andWhere("user.username = :" + USERNAME);
        setParameter(USERNAME, userName);
    }

    public Class<Organization> getPersistableClass() {
        return Organization.class;
    }

    protected String getObjectIdQueryString() {
    	if(isStudySiteLevel){
    		return "spa.studySite.id";
    	}
        return "ss.study.id";
    }


}
