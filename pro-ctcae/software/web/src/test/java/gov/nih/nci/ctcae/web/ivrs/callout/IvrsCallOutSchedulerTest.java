package gov.nih.nci.ctcae.web.ivrs.callout;

import gov.nih.nci.ctcae.core.SetupStatus;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.study.StudyController;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;

/**
 * The Class IvrsCallOutTest.
 *
 * @author Suneel Allareddy
 * @since March 21, 2011
 */
public class IvrsCallOutSchedulerTest extends WebTestCase {
    JmsTemplate jmsTemplate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }
    public void testCallout(){
        LoadSpringApplicationContext();
		try {
			producer();
		} catch (JMSException e) {
			System.out.print("exception: " + e);
			e.printStackTrace();
		}catch (InterruptedException e) {
			System.out.print("exception: " + e);
			e.printStackTrace();
		}
    }
    private String[] getConfigLocationsA() {
        return new String[]{
                "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml"
            , "classpath*:gov/nih/nci/ctcae/core/testapplicationContext-util.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-setup.xml"
            , "classpath*:gov/nih/nci/ctcae/core/applicationContext-core-security.xml"
            //, "classpath*:gov/nih/nci/ctcae/web/src/main/webapp/WEB-INF/spring-servlet.xml"
        };

    }

    private void LoadSpringApplicationContext()
	{
		// open/read the application context file
		//ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(getConfigLocationsA());
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:gov/nih/nci/ctcae/web/src/main/webapp/WEB-INF/spring-jms.xml");
        //CallAction action = (CallAction)ctx.getBean("action");
       SetupStatus setupStatus = (SetupStatus) ctx.getBean("action");
        assertFalse("Initial setup is not required. .", setupStatus.isSetupNeeded());

        //StudyController studyController =   (StudyController)ctx.getBean("studyController");
		jmsTemplate = (JmsTemplate)ctx.getBean("consumerJmsTemplate");

	}

	private void producer() throws JMSException,InterruptedException{
		ConnectionFactory connectionFactory = jmsTemplate.getConnectionFactory();
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("IVRS.DESTINATION");
		MessageProducer producer = session.createProducer(destination);

		// We will send a small text message saying 'Hello' in Japanese
	//	TextMessage message = session.createTextMessage("this is test message");

		ObjectMessage msg1 = session.createObjectMessage();
		msg1.setObject(new CallAction("" + 1, "SIP/oneuser", "outgoing1", "100", 1, 5000000, 1) );
	//	msg1.setJMSType(this.CMD_TYPE_MESSAGETYPE_OBJECT);

		ObjectMessage msg2 = session.createObjectMessage();
		msg2.setObject(new CallAction("" + 1, "SIP/twouser", "outgoing1", "100", 1, 5000000, 1));

		ObjectMessage msg3 = session.createObjectMessage();
		msg3.setObject(new CallAction("" + 1, "SIP/oneuser", "outgoing1", "100", 1, 5000000, 1));
		// Here we are sending the message!
		producer.send(msg1);
        // Thread.sleep(60*1000);
        //Thread.sleep(16*60*60*1000);
		producer.send(msg2);
		producer.send(msg3);
		System.out.println("Sent message------------>>>> '");

		connection.close();
	}
}
