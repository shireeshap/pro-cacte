package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.CrfItem;
import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @crated Dec 8, 2008
 */
public class NotNullValidatorTest extends TestCase {

	private NotNullValidator validator;
	private CrfItem crfItem;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		validator = new NotNullValidator();

	}


	public void testMessage() {
		assertNull(validator.message());
	}

	public void testValidate() {

		assertFalse("null are also null", validator.validate(null));
		assertFalse("null object is null", validator.validate(crfItem));
		assertTrue(validator.validate(new CrfItem()));
		assertTrue("must not igore space", validator.validate(" "));
	}
}
