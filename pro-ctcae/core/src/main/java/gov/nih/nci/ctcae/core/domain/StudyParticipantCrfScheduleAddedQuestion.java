package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class StudyParticipantCrfScheduleAddedQuestion.
 *
 * @author Harsh Agarwal
 * @crated Jan 5, 2008
 */

@Entity
@Table(name = "SP_CRF_SCH_ADDED_QUESTIONS")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_sp_crf_sch_added_questions_id")})
public class StudyParticipantCrfScheduleAddedQuestion extends BaseVersionable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The study participant crf schedule.
     */
    @JoinColumn(name = "sp_crf_schedule_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

    /**
     * The pro ctc valid value.
     */
    @JoinColumn(name = "pro_ctc_valid_value_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcValidValue proCtcValidValue;


    /**
     * Instantiates a new study participant crf schedule added question.
     */
    public StudyParticipantCrfScheduleAddedQuestion() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantCrfScheduleAddedQuestion that = (StudyParticipantCrfScheduleAddedQuestion) o;

        if (proCtcValidValue != null ? !proCtcValidValue.equals(that.proCtcValidValue) : that.proCtcValidValue != null)
            return false;
        if (studyParticipantCrfSchedule != null ? !studyParticipantCrfSchedule.equals(that.studyParticipantCrfSchedule) : that.studyParticipantCrfSchedule != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = studyParticipantCrfSchedule != null ? studyParticipantCrfSchedule.hashCode() : 0;
        result = 31 * result + (proCtcValidValue != null ? proCtcValidValue.hashCode() : 0);
        return result;
    }
}