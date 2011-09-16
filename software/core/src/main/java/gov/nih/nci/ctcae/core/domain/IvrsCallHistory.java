package gov.nih.nci.ctcae.core.domain;

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
 * The Class IvrsCallHistory.
 * 
 * @author Vinay Gangoli
 */
@Entity
@Table(name = "ivrs_call_history")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "SEQ_IVRS_CALL_HISTORY_ID")})
public class IvrsCallHistory extends BasePersistable{
	
    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "call_status", nullable = true)
	private IvrsCallStatus callStatus;
	
	@Column(name = "call_time", nullable = true)
	private Date callTime;
	
    @JoinColumn(name = "ivrs_schedules_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
	private IvrsSchedule ivrsSchedule;


	public IvrsCallStatus getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(IvrsCallStatus callStatus) {
		this.callStatus = callStatus;
	}

	public Date getCallTime() {
		return callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public IvrsSchedule getIvrsSchedule() {
		return ivrsSchedule;
	}

	public void setIvrsSchedule(IvrsSchedule ivrsSchedule) {
		this.ivrsSchedule = ivrsSchedule;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	

}
