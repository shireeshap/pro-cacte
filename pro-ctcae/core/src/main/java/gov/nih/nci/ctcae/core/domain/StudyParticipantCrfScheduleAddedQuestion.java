package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @crated Jan 5, 2008
 */

@Entity
@Table(name = "sp_crf_sch_added_questions")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_sp_crf_sch_added_questions_id")})
public class StudyParticipantCrfScheduleAddedQuestion extends BaseVersionable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "sp_crf_schedule_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

    @JoinColumn(name = "pro_ctc_valid_value_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcValidValue proCtcValidValue;


    public StudyParticipantCrfScheduleAddedQuestion() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
        return studyParticipantCrfSchedule;
    }

    public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
    }

    public ProCtcValidValue getProCtcValidValue() {
        return proCtcValidValue;
    }

    public void setProCtcValidValue(ProCtcValidValue proCtcValidValue) {
        this.proCtcValidValue = proCtcValidValue;
    }
}