package gov.nih.nci.ctcae.web.clinicalStaff;

import org.springframework.web.servlet.ModelAndView;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.UserNotification;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.expect;

/**
 * @author Amey
 * NotificationDetailsControllerTest class
 */
public class NotificationDetailsControllerTest extends WebTestCase{
	NotificationDetailsController controller;
	GenericRepository genericRepository;
	private final static String ID = "id";
	private final static String VIEW_NAME = "clinicalStaff/notification";
	private final static String NOTIFICATION = "notification";
	private final static String SCHEDULE_ID = "spCrfId";
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		genericRepository = registerMockFor(GenericRepository.class);
		controller = new NotificationDetailsController();
	}
	
	public void testHandleRequestInternal_nullId(){
		request.setParameter(ID, "");
		try{
			controller.handleRequestInternal(request, response);
		}catch (NumberFormatException e) {
			assertNotNull(e);
		}catch (Exception e) {
			assertNull("NumberFormatException expected", e);
		}
	}
	
	public void testHandleRequestInternal_NotificationId_NotFound(){
		request.setParameter(ID, "1");
		expect(genericRepository.findById(UserNotification.class, 1)).andReturn(null);
		controller.setGenericRepository(genericRepository);
		
		replayMocks();
		try{
			controller.handleRequestInternal(request, response);
		}catch (NullPointerException e) {
			assertNotNull(e);
		}catch (Exception e) {
			assertNull("NullPointerException", e);
		}
		verifyMocks();
	}
	
	public void testHandleRequestInternal(){
		UserNotification userNotification = mockUserNotificationObject();
		ModelAndView modelAndView = null;
		request.setParameter(ID, "1");
		expect(genericRepository.findById(UserNotification.class, 1)).andReturn(userNotification);
		expect(genericRepository.save(isA(UserNotification.class))).andReturn(userNotification);
		controller.setGenericRepository(genericRepository);
		
		replayMocks();
		try{
			modelAndView = controller.handleRequestInternal(request, response);
		}catch (NullPointerException e) {
			assertNotNull(e);
		}catch (Exception e) {
			assertNull("NullPointerException", e);
		}
		verifyMocks();
		
		assertEquals(VIEW_NAME, modelAndView.getViewName());
		assertEquals(userNotification, modelAndView.getModelMap().get(NOTIFICATION));
		assertEquals("2", modelAndView.getModelMap().get(SCHEDULE_ID));
	}
	
	private UserNotification mockUserNotificationObject(){
		UserNotification userNotification = new UserNotification();
		StudyParticipantCrfSchedule spcrfs = new StudyParticipantCrfSchedule();
		spcrfs.setId(2);
		userNotification.setStudyParticipantCrfSchedule(spcrfs);
		return userNotification;
	}
}
