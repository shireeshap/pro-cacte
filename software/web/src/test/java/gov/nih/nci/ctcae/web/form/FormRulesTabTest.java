package gov.nih.nci.ctcae.web.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.rules.NotificationRule;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;

/**
 * @author AmeyS
 * Testcases for FormRuleTab.java
 *
 */
public class FormRulesTabTest extends WebTestCase{
	private FormRulesTab tab;
	private CreateFormCommand command;
	private CRFRepository crfRepository;
	private static String UNIQUE_TITLE = "uniqueTitle";
	private CRF crf;
	private Map<String, Object> model;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		tab = new FormRulesTab();
		command = registerMockFor(CreateFormCommand.class);
		crfRepository = registerMockFor(CRFRepository.class);
		tab.setCrfRepository(crfRepository);
		crf = new CRF();
	}
	
	public void testGetRequiredPrivilege(){
		assertEquals(Privilege.PRIVILEGE_ADD_FORM_RULES, tab.getRequiredPrivilege());
	}
	
	public void testOnDisplay(){
		expect(crfRepository.save(crf)).andReturn(crf).anyTimes();
		expect(command.getCrf()).andReturn(crf).anyTimes();
		expect(command.getUniqueTitleForCrf()).andReturn(UNIQUE_TITLE).anyTimes();
		command.setReadonlyview("false");
		
		replayMocks();
		tab.onDisplay(request, command);
		verifyMocks();
		
		assertEquals(UNIQUE_TITLE, command.getCrf().getTitle());
	}
	
	public void testReferenceData(){
		expect(command.getCrf()).andReturn(crf).anyTimes();
		expect(command.getFormRules(crfRepository)).andReturn(new ArrayList<NotificationRule>());
		
		replayMocks();
		model = tab.referenceData(command);
		verifyMocks();
		
		assertTrue(((List<ProCtcTerm>) model.get("crfSymptoms")).isEmpty());
		assertFalse(((List<ListValues>) model.get("notifications")).isEmpty());
		assertTrue(((List<ListValues>) model.get("notificationRules")).isEmpty());
		assertEquals("false", (String) model.get("isSite"));
		assertEquals("true", (String) model.get("showOr"));
	}
}
