package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.security.ApplicationSecurityManager;
import java.util.List;

/**
 * @author Vinay Kumar
 * @since Mar 13, 2009
 */
public abstract class SecuredQuery<T extends Persistable> extends AbstractQuery {


    private static String OBJECT_IDS = "objectIds";

    /**
     * Instantiates a new abstract query. 
     *
     * @param queryString the query string
     */ // To be removed later. replace its calls directly with SecuredQuery(final String queryString, boolean) as internally this method does the same
    public SecuredQuery(final String queryString) {
        this(queryString, true);
    }
    
    /**
     * Instantiates a new abstract query. This is a new method and should completely replace used instances of above method.
     *
     * @param queryString the query string
     */// To be removed later. replace its calls directly with SecuredQuery(final String queryString, boolean secure) as internally this method does the same
    public SecuredQuery(QueryStrings queryString) {
        this(queryString, true);
    }

   
    public SecuredQuery(final String queryString, boolean secure) {
        super(queryString);
        if (secure) {
            User currentLoggedInUser = ApplicationSecurityManager.getCurrentLoggedInUser();
            List<Integer> objectIds = currentLoggedInUser.findAccessibleObjectIds(getPersistableClass());
            filterByObjectIds(objectIds);
        }
    }
    /**
     * Instantiates a new abstract query. This is a new method and should completely replace used instances of above method.
     *
     * @param queryString the query string
     */
    public SecuredQuery(QueryStrings queryString, boolean secure) {
        super(queryString);
        if (secure) {
            User currentLoggedInUser = ApplicationSecurityManager.getCurrentLoggedInUser();
            List<Integer> objectIds = currentLoggedInUser.findAccessibleObjectIds(getPersistableClass());
            filterByObjectIds(objectIds);
        }
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
