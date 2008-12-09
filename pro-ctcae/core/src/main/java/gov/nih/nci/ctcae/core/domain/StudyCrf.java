package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "study_crfs")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_crfs_id")})

public class StudyCrf extends BaseVersionable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@JoinColumn(name = "crf_id", referencedColumnName = "id")
	@OneToOne
	private CRF crf;

	@JoinColumn(name = "study_id", referencedColumnName = "id")
	@ManyToOne
	private Study study;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studyCrf", fetch = FetchType.LAZY)
	private Collection<StudyParticipantCrf> studyParticipantCrfs = new ArrayList<StudyParticipantCrf>();

	public StudyCrf() {
	}

	public StudyCrf(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CRF getCrf() {
		return crf;
	}

	public void setCrf(CRF crf) {
		this.crf = crf;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Collection<StudyParticipantCrf> getStudyParticipantCrfs() {
		return studyParticipantCrfs;
	}

	public void addStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
		if (studyParticipantCrf != null) {
			studyParticipantCrf.setStudyCrf(this);
			studyParticipantCrfs.add(studyParticipantCrf);
		}
	}

	public StudyCrf getCopy() {

		StudyCrf copiedStudyCrf = new StudyCrf();
		copiedStudyCrf.setStudy(study);
		CRF copiedCrf = crf.getCopy();
		copiedStudyCrf.setCrf(copiedCrf);
		copiedCrf.setStudyCrf(copiedStudyCrf);
		return copiedStudyCrf;


	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crf == null) ? 0 : crf.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((study == null) ? 0 : study.hashCode());
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
		StudyCrf other = (StudyCrf) obj;
		if (crf == null) {
			if (other.crf != null)
				return false;
		} else if (!crf.equals(other.crf))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (study == null) {
			if (other.study != null)
				return false;
		} else if (!study.equals(other.study))
			return false;
		return true;
	}

}
