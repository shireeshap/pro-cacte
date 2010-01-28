package gov.nih.nci.ctcae.core.domain.rules;

import gov.nih.nci.ctcae.core.domain.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class CRF.
 *
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "NOTIFICATION_RULE_ROLES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_rules_id")})

public class NotificationRuleRole extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    @JoinColumn(name = "notification_rule_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private NotificationRule notificationRule;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public NotificationRuleRole() {
    }

    public NotificationRuleRole(Role role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NotificationRule getNotificationRule() {
        return notificationRule;
    }

    public void setNotificationRule(NotificationRule notificationRule) {
        this.notificationRule = notificationRule;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationRuleRole that = (NotificationRuleRole) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (notificationRule != null ? !notificationRule.equals(that.notificationRule) : that.notificationRule != null)
            return false;
        if (role != that.role) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (notificationRule != null ? notificationRule.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    public NotificationRuleRole getCopy() {
        return new NotificationRuleRole(role);
    }
}