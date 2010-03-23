package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class StudyParticipantAssignment.
 *
 * @author
 * @since Oct 7, 2008
 */

@Entity
@Table(name = "USER_NOTIFICATIONS")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_user_notifications_id")})
public class UserNotification extends BaseVersionable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "is_new", nullable = false)
    private boolean isNew = true;

    @Column(name = "mark_delete", nullable = false)
    private boolean markDelete = false;

    @Column(name = "uuid", nullable = true)
    private String uuid;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private User user;

    @JoinColumn(name = "notification_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Notification notification;

    @JoinColumn(name = "study_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Study study;

    @JoinColumn(name = "participant_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Participant participant;

    @JoinColumn(name = "spc_schedule_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public boolean isNew() {

        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }


    public boolean isMarkDelete() {
        return markDelete;
    }

    public boolean getMarkDelete() {
        return isMarkDelete();
    }

    public void setMarkDelete(boolean markDelete) {
        this.markDelete = markDelete;
    }

    public boolean getIsNew() {
        return isNew();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

        UserNotification that = (UserNotification) o;

        if (isNew != that.isNew) return false;
        if (markDelete != that.markDelete) return false;
        if (notification != null ? !notification.equals(that.notification) : that.notification != null) return false;
        if (participant != null ? !participant.equals(that.participant) : that.participant != null) return false;
        if (study != null ? !study.equals(that.study) : that.study != null) return false;
        if (studyParticipantCrfSchedule != null ? !studyParticipantCrfSchedule.equals(that.studyParticipantCrfSchedule) : that.studyParticipantCrfSchedule != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (isNew ? 1 : 0);
        result = 31 * result + (markDelete ? 1 : 0);
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (notification != null ? notification.hashCode() : 0);
        result = 31 * result + (study != null ? study.hashCode() : 0);
        result = 31 * result + (participant != null ? participant.hashCode() : 0);
        result = 31 * result + (studyParticipantCrfSchedule != null ? studyParticipantCrfSchedule.hashCode() : 0);
        return result;
    }
}