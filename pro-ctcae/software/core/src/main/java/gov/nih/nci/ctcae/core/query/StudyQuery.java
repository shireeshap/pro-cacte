package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;

import org.apache.commons.lang.StringUtils;

/**
 * The Class StudyQuery.
 *
 * @author Vinay Kumar
 * @since Oct 14, 2008
 */
public class StudyQuery extends SecuredQuery<Study> {

    private static final String SHORT_TITLE = "shortTitle";
    private static final String LONG_TITLE = "longTitle";
    private static final String ASSIGNED_IDENTIFIER = "assignedIdentifier";
    private static final String PARTICIPANT_ID = "participantId";
    private static final String ROLE = "role";
    private static final String ORGANIZATION_ID = "organizationId";
    private static final String LEAD_SITE = "leadSite";
    private static final String DCC_SITE = "dataCordinatingCenter";
    private static final String FSP_SITE = "fundingSponsorer";
    private static final String SSP_SITE = "studySponsorer";
    private static final String USERNAME = "username";
    private static final String STUDY_SITE_NAME = "studySiteName";

    /**
     * TODO: Horrible overloading. Fix ASAP.
     * Instantiates a new study query. 
     */
    public StudyQuery() {
        super(QueryStrings.STUDY_QUERY_BASIC);
    }
    
    public StudyQuery(QueryStrings query, boolean secure) {
        super(query, secure);
    }
    
    /**
     * Filter studies with matching text.
     *
     * @param text the text
     */
    public void filterStudiesWithMatchingText(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("(lower(study.shortTitle) LIKE :%s or lower(study.longTitle) LIKE :%s or lower(study.assignedIdentifier) LIKE :%s)"
                , SHORT_TITLE, LONG_TITLE, ASSIGNED_IDENTIFIER));
        setParameter(SHORT_TITLE, searchString);
        setParameter(LONG_TITLE, searchString);
        setParameter(ASSIGNED_IDENTIFIER, searchString);

    }

    /**
     * Filter studies by long title.
     *
     * @param text the text
     */
    public void filterStudiesByLongTitle(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("lower(study.longTitle) LIKE :%s)", LONG_TITLE));
        setParameter(LONG_TITLE, searchString);

    }

    /**
     * Filter studies by assigned identifier.
     *
     * @param text the text
     */
    public void filterStudiesByAssignedIdentifier(String text) {
        String searchString = text != null ? text.toLowerCase() : null;
        searchString = "%"+ searchString + "%";
        andWhere(String.format("lower(study.assignedIdentifier) LIKE :%s)", ASSIGNED_IDENTIFIER));
        setParameter(ASSIGNED_IDENTIFIER, searchString);

    }

    /**
     * Filter by assigned identifier exact match.
     *
     * @param assignedIdentifier the assigned identifier
     */
    public void filterByAssignedIdentifierExactMatch(final String assignedIdentifier) {
        if (!StringUtils.isBlank(assignedIdentifier)) {
            andWhere("lower(study.assignedIdentifier) = :" + ASSIGNED_IDENTIFIER);
            setParameter(ASSIGNED_IDENTIFIER, assignedIdentifier.toLowerCase());
        }
    }

    /**
     * Filter studies by short title.
     *
     * @param text the text
     */
    public void filterStudiesByShortTitle(String text) {
        String searchString = text != null ? text.toLowerCase() : null;
        searchString = "%"+ searchString + "%";

        andWhere(String.format("lower(study.shortTitle) LIKE :%s)", SHORT_TITLE));
        setParameter(SHORT_TITLE, searchString);

    }


    /**
     * Filter studies for study site.
     *
     * @param siteId the site id
     */
    public void filterStudiesForStudySite(Integer siteId) {
        if (siteId != null) {
            leftJoin("study.studyOrganizations as sso");
            andWhere("sso.organization.id = :" + ORGANIZATION_ID);
            andWhere(String.format("(sso.class = :%s or sso.class = :%s or sso.class = :%s or sso.class = :%s )" ,
            		DCC_SITE, LEAD_SITE, FSP_SITE, SSP_SITE));
            setParameter(ORGANIZATION_ID, siteId);
            setParameter(DCC_SITE, "DCC");
            setParameter(LEAD_SITE, "LSS");
            setParameter(FSP_SITE, "FSP");
            setParameter(SSP_SITE, "SSP");
        }
    }

    
    public void setLeftJoin(){
        leftJoin("study.studyOrganizations as sso");
    }

    public void setLeftJoinForUserName() {
        leftJoin("study.studyOrganizations as so " +
                "left outer join sso.studyOrganizationClinicalStaffs as socs " +
                "left outer join socs.organizationClinicalStaff as oc " +
                "left outer join oc.clinicalStaff as cs " +
                "left outer join cs.user as user");
    }
    
    public void filterStudiesByUserAndRole(User user, Role role){
        leftJoin("study.studyOrganizations as ss join ss.studyOrganizationClinicalStaffs as socs");
        andWhere("socs.role =:" + ROLE + " and socs.organizationClinicalStaff.clinicalStaff.user.id =:" + PARTICIPANT_ID);
        setParameter(PARTICIPANT_ID, user.getId());
        setParameter(ROLE, role);
    }

    public void filterByUsername(final String userName) {
        setLeftJoinForUserName();
        andWhere("user.username = :" + USERNAME);
        setParameter(USERNAME, userName);
    }
    
    public void filterByFundingSponsor(){
    	andWhere("sso.class='FSP'");
    }

    public void filterByCoordinatingCenter(){
    	andWhere("sso.class='DCC'");
    }

    /**
     * Filter by participant.
     *
     * @param participantId the participant id
     */
    public void filterByParticipant(Integer participantId) {
        leftJoin("study.studyOrganizations as ss join ss.studyParticipantAssignments as spa join spa.participant as p");
        andWhere("p.id =:" + PARTICIPANT_ID);
        setParameter(PARTICIPANT_ID, participantId);
    }

    public void filterByAll(String text, String key, Boolean filterOnlyByStudySites){

        setLeftJoin();
        String searchString = StringUtils.isBlank(text) ? "%" : "%" + text.toLowerCase() + "%";

        String format = "(lower(study.shortTitle) LIKE :%s " +
                "or lower(study.assignedIdentifier) LIKE :%s " +
                "or lower(sso.organization.name) like :%s) "
                + (filterOnlyByStudySites ? "and sso.class NOT IN ('SSP', 'FSP', 'DCC')" : "");
        andWhere(String.format(format
                        , SHORT_TITLE + key, ASSIGNED_IDENTIFIER + key, STUDY_SITE_NAME + key)

        );

        setParameter(SHORT_TITLE+key, searchString);
        setParameter(ASSIGNED_IDENTIFIER+key, searchString);
        setParameter(STUDY_SITE_NAME+key, searchString);
    }

    public Class<Study> getPersistableClass() {
        return Study.class;
    }

    protected String getObjectIdQueryString() {
        return "study.id";
    }
}
