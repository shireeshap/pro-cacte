package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Organization;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

//

/**
 * The Class ParticipantQuery.
 *
 * @author mehul
 */

public class ParticipantQuery extends SecuredQuery<Organization> {

    /**
     * The query string.
     */
    private static String queryString = "SELECT p from Participant p order by p.id";

    /**
     * The FIRS t_ name.
     */
    private static String FIRST_NAME = "firstName";

    /**
     * The LAS t_ name.
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
    private static String PHONENUMBER = "phoneNumber";
    private static final String STUDY_SITE = "studySite";
    private static final String LEAD_SITE = "leadSite";

    /**
     * Instantiates a new participant query.
     */
    public ParticipantQuery() {

        super(queryString, false);
    }

    public ParticipantQuery(boolean secure) {
        super(queryString, secure);
        if (secure) {
            leftJoinStudySites();
        }
    }

    /**
     * Filter by participant first name.
     *
     * @param firstName the first name
     */
    public void filterByParticipantFirstName(final String firstName) {
        String searchString = firstName.toLowerCase();
        andWhere("lower(p.firstName) LIKE :" + FIRST_NAME);
        setParameter(FIRST_NAME, searchString);
    }

    /**
     * Filter by participant last name.
     *
     * @param lastName the last name
     */
    public void filterByParticipantLastName(final String lastName) {
        String searchString = lastName.toLowerCase();
        andWhere("lower(p.lastName) LIKE :" + LAST_NAME);
        setParameter(LAST_NAME, searchString);
    }

    /**
     * Filter by participant identifier.
     *
     * @param identifier the identifier
     */
    public void filterByParticipantIdentifier(final String identifier) {
        String searchString = identifier.toLowerCase();
        andWhere("lower(p.assignedIdentifier) LIKE :" + IDENTIFIER);
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
        leftJoin("p.studyParticipantAssignments as spa join spa.studySite as ss ");
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
            andWhere("spa.studyParticipantIdentifier LIKE :" + STUDY_PARTICIPANT_IDENTIFIER);
            setParameter(STUDY_PARTICIPANT_IDENTIFIER, spIdentifier);
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
            andWhere("p.phoneNumber =:" + PHONENUMBER);
            setParameter(PHONENUMBER, phoneNumber);
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

    public Class<Organization> getPersistableClass() {
        return Organization.class;
    }

    protected String getObjectIdQueryString() {
        return "ss.organization.id";
    }


}
