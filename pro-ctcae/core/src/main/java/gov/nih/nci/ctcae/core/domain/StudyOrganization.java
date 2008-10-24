package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "study_organizations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class StudyOrganization extends BasePersistable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studySite")
	private List<StudyParticipantAssignment> studyParticipantAssignments = new ArrayList<StudyParticipantAssignment>();

	@ManyToOne
	@JoinColumn(name = "organization_id", nullable = false)
	private Organization organization;

	@ManyToOne
	@JoinColumn(name = "study_id", nullable = false)
	private Study study;

	public List<StudyParticipantAssignment> getStudyParticipantAssignments() {
		return studyParticipantAssignments;
	}

	public Study getStudy() {
		return study;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public void setStudy(Study study) {
		this.study = study;
	}
}