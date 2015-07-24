package gov.nih.nci.ctcae.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="system_alerts")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_system_alerts_id")})
public class Alert extends BaseVersionable implements Comparable<Alert>{
	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name="id")
	private Integer id;
	
	@Column(name="start_date")
	private Date startDate;


	@Column(name="end_date")
	private Date endDate;

	@Column(name="alert_status")
	@Enumerated(EnumType.STRING)
	private AlertStatus alertStatus = AlertStatus.ACTIVE;

	@Column(name="alert_message")
	private String alertMessage;
	
	public Alert copy() {
		Alert copiedAlert = new Alert();
		copiedAlert.setStartDate(this.startDate);
		copiedAlert.setEndDate(this.endDate);
		copiedAlert.setAlertStatus(AlertStatus.ACTIVE);
		copiedAlert.setAlertMessage("Copy of " + this.alertMessage + "_" + System.currentTimeMillis());
		
		return copiedAlert;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		
		if(o == null || getClass() != o.getClass()) return false;
		
		Alert that = (Alert) o;
		
		if(this.alertMessage != null? 
				!alertMessage.equals(that.alertMessage) : that.alertMessage != null) {
			return false;
		}
		
		if(this.alertStatus != null? 
				!alertStatus.equals(that.alertStatus) : that.alertStatus != null) {
			return false;
		}
		
		if(this.startDate != null? 
				!this.startDate.equals(that.startDate) : that.startDate != null) {
			return false;
		}
		
		if(this.endDate != null? 
				!this.endDate.equals(that.endDate) : that.endDate != null) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		 int result = alertMessage != null ? alertMessage.hashCode() : 0;
		 
		 result = 31 * result + (alertStatus != null ? alertStatus.hashCode() : 0);
		 result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
		 result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
		 
		 return result;
	}
	
	@Override
	public int compareTo(Alert o) {
		return alertMessage.toLowerCase().compareTo(o.getAlertMessage().toLowerCase());
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public AlertStatus getAlertStatus() {
		return alertStatus;
	}

	public Date getEndDate() {
		return endDate;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}
	
	public void setAlertStatus(AlertStatus alertStatus) {
		this.alertStatus = alertStatus;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
