package gov.nih.nci.ctcae.core.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

    	Calendar prefTimeSetByParticipant = Calendar.getInstance(TimeZone.getTimeZone(studyParticipantAssignment.getCallTimeZone()));
    	prefTimeSetByParticipant.setTime(studyParticipantCrfScheduleDate);
    	if(studyParticipantAssignment.getCallAmPm().equalsIgnoreCase("am")){
    		prefTimeSetByParticipant.set(Calendar.AM_PM, Calendar.AM);
    	} else {
    		prefTimeSetByParticipant.set(Calendar.AM_PM, Calendar.PM);
    	}
    	//In AM_PM mode 12 is equivalent to 0. Hours run from 0-11 only.
    	if(studyParticipantAssignment.getCallHour() == 12){
    		prefTimeSetByParticipant.set(Calendar.HOUR, 0);
    	} else {
    		prefTimeSetByParticipant.set(Calendar.HOUR, studyParticipantAssignment.getCallHour());
    	}
    	
    	prefTimeSetByParticipant.set(Calendar.MINUTE, studyParticipantAssignment.getCallMinute());

    	Calendar prefDate = Calendar.getInstance();
    	prefDate.setTimeInMillis(prefTimeSetByParticipant.getTimeInMillis());
    	
    	this.preferredCallTime = prefDate.getTime();
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
