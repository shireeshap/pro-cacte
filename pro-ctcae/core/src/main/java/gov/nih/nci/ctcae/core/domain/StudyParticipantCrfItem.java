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

	@JoinColumn(name = "SP_CRF_SCHEDULE_ID", referencedColumnName = "id")
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



	public int getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}
}
