package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.*;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserNotification that = (UserNotification) o;

        if (isNew != that.isNew) return false;
        if (markDelete != that.markDelete) return false;
        if (!notification.equals(that.notification)) return false;
        if (!participant.equals(that.participant)) return false;
        if (!study.equals(that.study)) return false;
        if (!studyParticipantCrfSchedule.equals(that.studyParticipantCrfSchedule)) return false;
        if (!user.equals(that.user)) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (isNew ? 1 : 0);
        result = 31 * result + (markDelete ? 1 : 0);
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + user.hashCode();
        result = 31 * result + notification.hashCode();
        result = 31 * result + study.hashCode();
        result = 31 * result + participant.hashCode();
        result = 31 * result + studyParticipantCrfSchedule.hashCode();
        return result;
    }

    public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {

        return studyParticipantCrfSchedule;
    }

    public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
    }
}