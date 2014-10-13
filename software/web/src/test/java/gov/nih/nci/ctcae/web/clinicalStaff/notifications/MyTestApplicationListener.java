package gov.nih.nci.ctcae.web.clinicalStaff.notifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**MyTestApplicationListener class.
 * @author Amey
 * Test listener to test proctcae ApplicationEvent classes.
 */
public  class MyTestApplicationListener implements ApplicationListener, Ordered{
	List<ApplicationEvent> receivedApplicationEvents = new ArrayList<ApplicationEvent>();
	
	MyTestApplicationListener(){
		
	}
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		receivedApplicationEvents.add(event);
	}
	
	public List<ApplicationEvent> getReceivedApplicationEvents(){
		return receivedApplicationEvents;
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
