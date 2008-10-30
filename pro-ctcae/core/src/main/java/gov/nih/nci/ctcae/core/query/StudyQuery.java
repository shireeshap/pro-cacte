package gov.nih.nci.ctcae.core.query;

import org.apache.commons.lang.StringUtils;
import gov.nih.nci.ctcae.core.domain.StudySite;

/**
 * @author Vinay Kumar
 * @crated Oct 14, 2008
 */
public class StudyQuery extends AbstractQuery {

    private static String queryString = "Select study from Study study order by study.shortTitle ";

    private static final String SHORT_TITLE = "shortTitle";
    private static final String LONG_TITLE = "longTitle";
    private static final String ASSIGNED_IDENTIFIER = "assignedIdentifier";

    private static String ORGANIZATION_ID = "organizationId";
    private static final String STUDY_SITE = "studySite";

    public StudyQuery() {
        super(queryString);


    }

    public void filterStudiesWithMatchingText(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("(lower(study.shortTitle) LIKE :%s or lower(study.longTitle) LIKE :%s or lower(study.assignedIdentifier) LIKE :%s)"
                , SHORT_TITLE, LONG_TITLE, ASSIGNED_IDENTIFIER));
        setParameter(SHORT_TITLE, searchString);
        setParameter(LONG_TITLE, searchString);
        setParameter(ASSIGNED_IDENTIFIER, searchString);

    }

    public void filterStudiesByLongTitle(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("lower(study.longTitle) LIKE :%s", LONG_TITLE));
        setParameter(LONG_TITLE, searchString);

    }

    public void filterStudiesByAssignedIdentifier(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("lower(study.assignedIdentifier) LIKE :%s", ASSIGNED_IDENTIFIER));
        setParameter(ASSIGNED_IDENTIFIER, searchString);

    }

    public void filterByAssignedIdentifierExactMatch(final String assignedIdentifier) {
        if (!StringUtils.isBlank(assignedIdentifier)) {
            andWhere("lower(study.assignedIdentifier) = :" + ASSIGNED_IDENTIFIER);
            setParameter(ASSIGNED_IDENTIFIER, assignedIdentifier.toLowerCase());
        }
    }

    public void filterStudiesByShortTitle(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("lower(study.shortTitle) LIKE :%s", SHORT_TITLE));
        setParameter(SHORT_TITLE, searchString);

    }


    public void filterStudiesForStudySite(Integer siteId) {
        if (siteId != null) {
            leftJoin("study.studyOrganizations as sso");
            andWhere("sso.organization.id = :" + ORGANIZATION_ID);
            andWhere("sso.class = :" +STUDY_SITE );
            setParameter(ORGANIZATION_ID, siteId);
            setParameter(STUDY_SITE, "SST");
        }
    }
}
