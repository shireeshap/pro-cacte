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
	private CrfItem crfItem;

	@JoinColumn(name = "study_participant_crf_schedule_id", referencedColumnName = "id")
	@ManyToOne
	private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

	@Transient
	private int itemIndex;

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

	public CrfItem getCrfItem() {
		return crfItem;
	}

	public void setCrfItem(CrfItem crfItem) {
		this.crfItem = crfItem;
	}

	public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
		return studyParticipantCrfSchedule;
	}

	public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
		this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StudyParticipantCrfItem that = (StudyParticipantCrfItem) o;

		if (crfItem != null ? !crfItem.equals(that.crfItem) : that.crfItem != null) return false;
		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (proCtcValidValue != null ? !proCtcValidValue.equals(that.proCtcValidValue) : that.proCtcValidValue != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (proCtcValidValue != null ? proCtcValidValue.hashCode() : 0);
		result = 31 * result + (crfItem != null ? crfItem.hashCode() : 0);
		return result;
	}

	public int getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}
}
