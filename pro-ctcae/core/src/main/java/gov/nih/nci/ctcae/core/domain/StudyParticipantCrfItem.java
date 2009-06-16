package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class StudyParticipantCrfItem.
 *
 * @author
 * @since Oct 7, 2008
 */

@Entity
@Table(name = "STUDY_PARTICIPANT_CRF_ITEMS")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_participant_crf_items_id")})
public class StudyParticipantCrfItem extends BaseVersionable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The pro ctc valid value.
     */
    @JoinColumn(name = "pro_ctc_valid_value_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcValidValue proCtcValidValue;

    /**
     * The crf page item.
     */
    @JoinColumn(name = "crf_item_id", referencedColumnName = "id")
    @ManyToOne
    private CrfPageItem crfPageItem;

    /**
     * The study participant crf schedule.
     */
    @JoinColumn(name = "SP_CRF_SCHEDULE_ID", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

    /**
     * Instantiates a new study participant crf item.
     */
    public StudyParticipantCrfItem() {
        super();
    }


    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.domain.Persistable#getId()
      */
    public Integer getId() {
        return id;
    }

    /* (non-Javadoc)
      * @see gov.nih.nci.ctcae.core.domain.Persistable#setId(java.lang.Integer)
      */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the pro ctc valid value.
     *
     * @return the pro ctc valid value
     */
    public ProCtcValidValue getProCtcValidValue() {
        return proCtcValidValue;
    }

    /**
     * Sets the pro ctc valid value.
     *
     * @param proCtcValidValue the new pro ctc valid value
     */
    public void setProCtcValidValue(ProCtcValidValue proCtcValidValue) {
        this.proCtcValidValue = proCtcValidValue;
    }

    /**
     * Gets the crf page item.
     *
     * @return the crf page item
     */
    public CrfPageItem getCrfPageItem() {
        return crfPageItem;
    }

    /**
     * Sets the crf page item.
     *
     * @param crfPageItem the new crf page item
     */
    public void setCrfPageItem(CrfPageItem crfPageItem) {
        this.crfPageItem = crfPageItem;
    }


    /**
     * Gets the study participant crf schedule.
     *
     * @return the study participant crf schedule
     */
    public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
        return studyParticipantCrfSchedule;
    }

    /**
     * Sets the study participant crf schedule.
     *
     * @param studyParticipantCrfSchedule the new study participant crf schedule
     */
    public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
    }

    /* (non-Javadoc)
      * @see java.lang.Object#equals(java.lang.Object)
      */
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

    /* (non-Javadoc)
      * @see java.lang.Object#hashCode()
      */
    @Override
    public int hashCode() {
        int result = proCtcValidValue != null ? proCtcValidValue.hashCode() : 0;
        result = 31 * result + (crfPageItem != null ? crfPageItem.hashCode() : 0);
        result = 31 * result + (studyParticipantCrfSchedule != null ? studyParticipantCrfSchedule.hashCode() : 0);
        return result;
    }
}
