package gov.nih.nci.ctcae.web.clinicalStaff;

import static org.easymock.EasyMock.expect;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.WebTestCase;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


/**
 * @author Amey
 * EffectiveStaffControllerTest class
 */
public class EffectiveStaffControllerTest extends WebTestCase{
	EffectiveStaffController controller;
	ClinicalStaffRepository clinicalStaffRepository;
	private final static String FORM_VIEW = "clinicalStaff/effectiveStaff";
	private final static String CLINICAL_STAFF_ID = "cId";
	private final static String VIEW_NAME = "../searchClinicalStaff";
    private final static String CLINICAL_STAFF_SEARCH_STRING = "clinicalStaffSearchString";
    private final static String USER_SEARCH_STRING = "userSearchString";
    private final static String BLANK_STRING = "";
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		controller = new EffectiveStaffController();
		request.setParameter(CLINICAL_STAFF_ID, "10");
		clinicalStaffRepository = registerMockFor(ClinicalStaffRepository.class);
		request.getSession().setAttribute(CLINICAL_STAFF_SEARCH_STRING, BLANK_STRING);
	}
	
	public void testEffectiveStaffController(){
		controller = new EffectiveStaffController();
		assertEquals(ClinicalStaff.class, controller.getCommandClass());
		assertEquals(FORM_VIEW, controller.getFormView());
		assertTrue(controller.isSessionForm());
	}
	
	
	public void testFormBackingObject() throws Exception{
		ClinicalStaff clinicalStaff;
		expect(clinicalStaffRepository.findById(10)).andReturn(getClinicalStaffStub());
		replayMocks();
		
		controller.setClinicalStaffRepository(clinicalStaffRepository);
		clinicalStaff = (ClinicalStaff) controller.formBackingObject(request);
		
		assertEquals(getClinicalStaffStub(), clinicalStaff);
		verifyMocks();
	}
	
	public void testOnSubmit_withoutSearchString() throws Exception{
		ModelAndView modelAndView;
		ClinicalStaff clinicalStaff = getClinicalStaffStub();
		controller.setClinicalStaffRepository(clinicalStaffRepository);
		expect(clinicalStaffRepository.save(clinicalStaff)).andReturn(clinicalStaff);
		
		replayMocks();
		modelAndView = controller.onSubmit(request, response, clinicalStaff, new BindException(controller.getCommandClass(), controller.getCommandName()));
		verifyMocks();
		
		assertEquals(VIEW_NAME, ((RedirectView) modelAndView.getView()).getUrl());
	}
	
	public void testOnSubmit_withSearchString() throws Exception{
		ModelAndView modelAndView;
		ClinicalStaff clinicalStaff = getClinicalStaffStub();
		controller.setClinicalStaffRepository(clinicalStaffRepository);
		request.getSession().setAttribute(CLINICAL_STAFF_SEARCH_STRING, USER_SEARCH_STRING);
		expect(clinicalStaffRepository.save(clinicalStaff)).andReturn(clinicalStaff);
		
		replayMocks();
		modelAndView = controller.onSubmit(request, response, clinicalStaff, new BindException(controller.getCommandClass(), controller.getCommandName()));
		verifyMocks();
		
		assertEquals(VIEW_NAME + "?searchString=" + USER_SEARCH_STRING, ((RedirectView) modelAndView.getView()).getUrl());
	}
	
	
	private ClinicalStaff getClinicalStaffStub(){
		ClinicalStaff clinicalStaff = new ClinicalStaff();
		clinicalStaff.addOrganizationClinicalStaff(new OrganizationClinicalStaff());
		for(OrganizationClinicalStaff ocs : clinicalStaff.getOrganizationClinicalStaffs()){
			ocs.getStudyOrganizationClinicalStaff().add(new StudyOrganizationClinicalStaff());
		}
		clinicalStaff.setUser(new User());
		return clinicalStaff;
	}
}
