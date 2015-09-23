package gov.nih.nci.ctcae.commons.utils;

/**
 * @author Vinay Kumar
 * @since Oct 27, 2008
 */
public class PropertyUtil {


    private static final String PROPERTY_KEY_PREFIX = "[";

    private static final String PROPERTY_KEY_SUFFIX = "]";

    private static final char DOT_CHAR = '.';

    public static String getCollectionMethodName(String propertyName) {

        if (propertyName == null || propertyName.indexOf(PROPERTY_KEY_PREFIX) == -1
                || propertyName.indexOf(PROPERTY_KEY_SUFFIX) == -1
            // || propertyName.indexOf(DOT_CHAR) == -1
                ) {
            return null;
        }
        int keyStart = propertyName.lastIndexOf(PROPERTY_KEY_PREFIX);

        return propertyName.substring(0, keyStart);
    }


    public static String getColletionPropertyName(String propertyName) {

        if (propertyName == null || propertyName.indexOf(PROPERTY_KEY_PREFIX) == -1
                || propertyName.indexOf(PROPERTY_KEY_SUFFIX) == -1
                || propertyName.indexOf(DOT_CHAR) == -1) {
            return null;
        }
        int keyEnd = propertyName.lastIndexOf(PROPERTY_KEY_SUFFIX);

        return propertyName.substring(0, keyEnd + 1);
    }


    public static String getNestedPropertyParent(final String propertyName) {
        if (propertyName == null) {
            return null;
        } else if (propertyName.indexOf(DOT_CHAR) == -1) {
            return propertyName;
        }
        int keyEnd = propertyName.lastIndexOf(DOT_CHAR);

        return propertyName.substring(0, keyEnd);
    }
}

