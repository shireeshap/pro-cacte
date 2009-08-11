package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class StudyParticipantCrfScheduleAddedQuestion.
 *
 * @author Harsh Agarwal
 * @since Jan 5, 2008
 */

@Entity
@Table(name = "SP_CRF_SCH_ADDED_QUESTIONS")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "SEQ_SP_CRF_SCH_ADDED_QUESTI_ID")})
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
    @JoinColumn(name = "sp_crf_schedule_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

    /**
     * The pro ctc valid value.
     */
    @JoinColumn(name = "pro_ctc_valid_value_id", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private ProCtcValidValue proCtcValidValue;


    /**
     * The page number.
     */
    @Column(name = "page_number", nullable = true)
    private Integer pageNumber;

    /**
     * The pro ctc question.
     */
    @JoinColumn(name = "question_id", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private ProCtcQuestion proCtcQuestion;

    @JoinColumn(name = "sp_crf_add_ques_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion;

    @JoinColumn(name = "meddra_question_id", referencedColumnName = "id")
     @ManyToOne
     private MeddraQuestion meddraQuestion;

    @JoinColumn(name = "meddra_valid_value_id", referencedColumnName = "id")
     @ManyToOne
     private MeddraValidValue meddraValidValue;



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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public ProCtcQuestion getProCtcQuestion() {
        return proCtcQuestion;
    }

    public void setProCtcQuestion(ProCtcQuestion proCtcQuestion) {
        this.proCtcQuestion = proCtcQuestion;
    }

    public StudyParticipantCrfAddedQuestion getStudyParticipantCrfAddedQuestion() {

        return studyParticipantCrfAddedQuestion;
    }

    public void setStudyParticipantCrfAddedQuestion(StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion) {
        this.studyParticipantCrfAddedQuestion = studyParticipantCrfAddedQuestion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyParticipantCrfScheduleAddedQuestion)) return false;

        StudyParticipantCrfScheduleAddedQuestion that = (StudyParticipantCrfScheduleAddedQuestion) o;

        if (pageNumber != null ? !pageNumber.equals(that.pageNumber) : that.pageNumber != null) return false;
        if (proCtcQuestion != null ? !proCtcQuestion.equals(that.proCtcQuestion) : that.proCtcQuestion != null)
            return false;
        if (proCtcValidValue != null ? !proCtcValidValue.equals(that.proCtcValidValue) : that.proCtcValidValue != null)
            return false;
        if (studyParticipantCrfAddedQuestion != null ? !studyParticipantCrfAddedQuestion.equals(that.studyParticipantCrfAddedQuestion) : that.studyParticipantCrfAddedQuestion != null)
            return false;
        if (studyParticipantCrfSchedule != null ? !studyParticipantCrfSchedule.equals(that.studyParticipantCrfSchedule) : that.studyParticipantCrfSchedule != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = studyParticipantCrfSchedule != null ? studyParticipantCrfSchedule.hashCode() : 0;
        result = 31 * result + (proCtcValidValue != null ? proCtcValidValue.hashCode() : 0);
        result = 31 * result + (pageNumber != null ? pageNumber.hashCode() : 0);
        result = 31 * result + (proCtcQuestion != null ? proCtcQuestion.hashCode() : 0);
        result = 31 * result + (studyParticipantCrfAddedQuestion != null ? studyParticipantCrfAddedQuestion.hashCode() : 0);
        return result;
    }


    public MeddraQuestion getMeddraQuestion() {
        return meddraQuestion;
    }

    public void setMeddraQuestion(MeddraQuestion meddraQuestion) {
        this.meddraQuestion = meddraQuestion;
    }

    public MeddraValidValue getMeddraValidValue() {
        return meddraValidValue;
    }

    public void setMeddraValidValue(MeddraValidValue meddraValidValue) {
        this.meddraValidValue = meddraValidValue;
    }
}