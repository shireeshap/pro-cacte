package gov.nih.nci.ctcae.web.ivrs.callout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.util.Date;

/**
 * The Class IvrsCallOutTest.
 *
 * @author Suneel Allareddy
 * @since March 21, 2011
 */
public class IvrsCallOutScheduler {
    protected static final Log logger = LogFactory.getLog(IvrsCallOutScheduler.class);
    private JmsTemplate jmsTemplate;
     public void setJmsTemplate(JmsTemplate jmsTemplate) {
            this.jmsTemplate = jmsTemplate;
        }
    public void testCallout(){
        logger.error("----------------------------------------------------------------------------\n\r\n\r");
        logger.error("testCallout starts...."+new Date());
        LoadSpringApplicationContext();
		try {
			producer();
		} catch (JMSException e) {
			System.out.print("exception: " + e);
			e.printStackTrace();
		}catch (InterruptedException e) {
			logger.error("exception: " + e);
			e.printStackTrace();
		}
    }
    private void LoadSpringApplicationContext()
	{
		// open/read the application context file
		//ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:gov/nih/nci/ctcae/web/src/main/webapp/WEB-INF/spring-jms.xml");
        //CallAction action = (CallAction)ctx.getBean("action");
        //StudyController studyController =   (StudyController)ctx.getBean("studyController");
		//jmsTemplate = (JmsTemplate)ctx.getBean("consumerJmsTemplate");

	}

	private void producer() throws JMSException,InterruptedException{
        logger.error("producer starts....");
		ConnectionFactory connectionFactory = jmsTemplate.getConnectionFactory();
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("IVRS.DESTINATION");
		MessageProducer producer = session.createProducer(destination);

		// We will send a small text message saying 'Hello' in Japanese
	//	TextMessage message = session.createTextMessage("this is test message");

		ObjectMessage msg1 = session.createObjectMessage();
		msg1.setObject(new CallAction("" + 1, "SIP/oneuser", "outgoing1", "100", 1, 5000000) );
	//	msg1.setJMSType(this.CMD_TYPE_MESSAGETYPE_OBJECT);

		ObjectMessage msg2 = session.createObjectMessage();
		msg2.setObject(new CallAction("" + 1, "SIP/twouser", "outgoing1", "100", 1, 5000000));

		ObjectMessage msg3 = session.createObjectMessage();
		msg3.setObject(new CallAction("" + 1, "SIP/oneuser", "outgoing1", "100", 1, 5000000));
		// Here we are sending the message!
		producer.send(msg1);
        // Thread.sleep(60*1000);
        //Thread.sleep(16*60*60*1000);
		producer.send(msg2);
		producer.send(msg3);
        logger.error("Sent message....");
		logger.error("Sent message------------>>>> '");

		connection.close();
	}
}
