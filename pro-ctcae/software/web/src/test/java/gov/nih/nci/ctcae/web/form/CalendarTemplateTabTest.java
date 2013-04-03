package gov.nih.nci.ctcae.web.form;

import static org.easymock.EasyMock.expect;
import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CRFCalendar;
import gov.nih.nci.ctcae.core.domain.CRFCycleDefinition;
import gov.nih.nci.ctcae.core.domain.FormArmSchedule;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.WebTestCase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * @author AmeyS
 * Testcases for CalendarTemplateTab
 * Includes tests for referenceData(), onDisplay(), validate() and postProcess() methods.
 */
public class CalendarTemplateTabTest extends WebTestCase {
	private static CalendarTemplateTab tab;
	private static CreateFormCommand command;
	private StudyRepository studyRepository;
	private CRFRepository crfRepository;
	private Study study;
	private CRF crf;
	private Arm arm;
	private FormArmSchedule testFormArmSchedule;
	private CRFCycleDefinition crfCycleDefinition;
	private CRFCycleDefinition crfCycleDefinition2;
	private CRFCycleDefinition crfCycleDefinition3;
	private List<FormArmSchedule> formArmSchedules;
	private BindException errors;
	private CRFCalendar crfCalendar;
	private List<CRFCalendar> crfCalendars; 
	private static String ARM_TITLE1 = "testArm1";
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		tab = new CalendarTemplateTab();
		studyRepository = registerMockFor(StudyRepository.class);
		crfRepository = registerMockFor(CRFRepository.class);
		createStudyWithArm();
		crf = new CRF();
		crf.setId(1);
		setupTab();
		
		testFormArmSchedule = new FormArmSchedule();
		testFormArmSchedule.setId(22);
		crfCycleDefinition = createCRFCycleDefinition("2.2", 0);
		crfCycleDefinition2 = createCRFCycleDefinition("-2", 1);
		crfCycleDefinition3 = createCRFCycleDefinition("3", 3);
		addCrfCycleDefinitions(testFormArmSchedule, crfCycleDefinition, crfCycleDefinition2, crfCycleDefinition3);
		formArmSchedules = new ArrayList<FormArmSchedule>();
		formArmSchedules.add(testFormArmSchedule);
		
		command = new CreateFormCommand();
		errors = new BindException(crf, "crf");
		
		crfCalendar = new CRFCalendar();
		crfCalendars = new ArrayList<CRFCalendar>();
		crfCalendars.add(crfCalendar);
		testFormArmSchedule.addCrfCalendar(crfCalendar);
	}
	
	@SuppressWarnings("unchecked")
	public void testReferenceData(){
		Map<String, Object> refDateMap = tab.referenceData(command);
		assertEquals(3, ((List<ListValues>) refDateMap.get("repetitionunits")).size());
		assertEquals(2, ((List<ListValues>) refDateMap.get("duedateunits")).size());
		assertEquals(3, ((List<ListValues>) refDateMap.get("repeatuntilunits")).size());
		assertEquals(3, ((List<ListValues>) refDateMap.get("cyclelengthunits")).size());
		assertEquals(2, ((List<ListValues>) refDateMap.get("cycleplannedrepetitions")).size());
	}
	
	public void testOnDisplay(){
		command.setCrf(crf);
		crf.setStudy(study);
		expect(studyRepository.findById(1)).andReturn(study).anyTimes();
		expect(crfRepository.save(crf)).andReturn(crf).anyTimes();

		assertEquals("crf has no formArmSchedules", 0, crf.getFormArmSchedules().size());
		assertNull("command object yet is no selectedFromArmSchedule", command.getSelectedFormArmSchedule());
		// first time invocation of CalendarTab
		replayMocks();
		tab.onDisplay(request, command);
		verifyMocks();
		assertEquals("one formArmSchedule is created corresponding study's testArm1", 1, crf.getFormArmSchedules().size());
		assertNotNull("newly created formArmSchedule is assigned as a selectedFromArmSchedule in command object", command.getSelectedFormArmSchedule());
		assertNotNull("newly created formArmSchedule is assigned as a newSelectedFromArmSchedule in command object", command.getNewSelectedFormArmSchedule());
		
		command.setNewSelectedFormArmSchedule(testFormArmSchedule);
		assertNotSame("selectedFormArm schedule and newSelectedForm arm schedule are not equal", command.getSelectedFormArmSchedule(), command.getNewSelectedFormArmSchedule());
		// successive invocation of CalendarTab
		tab.onDisplay(request, command);
		verifyMocks();
		assertEquals("newSelectedFormArmSchedule is assigned to selectedFromArmSchedule in command object", command.getSelectedFormArmSchedule(), testFormArmSchedule);
		assertNull("newSelectedFromArmSchedule is now null", command.getNewSelectedFormArmSchedule());
	}
	
	
	@SuppressWarnings("unchecked")
	public void testValidate(){
		crf.setFormArmSchedules(formArmSchedules);
		command.setCrf(crf);
		
		replayMocks();
		tab.validate(command, errors);
		verifyMocks();
		List<FieldError> fieldErrors = errors.getAllErrors();
		assertEquals("expect only 2 error in errors object", 2, fieldErrors.size());
		assertEquals("Value for form expiration should be a whole number greater than 0", ((ObjectError) fieldErrors.get(0)).getDefaultMessage());
		assertEquals("Value for form expiration should be a whole number greater than 0", ((ObjectError) fieldErrors.get(1)).getDefaultMessage());
	}
	
	public void testPostProcess(){
		request.setParameter("scheduleType", "cycleBased");
		command.setSelectedFormArmSchedule(testFormArmSchedule);
		formArmSchedules.clear();
		formArmSchedules.add(testFormArmSchedule);
		crf.setFormArmSchedules(formArmSchedules);
		command.setCrf(crf);
		crfCycleDefinition.setCycleLength(7);
		crfCycleDefinition.setRepeatTimes("2");
		crfCycleDefinition2.setCycleLength(-1);
		crfCycleDefinition3.setCycleLength(10);
		crfCycleDefinition3.setRepeatTimes("1");
		request.setParameter("selecteddays_0_0", ",1,2,4,5");
		request.setParameter("selecteddays_0_1", ",1,2,4,5");
		request.setParameter("selecteddays_2_0", ",4,5");
		expect(crfRepository.save(crf)).andReturn(crf).anyTimes();
		
		assertTrue("crfCycleDefinition has empty cycle", crfCycleDefinition.getCrfCycles().isEmpty());
		assertTrue("crfCycleDefinition1 has empty cycle", crfCycleDefinition.getCrfCycles().isEmpty());
		assertTrue("crfCycleDefinition2 has empty cycle", crfCycleDefinition2.getCrfCycles().isEmpty());
		assertEquals("3 crfCycleDefinitions are associated with the testFormArmSchedule", 3, testFormArmSchedule.getCrfCycleDefinitions().size());
		replayMocks();
		tab.postProcess(request, command, errors);
		verifyMocks();
		assertEquals(",1,2,4,5", crfCycleDefinition.getCrfCycles().get(0).getCycleDays());
		assertEquals(",1,2,4,5", crfCycleDefinition.getCrfCycles().get(1).getCycleDays());
		assertEquals(",4,5", crfCycleDefinition3.getCrfCycles().get(0).getCycleDays());
		assertEquals("one invalid crfCycleDefinition is removed and now 2 crfCycleDefinition are associated " +
				"with the formArmSchedule", 2, testFormArmSchedule.getCrfCycleDefinitions().size());
	}
	
	private CRFCycleDefinition createCRFCycleDefinition(String dueDateValue, int order){
		CRFCycleDefinition crfCycleDefinition = new CRFCycleDefinition();
		crfCycleDefinition.setDueDateValue(dueDateValue);
		crfCycleDefinition.setOrder(order);
		return crfCycleDefinition;
	}
	
	private void addCrfCycleDefinitions(FormArmSchedule formArmSchedule, CRFCycleDefinition ... crfCycleDefinitions ){
		for(CRFCycleDefinition crfCycleDefinition : Arrays.asList(crfCycleDefinitions)){
			formArmSchedule.addCrfCycleDefinition(crfCycleDefinition);
			crfCycleDefinition.setFormArmSchedule(formArmSchedule);
		}
	}
	
	private void setupTab(){
		tab.setStudyRepository(studyRepository);
		tab.setCrfRepository(crfRepository);
	}
	
	private void createStudyWithArm(){
		study = new Study();
		study.setId(1);
		arm = new Arm();
		arm.setTitle(ARM_TITLE1);
		arm.setStudy(study);
		study.addArm(arm);
	}
}
