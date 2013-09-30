package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * User: Mehul Gulati
 * Date: Oct 15, 2008.
 */
public class ClinicalStaffQuery extends AbstractQuery {

    private static String ORGANIZATION_ID = "organizationId";
    private static String FIRST_NAME = "firstName";
    private static String LAST_NAME = "lastName";
    private static String USER_ID = "userId";
    private static String EMAIL = "emailAddress";
    private static String NCI_IDENTIFIER = "nciIdentifier";
    private static String STUDY_ASSIGNED_IDENTIFIER = "assignedIdentifier";
    private static final String NAME = "name";
    private static final String SHORT_TITLE = "shortTitle";
    private static final String NCI_INSTITUTIONAL_CODE = "nciInstituteCode";


    /**
     * Instantiates a new clinical staff query.
     */
    public ClinicalStaffQuery() {
        super(QueryStrings.STAFF_QUERY_BASIC);
        filterByActive();
    }

    public ClinicalStaffQuery(QueryStrings query, boolean showInactive) {
        super(query);
        if (!showInactive) {
            filterByActive();
        }
    }
    
    /**
     * Sorting by study site name name. But this feature of sorting by study site 
       is not made available as the join produces duplicate results. 
     */
    public void leftJoinForsortBySite(){
    	leftJoin("cs.organizationClinicalStaffs ocs ");
    }
    
    /**
     * Sorting by study name name. But this feature of sorting by study 
       is not made available as the join produces duplicate results. 
     */
    public void leftJoinForsortByStudy(){
    	leftJoin("cs.organizationClinicalStaffs ocs left join ocs.studyOrganizationClinicalStaff socs left join  socs.studyOrganization so ");
    }
    
     
    /**
     * Filter by clinical staff first name.
     *
     * @param firstName the first name
     */
    public void filterByClinicalStaffFirstName(final String firstName) {
        String searchString = "%" + firstName.toLowerCase() + "%";
        andWhere("lower(cs.firstName) LIKE :" + FIRST_NAME);
        setParameter(FIRST_NAME, searchString);
    }

    /**
     * Filter by clinical staff last name.
     *
     * @param lastName the last name
     */
    public void filterByClinicalStaffLastName(final String lastName) {
        String searchString = "%" + lastName.toLowerCase() + "%";
        andWhere("lower(cs.lastName) LIKE :" + LAST_NAME);
        setParameter(LAST_NAME, searchString);
    }

    public void filterByEmail(final String email) {
        String searchString = email.toLowerCase();
        andWhere("lower(cs.emailAddress) LIKE :" + EMAIL);
        setParameter(EMAIL, searchString);
    }

    /**
     * Filter by excludes existing Staff .
     *
     * @param staffId the Db identifier
     */
    public void excludeByStaffId(final Integer staffId) {
        if (staffId != null) {
            andWhere("cs.id <> :id");
            setParameter("id", staffId);
        }
    }

    public void filterByUserId(final Integer userId) {
        andWhere("cs.user.id =:" + USER_ID);
        setParameter(USER_ID, userId);
    }

    public void filterByUserName(final String userName) {
        andWhere("cs.user.username =:" + "username");
        setParameter("username", userName);
    }

    /**
     * Filter by nci identifier.
     *
     * @param nciIdentifier the nci identifier
     */
    public void filterByNciIdentifier(final String nciIdentifier) {
        String searchString = "%" + nciIdentifier.toLowerCase() + "%";
        andWhere("lower(cs.nciIdentifier) LIKE :" + NCI_IDENTIFIER);
        setParameter(NCI_IDENTIFIER, searchString);
    }


    public void filterByOrganization(final Integer organizationId) {
        leftJoin("cs.organizationClinicalStaffs as scs");
        andWhere("scs.organization.id = :" + ORGANIZATION_ID);
        setParameter(ORGANIZATION_ID, organizationId);
    }

    public void filterByOrganization(final Integer organizationId, String key) {
        leftJoin("cs.organizationClinicalStaffs as scs");
        andWhere("scs.organization.id = :" + ORGANIZATION_ID + key);
        setParameter(ORGANIZATION_ID + key, organizationId);
    }

    public void filterByOrganization(List<Integer> ids) {
        leftJoin("cs.organizationClinicalStaffs as scs");
        andWhere("scs.organization.id IN (:ids)");
        setParameterList("ids", ids);
    }

    public void filterByActive() {
        andWhere("((cs.status =:status1 and cs.effectiveDate <=:effectiveDate) or (cs.status =:status2 and cs.effectiveDate > :effectiveDate))");
        setParameter("status1", RoleStatus.ACTIVE);
        setParameter("status2", RoleStatus.IN_ACTIVE);
        setParameter("effectiveDate", new Date());
    }


    public void setLeftJoin() {
        leftJoin("cs.organizationClinicalStaffs as orgcs left outer join orgcs.studyOrganizationClinicalStaff as storgcs left outer join orgcs.organization as org left outer join storgcs.studyOrganization as studyOrg left outer join studyOrg.study as std");
    }

    public void filterByAll(String text, String key) {
        String searchString = text != null && StringUtils.isNotBlank(text) ? "%" + StringUtils.trim(StringUtils.lowerCase(text)) + "%" : null;
        andWhere(String.format("(lower(cs.firstName) LIKE :%s " +
                "or lower(cs.lastName) LIKE :%s " +
                "or lower(cs.emailAddress) LIKE :%s " +
                "or lower(cs.nciIdentifier) LIKE :%s " +
                "or lower(org.name) LIKE :%s " +
                "or lower(org.nciInstituteCode) LIKE :%s " +
                "or lower(std.shortTitle) LIKE :%s " +
                "or lower(std.assignedIdentifier) LIKE :%s )", FIRST_NAME + key, LAST_NAME + key, EMAIL + key, NCI_IDENTIFIER + key, NAME + key, NCI_INSTITUTIONAL_CODE + key, SHORT_TITLE + key, STUDY_ASSIGNED_IDENTIFIER + key));
        setParameter(FIRST_NAME + key, searchString);
        setParameter(LAST_NAME + key, searchString);
        setParameter(EMAIL + key, searchString);
        setParameter(NCI_IDENTIFIER + key, searchString);
        setParameter(NAME + key, searchString);
        setParameter(NCI_INSTITUTIONAL_CODE + key, searchString);
        setParameter(SHORT_TITLE + key, searchString);
        setParameter(STUDY_ASSIGNED_IDENTIFIER + key, searchString);
    }


}
