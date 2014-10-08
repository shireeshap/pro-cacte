package gov.nih.nci.ctcae.core.utils.ranking;

/**
 * @author: Suneel Allareddy
 * @since 08-Feb-2011
 */
public interface Serializer<T> {
    /**
     * Will serailize to string the object passed-in. 
     * @param object
     * @return
     */
    public String serialize(T object);
}
