package gov.nih.nci.ctcae.commons.utils;

import junit.framework.TestCase;
import edu.nwu.bioinformatics.commons.StringUtils;

/**
 * @author Saurabh Agrawal
 * @crated Oct 27, 2008
 */
public class PropertyUtilTest extends TestCase {

    public void testValidateForNestedProperty() throws Exception {

        String studySiteMethodName = PropertyUtil
                .getCollectionMethodName("studySites[0].statusCode");
        assertEquals("studySites", studySiteMethodName);

        String testProperty = PropertyUtil.getCollectionMethodName("studySites[0]");
        assertEquals("studySites", testProperty);

        String testStudySiteMethodName = PropertyUtil
                .getCollectionMethodName("studySites[0].test[4].statusCode");
        assertEquals("studySites[0].test", testStudySiteMethodName);

        testProperty = PropertyUtil.getCollectionMethodName(null);
        assertEquals(null, testProperty);

        testProperty = PropertyUtil.getCollectionMethodName("studySites");
        assertEquals(null, testProperty);

    }

    public void testValidateCollectionPropertyName() throws Exception {

        String studySiteMethodName = PropertyUtil
                .getColletionPropertyName("studySites[0].statusCode");
        assertEquals("studySites[0]", studySiteMethodName);

        String testProperty = PropertyUtil.getColletionPropertyName("studySites[0]");
        assertNull(testProperty);


    }
}

