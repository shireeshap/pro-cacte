package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "study_participant_crf_items")
public class StudyParticipantCrfItem extends BaseVersionable {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(name = "selected_response")
	private String selectedResponse;

	@JoinColumn(name = "crf_item_id", referencedColumnName = "id")
	@ManyToOne
	private CrfItem crfItem;

	@JoinColumn(name = "study_participant_crf_id", referencedColumnName = "id")
	@ManyToOne
	private StudyParticipantCrf studyParticipantCrf;

	public StudyParticipantCrfItem() {
	}

	public StudyParticipantCrfItem(Integer id) {
		this.id = id;
	}

	public StudyParticipantCrfItem(Integer id, String selectedResponse) {
		this.id = id;
		this.selectedResponse = selectedResponse;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSelectedResponse() {
		return selectedResponse;
	}

	public void setSelectedResponse(String selectedResponse) {
		this.selectedResponse = selectedResponse;
	}

	public CrfItem getCrfItem() {
		return crfItem;
	}

	public void setCrfItem(CrfItem crfItem) {
		this.crfItem = crfItem;
	}

	public StudyParticipantCrf getStudyParticipantCrf() {
		return studyParticipantCrf;
	}

	public void setStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
		this.studyParticipantCrf = studyParticipantCrf;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crfItem == null) ? 0 : crfItem.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((selectedResponse == null) ? 0 : selectedResponse.hashCode());
		result = prime
				* result
				+ ((studyParticipantCrf == null) ? 0 : studyParticipantCrf
						.hashCode());
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
		StudyParticipantCrfItem other = (StudyParticipantCrfItem) obj;
		if (crfItem == null) {
			if (other.crfItem != null)
				return false;
		} else if (!crfItem.equals(other.crfItem))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (selectedResponse == null) {
			if (other.selectedResponse != null)
				return false;
		} else if (!selectedResponse.equals(other.selectedResponse))
			return false;
		if (studyParticipantCrf == null) {
			if (other.studyParticipantCrf != null)
				return false;
		} else if (!studyParticipantCrf.equals(other.studyParticipantCrf))
			return false;
		return true;
	}

}
