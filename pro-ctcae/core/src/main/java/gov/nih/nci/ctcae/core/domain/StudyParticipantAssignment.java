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
@Table(name = "study_participant_assignments")
public class StudyParticipantAssignment extends BaseVersionable {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@JoinColumn(name = "participant_id", referencedColumnName = "id")
	@ManyToOne
	private Participant participant;

	@JoinColumn(name = "study_site_id", referencedColumnName = "id")
	@ManyToOne
	private StudyOrganization studySite;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studyParticipant")
	private Collection<StudyParticipantCrf> studyParticipantCrfs = new ArrayList<StudyParticipantCrf>();

	public StudyParticipantAssignment() {
	}

	public StudyParticipantAssignment(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public StudyOrganization getStudySite() {
		return studySite;
	}

	public void setStudySite(StudyOrganization studySite) {
		this.studySite = studySite;
	}

	public Collection<StudyParticipantCrf> getStudyParticipantCrfs() {
		return studyParticipantCrfs;
	}

	public void addStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
		if (studyParticipantCrf != null) {
			studyParticipantCrf.setStudyParticipant(this);
			studyParticipantCrfs.add(studyParticipantCrf);
		}
	}

	public void addStudyParticipantCrfs(
			List<StudyParticipantCrf> studyParticipantCrfs) {
		for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
			addStudyParticipantCrf(studyParticipantCrf);
		}
	}

	public void removeStudyParticipantCrf(
			StudyParticipantCrf studyParticipantCrf) {
		if (studyParticipantCrf != null) {
			studyParticipantCrfs.remove(studyParticipantCrf);
		}
	}

	public void removeStudyParticipantCrfs(
			List<StudyParticipantCrf> studyParticipantCrfs) {
		for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
			removeStudyParticipantCrf(studyParticipantCrf);
		}
	}

}
