package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.security.ApplicationSecurityManager;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 13, 2009
 */
public abstract class SecuredQuery<T extends Persistable> extends AbstractQuery {


    private static String OBJECT_IDS = "objectIds";

    /**
     * Instantiates a new abstract query.
     *
     * @param queryString the query string
     */
    public SecuredQuery(final String queryString) {
        super(queryString);
        User currentLoggedInUser = ApplicationSecurityManager.getCurrentLoggedInUser();

        List<Integer> objectIds = currentLoggedInUser.findAccessibleObjectIds(getPersistableClass());

        filterByObjectIds(objectIds);

    }

    public abstract Class<T> getPersistableClass();

    protected void filterByObjectIds(List<Integer> objectIds) {
        if (!objectIds.isEmpty()) {
            andWhere(String.format("%s in (:%s )", getObjectIdQueryString(), OBJECT_IDS));
            setParameterList(OBJECT_IDS, objectIds);
        }
    }

    protected abstract String getObjectIdQueryString();
}
