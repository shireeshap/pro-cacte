package gov.nih.nci.ctcae.commons.utils;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @since Oct 27, 2008
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

    public void testNestedProperty() throws Exception {

        String studySiteMethodName = PropertyUtil.getNestedPropertyParent("studySites[0].statusCode");
        assertEquals("studySites[0]", studySiteMethodName);

        String testProperty = PropertyUtil.getNestedPropertyParent("studySites[0]");
        assertEquals("studySites[0]", testProperty);

        String testStudySiteMethodName = PropertyUtil
                .getNestedPropertyParent("studySites[0].test[4].statusCode");
        assertEquals("studySites[0].test[4]", testStudySiteMethodName);

        testProperty = PropertyUtil.getNestedPropertyParent(null);
        assertEquals(null, testProperty);

        testProperty = PropertyUtil.getNestedPropertyParent("crf.title");
        assertEquals("crf", testProperty);

    }

    public void testValidateCollectionPropertyName() throws Exception {

        String studySiteMethodName = PropertyUtil
                .getColletionPropertyName("studySites[0].statusCode");
        assertEquals("studySites[0]", studySiteMethodName);

        String testProperty = PropertyUtil.getColletionPropertyName("studySites[0]");
        assertNull(testProperty);


    }
}

