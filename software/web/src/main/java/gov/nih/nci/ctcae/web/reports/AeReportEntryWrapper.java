package gov.nih.nci.ctcae.web.reports;

import java.io.Serializable;
import java.util.Date;

/**AeReportEntryWrapper class
 * @author AmeyS
 * Used in generating ACCRU AE Report.
 * Serves as a wrapper object holding data for each of the AE entry displayed in the adverse events table in pdf report.
 */
public class AeReportEntryWrapper implements Serializable{
	private String id;
	private String ctcaeTerm;
	private String meddraCode;
	private String proCtcTerm;
	private Date startDate;
	private Date endDate;
	private String grade;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCtcaeTerm() {
		return ctcaeTerm;
	}
	public void setCtcaeTerm(String symptom) {
		this.ctcaeTerm = symptom;
	}
	public String getMeddraCode() {
		return meddraCode;
	}
	public void setMeddraCode(String meddraCode) {
		this.meddraCode = meddraCode;
	}
	
	public String getProCtcTerm() {
		return proCtcTerm;
	}
	public void setProCtcTerm(String proCtcTerm) {
		this.proCtcTerm = proCtcTerm;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
}
