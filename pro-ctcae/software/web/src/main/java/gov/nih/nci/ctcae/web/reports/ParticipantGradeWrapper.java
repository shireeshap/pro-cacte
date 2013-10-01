package gov.nih.nci.ctcae.web.reports;

import java.util.Date;

/**ParticipantGradeWrapper class
 * @author AmeyS
 * This class is used as valueObject in the process for generating ACCRU AE report
 */
public class ParticipantGradeWrapper {
	private Date startDate;
	private Date endDate;
	private String grade;
	private String proCtcTerms;

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
	public String getProCtcTerms() {
		return proCtcTerms;
	}
	public void setProCtcTerms(String proCtcTerms) {
		this.proCtcTerms = proCtcTerms;
	}
}
