package gov.nih.nci.ctcae.web.ivrs.callout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.asteriskjava.live.*;
import org.asteriskjava.manager.action.OriginateAction;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The Class IvrsMessageListener.
 *
 * @author Suneel Allareddy
 * @since March 21, 2011
 */
public class IvrsMessageListener implements MessageListener {
     protected static final Log logger = LogFactory.getLog(IvrsMessageListener.class);
    static private DefaultAsteriskServer defaultAsteriskServer = new DefaultAsteriskServer("10.10.10.90", 5038, "admin", "admin");
    public void onMessage(Message message) {
		CallAction CallAction = null;
		//	ManagerResponse originateResponse;
		try {
			ObjectMessage msg = (ObjectMessage) message;;
			CallAction = (CallAction)msg.getObject();
			System.out.println("*****NEW CALL STARTING****for user-->*" + CallAction.getOriginateAction().getChannel());
			OriginateAction originateAction = CallAction.getOriginateAction();
			originateAction.setAsync(false);

			final AsteriskChannel channel = defaultAsteriskServer.originate(originateAction);
			logger.error("*****channel created-->> " + channel);
			if(channel != null){
				logger.error("*****Channel hangup cause-->>>"+ channel.getHangupCauseText());
				if(channel.wasInState(ChannelState.UP)){
					synchronized (channel){
						channel.addPropertyChangeListener(AsteriskChannel.PROPERTY_STATE, new PropertyChangeListener()
						{
							public void propertyChange(PropertyChangeEvent evt)
							{
								if (evt.getNewValue() == ChannelState.HUNGUP)
								{
									logger.error("YIPPY!!!USER HANGED UP CALL--->>>" + ((AsteriskChannel)evt.getSource()).getName());
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
			}

			//			originateResponse = defaultAsteriskServer.getManagerConnection().sendAction(CallAction.getOriginateAction());
			//			Logger.error("method call over originateResponse--" + originateResponse);
			logger.error("*****CALL OVER*****" +  CallAction.getOriginateAction().getChannel());
			//LOG.info("Consumed message: " + msg.getText());
		} catch (JMSException e) {			
			e.printStackTrace();
		}catch(ManagerCommunicationException e){
			e.printStackTrace();
			logger.error("ManagerCommunicationException occuerd -->> "+ e  + " --cause-->>" + e.getCause());
			logger.error("*****CALL OVER*****WITH ManagerCommunicationException");
		}catch(NoSuchChannelException e){
			//	e.printStackTrace();
			logger.error("NoSuchChannelException occuerd -->> "+ e + " --cause-->>" + e.getLocalizedMessage());
			logger.error("*****CALL OVER*****WITH NoSuchChannelException for user-->" + CallAction.getOriginateAction().getChannel());
		}
		catch (Exception e){
			e.printStackTrace();
			logger.error("Exception occuerd -->> "+ e);
			logger.error("*****CALL OVER*****WITH EXCEPTION");
		}
	}
}
