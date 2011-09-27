package gov.nih.nci.ctcae.web.ivrs.callout;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.IvrsCallStatus;
import gov.nih.nci.ctcae.core.domain.IvrsSchedule;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.query.IvrsScheduleQuery;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.IvrsScheduleRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
    
	//specified in the datasource.props
	public static final String CHANNEL_SOFTPHONE = "SOFTPHONE";
	public static final String CHANNEL_VOIP      = "VOIP";
	public static final String CHANNEL_PSTN      = "PSTN";
	
	public static final String MODE_IVRSCALLOUT  = "mode.ivrscallout";
	public static final String IVRS_CONTEXT_ENGLISH  = "ivrs.context.english";
	public static final String IVRS_CONTEXT_SPANISH  = "ivrs.context.spanish";
	public static final String IVRS_EXTENSION  = "ivrs.extension";
	public static final String IVRS_PRIORITY  = "ivrs.priority";
	public static final String IVRS_TIMEOUT  = "ivrs.timeout";
	public static final String IVRS_CHANNEL  = "ivrs.channel";
	
	public static final int SCHEDULER_FREQUENCY = 2;
	
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
        logger.debug("scheduleJobInQueue starts...." + new Date());
		try {
			List<IvrsSchedule> ivrScheduleList = getJobsForNextTwoMinutes();
			updateIvrsScheduleStatus(ivrScheduleList);
			List<CallAction> callActionList = generateCallActionList(ivrScheduleList, ivrsCalloutMode);
			executeProducer(callActionList);
		} catch (JMSException e) {
			logger.error("exception: " + e.getMessage());
			e.printStackTrace();
		}catch (InterruptedException e) {
			logger.error("exception: " + e.getMessage());
			e.printStackTrace();
		}
    }


	/**
	 * Gets the jobs for next two minutes. Looks up the IvrsSchedule table for picking candidate jobs.
	 * The repeat interval should be the same as the frequency of the trigger configured in the spring-servlet for the ivrsTrigger.
	 *
	 * @return the jobs for next five minutes
	 */
	private List<IvrsSchedule> getJobsForNextTwoMinutes() {
		List<IvrsSchedule> ivrsScheduleList = new ArrayList<IvrsSchedule>();
		Calendar now = Calendar.getInstance();
		Calendar twoMinutesFromNow = Calendar.getInstance();
		twoMinutesFromNow.add(Calendar.MINUTE, SCHEDULER_FREQUENCY);
		
		//Get all ivrsSchedules with Status=PENDING/Scheduled/Failed
		Set<IvrsCallStatus> statusSet = new HashSet<IvrsCallStatus>(); 
		statusSet.add(IvrsCallStatus.PENDING);
		statusSet.add(IvrsCallStatus.SCHEDULED);
		statusSet.add(IvrsCallStatus.FAILED);
		
		IvrsScheduleQuery ivrsScheduleQuery = new IvrsScheduleQuery();
		ivrsScheduleQuery.filterByDate(now.getTime(), twoMinutesFromNow.getTime());
		ivrsScheduleQuery.filterByStatuses(statusSet);
		
		//ensure the mode hasn't been switched to Non-IVRS as we don't support mixed modality
		ivrsScheduleQuery.filterByStudyParticipantAssignmentMode(AppMode.IVRS);
		//don't get the ones whose callCount is already Zero
		ivrsScheduleQuery.filterByCallCountGreaterThan(0);
		ivrsScheduleList.addAll(ivrsScheduleRepository.find(ivrsScheduleQuery));
		
		logger.debug("Number of calls to make in next 2 minutes: " + ivrsScheduleList.size());
		return ivrsScheduleList;
	}
	

	/**
	 * Update ivrs schedule status.
	 *
	 * @param ivrScheduleList the ivrs schedule list
	 */
	private void updateIvrsScheduleStatus(List<IvrsSchedule> ivrScheduleList) {
		for(IvrsSchedule ivrsSchedule : ivrScheduleList){
			//reduce the callCount by one
			ivrsSchedule.setCallCount(ivrsSchedule.getCallCount() - 1);
			
			//set the next CallTime as PrefCallTime + retry period in minutes
			Calendar nextCallTime = Calendar.getInstance();
			nextCallTime.setTime(ivrsSchedule.getNextCallTime());
			nextCallTime.add(Calendar.MINUTE, ivrsSchedule.getRetryPeriod());
			ivrsSchedule.setNextCallTime(nextCallTime.getTime());
			
			ivrsSchedule.setCallStatus(IvrsCallStatus.SCHEDULED);
			ivrsScheduleRepository.save(ivrsSchedule);
		}
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

        String extension = properties.getProperty(IVRS_EXTENSION);
        int priority = Integer.valueOf(properties.getProperty(IVRS_PRIORITY)).intValue();
        long timeout = Long.valueOf(properties.getProperty(IVRS_TIMEOUT)).longValue();
        String channel = properties.getProperty(IVRS_CHANNEL);
		
        String context = null;
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
			phoneNumber = buildPhoneNumber(participant);
			context = getContext(participant);
			//new CallAction(String id, String channel, String context, String extension, int priority, long timeout, int ivrsScheduleId)
			if(channel.equalsIgnoreCase(CHANNEL_SOFTPHONE)){
				logger.debug("Adding CallAction for SoftPhone....");
				callAction = new CallAction("" + 1, ivrsCalloutMode, context, extension, priority, timeout, ivrsSchedule.getId());
			} else if(channel.equalsIgnoreCase(CHANNEL_VOIP)){
				logger.debug("Adding CallAction for VOIP....");
				callAction = new CallAction("" + 1, ivrsCalloutMode + "/" + phoneNumber, context, extension, priority, timeout, ivrsSchedule.getId());
			} else if(channel.equalsIgnoreCase(CHANNEL_PSTN)){
				logger.debug("Adding CallAction for PSTN....");
				callAction = new CallAction("" + 1, ivrsCalloutMode + "/" + phoneNumber, context, extension, priority, timeout, ivrsSchedule.getId());
			}
			callActionList.add(callAction);
		}
		return callActionList;
	}


	/**
	 * Gets the context for the participant depending on his/her preferred language.
	 *
	 * @param participant the participant
	 * @return the context
	 */
	private String getContext(Participant participant) {
        String contextSpanish = properties.getProperty(IVRS_CONTEXT_SPANISH);
        
        //default to English
        String contextToBeReturned = properties.getProperty(IVRS_CONTEXT_ENGLISH);
        participant = genericRepository.initialize(participant);
        
        StudyParticipantAssignment studyParticipantAssignment = participant.getStudyParticipantAssignments().get(0);
        if(studyParticipantAssignment != null){
        	if(studyParticipantAssignment.getIvrsLanguage() != null && studyParticipantAssignment.getIvrsLanguage().equalsIgnoreCase("SPANISH")){
        			contextToBeReturned = contextSpanish;
            } //else if(studyParticipantAssignment.getHomeWebLanguage() != null && studyParticipantAssignment.getHomeWebLanguage().equalsIgnoreCase("SPANISH")){
            	//look for home Web Lang if ivrsLang is not Spanish.
            	//contextToBeReturned = contextSpanish;
            //}
        }
		return contextToBeReturned;
	}

	/**
	 * Executes producer. Actually pushes the callAction jobs into the JMS queue.
	 *
	 * @param callActionList the call action list
	 * @throws JMSException the jMS exception
	 * @throws InterruptedException the interrupted exception
	 */
	private void executeProducer(List<CallAction> callActionList) throws JMSException,InterruptedException{
		
        logger.debug("producer starts....");
		ConnectionFactory connectionFactory = jmsTemplate.getConnectionFactory();
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("IVRS.DESTINATION");
		MessageProducer producer = session.createProducer(destination);
		ObjectMessage objectMessage = null;
		
		logger.debug("Total messages to be sent: " + callActionList.size());
		for(CallAction callAction: callActionList){
			objectMessage = session.createObjectMessage();
			objectMessage.setObject(callAction);
			producer.send(objectMessage);
			logger.debug("Sent message------------" + callAction.getId());
		}
		logger.debug("producer stops....");
		connection.close();
	}
	

	/**
	 * Builds the phone number. Wont work if number is a 11 digit number including the country code 1.
	 * Basically returns only the digits from the string.
	 * e.g: converts 908-887-0987 to 19088870987
	 *
	 * @param participant the participant
	 * @return the string
	 */
	private String buildPhoneNumber(Participant participant) {
		return "1" + participant.getPhoneNumber().replaceAll( "[^\\d]", "" );
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

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

//  private void LoadSpringApplicationContext()
//	{
		//ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:gov/nih/nci/ctcae/web/src/main/webapp/WEB-INF/spring-jms.xml");
        //CallAction action = (CallAction)ctx.getBean("action");
        //StudyController studyController =   (StudyController)ctx.getBean("studyController");
		//jmsTemplate = (JmsTemplate)ctx.getBean("consumerJmsTemplate");
//	}
	
}


