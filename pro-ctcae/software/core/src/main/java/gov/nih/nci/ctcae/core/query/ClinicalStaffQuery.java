package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.RoleStatus;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

//
/**
 * User: Mehul Gulati
 * Date: Oct 15, 2008.
 */
public class ClinicalStaffQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT distinct cs from ClinicalStaff cs order by cs.id";
    private static String queryString1 = "SELECT count(distinct cs) from ClinicalStaff cs";
    private static String queryString2 = "SELECT distinct cs from ClinicalStaff cs ";

    private static String ORGANIZATION_ID = "organizationId";
    private static String ORGANIZATION_CLASS = "domainObjectClass";

    /**
     * The FIRS t_ name.
     */
    private static String FIRST_NAME = "firstName";

    /**
     * The LAS t_ name.
     */
    private static String LAST_NAME = "lastName";
    private static String USER_ID = "userId";
    private static String EMAIL = "emailAddress";

    /**
     * The NC i_ identifier.
     */
    private static String NCI_IDENTIFIER = "nciIdentifier";
    private static String STUDY_ASSIGNED_IDENTIFIER = "assignedIdentifier";
    private String ROLES = "roles";

    private static final String NAME = "name";
    private static final String SHORT_TITLE = "shortTitle";
    private static final String NCI_INSTITUTIONAL_CODE = "nciInstituteCode";



    /**
     * Instantiates a new clinical staff query.
     */
    public ClinicalStaffQuery() {
        super(queryString);
        filterByActive();
    }

    public ClinicalStaffQuery(boolean count, boolean showInactive){
        super(queryString1);
        if (!showInactive) {
            filterByActive();
        }
    }

    public ClinicalStaffQuery(boolean sort, boolean count,boolean showInactive){
        super(queryString2);
         if (!showInactive) {
            filterByActive();
        }
    }


    public ClinicalStaffQuery(boolean showInactive) {
        super(queryString);
        if (!showInactive) {
            filterByActive();
        }
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
    public void excludeByStaffId(final Integer staffId){
          if (staffId != null){
              andWhere("cs.id <> :id");
              setParameter("id",staffId);
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
        String searchString = "%" + nciIdentifier.toLowerCase()+ "%";
        andWhere("lower(cs.nciIdentifier) LIKE :" + NCI_IDENTIFIER);
        setParameter(NCI_IDENTIFIER, searchString);
    }


    public void filterByOrganization(final Integer organizationId) {
        leftJoin("cs.organizationClinicalStaffs as scs");
        andWhere("scs.organization.id = :" + ORGANIZATION_ID);
        setParameter(ORGANIZATION_ID, organizationId);
    }

    public void filterByActive() {
        andWhere("((cs.status =:status1 and cs.effectiveDate <=:effectiveDate) or (cs.status =:status2 and cs.effectiveDate > :effectiveDate))");
        setParameter("status1", RoleStatus.ACTIVE);
        setParameter("status2", RoleStatus.IN_ACTIVE);
        setParameter("effectiveDate",  new Date());
    }

     public void filterByAll(String text) {
        String searchString = text != null && StringUtils.isNotBlank(text) ? "%" + StringUtils.trim(StringUtils.lowerCase(text)) + "%" : null;
        if(!(searchString == null || "".equals(searchString))){
            orWhere(String.format("lower(cs.firstName) LIKE :%s ",FIRST_NAME));
            orWhere(String.format("lower(cs.lastName) LIKE :%s ",LAST_NAME));
            orWhere(String.format("lower(cs.nciIdentifier) LIKE :%s ",NCI_IDENTIFIER));

            setParameter(FIRST_NAME, searchString);
            setParameter(LAST_NAME, searchString);
            setParameter(NCI_IDENTIFIER, searchString);

            leftJoin("cs.organizationClinicalStaffs as orgcs left join orgcs.studyOrganizationClinicalStaff as storgcs join storgcs.studyOrganization as storg");
            orWhere(String.format("lower(storg.organization.name) LIKE :%s", NAME));
            orWhere(String.format("lower(storg.organization.nciInstituteCode) LIKE :%s", NCI_INSTITUTIONAL_CODE));
            setParameter(NAME, searchString);
            setParameter(NCI_INSTITUTIONAL_CODE, searchString);
            orWhere(String.format("lower(storg.study.shortTitle) LIKE :%s", SHORT_TITLE));
            orWhere(String.format("lower(storg.study.assignedIdentifier) LIKE :%s", STUDY_ASSIGNED_IDENTIFIER));
            setParameter(SHORT_TITLE, searchString);
            setParameter(STUDY_ASSIGNED_IDENTIFIER, searchString);
        }



    }


}
