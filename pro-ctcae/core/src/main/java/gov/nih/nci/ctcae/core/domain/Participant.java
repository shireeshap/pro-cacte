package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author mehul
 */

@Entity
@Table(name = "participants")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "participants_id_seq") })
public class Participant extends Person {

	@Column(name = "maiden_name", nullable = true)
	private String maidenName;

	@Column(name = "birth_date", nullable = true)
	private Date birthDate;

	@Column(name = "race", nullable = true)
	private String race;

	@Column(name = "ethnicity", nullable = true)
	private String ethnicity;

	@Column(name = "gender", nullable = true)
	private String gender;

	@Column(name = "mrn_identifier", nullable = false)
	private String assignedIdentifier;

	public String getAssignedIdentifier() {
		return assignedIdentifier;
	}

	public void setAssignedIdentifier(String assignedIdentifier) {
		this.assignedIdentifier = assignedIdentifier;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "participant")
	private List<StudyParticipantAssignment> studyParticipantAssignments = new ArrayList<StudyParticipantAssignment>();

	public String getMaidenName() {
		return maidenName;
	}

	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void addStudyParticipantAssignment(StudyParticipantAssignment spa) {
		if (spa != null) {
			spa.setParticipant(this);
			studyParticipantAssignments.add(spa);
		}
	}

	public List<StudyParticipantAssignment> getStudyParticipantAssignments() {
		return studyParticipantAssignments;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Participant))
			return false;
		if (!super.equals(o))
			return false;

		Participant that = (Participant) o;

		if (birthDate != null ? !birthDate.equals(that.birthDate)
				: that.birthDate != null)
			return false;
		if (ethnicity != null ? !ethnicity.equals(that.ethnicity)
				: that.ethnicity != null)
			return false;
		if (gender != null ? !gender.equals(that.gender) : that.gender != null)
			return false;
		if (maidenName != null ? !maidenName.equals(that.maidenName)
				: that.maidenName != null)
			return false;
		if (race != null ? !race.equals(that.race) : that.race != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (maidenName != null ? maidenName.hashCode() : 0);
		result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
		result = 31 * result + (race != null ? race.hashCode() : 0);
		result = 31 * result + (ethnicity != null ? ethnicity.hashCode() : 0);
		result = 31 * result + (gender != null ? gender.hashCode() : 0);
		return result;
	}
}
