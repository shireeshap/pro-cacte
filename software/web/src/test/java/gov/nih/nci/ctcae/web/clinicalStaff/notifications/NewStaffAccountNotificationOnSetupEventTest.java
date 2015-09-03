package gov.nih.nci.ctcae.web.clinicalStaff.notifications;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.StaticApplicationContext;

/**NewStaffAccountNotificationOnSetupEventTest class.
 * @author Amey
 */
public class NewStaffAccountNotificationOnSetupEventTest extends AbstractWebIntegrationTestCase{
	ClinicalStaff clinicalStaff;
	private final static String PI_USER_NAME = "ethan.basch@demo.com";
	NewStaffAccountNotificationOnSetupEvent newStaffAccountNotificationOnSetupEvent;
	

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
		
		NewStaffAccountNotificationOnSetupEvent newStaffAccountNotificationOnSetupEvent = 
			new NewStaffAccountNotificationOnSetupEvent(this, PI_USER_NAME, clinicalStaff.getUser().getPassword(), clinicalStaff.getEmailAddress());
		
		staticAppContext.publishEvent(newStaffAccountNotificationOnSetupEvent);
		
		ApplicationEvent listenedEvent = 
			myApplicationListener.getReceivedApplicationEvents().get(1);
		assertTrue(listenedEvent instanceof NewStaffAccountNotificationOnSetupEvent);
		assertEquals(clinicalStaff.getUser().getUsername(), ((NewStaffAccountNotificationOnSetupEvent) listenedEvent).getUserName());
		assertEquals(clinicalStaff.getEmailAddress(), ((NewStaffAccountNotificationOnSetupEvent) listenedEvent).getEmailAddress());
	}
}
