package gov.nih.nci.ctcae.web.validation.validator;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueObjectInCollectionValidator;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class WebControllerValidatorTest extends WebTestCase {

	private WebControllerValidatorImpl validator;
	private Study study;
	private BindException errors;
	private ApplicationContext applicationContext;
	private Map validators;
	UniqueObjectInCollectionValidator uniqueObjectInCollectionValidator;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		applicationContext = registerMockFor(ApplicationContext.class);
		validator = new WebControllerValidatorImpl();
		validator.setApplicationContext(applicationContext);
		study = new Study();
		errors = new BindException(study, "study");
		validators = new HashMap();
		uniqueObjectInCollectionValidator = new UniqueObjectInCollectionValidator();

	}

	public void testReturnTrueForNullValues() {
		validator.validate(null, study, errors);
		assertFalse("must not have any errors for null values", errors.hasErrors());

		validator.validate(request, null, errors);
		assertFalse("must not have any errors for null values", errors.hasErrors());
		validator.validate(request, study, null);

	}

	public void testValidateTrueOnCollectionProperty() {
		HashMap<String, String> parmameterNameValues = new HashMap<String, String>();

		parmameterNameValues.put("studySites[0].organization.displayName", "org1");
		parmameterNameValues.put("studySites[1].organization.displayName", "org2");
		request.setParameters(parmameterNameValues);

		StudySite studySite1 = new StudySite();
		studySite1.setOrganization(Fixture.NCI);
		study.addStudySite(studySite1);

		StudySite studySite2 = new StudySite();
		studySite2.setOrganization(Fixture.DUKE);

		study.addStudySite(studySite2);
		validators.put("uniqueObjectInCollectionValidator", uniqueObjectInCollectionValidator);
		expect(applicationContext.getBeansOfType(UniqueObjectInCollectionValidator.class)).andReturn(validators).anyTimes();
		replayMocks();
		validator.validate(request, study, errors);
		verifyMocks();
		assertFalse(errors.hasErrors());

	}

	public void testValidateFalseOnCollectionProperty() {
		HashMap<String, String> parmameterNameValues = new HashMap<String, String>();

		parmameterNameValues.put("studySites[0].organization.displayName", "org1");
		parmameterNameValues.put("studySites[1].organization.displayName", "org1");
		request.setParameters(parmameterNameValues);

		StudySite studySite1 = new StudySite();
		studySite1.setOrganization(Fixture.NCI);
		study.addStudySite(studySite1);

		StudySite studySite2 = new StudySite();
		studySite2.setOrganization(Fixture.NCI);

		study.addStudySite(studySite2);
		validators.put("uniqueObjectInCollectionValidator", uniqueObjectInCollectionValidator);
		expect(applicationContext.getBeansOfType(UniqueObjectInCollectionValidator.class)).andReturn(validators).anyTimes();
		replayMocks();
		validator.validate(request, study, errors);
		verifyMocks();
		assertTrue(errors.hasErrors());
		List<FieldError> list = errors.getAllErrors();
		assertEquals("must have 1 error only", Integer.valueOf(1), Integer.valueOf(list.size()));


		assertEquals("studySites[1].organization.displayName", list.get(0).getField());

	}

	public static MockHttpServletRequest populateRequestForCreateStudy() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute("gov.nih.nci.cabig.caaers.web.study.CreateStudyController.PAGE.command", Integer
			.valueOf(0));

		HashMap<String, String> parmameterNameValues = new HashMap<String, String>();

		parmameterNameValues.put("shortTitle", "desc");
		parmameterNameValues.put("longTitle", "abc");
		parmameterNameValues.put("assignedIdentifier", "assigned id");
		parmameterNameValues.put("studyCoordinatingCenter.organization", "org1");
		parmameterNameValues.put("studyFundingSponsor.organization", "org2");
		parmameterNameValues.put("shortTitle", "short title");
		parmameterNameValues.put("studySites[0].statusCode", "value1");
		parmameterNameValues.put("studySites[1].statusCode", "value2");

		for (String key : parmameterNameValues.keySet()) {

			request.setParameter(key, parmameterNameValues.get(key));
		}

		return request;
	}

}
