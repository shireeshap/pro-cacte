package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

/**
 * @author Vinay Kumar
 * @since Nov 5, 2008
 */
public class UniqueTitleForCrfValidatorTest extends AbstractTestCase {

	private UniqueTitleForCrfValidator validator;
	private CRF crf;
	private CRFRepository crfRepository;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		validator = new UniqueTitleForCrfValidator();
		crfRepository = registerMockFor(CRFRepository.class);
		validator.setCrfRepository(crfRepository);
		crf = new CRF();
        Study study = new Study();
        study.setId(1);
        crf.setStudy(study);
	}

	public void testValidateUniqueIdentifier() {

		expect(crfRepository.find(isA(CRFQuery.class))).andReturn(new ArrayList());
		replayMocks();
		assertTrue("title does not exists", validator.validate(crf, "title"));
		verifyMocks();

	}


	public void testInitialzie() {

		BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(crf);
		Annotation[] annotationsArray = beanWrapperImpl.getPropertyDescriptor("title").getReadMethod().getAnnotations();
		assertFalse("must find annotation", annotationsArray.length == 0);
		assertTrue("must find UniqueTitleForCrf annotation", annotationsArray[1].annotationType().equals(UniqueTitleForCrf.class));


		validator.initialize((UniqueTitleForCrf) annotationsArray[1]);
		assertEquals("Title already exists in database", validator.message());

	}

	public void testValidateReturnTrueForWrongValue() {

		assertTrue("identifier does not exists", validator.validate(null, crf));
		assertTrue("default is true", validator.validate(null, new Study()));
		assertTrue("default is true", validator.validate(crf));

	}

	public void testValidateNonUniqueIdentifier() {

		ArrayList list = new ArrayList();
		list.add(crf);
		expect(crfRepository.find(isA(CRFQuery.class))).andReturn(list);
		replayMocks();
		assertFalse("title already exists", validator.validate(crf, "title"));
		verifyMocks();

	}
}