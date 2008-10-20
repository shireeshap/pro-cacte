package gov.nih.nci.ctcae.core.query;

/**
 * @author Vinay Kumar
 * @crated Oct 14, 2008
 */
public class StudyQuery extends AbstractQuery {

    private static String queryString = "Select study from Study study order by study.shortTitle ";

    private static final String SHORT_TITLE = "shortTitle";
    private static final String LONG_TITLE = "longTitle";
    private static final String ASSIGNED_IDENTIFIER = "assignedIdentifier";


    public StudyQuery() {
        super(queryString);


    }

    public void filterStudiesWithMatchingText(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("(lower(study.shortTitle) LIKE :%s or lower(study.longTitle) LIKE :%s or lower(study.assignedIdentifier) LIKE :%s)"
                , SHORT_TITLE, LONG_TITLE,ASSIGNED_IDENTIFIER));
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

    public void filterStudiesByShortTitle(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("lower(study.shortTitle) LIKE :%s", SHORT_TITLE));
        setParameter(SHORT_TITLE, searchString);

    }


}
