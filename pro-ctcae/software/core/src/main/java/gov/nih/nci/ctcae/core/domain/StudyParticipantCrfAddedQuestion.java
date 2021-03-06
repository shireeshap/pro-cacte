package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class StudyParticipantCrfAddedQuestion.
 *
 * @author Harsh Agarwal
 * @since jan 5, 2008
 */

@Entity
@Table(name = "SP_CRF_ADDED_QUESTIONS")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_sp_crf_added_questions_id")})
public class StudyParticipantCrfAddedQuestion extends BaseVersionable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The page number.
     */
    @Column(name = "page_number")
    private Integer pageNumber;

    /**
     * The study participant crf.
     */
    @JoinColumn(name = "sp_crf_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantCrf studyParticipantCrf;

    /**
     * The pro ctc question.
     */
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcQuestion proCtcQuestion;

    @JoinColumn(name = "meddra_question_id", referencedColumnName = "id")
    @ManyToOne
    private MeddraQuestion meddraQuestion;

    /**
     * Instantiates a new study participant crf added question.
     */
    public StudyParticipantCrfAddedQuestion() {
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
     * Gets the study participant crf.
     *
     * @return the study participant crf
     */
    public StudyParticipantCrf getStudyParticipantCrf() {
        return studyParticipantCrf;
    }

    /**
     * Sets the study participant crf.
     *
     * @param studyParticipantCrf the new study participant crf
     */
    public void setStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        this.studyParticipantCrf = studyParticipantCrf;
    }

    /**
     * Gets the pro ctc question.
     *
     * @return the pro ctc question
     */
    public ProCtcQuestion getProCtcQuestion() {
        return proCtcQuestion;
    }

    /**
     * Sets the pro ctc question.
     *
     * @param proCtcQuestion the new pro ctc question
     */
    public void setProCtcQuestion(ProCtcQuestion proCtcQuestion) {
        this.proCtcQuestion = proCtcQuestion;
    }

    /**
     * Gets the page number.
     *
     * @return the page number
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the page number.
     *
     * @param pageNumber the new page number
     */
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }


    /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantCrfAddedQuestion that = (StudyParticipantCrfAddedQuestion) o;

        if (pageNumber != null ? !pageNumber.equals(that.pageNumber) : that.pageNumber != null) return false;
        if (proCtcQuestion != null ? !proCtcQuestion.equals(that.proCtcQuestion) : that.proCtcQuestion != null)
            return false;
        if (studyParticipantCrf != null ? !studyParticipantCrf.equals(that.studyParticipantCrf) : that.studyParticipantCrf != null)
            return false;

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = pageNumber != null ? pageNumber.hashCode() : 0;
        result = 31 * result + (studyParticipantCrf != null ? studyParticipantCrf.hashCode() : 0);
        result = 31 * result + (proCtcQuestion != null ? proCtcQuestion.hashCode() : 0);
        return result;
    }

    public MeddraQuestion getMeddraQuestion() {
        return meddraQuestion;
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

    public void setMeddraQuestion(MeddraQuestion meddraQuestion) {
        this.meddraQuestion = meddraQuestion;
    }
}