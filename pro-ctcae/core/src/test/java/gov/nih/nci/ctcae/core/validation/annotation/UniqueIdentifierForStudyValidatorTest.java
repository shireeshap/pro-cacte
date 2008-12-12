package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class UniqueIdentifierForStudyValidatorTest extends AbstractTestCase {

	private Validator validator;
	private StudyRepository studyRepository;
	private Study study;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		validator = new UniqueIdentifierForStudyValidator();
		studyRepository = registerMockFor(StudyRepository.class);
		((UniqueIdentifierForStudyValidator) validator).setStudyRepository(studyRepository);
		study = new Study();
		study.addStudySite(new StudySite());
	}

	public void testValidateUniqueIdentifier() {

		expect(studyRepository.find(isA(StudyQuery.class))).andReturn(new ArrayList());
		replayMocks();
		assertTrue("Identifier already exists.", validator.validate("identifier"));
		verifyMocks();

	}

	public void testInitialzie() {

		BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(study);
		Annotation[] annotationsArray = beanWrapperImpl.getPropertyDescriptor("assignedIdentifier").getReadMethod().getAnnotations();
		assertFalse("must find annotation", annotationsArray.length == 0);
		assertTrue("must find UniqueTitleForCrf annotation", annotationsArray[0].annotationType().equals(UniqueIdentifierForStudy.class));


		validator.initialize(annotationsArray[0]);
		assertEquals("Identifier already exists.", validator.message());

	}

	public void testValidateReturnTrueForWrongValue() {

		assertTrue("identifier does not exists", validator.validate(study));

	}


	public void testValidateNonUniqueIdentifier() {

		ArrayList list = new ArrayList();
		list.add(new Study());
		expect(studyRepository.find(isA(StudyQuery.class))).andReturn(list);
		replayMocks();
		assertFalse("identifier already exists", validator.validate("identifier"));
		verifyMocks();

	}
}
