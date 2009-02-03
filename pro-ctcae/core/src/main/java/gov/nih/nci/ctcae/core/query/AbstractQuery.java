package gov.nih.nci.ctcae.core.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//
/**
 * The Class AbstractQuery.
 *
 * @author
 */
public abstract class AbstractQuery implements Query {

    /**
     * The query string.
     */
    private final String queryString;

    /**
     * The query buffer.
     */
    private StringBuffer queryBuffer;

    /**
     * The and conditions.
     */
    private final Set<String> andConditions = new HashSet<String>();

    /**
     * The or conditions.
     */
    private final Set<String> orConditions = new HashSet<String>();

    /**
     * The joins.
     */
    private final Set<String> joins = new HashSet<String>();

    /**
     * The query parameter map.
     */
    private final Map<String, Object> queryParameterMap;

    /**
     * The Constant ID.
     */
    public final static String ID = "OBJID";

    /**
     * The Constant WHERE.
     */
    public final static String WHERE = "WHERE";

    /**
     * The Constant AND.
     */
    public final static String AND = "AND";

    /**
     * The Constant OR.
     */
    public final static String OR = "OR";

    /**
     * The maximum results.
     */
    private Integer maximumResults = 30;

    /**
     * Instantiates a new abstract query.
     *
     * @param queryString the query string
     */
    public AbstractQuery(final String queryString) {
        this.queryString = queryString;
        queryParameterMap = new HashMap<String, Object>(0);
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.query.Query#getQueryString()
      */
    public String getQueryString() {
        String orderByString = "";
        if (queryString.lastIndexOf("order by") > 0) {
            orderByString = queryString.substring(queryString.lastIndexOf("order by"),
                    queryString.length()).trim();
            queryBuffer = new StringBuffer(queryString.substring(0,
                    queryString.lastIndexOf("order by")).trim());
        } else {
            queryBuffer = new StringBuffer(queryString.trim());
        }

        for (String join : joins) {
            queryBuffer.append(join);
        }

        for (String conditon : andConditions) {
            if (queryBuffer.toString().toUpperCase().indexOf(WHERE) < 0) {
                queryBuffer.append(" " + WHERE + " " + conditon);
            } else {
                queryBuffer.append(" " + AND + " " + conditon);
            }

        }

//        if (!orConditions.isEmpty()) {
//            boolean groupOR = andConditions.size() > 0
//                    || queryBuffer.toString().toUpperCase().indexOf(WHERE) > 0;
//
//            if (groupOR) {
//                queryBuffer.append(" " + AND + " (");
//            }
//
//            int orIndx = 0;
//            for (String conditon : orConditions) {
//                if (orIndx == 0 && groupOR) {
//                    queryBuffer.append(" " + conditon);
//                } else {
//                    queryBuffer.append(" " + OR + " " + conditon);
//                }
//                orIndx++;
//            }
//
//            if (groupOR) {
//                queryBuffer.append(" )");
//            }
//        }

        if (!orderByString.equalsIgnoreCase("")) {
            // finally add order by
            queryBuffer.append(" " + orderByString);
        }

        return queryBuffer.toString();
    }

    /**
     * Bind an argument to a named parameter.
     *
     * @param key   the key of the parameter
     * @param value the value of the parameter
     */
    protected void setParameter(final String key, final Object value) {
        queryParameterMap.put(key, value);
    }

    /**
     * add the 'Where' condition to the existing Query String.
     * <p>
     * For example if for the queryString is "Select * from Article a order by a.id";
     * andWhere("a.name=:name") will append queryString to "Select * from Article a WHERE
     * a.name=:name order by a.id"
     * </p>
     *
     * @param condition the condition
     */
    protected void andWhere(final String condition) {
        andConditions.add(condition);
    }

    /**
     * Gets the parameter map.
     *
     * @return the parameter map
     */
    public Map<String, Object> getParameterMap() {
        return queryParameterMap;
    }

    /**
     * Joins an object to the query select * from Study s join s.identifiers as id where
     * s.shortTitle='study'
     *
     * @param objectQuery the object query
     */
    protected void join(String objectQuery) {
        addToJoinsSet(" join " + objectQuery);

    }

    /**
     * Joins an object to the query select * from Study s left join s.identifiers as id where
     * s.shortTitle='study'
     *
     * @param objectQuery the object query
     */
    protected void leftJoin(String objectQuery) {
        addToJoinsSet(" left join " + objectQuery);
    }

    /**
     * Inner join.
     *
     * @param objectQuery the object query
     */
    protected void innerJoin(String objectQuery) {
        addToJoinsSet(" inner join " + objectQuery);
    }

    /**
     * Left join fetch.
     *
     * @param objectQuery the object query
     */
    protected void leftJoinFetch(String objectQuery) {
        addToJoinsSet(" left join fetch " + objectQuery);
    }

    /**
     * Adds the to joins set.
     *
     * @param object the object
     */
    private void addToJoinsSet(String object) {
        if (!joins.contains(object)) joins.add(object);
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.query.Query#getMaximumResults()
      */
    public Integer getMaximumResults() {
        return maximumResults;
    }

    /**
     * Sets the maximum results.
     *
     * @param maximumResults the new maximum results
     */
	public void setMaximumResults(final Integer maximumResults) {
		this.maximumResults = maximumResults;
	}


}
