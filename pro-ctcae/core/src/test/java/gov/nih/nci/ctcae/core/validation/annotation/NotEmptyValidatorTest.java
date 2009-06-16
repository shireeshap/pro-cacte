package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.TestBean;
import junit.framework.TestCase;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @since Nov 8, 2008
 */
public class NotEmptyValidatorTest extends TestCase {

	private NotEmptyValidator validator;

	private TestBean testBean;
	private BeanWrapperImpl beanWrapperImpl;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		validator = new NotEmptyValidator();
		testBean = new TestBean();
		beanWrapperImpl = new BeanWrapperImpl(testBean);

	}


	public void testMessage() {
		assertNull(validator.message());

	}

	public void testInitialzie() {

		Annotation[] annotationsArray = beanWrapperImpl.getPropertyDescriptor("title").getReadMethod().getAnnotations();
		assertNotNull(annotationsArray);
		assertFalse("must find not empty annotation", annotationsArray.length == 0);
		Annotation annotation = annotationsArray[0];
		validator.initialize((NotEmpty) annotation);


	}

	public void testValidate() {
		testBean = null;
		assertFalse("null are also empty", validator.validate(testBean));

		testBean = new TestBean();
		testBean.setTitle("");
		assertFalse(validator.validate(testBean.getTitle()));

		testBean.setTitle(" ");

		assertFalse("must ignore white space", validator.validate(testBean.getTitle()));
		assertFalse(validator.validate(new String[]{}));
		assertFalse(validator.validate(new ArrayList()));
		ArrayList list = new ArrayList();
		list.add("abc");
		assertTrue(validator.validate(list));
		assertTrue(validator.validate(new String[]{"abc"}));

		Map map = new HashMap();
		assertFalse(validator.validate(map));
		map.put("key", "value");
		assertTrue(validator.validate(map));


	}
}
