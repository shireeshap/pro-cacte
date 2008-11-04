package gov.nih.nci.ctcae.core.exception;

import gov.nih.nci.ctcae.core.AbstractTestCase;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class CtcAeSystemExceptionTest extends AbstractTestCase {

    private CtcAeSystemException exception;
    private String message;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        message = "exception message";
        exception = new CtcAeSystemException(message);
    }

    public void testConstructor() {
        assertEquals(message, exception.getMessage());


    }
}
