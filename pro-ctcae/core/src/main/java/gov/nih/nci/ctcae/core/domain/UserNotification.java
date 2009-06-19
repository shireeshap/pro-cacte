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
    private boolean isNew;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserNotification that = (UserNotification) o;

        if (isNew != that.isNew) return false;
        if (!notification.equals(that.notification)) return false;
        if (!participant.equals(that.participant)) return false;
        if (!study.equals(that.study)) return false;
        if (!user.equals(that.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (isNew ? 1 : 0);
        result = 31 * result + user.hashCode();
        result = 31 * result + notification.hashCode();
        result = 31 * result + study.hashCode();
        result = 31 * result + participant.hashCode();
        return result;
    }

    public boolean getIsNew() {
        return isNew();
    }
}