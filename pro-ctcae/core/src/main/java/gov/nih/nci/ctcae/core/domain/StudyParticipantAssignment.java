package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "study_participant_assignments")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_participant_assignments_id")})
public class StudyParticipantAssignment extends BaseVersionable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@Column(name = "study_participant_identifier", nullable = false)
	private String studyParticipantIdentifier;

	@JoinColumn(name = "participant_id", referencedColumnName = "id")
	@ManyToOne
	private Participant participant;

	@JoinColumn(name = "study_site_id", referencedColumnName = "id")
	@ManyToOne
	private StudyOrganization studySite;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studyParticipantAssignment", fetch = FetchType.LAZY)
	private List<StudyParticipantCrf> studyParticipantCrfs = new ArrayList<StudyParticipantCrf>();

	public StudyParticipantAssignment() {
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStudyParticipantIdentifier() {
		return studyParticipantIdentifier;
	}

	public void setStudyParticipantIdentifier(String studyParticipantIdentifier) {
		this.studyParticipantIdentifier = studyParticipantIdentifier;
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

	public List<StudyParticipantCrf> getStudyParticipantCrfs() {
		return studyParticipantCrfs;
	}

	public void addStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
		if (studyParticipantCrf != null) {
			studyParticipantCrf.setStudyParticipantAssignment(this);
			studyParticipantCrfs.add(studyParticipantCrf);
		}
	}

}
