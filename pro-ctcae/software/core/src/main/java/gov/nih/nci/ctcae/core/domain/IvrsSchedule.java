package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * The Class IvrsSchedule. 
 * Maintains the IVRS outgoing call details. This is used to determine when a call needs to be placed.
 * 
 * @author Vinay Gangoli
 */
@Entity
@Table(name = "ivrs_schedules")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "SEQ_IVRS_SCHEDULES_ID")})
public class IvrsSchedule extends BasePersistable{
	
    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;
	
    /*
     * Initializes to the total number of attempts to be made and reduces with every attempt to Zero.
     */
	@Column(name = "call_count", nullable = false)
	private int callCount;
	
	/*
	 * This status is pending by default. It is set to Scheduled when put in the JMS queue.
	 * After that its either changed to successful on call completion or updated to a appropriate value when
	 * callCount equals the total number of retries.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "call_status", nullable = false)
	private IvrsCallStatus callStatus;
	
	@Column(name = "next_call_time", nullable = false)
	private Date nextCallTime;
	
	@Column(name = "pref_call_time", nullable = false)
	private Date preferredCallTime;

	@Column(name = "retry_period", nullable = false)
	private int retryPeriod;
	
    @JoinColumn(name = "study_participant_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
	private StudyParticipantAssignment studyParticipantAssignment;
	
    @JoinColumn(name = "spc_schedule_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
	private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

    public IvrsSchedule(){
    	
    }
    
    public IvrsSchedule(StudyParticipantAssignment studyParticipantAssignment, Date studyParticipantCrfScheduleDate){
    	this.studyParticipantAssignment = studyParticipantAssignment;
    	this.callStatus = IvrsCallStatus.PENDING;
    	
    	Date preferredTime = getPreferredTimeInSystemTimeZone(studyParticipantAssignment, studyParticipantCrfScheduleDate);
    	Date actualPreferredTime = DateUtils.getEquivalentSystemTimeForTimeZone(preferredTime, studyParticipantAssignment.getCallTimeZone());
    	this.preferredCallTime = actualPreferredTime;
    	
    	this.nextCallTime = this.preferredCallTime;
    	//retry period is in minutes
    	this.retryPeriod = studyParticipantAssignment.getStudySite().getStudy().getCallBackHour();
    	//number of reminders plus the original call itself
    	if(studyParticipantAssignment.getStudySite().getStudy().getCallBackFrequency() != null){
    		this.callCount = studyParticipantAssignment.getStudySite().getStudy().getCallBackFrequency() + 1;
    	} else {
    		this.callCount = 1;
    	}
    }
    
    /**
     * getPreferredTimeInSystemTimeZone() method
     * @param studyParticipantAssignment
     * @param studyParticipantCrfScheduleDate
     * @return Date
     * Builds date (yyyy-MM-dd hh:mm:ss) in 24 hour format.
     * This date represents participant's preferred call time but converted to the systems's timezone 
     * (i.e actual date evaluated according to the particiant's timezone would differ this date.)
     * e.g if participants preferred time is Nov 4th 2013 7:30:30 am CST (which is Nov 4th 2013 8:30 EST),
     *     this method would return Nov 4th 2013 6:30:30 EST (assuming system's timezone as EST)
     */
    public Date getPreferredTimeInSystemTimeZone(StudyParticipantAssignment studyParticipantAssignment, Date studyParticipantCrfScheduleDate){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(studyParticipantCrfScheduleDate);
    	int callHour;
    	//In AM_PM mode 12 is equivalent to 0. Hours run from 0-11 only.
    	if(studyParticipantAssignment.getCallHour() == 12){
    		callHour = 0;
    	} else {
    		callHour = studyParticipantAssignment.getCallHour();
    	}
    	if(studyParticipantAssignment.getCallAmPm().equalsIgnoreCase("pm")){
    		callHour =  callHour + 12;
    	}
    	calendar.set(Calendar.HOUR_OF_DAY, callHour);
    	calendar.set(Calendar.MINUTE, studyParticipantAssignment.getCallMinute());
    	calendar.set(Calendar.SECOND, 0);
    	
    	return calendar.getTime();
    }
    
	public int getCallCount() {
		return callCount;
	}

	public void setCallCount(int callCount) {
		this.callCount = callCount;
	}

	public IvrsCallStatus getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(IvrsCallStatus callStatus) {
		this.callStatus = callStatus;
	}

	public Date getNextCallTime() {
		return nextCallTime;
	}

	public void setNextCallTime(Date nextCallTime) {
		this.nextCallTime = nextCallTime;
	}

	public Date getPreferredCallTime() {
		return preferredCallTime;
	}

	public void setPreferredCallTime(Date preferredCallTime) {
		this.preferredCallTime = preferredCallTime;
	}
	
	public int getRetryPeriod() {
		return retryPeriod;
	}

	public void setRetryPeriod(int retryPeriod) {
		this.retryPeriod = retryPeriod;
	}

	public StudyParticipantAssignment getStudyParticipantAssignment() {
		return studyParticipantAssignment;
	}

	public void setStudyParticipantAssignment(
			StudyParticipantAssignment studyParticipantAssignment) {
		this.studyParticipantAssignment = studyParticipantAssignment;
	}

	public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
		return studyParticipantCrfSchedule;
	}

	public void setStudyParticipantCrfSchedule(
			StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
		this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


}

