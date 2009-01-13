package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @crated jan 5, 2008
 */

@Entity
@Table(name = "sp_crf_added_questions")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_sp_crf_added_questions_id")})
public class StudyParticipantCrfAddedQuestion extends BaseVersionable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "page_number")
    private Integer pageNumber;

    @JoinColumn(name = "sp_crf_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantCrf studyParticipantCrf;

    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcQuestion proCtcQuestion;


    public StudyParticipantCrfAddedQuestion() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StudyParticipantCrf getStudyParticipantCrf() {
        return studyParticipantCrf;
    }

    public void setStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        this.studyParticipantCrf = studyParticipantCrf;
    }

    public ProCtcQuestion getProCtcQuestion() {
        return proCtcQuestion;
    }

    public void setProCtcQuestion(ProCtcQuestion proCtcQuestion) {
        this.proCtcQuestion = proCtcQuestion;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public StudyParticipantCrfAddedQuestion getCopy(){
        StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion = new StudyParticipantCrfAddedQuestion();
        studyParticipantCrfAddedQuestion.setPageNumber(pageNumber);
        studyParticipantCrfAddedQuestion.setStudyParticipantCrf(studyParticipantCrf);
        studyParticipantCrfAddedQuestion.setProCtcQuestion(proCtcQuestion);
        return studyParticipantCrfAddedQuestion;
    }
}