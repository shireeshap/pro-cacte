package gov.nih.nci.ctcae.web.ivrs.callout;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.IvrsCallStatus;
import gov.nih.nci.ctcae.core.domain.IvrsSchedule;
import gov.nih.nci.ctcae.core.repository.IvrsScheduleRepository;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.NoSuchChannelException;
import org.asteriskjava.manager.action.OriginateAction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * The Class IvrsMessageListener.
 *
 * @author Suneel Allareddy, Vinay Gangoli
 * @since March 21, 2011
 */
public class IvrsMessageListener implements MessageListener, ApplicationContextAware {
    
	public static final String ASTERISK_IP = "asterisk.ip";
	public static final String ASTERISK_USERNAME = "asterisk.username";
	public static final String ASTERISK_PASSWORD = "asterisk.password";
	public static final int port = 5038;
	
	protected static final Log logger = LogFactory.getLog(IvrsMessageListener.class);
    
	private IvrsScheduleRepository ivrsScheduleRepository;
	
	protected ApplicationContext applicationContext;
	
	private Properties properties;
     
    static private DefaultAsteriskServer defaultAsteriskServer = null;
    
    public IvrsMessageListener(){
    	
    }
    
    public void onMessage(Message message) {
    	String asteriskIp = properties.getProperty(ASTERISK_IP);
    	String asteriskUsername = properties.getProperty(ASTERISK_USERNAME);
    	String asteriskPassword = properties.getProperty(ASTERISK_PASSWORD);
    	
    	defaultAsteriskServer = new DefaultAsteriskServer(asteriskIp, IvrsMessageListener.port, asteriskUsername, asteriskPassword);
    	
		CallAction callAction = null;
		IvrsSchedule ivrsSchedule = null;
		ivrsScheduleRepository = (IvrsScheduleRepository) applicationContext.getBean("ivrsScheduleRepository");
		//ManagerResponse originateResponse;
		try {
			ObjectMessage msg = (ObjectMessage) message;;
			callAction = (CallAction)msg.getObject();
			Integer ivrsScheduleId = callAction.getIvrsScheduleId();
			ivrsSchedule = ivrsScheduleRepository.findById(ivrsScheduleId);
	        
			DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
	        DataAuditInfo.setLocal(auditInfo);
			
	        logger.debug("*****NEW CALL STARTING****for user-->*" + callAction.getOriginateAction().getChannel());
			OriginateAction originateAction = callAction.getOriginateAction();
			originateAction.setAsync(false);
			//CallerId is mandatory for VOIP
			originateAction.setCallerId("ProCtcAE");

			final AsteriskChannel channel = defaultAsteriskServer.originate(originateAction);
			logger.debug("*****channel created-->> " + channel);
			if(channel != null){
				logger.debug("*****Channel hangup cause-->>>"+ channel.getHangupCauseText());
				if(channel.wasInState(ChannelState.UP)){
					//update to COMPLETED should be done by the stored procedure.
					//ivrsSchedule.setCallStatus(IvrsCallStatus.COMPLETED);
					synchronized (channel){
						channel.addPropertyChangeListener(AsteriskChannel.PROPERTY_STATE, new PropertyChangeListener()
						{
							public void propertyChange(PropertyChangeEvent evt)
							{
								if (evt.getNewValue() == ChannelState.HUNGUP)
								{
									logger.debug("USER HUNG UP --->>>" + ((AsteriskChannel)evt.getSource()).getName());
									channel.notify();
								}
							}
						});

						// not yet hung up?
						if (channel.getState() != ChannelState.HUNGUP){
							// wait for the notification
							channel.wait();
						}
					}
				}
			} else {
				logger.error("The channel returned by the Server was null.");
				ivrsSchedule.setCallStatus(IvrsCallStatus.FAILED);
			}
			logger.debug("*****CALL OVER*****" +  callAction.getOriginateAction().getChannel());
		} catch (JMSException e) {
			if(ivrsSchedule != null){
				ivrsSchedule.setCallStatus(IvrsCallStatus.FAILED);
			}
			e.printStackTrace();
			logger.error("JMSException occurred -->> "+ e  + " --cause-->>" + e.getCause());
			logger.error("*****CALL OVER*****WITH JMSException");
		}catch(ManagerCommunicationException e){
			if(ivrsSchedule != null){
				ivrsSchedule.setCallStatus(IvrsCallStatus.FAILED);
			}
			e.printStackTrace();
			logger.error("ManagerCommunicationException occuerd -->> "+ e  + " --cause-->>" + e.getCause());
			logger.error("*****CALL OVER*****WITH ManagerCommunicationException");
		}catch(NoSuchChannelException e){
			if(ivrsSchedule != null){
				ivrsSchedule.setCallStatus(IvrsCallStatus.FAILED);
			}
			e.printStackTrace();
			logger.error("NoSuchChannelException occuerd -->> "+ e + " --cause-->>" + e.getCause());
			logger.error("*****CALL OVER*****WITH NoSuchChannelException for user-->" + callAction.getOriginateAction().getChannel());
		}
		catch (Exception e){
			if(ivrsSchedule != null){
				ivrsSchedule.setCallStatus(IvrsCallStatus.FAILED);
			}
			e.printStackTrace();
			logger.error("Exception occuerd -->> "+ e + " --cause-->>" + e.getCause());
			logger.error("*****CALL OVER*****WITH EXCEPTION");
		} finally {
			ivrsScheduleRepository.save(ivrsSchedule);
		}
	}

    
	public IvrsScheduleRepository getIvrsScheduleRepository() {
		return ivrsScheduleRepository;
	}

	public void setIvrsScheduleRepository(
			IvrsScheduleRepository ivrsScheduleRepository) {
		this.ivrsScheduleRepository = ivrsScheduleRepository;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
