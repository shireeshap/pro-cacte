package gov.nih.nci.ctcae.web.reports;

import java.io.Serializable;
import java.util.HashSet;

/**Class GradeValueWrapper
 * @author AmeyS
 * This class is used as valueObject in ACCRU AE report generation.
 * It stores the evaluated ctcae grade for a ctcae term and set of proCtcTerms contributing to that grade.
 */
public class GradeValueWrapper implements Serializable{
	private String grade;
	private HashSet<String> proCtcTerms = new HashSet<String>();
	
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public HashSet<String> getProCtcTerms() {
		return proCtcTerms;
	}
	public void setProCtcTerms(HashSet<String> proCtcTerms) {
		this.proCtcTerms = proCtcTerms;
	}
}