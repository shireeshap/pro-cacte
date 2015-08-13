package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.QueryStrings;

import org.apache.commons.lang.StringUtils;

/**
 * The Class OrganizationQuery.
 *
 * @author Vinay Kumar
 * @since Oct 7, 2008
 */
public class OrganizationQuery extends SecuredQuery<Organization> {

    private static String ORGANIZATION_NAME = "name";
    private static String NCI_CODE = "nciInstituteCode";
    private static final String SHORT_TITLE = "shortTitle";
    private static final String LONG_TITLE = "longTitle";
    private static final String ASSIGNED_IDENTIFIER = "assignedIdentifier";

    /**
     * Instantiates a new organization query.
     */
    public OrganizationQuery() {
        this(QueryStrings.ORGANIZATION_QUERY_BASIC, true);
    }

    public OrganizationQuery(QueryStrings query, boolean secure) {
        super(query, secure);
    }
    
    /**
     * Filter by organization name.
     *
     * @param name the name
     */
    public void filterByOrganizationName(final String name) {
        String searchString = "%" + name.toLowerCase() + "%";
        andWhere("lower(o.name) LIKE :" + ORGANIZATION_NAME);
        setParameter(ORGANIZATION_NAME, searchString);
    }

    /**
     * Filter by organization name or nci institute code.
     *
     * @param text the text
     */
    public void filterByOrganizationNameOrNciInstituteCode(final String text) {
        String searchString = "%" + text.toLowerCase() + "%";
        andWhere(String.format("(lower(o.name) LIKE :%s or lower(o.nciInstituteCode) LIKE :%s)", ORGANIZATION_NAME, NCI_CODE));
        setParameter(ORGANIZATION_NAME, searchString);
        setParameter(NCI_CODE, searchString);
    }
    
    public void leftJoinStudy() {
    	leftJoin(" o.studyOrganizations as sso left outer join sso.study as study ");
    }
    
    /**
     * Where to filter duplicate sites.
     *
     * @param studyId the study id
     */
    public void whereToFilterDuplicateSites(String studyId) {
    	andWhere(" o.id not in ( select sso.organization.id from StudyOrganization sso where sso.study.id='"+studyId+"') ");
    	
    }
    
    public void filterStudySiteIfParticipantPresent(String studyId){
    	andWhere(" o.id not in ( select sso.organization.id from StudyOrganization sso where sso.study.id='"+studyId+"' and sso.class = 'SST' and sso.studyParticipantAssignments IS NOT EMPTY) ");
    }
    
    /**
     * Filter by nci institute code.
     *
     * @param nciInstituteCode the nci institute code
     */
    public void filterByNciInstituteCode(final String nciInstituteCode) {
        String searchString = "%" + nciInstituteCode.toLowerCase() + "%";
        andWhere("lower(o.nciInstituteCode) LIKE :" + NCI_CODE);
        setParameter(NCI_CODE, searchString);
    }

    /**
     * Filter by nci code exact match.
     *
     * @param nciCode the nci code
     */
    public void filterByNciCodeExactMatch(final String nciCode) {
        andWhere("o.nciInstituteCode = :" + NCI_CODE);
        setParameter(NCI_CODE, nciCode);
    }

    public Class<Organization> getPersistableClass() {
        return Organization.class;
    }

    protected String getObjectIdQueryString() {
        return "o.id";
    }
    
    public void filterByAll(String text, String key) {
        String searchString = text != null && StringUtils.isNotBlank(text) ? "%" + StringUtils.trim(StringUtils.lowerCase(text)) + "%" : null;
        andWhere(String.format("(lower(o.name) LIKE :%s " +
                "or lower(o.nciInstituteCode) LIKE :%s " +
                "or lower(study.assignedIdentifier) LIKE :%s " +
                "or lower(study.longTitle) LIKE :%s " +
                "or lower(study.shortTitle) LIKE :%s )", ORGANIZATION_NAME + key, NCI_CODE + key, ASSIGNED_IDENTIFIER + key, LONG_TITLE + key, SHORT_TITLE + key));
        setParameter(ORGANIZATION_NAME + key, searchString);
        setParameter(NCI_CODE + key, searchString);
        setParameter(ASSIGNED_IDENTIFIER + key, searchString);
        setParameter(SHORT_TITLE + key, searchString);
        setParameter(LONG_TITLE + key, searchString);
    }
}