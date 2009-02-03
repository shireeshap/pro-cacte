package gov.nih.nci.ctcae.core.query;

//
/**
 * The Interface Query.
 */
public interface Query {

    /**
     * Gets the query string.
     *
     * @return the query string
     */
    String getQueryString();

    /**
     * Gets the maximum results.
     *
     * @return the maximum results
     */
    Integer getMaximumResults();
}
