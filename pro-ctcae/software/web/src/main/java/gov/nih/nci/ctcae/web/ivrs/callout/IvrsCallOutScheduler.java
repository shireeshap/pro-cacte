package gov.nih.nci.ctcae.web.ivrs.callout;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.IvrsCallStatus;
import gov.nih.nci.ctcae.core.domain.IvrsSchedule;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.IvrsScheduleQuery;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.IvrsScheduleRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;

/**
 * The Class IvrsCallOutTest.
 *
 * @author Suneel Allareddy, Vinay Gangoli
 * @since March 21, 2011
 */
public class IvrsCallOutScheduler implements ApplicationContextAware{
    
	public static final String CONTEXT = "myTest";
	public static final String EXTENSION = "1";
	public static final int PRIORITY = 1;
	public static final long TIMEOUT = 5000000;
	
	public static final String CHANNEL_SOFTPHONE = "SIP/oneUser";
	public static final String CHANNEL_VOIP      = "SIP/sip.broadvoice.com";
	public static final String CHANNEL_PSTN      = "DAHDI/G1";
	
	public static final String MODE_IVRSCALLOUT  = "mode.ivrscallout";
	
	protected static final Log logger = LogFactory.getLog(IvrsCallOutScheduler.class);
    private JmsTemplate jmsTemplate;
    protected ApplicationContext applicationContext;
    private GenericRepository genericRepository;
    private ParticipantRepository participantRepository;
    private IvrsScheduleRepository ivrsScheduleRepository;
    private Properties properties;
    
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    
    /**
     * Schedule job in queue. Master method which uses utility methods below to find 
     * candidate jobs and then push them in the queue.
     */
    public void scheduleJobInQueue(){
    	//applicationContext = (ApplicationContext) scheduler.getContext().get("applicationContext");
    	participantRepository = (ParticipantRepository) applicationContext.getBean("participantRepository");
    	ivrsScheduleRepository = (IvrsScheduleRepository) applicationContext.getBean("ivrsScheduleRepository");
    	genericRepository = (GenericRepository) applicationContext.getBean("genericRepository");

        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);
        
        String ivrsCalloutMode = properties.getProperty(MODE_IVRSCALLOUT);
        
        logger.debug("----------------------------------------------------------------------------\n\r\n\r");
        logger.debug("scheduleJobInQueue starts...." + new Date());
        //LoadSpringApplicationContext();
		try {
			List<IvrsSchedule> ivrScheduleList = getJobsForNextFiveMinutes();
			List<CallAction> callActionList = generateCallActionList(ivrScheduleList, ivrsCalloutMode);
			executeProducer(callActionList);
			updateIvrsScheduleStatus(ivrScheduleList);
		} catch (JMSException e) {
			logger.error("exception: " + e.getMessage());
			e.printStackTrace();
		}catch (InterruptedException e) {
			logger.error("exception: " + e.getMessage());
			e.printStackTrace();
		}
    }

	/**
	 * Update ivrs schedule status.
	 *
	 * @param ivrScheduleList the ivr schedule list
	 */
	private void updateIvrsScheduleStatus(List<IvrsSchedule> ivrScheduleList) {
		for(IvrsSchedule ivrsSchedule : ivrScheduleList){
			//TODO: Should additional checks be performed before updating statuses? update to COMPLETED in the listener?
			ivrsSchedule.setCallStatus(IvrsCallStatus.SCHEDULED);
			ivrsScheduleRepository.save(ivrsSchedule);
		}
	}

	/**
	 * Gets the jobs for next five minutes. Looks up the IvrsSchedule table for picking candidate jobs.
	 *
	 * @return the jobs for next five minutes
	 */
	private List<IvrsSchedule> getJobsForNextFiveMinutes() {
		List<IvrsSchedule> ivrsScheduleList = new ArrayList<IvrsSchedule>();
		Calendar now = Calendar.getInstance();
		Calendar fiveMinutesFromNow = Calendar.getInstance();
		fiveMinutesFromNow.add(Calendar.MINUTE, 5);
		
		//add all ivrsSchedules with Status=PENDING
		IvrsScheduleQuery ivrsScheduleQueryForPendingStatus = new IvrsScheduleQuery();
		ivrsScheduleQueryForPendingStatus.filterByDate(now.getTime(), fiveMinutesFromNow.getTime());
		ivrsScheduleQueryForPendingStatus.filterByStatus(IvrsCallStatus.PENDING);
		ivrsScheduleList.addAll(ivrsScheduleRepository.find(ivrsScheduleQueryForPendingStatus));
		
		//add all ivrsSchedules with Status=SCHEDULED
		IvrsScheduleQuery ivrsScheduleQueryForScheduledStatus = new IvrsScheduleQuery();
		ivrsScheduleQueryForScheduledStatus.filterByDate(now.getTime(), fiveMinutesFromNow.getTime());
		ivrsScheduleQueryForScheduledStatus.filterByStatus(IvrsCallStatus.SCHEDULED);
		ivrsScheduleList.addAll(ivrsScheduleRepository.find(ivrsScheduleQueryForScheduledStatus));
		
		return ivrsScheduleList;
	}

	/**
	 * Generates the callAction list. This is the list of jobs that need to be eventually pushed into the JMS queue.
	 *
	 * @param ivrScheduleList the ivrs schedule list
	 * @return the list
	 */
	private List<CallAction> generateCallActionList(List<IvrsSchedule> ivrScheduleList, String ivrsCalloutMode) {
		
		List<CallAction> callActionList = new ArrayList<CallAction>();
		CallAction callAction = null;
		Participant participant = null;
		ParticipantQuery participantQuery = null;
		String phoneNumber = null;
		
		for(IvrsSchedule ivrsSchedule: ivrScheduleList){
			//Get the participant using the StudyParticipantAssignment and get the phoneNumber from the participant.
			participantQuery = new ParticipantQuery();
			participantQuery.filterByStudyParticipantAssignmentId(ivrsSchedule.getStudyParticipantAssignment().getId());
			Collection<Participant> participantList = genericRepository.find(participantQuery);
			if(participantList.size() > 1){
				logger.error("Got more than 1 participant for a given StudyParticipantAssignment.");
			}
			Iterator<Participant> pIter = participantList.iterator();
			while(pIter.hasNext()){
				participant = pIter.next();
			}
			phoneNumber = participant.getPhoneNumber();
			//phoneNumber = "91"+phoneNumber.getAlldigitsOnlyFromNumber();
			
			//new CallAction(String id, String channel, String context, String extension, int priority, long timeout)
			if(ivrsCalloutMode.equalsIgnoreCase(CHANNEL_SOFTPHONE)){
				callAction = new CallAction("" + 1, CHANNEL_SOFTPHONE, CONTEXT, EXTENSION, PRIORITY, TIMEOUT);
			}
			if(ivrsCalloutMode.equalsIgnoreCase(CHANNEL_VOIP)){
				callAction = new CallAction("" + 1, CHANNEL_VOIP + "/" + "17039081998", CONTEXT, EXTENSION, PRIORITY, TIMEOUT);
			}
			if(ivrsCalloutMode.equalsIgnoreCase(CHANNEL_PSTN)){
				//callAction = new CallAction("" + 1, CHANNEL_PSTN + "/" + phoneNumber, "outgoing1", "100", 1, 5000000);
				callAction = new CallAction("" + 1, CHANNEL_PSTN + "/" + "226", CONTEXT, EXTENSION, PRIORITY, TIMEOUT);
			}
			callActionList.add(callAction);
		}
		return callActionList;
	}

	/**
	 * Executes producer. Actually pushes the callAction jobs into the JMS queue.
	 *
	 * @param callActionList the call action list
	 * @throws JMSException the jMS exception
	 * @throws InterruptedException the interrupted exception
	 */
	private void executeProducer(List<CallAction> callActionList) throws JMSException,InterruptedException{
		
        logger.error("producer starts....");
		ConnectionFactory connectionFactory = jmsTemplate.getConnectionFactory();
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("IVRS.DESTINATION");
		MessageProducer producer = session.createProducer(destination);
		ObjectMessage objectMessage = null;
		
		for(CallAction callAction: callActionList){
			objectMessage = session.createObjectMessage();
			objectMessage.setObject(callAction);
			producer.send(objectMessage);
			logger.debug("Sent message------------" + callAction.getId());
		}
		connection.close();
		
		// We will send a small text message saying 'Hello' in Japanese
		//	TextMessage message = session.createTextMessage("this is test message");
/*		ObjectMessage msg1 = session.createObjectMessage();
		msg1.setObject(new CallAction("" + 1, "SIP/oneuser", "outgoing1", "100", 1, 5000000) );
		//	msg1.setJMSType(this.CMD_TYPE_MESSAGETYPE_OBJECT);
		ObjectMessage msg2 = session.createObjectMessage();
		msg2.setObject(new CallAction("" + 1, "SIP/twouser", "outgoing1", "100", 1, 5000000));
		// Here we are sending the message!
		producer.send(msg1);
        //Thread.sleep(16*60*60*1000);
		producer.send(msg2);
		logger.debug("Sent message------------");
*/
	}

	public ParticipantRepository getParticipantRepository() {
		return participantRepository;
	}

	public void setParticipantRepository(ParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}

	public IvrsScheduleRepository getIvrsScheduleRepository() {
		return ivrsScheduleRepository;
	}

	public void setIvrsScheduleRepository(
			IvrsScheduleRepository ivrsScheduleRepository) {
		this.ivrsScheduleRepository = ivrsScheduleRepository;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	
    
//  private void LoadSpringApplicationContext()
//	{
		// open/read the application context file
		//ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:gov/nih/nci/ctcae/web/src/main/webapp/WEB-INF/spring-jms.xml");
        //CallAction action = (CallAction)ctx.getBean("action");
        //StudyController studyController =   (StudyController)ctx.getBean("studyController");
		//jmsTemplate = (JmsTemplate)ctx.getBean("consumerJmsTemplate");
//	}
	
}


