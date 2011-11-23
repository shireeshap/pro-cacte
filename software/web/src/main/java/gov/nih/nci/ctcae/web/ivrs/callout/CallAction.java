package gov.nih.nci.ctcae.web.ivrs.callout;
import java.io.Serializable;

import org.asteriskjava.manager.action.OriginateAction;

/**
 * The Class CallAction.
 *
 * @author Suneel Allareddy
 * @since March 21, 2011
 */
public class CallAction implements Serializable{
    private OriginateAction action;
    
    private Integer ivrsScheduleId;
    
    public CallAction(){

    }
    public CallAction(String id, String channel, String context, String extension, int priority, long timeout, int ivrsScheduleId) {
        this.action = new OriginateAction();
        this.action.setActionId(id);
        this.action.setChannel(channel);
        this.action.setContext(context);
        this.action.setExten(extension);
        this.action.setPriority(priority);
        this.action.setTimeout(timeout);
        this.ivrsScheduleId = ivrsScheduleId;
    }

    public OriginateAction getOriginateAction() {
        return action;
    }

    /** Change this to getActionId?
     * @return the actionId for the OrignateAction
     */
    public String getId(){
        return getOriginateAction().getActionId();
    }
	public Integer getIvrsScheduleId() {
		return ivrsScheduleId;
	}
	public void setIvrsScheduleId(Integer ivrsScheduleId) {
		this.ivrsScheduleId = ivrsScheduleId;
	}
}

