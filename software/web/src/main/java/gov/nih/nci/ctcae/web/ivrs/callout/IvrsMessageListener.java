package gov.nih.nci.ctcae.web.ivrs.callout;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.IvrsCallStatus;
import gov.nih.nci.ctcae.core.domain.IvrsSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.repository.IvrsScheduleRepository;
import gov.nih.nci.ctcae.web.ivrs.callout.CallAction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

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
	public static final String IVRS_CALLER_ID = "ivrs.callerid";
	public static final int port = 5038;
	
	//values like 21:00 and 04:59
	public static final String BLACKOUT_START = "ivrs.blackout.start";
	public static final String BLACKOUT_END = "ivrs.blackout.end";

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
    	String callerId = properties.getProperty(IVRS_CALLER_ID) == null? "PRO-CTCAE" : properties.getProperty(IVRS_CALLER_ID);
    	
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
	        
			if(isCurrentTimeInBlackoutRange(ivrsSchedule) || 
					isCallASpilloverFromYesterdayOrEarlier(ivrsSchedule)){
				return;
			}
			
			DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
	        DataAuditInfo.setLocal(auditInfo);
			
	        logger.error("*****NEW CALL STARTING****for user-->*" + callAction.getOriginateAction().getChannel());
	        logger.error("Placing call for schedule Id: " + callAction.getIvrsScheduleId());
	        
			OriginateAction originateAction = callAction.getOriginateAction();
			originateAction.setAsync(false);
			//CallerId is mandatory for VOIP
			originateAction.setCallerId(callerId);

			final AsteriskChannel channel = defaultAsteriskServer.originate(originateAction);
			logger.error("*****channel created-->> " + channel);
			if(channel != null){
				logger.debug("*****Channel hangup cause-->>>"+ channel.getHangupCauseText());
				if(channel.wasInState(ChannelState.UP)){
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
				markScheduleAsFailed(ivrsSchedule);
			}
			logger.error("*****CALL OVER*****" +  callAction.getOriginateAction().getChannel());
		} catch (JMSException e) {
			if(ivrsSchedule != null){
				markScheduleAsFailed(ivrsSchedule);
			}
			e.printStackTrace();
			logger.error("JMSException occurred -->> "+ e  + " --cause-->>" + e.getCause());
			logger.error("*****CALL OVER*****WITH JMSException");
		}catch(ManagerCommunicationException e){
			if(ivrsSchedule != null){
				markScheduleAsFailed(ivrsSchedule);
			}
			e.printStackTrace();
			logger.error("ManagerCommunicationException occurred -->> "+ e  + " --cause-->>" + e.getCause());
			logger.error("*****CALL OVER*****WITH ManagerCommunicationException");
		}catch(NoSuchChannelException e){
			if(ivrsSchedule != null){
				markScheduleAsFailed(ivrsSchedule);
			}
			e.printStackTrace();
			logger.error("NoSuchChannelException occurred -->> "+ e + " --cause-->>" + e.getCause());
			logger.error("*****CALL OVER*****WITH NoSuchChannelException for user-->" + callAction.getOriginateAction().getChannel());
		}
		catch (Exception e){
			if(ivrsSchedule != null){
				markScheduleAsFailed(ivrsSchedule);
			}
			e.printStackTrace();
			logger.error("Exception occurred -->> "+ e + " --cause-->>" + e.getCause());
			logger.error("*****CALL OVER*****WITH EXCEPTION");
		} finally {
			//update to COMPLETED for all the ivrsSchedules related to the spCrfSchedule.
			ivrsScheduleRepository.refresh(ivrsSchedule);
			if(ivrsSchedule.getStudyParticipantCrfSchedule().getStatus().equals(CrfStatus.COMPLETED)){
				for(IvrsSchedule iSchedule : ivrsSchedule.getStudyParticipantCrfSchedule().getIvrsSchedules()){
					iSchedule.setCallStatus(IvrsCallStatus.COMPLETED);
					ivrsScheduleRepository.save(iSchedule);
				}
			}
		}
	}

    
	/**
	 * Checks if the call is a spillover from yesterday or earlier. This can happen in case of an outage. 
	 * If call was originally scheduled for yesterday or earlier but was backed up in the queue due to an outage then do not make it.
	 * Note that the callCount is not reverted in this case.
	 *
	 * @param spa the spa
	 * @return true, if is call a spillover from yesterday or earlier
	 */
	private boolean isCallASpilloverFromYesterdayOrEarlier(IvrsSchedule ivrsSchedule ) {
		boolean returnBool = false;
		
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_MONTH);
		
		cal.setTime(ivrsSchedule.getNextCallTime());
		int scheduled = cal.get(Calendar.DAY_OF_MONTH);
		
		logger.error("NextCallTime: " + cal.getTime());
		logger.error("Today: " + today + " scheduled: " + scheduled);
		if(scheduled != today && (Math.abs(scheduled - today) > 1)){
			logger.error("Aborting call for ivrsSchedule.id="+ ivrsSchedule.getId() + ". Reason: Call not originally scheduled for today.");
			returnBool = true;
		}
		return returnBool;
	}

	/**
	 * Checks if is current time in the Participant's timezone is in blackout range.
	 *
	 * @return true, if is current time in blackout range
	 */
	private boolean isCurrentTimeInBlackoutRange(IvrsSchedule ivrsSchedule) {
		
		StudyParticipantAssignment spa = ivrsSchedule.getStudyParticipantAssignment();
		boolean blackoutIndicator = false;
		String blackoutStartTime = properties.getProperty(BLACKOUT_START);
    	String blackoutEndTime = properties.getProperty(BLACKOUT_END);
    	
    	String [] sTokens = blackoutStartTime.split(":");
    	int hhStart, mmStart;
    	hhStart = Integer.valueOf(sTokens[0]);
    	mmStart = Integer.valueOf(sTokens[1]);
    	
    	String [] eTokens = blackoutEndTime.split(":");
    	int hhEnd, mmEnd;
    	hhEnd = Integer.valueOf(eTokens[0]);
    	mmEnd = Integer.valueOf(eTokens[1]);
    	
    	boolean isSameDay = isSameDay(hhStart, mmStart, hhEnd, mmEnd);
    	Calendar currentTimeInParticipantTimeZone = Calendar.getInstance(TimeZone.getTimeZone(spa.getCallTimeZone()));
    	
    	logger.error("Vaue of isSame:" + isSameDay);
    	logger.error("Patient time hourOfDay: " + currentTimeInParticipantTimeZone.get(Calendar.HOUR_OF_DAY));
    	logger.error("Patient time minutesOfDay: " + currentTimeInParticipantTimeZone.get(Calendar.MINUTE));
    	logger.error("Blackout parameters, hhStart: " + hhStart + " hhEnd: " + hhEnd + " mmStart: " + mmStart + " mmEnd:" + mmEnd);
    	
    	//isSameDay == false means timings like 21:00 to 04:59
    	if(!isSameDay){
    		if(currentTimeInParticipantTimeZone.get(Calendar.HOUR_OF_DAY) > hhStart || currentTimeInParticipantTimeZone.get(Calendar.HOUR_OF_DAY) < hhEnd){
    			logger.error("Aborting call for ivrsSchedule.id="+ ivrsSchedule.getId() + ".  Call hour in blackout range (" + blackoutStartTime + "-" + blackoutEndTime + ").");
    			blackoutIndicator = true;
    		} else if(currentTimeInParticipantTimeZone.get(Calendar.HOUR_OF_DAY) == hhStart){
    			if(currentTimeInParticipantTimeZone.get(Calendar.MINUTE) >= mmStart){
    				logger.error("Aborting call for ivrsSchedule.id="+ ivrsSchedule.getId() + ". Call min in blackout range (" + blackoutStartTime + "-" + blackoutEndTime + ").");
    				blackoutIndicator = true;
    			}
    		} else if(currentTimeInParticipantTimeZone.get(Calendar.HOUR_OF_DAY) == hhEnd){
    			if(currentTimeInParticipantTimeZone.get(Calendar.MINUTE) <= mmEnd){
    				logger.error("Aborting call for ivrsSchedule.id="+ ivrsSchedule.getId() +". Call min in blackout range (" + blackoutStartTime + "-" + blackoutEndTime + ").");
    				blackoutIndicator = true;
    			}
    		}    		
    	} else {
        	//isSameDay == true means timings like 01:00 to 04:59
    		if(currentTimeInParticipantTimeZone.get(Calendar.HOUR_OF_DAY) > hhStart && currentTimeInParticipantTimeZone.get(Calendar.HOUR_OF_DAY) < hhEnd){
    			logger.error("Aborting same day call for ivrsSchedule.id="+ ivrsSchedule.getId() + ". Call hour in blackout range (" + blackoutStartTime + "-" + blackoutEndTime + ").");
    			blackoutIndicator = true;
    		} else if(currentTimeInParticipantTimeZone.get(Calendar.HOUR_OF_DAY) == hhStart || currentTimeInParticipantTimeZone.get(Calendar.HOUR_OF_DAY) == hhEnd){
    			if(currentTimeInParticipantTimeZone.get(Calendar.MINUTE) >= mmStart && currentTimeInParticipantTimeZone.get(Calendar.MINUTE) <= mmEnd){
    				logger.error("Aborting same day call for ivrsSchedule.id="+ ivrsSchedule.getId() + ". Call min in blackout range (" + blackoutStartTime + "-" + blackoutEndTime + ").");
    				blackoutIndicator = true;
    			}
    		}
    	}
		
    	logger.error("IvrsMessageListener blackoutIndicator value: " + blackoutIndicator);
		return blackoutIndicator;
	}

	/**
	 * Based on the start and end of the blackout period, determines if the period starts and ends on the same day or 
	 * starts on one day and ends on the next day.
	 *
	 * @param hhStart the hh start
	 * @param mmStart the mm start
	 * @param hhEnd the hh end
	 * @param mmEnd the mm end
	 * @return true, if is same day
	 */
	private boolean isSameDay(int hhStart, int mmStart, int hhEnd, int mmEnd) {
		boolean isSameDay = false;
		if(hhStart > hhEnd){
			isSameDay = false;
		} else if(hhStart < hhEnd){
			isSameDay = true;
		} else {
			if(mmStart > mmEnd){
				isSameDay = false;
			} else if(mmStart < mmEnd){
				isSameDay = true;
			} 
		}
		logger.debug("Same day value evaluated to: "+isSameDay);
		return isSameDay;
	}

	/**
	 * Mark schedule as failed and reset the callcount as the call never went through.
	 *
	 * @param ivrsSchedule the ivrs schedule
	 */
	private void markScheduleAsFailed(IvrsSchedule ivrsSchedule) {
		ivrsScheduleRepository.refresh(ivrsSchedule);
		logger.debug("Updating status and call count for Failed call. IvrsSchedule.id = " + ivrsSchedule.getId());
		ivrsSchedule.setCallStatus(IvrsCallStatus.FAILED);
		//increment the callCount by one as technical failures dont count
		ivrsSchedule.setCallCount(ivrsSchedule.getCallCount() + 1);
		ivrsScheduleRepository.save(ivrsSchedule);
		
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
