package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "study_participant_crfs")
public class StudyParticipantCrf extends BaseVersionable {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studyParticipantCrf")
	private Collection<StudyParticipantCrfItem> studyParticipantCrfItems = new ArrayList<StudyParticipantCrfItem>();

	@JoinColumn(name = "study_crf_id", referencedColumnName = "id")
	@ManyToOne
	private StudyCrf studyCrf;

	@JoinColumn(name = "study_participant_id", referencedColumnName = "id")
	@ManyToOne
	private StudyParticipantAssignment studyParticipant;

	public StudyParticipantCrf() {
	}

	public StudyParticipantCrf(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Collection<StudyParticipantCrfItem> getStudyParticipantCrfItems() {
		return studyParticipantCrfItems;
	}

	public void addStudyParticipantCrfItem(
			StudyParticipantCrfItem studyParticipantCrfItem) {
		if (studyParticipantCrfItem != null) {
			studyParticipantCrfItem.setStudyParticipantCrf(this);
			studyParticipantCrfItems.add(studyParticipantCrfItem);
		}
	}

	public void addStudyParticipantCrfItems(
			List<StudyParticipantCrfItem> studyParticipantCrfItems) {
		for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfItems) {
			addStudyParticipantCrfItem(studyParticipantCrfItem);
		}
	}

	public void removeStudyParticipantCrfItem(
			StudyParticipantCrfItem studyParticipantCrfItem) {
		if (studyParticipantCrfItem != null) {
			studyParticipantCrfItems.remove(studyParticipantCrfItem);
		}
	}

	public void removeStudyParticipantCrfItems(
			List<StudyParticipantCrfItem> studyParticipantCrfItems) {
		for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfItems) {
			removeStudyParticipantCrfItem(studyParticipantCrfItem);
		}
	}

	public StudyCrf getStudyCrf() {
		return studyCrf;
	}

	public void setStudyCrf(StudyCrf studyCrf) {
		this.studyCrf = studyCrf;
	}

	public StudyParticipantAssignment getStudyParticipant() {
		return studyParticipant;
	}

	public void setStudyParticipant(StudyParticipantAssignment studyParticipant) {
		this.studyParticipant = studyParticipant;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((studyCrf == null) ? 0 : studyCrf.hashCode());
		result = prime
				* result
				+ ((studyParticipant == null) ? 0 : studyParticipant.hashCode());
		result = prime
				* result
				+ ((studyParticipantCrfItems == null) ? 0
						: studyParticipantCrfItems.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudyParticipantCrf other = (StudyParticipantCrf) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (studyCrf == null) {
			if (other.studyCrf != null)
				return false;
		} else if (!studyCrf.equals(other.studyCrf))
			return false;
		if (studyParticipant == null) {
			if (other.studyParticipant != null)
				return false;
		} else if (!studyParticipant.equals(other.studyParticipant))
			return false;
		if (studyParticipantCrfItems == null) {
			if (other.studyParticipantCrfItems != null)
				return false;
		} else if (!studyParticipantCrfItems
				.equals(other.studyParticipantCrfItems))
			return false;
		return true;
	}

}
