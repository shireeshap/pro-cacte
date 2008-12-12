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
@Table(name = "study_organizations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_organizations_id")})
public abstract class StudyOrganization extends BasePersistable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studySite", fetch = FetchType.LAZY)
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