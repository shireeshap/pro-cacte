package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Amey
 * FetchClinicalStaffControllerTest class
 */

public class FetchClinicalStaffControllerTest extends AbstractWebIntegrationTestCase{
	ClinicalStaff clinicalStaff;
	FetchClinicalStaffController controller = new FetchClinicalStaffController();
//	ClinicalStaffAjaxFacade clinicalStaffAjaxFacade;
	List<ClinicalStaff> clinicalStaffs = new ArrayList<ClinicalStaff>();
	private final static String SYSTEM_ADMIN ="system_admin";
	private final static String START_INDEX = "startIndex";
	private final static String RESULTS = "results";
	private final static String SORT = "sort";
	private final static String DIR = "dir";
	private final static String CLINICAL_STAFF_SEARCH_STRING = "clinicalStaffSearchString";
	private final static String VIEW_NAME = "jsonView";
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
//		clinicalStaffAjaxFacade = registerMockFor(ClinicalStaffAjaxFacade.class);
		controller.setClinicalStaffAjaxFacade((ClinicalStaffAjaxFacade) getApplicationContext().getBean("clinicalStaffAjaxFacade"));
		login(SYSTEM_ADMIN);
	}
	
	public void testHandleRequestInternal() throws Exception{
		ModelAndView modelAndView;
		request.setParameter(START_INDEX, "1");
		request.setParameter(RESULTS, "5");
		request.setParameter(SORT, "id");
		request.setParameter(DIR, "asc");
		request.getSession().setAttribute(CLINICAL_STAFF_SEARCH_STRING, "a");
		
	/*	expect(clinicalStaffAjaxFacade.resultCount(isA(String[].class))).andReturn( (long) 1);
		expect(clinicalStaffAjaxFacade.searchClinicalStaff(isA(String[].class), isA(Integer.class), isA(Integer.class), isA(String.class), isA(String.class), isA(Long.class))).andReturn(clinicalStaffs);
	*/	
		replayMocks();
		modelAndView = controller.handleRequest(request, response);
		verifyMocks();
		
		assertEquals(VIEW_NAME, modelAndView.getViewName());
		assertEquals(5, ((JSONArray) ((Map<String, Object>) modelAndView.getModelMap().get("shippedRecordSet")).get("searchClinicalStaffDTOs")).size());
	}
	
	public void testGetSiteNames(){
		ClinicalStaff clinicalStaff = ClinicalStaffTestHelper.getDefaultClinicalStaff();
	}
}
