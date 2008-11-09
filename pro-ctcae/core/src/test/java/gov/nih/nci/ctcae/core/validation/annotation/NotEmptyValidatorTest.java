package gov.nih.nci.ctcae.core.validation.annotation;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @crated Nov 8, 2008
 */
public class NotEmptyValidatorTest extends TestCase {

    private NotEmptyValidator validator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        validator = new NotEmptyValidator();

    }


    public void testMessage() {
        assertNull(validator.message());
    }

    public void testValidate() {

        assertFalse("null are also empty", validator.validate(null));
        assertFalse(validator.validate(""));
        assertFalse("must ignore white space", validator.validate(" "));
    }
}
