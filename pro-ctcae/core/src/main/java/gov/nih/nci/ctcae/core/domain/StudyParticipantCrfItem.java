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

	@JoinColumn(name = "study_participant_crf_schedule_id", referencedColumnName = "id")
	@ManyToOne
	private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

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
        if (selectedResponse != null ? !selectedResponse.equals(that.selectedResponse) : that.selectedResponse != null)
            return false;
        if (studyParticipantCrfSchedule != null ? !studyParticipantCrfSchedule.equals(that.studyParticipantCrfSchedule) : that.studyParticipantCrfSchedule != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (selectedResponse != null ? selectedResponse.hashCode() : 0);
        result = 31 * result + (crfItem != null ? crfItem.hashCode() : 0);
        result = 31 * result + (studyParticipantCrfSchedule != null ? studyParticipantCrfSchedule.hashCode() : 0);
        return result;
    }
}
