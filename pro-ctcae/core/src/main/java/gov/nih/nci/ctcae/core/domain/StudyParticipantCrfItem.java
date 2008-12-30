package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "study_participant_crf_items")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_participant_crf_items_id")})
public class StudyParticipantCrfItem extends BaseVersionable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@JoinColumn(name = "pro_ctc_valid_value_id", referencedColumnName = "id")
	@ManyToOne
	private ProCtcValidValue proCtcValidValue;

	@JoinColumn(name = "crf_item_id", referencedColumnName = "id")
	@ManyToOne
	private CrfPageItem crfPageItem;

	@JoinColumn(name = "SP_CRF_SCHEDULE_ID", referencedColumnName = "id")
	@ManyToOne
	private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

	public StudyParticipantCrfItem() {
	}

	public StudyParticipantCrfItem(Integer id) {
		this.id = id;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ProCtcValidValue getProCtcValidValue() {
		return proCtcValidValue;
	}

	public void setProCtcValidValue(ProCtcValidValue proCtcValidValue) {
		this.proCtcValidValue = proCtcValidValue;
	}

	public CrfPageItem getCrfPageItem() {
		return crfPageItem;
	}

	public void setCrfPageItem(CrfPageItem crfPageItem) {
		this.crfPageItem = crfPageItem;
	}




	public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
		return studyParticipantCrfSchedule;
	}

	public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
		this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final StudyParticipantCrfItem that = (StudyParticipantCrfItem) o;

		if (crfPageItem != null ? !crfPageItem.equals(that.crfPageItem) : that.crfPageItem != null) return false;
		if (proCtcValidValue != null ? !proCtcValidValue.equals(that.proCtcValidValue) : that.proCtcValidValue != null)
			return false;
		if (studyParticipantCrfSchedule != null ? !studyParticipantCrfSchedule.equals(that.studyParticipantCrfSchedule) : that.studyParticipantCrfSchedule != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = proCtcValidValue != null ? proCtcValidValue.hashCode() : 0;
		result = 31 * result + (crfPageItem != null ? crfPageItem.hashCode() : 0);
		result = 31 * result + (studyParticipantCrfSchedule != null ? studyParticipantCrfSchedule.hashCode() : 0);
		return result;
	}
}
