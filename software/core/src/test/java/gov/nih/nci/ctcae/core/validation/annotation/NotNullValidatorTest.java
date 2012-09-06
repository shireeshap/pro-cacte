package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.TestBean;
import junit.framework.TestCase;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.Annotation;

/**
 * @author Vinay Kumar
 * @since Dec 8, 2008
 */
public class NotNullValidatorTest extends TestCase {

	private NotNullValidator validator;
	private TestBean testBean;
	private BeanWrapperImpl beanWrapperImpl;

	@Override
	protected void setUp() throws Exception {
		super.setUp();


		validator = new NotNullValidator();

		testBean = new TestBean();
		beanWrapperImpl = new BeanWrapperImpl(testBean);

	}

	public void testInitialzie() {

		Annotation[] annotationsArray = beanWrapperImpl.getPropertyDescriptor("firstName").getReadMethod().getAnnotations();
		assertNotNull(annotationsArray);
		assertFalse("must find not empty annotation", annotationsArray.length == 0);
		Annotation annotation = annotationsArray[0];
		validator.initialize((NotNull) annotation);


	}

	public void testMessage() {
		assertNull(validator.message());
	}

	public void testValidate() {

		testBean = null;
		assertFalse("null are also null", validator.validate(null));
		assertFalse("null object is null", validator.validate(testBean));
		testBean = new TestBean();
		assertTrue(validator.validate(testBean));

		this.testBean.setFirstName(" ");
		assertTrue("must not igore space", validator.validate(this.testBean.getFirstName()));
	}
}
