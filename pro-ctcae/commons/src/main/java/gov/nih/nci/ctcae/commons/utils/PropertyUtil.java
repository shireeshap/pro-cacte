package gov.nih.nci.ctcae.commons.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public class PropertyUtil {
	private static final Log logger = LogFactory.getLog(PropertyUtil.class);

	private static String PROPERTY_KEY_PREFIX = "[";

	private static char PROPERTY_KEY_PREFIX_CHAR = '[';

	private static String PROPERTY_KEY_SUFFIX = "]";

	private static char PROPERTY_KEY_SUFFIX_CHAR = ']';

	private static char DOT_CHAR = '.';

	public static String getCollectionMethodName(String propertyName) {

		if (propertyName == null || propertyName.indexOf(PROPERTY_KEY_PREFIX) == -1
			|| propertyName.indexOf(PROPERTY_KEY_SUFFIX) == -1
			// || propertyName.indexOf(DOT_CHAR) == -1
			) {
			return null;
		}
		int keyStart = propertyName.lastIndexOf(PROPERTY_KEY_PREFIX);
		int keyEnd = propertyName.lastIndexOf(PROPERTY_KEY_SUFFIX);

		return propertyName.substring(0, keyStart);
	}


	public static String getColletionPropertyName(String propertyName) {

		if (propertyName == null || propertyName.indexOf(PROPERTY_KEY_PREFIX) == -1
			|| propertyName.indexOf(PROPERTY_KEY_SUFFIX) == -1
			|| propertyName.indexOf(DOT_CHAR) == -1) {
			return null;
		}
		int keyStart = propertyName.indexOf(DOT_CHAR);
		// propertyName.lastIndexOf(PROPERTY_KEY_PREFIX);
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

