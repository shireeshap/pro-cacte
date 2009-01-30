package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "sp_crf_schedules")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_sp_crf_schedules_id")})
public class StudyParticipantCrfSchedule extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CrfStatus status = CrfStatus.SCHEDULED;


    @JoinColumn(name = "study_participant_crf_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantCrf studyParticipantCrf;

    @OneToMany(mappedBy = "studyParticipantCrfSchedule", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})

    private List<StudyParticipantCrfItem> studyParticipantCrfItems = new ArrayList<StudyParticipantCrfItem>();

    @OneToMany(mappedBy = "studyParticipantCrfSchedule", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantCrfScheduleAddedQuestion> studyParticipantCrfScheduleAddedQuestions = new ArrayList<StudyParticipantCrfScheduleAddedQuestion>();

    public StudyParticipantCrfSchedule() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<StudyParticipantCrfItem> getStudyParticipantCrfItems() {
        Collections.sort(studyParticipantCrfItems, new ParticipantCrfDisplayOrderComparator());
        return studyParticipantCrfItems;
    }

    public void addStudyParticipantCrfItem(
            StudyParticipantCrfItem studyParticipantCrfItem) {
        if (studyParticipantCrfItem != null) {
            studyParticipantCrfItem.setStudyParticipantCrfSchedule(this);
            studyParticipantCrfItems.add(studyParticipantCrfItem);
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public StudyParticipantCrf getStudyParticipantCrf() {
        return studyParticipantCrf;
    }

    public void setStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        this.studyParticipantCrf = studyParticipantCrf;
    }

    public CrfStatus getStatus() {
        return status;
    }

    public void setStatus(CrfStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantCrfSchedule that = (StudyParticipantCrfSchedule) o;

        if (dueDate != null ? !dueDate.equals(that.dueDate) : that.dueDate != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (status != that.status) return false;
        if (studyParticipantCrf != null ? !studyParticipantCrf.equals(that.studyParticipantCrf) : that.studyParticipantCrf != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (dueDate != null ? dueDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (studyParticipantCrf != null ? studyParticipantCrf.hashCode() : 0);
        return result;
    }

    public List<StudyParticipantCrfScheduleAddedQuestion> getStudyParticipantCrfScheduleAddedQuestions() {
        return studyParticipantCrfScheduleAddedQuestions;
    }

    public void addStudyParticipantCrfScheduleAddedQuestion(StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion) {
        if (studyParticipantCrfScheduleAddedQuestion != null) {
            studyParticipantCrfScheduleAddedQuestion.setStudyParticipantCrfSchedule(this);
            studyParticipantCrfScheduleAddedQuestions.add(studyParticipantCrfScheduleAddedQuestion);

        }
    }
}