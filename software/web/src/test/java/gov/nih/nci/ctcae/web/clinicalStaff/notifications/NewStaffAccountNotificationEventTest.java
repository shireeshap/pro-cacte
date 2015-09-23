package gov.nih.nci.ctcae.web.clinicalStaff.notifications;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.StaticApplicationContext;

/**NewStaffAccountNotificationEventTest class.
 * @author Amey
 */
public class NewStaffAccountNotificationEventTest extends AbstractWebIntegrationTestCase{
	ClinicalStaff clinicalStaff;
	private final static String URL = "http://localhost:8080/public/resetPassword";
	private final static String PI_USER_NAME = "ethan.basch@demo.com";
	NewStaffAccountNotificationEvent newStaffAccountNotificationEvent;
	

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		ClinicalStaffQuery query = new ClinicalStaffQuery();
		query.filterByUserName(PI_USER_NAME);
		ClinicalStaffRepository clinicalStaffRepository = new ClinicalStaffRepository();
		clinicalStaffRepository.setGenericRepository(genericRepository);
		clinicalStaff = (ClinicalStaff) clinicalStaffRepository.findSingle(query);
	}
	
	public void testOnApplicationEvent(){
		StaticApplicationContext staticAppContext = new StaticApplicationContext();
		staticAppContext.registerBeanDefinition("myApplicationListener", new RootBeanDefinition(MyTestApplicationListener.class));
		MyTestApplicationListener myApplicationListener = (MyTestApplicationListener) staticAppContext.getBean("myApplicationListener");
		staticAppContext.refresh();
		staticAppContext.addApplicationListener(myApplicationListener);
		
		NewStaffAccountNotificationEvent newStaffAccountNotificationEvent = 
			new NewStaffAccountNotificationEvent(this, URL, clinicalStaff);
		
		staticAppContext.publishEvent(newStaffAccountNotificationEvent);
		
		ApplicationEvent listenedEvent = 
			myApplicationListener.getReceivedApplicationEvents().get(1);
		assertTrue(listenedEvent instanceof NewStaffAccountNotificationEvent);
		assertEquals(URL, ((NewStaffAccountNotificationEvent) listenedEvent).getLink());
		assertEquals(clinicalStaff, ((NewStaffAccountNotificationEvent) listenedEvent).getClinicalStaff());
	}
}
