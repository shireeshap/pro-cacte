package gov.nih.nci.ctcae.web.form;

import java.util.Map;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;

/**
 * @author AmeyS
 * Testcases for EmptyFormTabTest.java
 *
 */
public class EmptyFormTabTest extends WebTestCase{
	private EmptyFormTab tab;
	private CreateFormCommand command;
	private CRFRepository crfRepository;
	private static String DUMMY_TITLE = "dummyTitle";
	private static String DUMMY_VIEW_NAME = "dummyViewName";
	private static String CRF_TITLE = "crfTitleBeforeSaving";
	private static String SAVED_CRF_TITLE = "crfTitleAfterSaving";
	private CRF crf;
	private CRF savedCrf;
	private Map<String, Object> model;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		tab = new EmptyFormTab(DUMMY_TITLE, DUMMY_TITLE, DUMMY_VIEW_NAME);
		command = registerMockFor(CreateFormCommand.class);
		crfRepository = registerMockFor(CRFRepository.class);
		tab.setCrfRepository(crfRepository);
		crf = new CRF();
		crf.setTitle(CRF_TITLE);
		savedCrf = new CRF();
		savedCrf.setTitle(SAVED_CRF_TITLE);
	}
	
	public void testGetRequiredPrivilege(){
		assertEquals(Privilege.PRIVILEGE_CREATE_FORM, tab.getRequiredPrivilege());
	}
	
	public void testOnDisplay(){
		expect(crfRepository.save(crf)).andReturn(savedCrf).anyTimes();
		expect(command.getCrf()).andReturn(crf).once();
		expect(command.getCrf()).andReturn(savedCrf).anyTimes();
		command.setCrf(crf);
		command.setCrf(savedCrf);
		
		replayMocks();
		tab.onDisplay(request, command);
		verifyMocks();
		
		assertEquals(savedCrf.getTitle(), command.getCrf().getTitle());
	}
	
	public void testReferenceData(){
		expect(command.getCrf()).andReturn(crf).anyTimes();
		
		replayMocks();
		model = tab.referenceData(command);
		verifyMocks();
		
		assertEquals(crf.getTitle(), ((CRF) model.get("crf")).getTitle());
	}
}
