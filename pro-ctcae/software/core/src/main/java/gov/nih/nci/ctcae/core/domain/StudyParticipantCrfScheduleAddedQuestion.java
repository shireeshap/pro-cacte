package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

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

    @JoinColumn(name = "meddra_question_id", referencedColumnName = "id")
    @ManyToOne
    private MeddraQuestion meddraQuestion;

    @JoinColumn(name = "meddra_valid_value_id", referencedColumnName = "id")
    @ManyToOne
    private MeddraValidValue meddraValidValue;

    @Column(name = "spc_added_question_id")
    private Integer studyParticipantCrfAddedQuestionId;

    @Column(name = "response_date", nullable = true)
    private Date reponseDate;

    @Column(name = "updated_by", nullable = true)
    private String updatedBy;

    @Column(name = "response_mode", nullable = true)
    @Enumerated(value = EnumType.STRING)
    private AppMode responseMode;

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

    public Question getProCtcOrMeddraQuestion() {
        if (proCtcQuestion != null) {
            return proCtcQuestion;
        }
        if (meddraQuestion != null) {
            return meddraQuestion;
        }
        return null;
    }

    public void setProCtcQuestion(ProCtcQuestion proCtcQuestion) {
        this.proCtcQuestion = proCtcQuestion;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantCrfScheduleAddedQuestion that = (StudyParticipantCrfScheduleAddedQuestion) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (meddraQuestion != null ? !meddraQuestion.equals(that.meddraQuestion) : that.meddraQuestion != null)
            return false;
        if (meddraValidValue != null ? !meddraValidValue.equals(that.meddraValidValue) : that.meddraValidValue != null)
            return false;
        if (pageNumber != null ? !pageNumber.equals(that.pageNumber) : that.pageNumber != null) return false;
        if (proCtcQuestion != null ? !proCtcQuestion.equals(that.proCtcQuestion) : that.proCtcQuestion != null)
            return false;
        if (proCtcValidValue != null ? !proCtcValidValue.equals(that.proCtcValidValue) : that.proCtcValidValue != null)
            return false;
        if (studyParticipantCrfAddedQuestionId != null ? !studyParticipantCrfAddedQuestionId.equals(that.studyParticipantCrfAddedQuestionId) : that.studyParticipantCrfAddedQuestionId != null)
            return false;
        if (studyParticipantCrfSchedule != null ? !studyParticipantCrfSchedule.equals(that.studyParticipantCrfSchedule) : that.studyParticipantCrfSchedule != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (studyParticipantCrfSchedule != null ? studyParticipantCrfSchedule.hashCode() : 0);
        result = 31 * result + (proCtcValidValue != null ? proCtcValidValue.hashCode() : 0);
        result = 31 * result + (pageNumber != null ? pageNumber.hashCode() : 0);
        result = 31 * result + (proCtcQuestion != null ? proCtcQuestion.hashCode() : 0);
        result = 31 * result + (meddraQuestion != null ? meddraQuestion.hashCode() : 0);
        result = 31 * result + (meddraValidValue != null ? meddraValidValue.hashCode() : 0);
        result = 31 * result + (studyParticipantCrfAddedQuestionId != null ? studyParticipantCrfAddedQuestionId.hashCode() : 0);
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

    public Question getQuestion() {
        if (proCtcQuestion != null) {
            return proCtcQuestion;
        }
        if (meddraQuestion != null) {
            return meddraQuestion;
        }
        return null;
    }

    public ValidValue getValidValue() {
        if (proCtcQuestion != null) {
            return proCtcValidValue;
        }
        if (meddraQuestion != null) {
            return meddraValidValue;
        }
        return null;
    }

    public void setValidValue(ValidValue validValue) {
        if (proCtcQuestion != null) {
            setProCtcValidValue((ProCtcValidValue) validValue);
        }
        if (meddraQuestion != null) {
            setMeddraValidValue((MeddraValidValue) validValue);
        }
    }

    public Integer getStudyParticipantCrfAddedQuestionId() {
        return studyParticipantCrfAddedQuestionId;
    }

    public void setStudyParticipantCrfAddedQuestionId(Integer studyParticipantCrfAddedQuestionId) {
        this.studyParticipantCrfAddedQuestionId = studyParticipantCrfAddedQuestionId;
    }

    public Date getReponseDate() {
        return reponseDate;
    }

    public void setReponseDate(Date reponseDate) {
        this.reponseDate = reponseDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public AppMode getResponseMode() {
        return responseMode;
    }

    public void setResponseMode(AppMode responseMode) {
        this.responseMode = responseMode;
    }
}