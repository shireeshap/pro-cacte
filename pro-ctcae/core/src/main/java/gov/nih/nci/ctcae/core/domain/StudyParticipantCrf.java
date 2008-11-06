package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Date;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "study_participant_crfs")
public class StudyParticipantCrf extends BaseVersionable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studyParticipantCrf", fetch = FetchType.LAZY)
    private List<StudyParticipantCrfItem> studyParticipantCrfItems = new ArrayList<StudyParticipantCrfItem>();

    @JoinColumn(name = "study_crf_id", referencedColumnName = "id")
    @ManyToOne
    private StudyCrf studyCrf;

    @JoinColumn(name = "study_participant_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantAssignment studyParticipantAssignment;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    public StudyParticipantCrf(StudyCrf studyCrf) {
        this.studyCrf = studyCrf;
        for (CrfItem crfItem : studyCrf.getCrf().getCrfItems()) {
            StudyParticipantCrfItem studyParticipantCrfItem = new StudyParticipantCrfItem();
            studyParticipantCrfItem.setCrfItem(crfItem);
            addStudyParticipantCrfItem(studyParticipantCrfItem);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<StudyParticipantCrfItem> getStudyParticipantCrfItems() {
        return studyParticipantCrfItems;
    }

    public void addStudyParticipantCrfItem(
            StudyParticipantCrfItem studyParticipantCrfItem) {
        if (studyParticipantCrfItem != null) {
            studyParticipantCrfItem.setStudyParticipantCrf(this);
            studyParticipantCrfItems.add(studyParticipantCrfItem);
        }
    }

    public void removeStudyParticipantCrfItem(
            StudyParticipantCrfItem studyParticipantCrfItem) {
        if (studyParticipantCrfItem != null) {
            studyParticipantCrfItems.remove(studyParticipantCrfItem);
        }
    }

    public StudyCrf getStudyCrf() {
        return studyCrf;
    }


    public StudyParticipantAssignment getStudyParticipantAssignment() {
        return studyParticipantAssignment;
    }

    public void setStudyParticipantAssignment(StudyParticipantAssignment studyParticipant) {
        this.studyParticipantAssignment = studyParticipant;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((studyCrf == null) ? 0 : studyCrf.hashCode());
        result = prime
                * result
                + ((studyParticipantAssignment == null) ? 0 : studyParticipantAssignment.hashCode());
        result = prime
                * result
                + ((studyParticipantCrfItems == null) ? 0
                : studyParticipantCrfItems.hashCode());
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
        StudyParticipantCrf other = (StudyParticipantCrf) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (studyCrf == null) {
            if (other.studyCrf != null)
                return false;
        } else if (!studyCrf.equals(other.studyCrf))
            return false;
        if (studyParticipantAssignment == null) {
            if (other.studyParticipantAssignment != null)
                return false;
        } else if (!studyParticipantAssignment.equals(other.studyParticipantAssignment))
            return false;
        if (studyParticipantCrfItems == null) {
            if (other.studyParticipantCrfItems != null)
                return false;
        } else if (!studyParticipantCrfItems
                .equals(other.studyParticipantCrfItems))
            return false;
        return true;
    }

}
