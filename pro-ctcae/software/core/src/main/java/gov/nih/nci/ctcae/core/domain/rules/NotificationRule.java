package gov.nih.nci.ctcae.core.domain.rules;

import gov.nih.nci.ctcae.core.domain.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;

//
/**
 * The Class CRF.
 *
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "NOTIFICATION_RULES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_rules_id")})

public class NotificationRule extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    @Column(name = "title", nullable = true)
    private String title;
    @OneToMany(mappedBy = "notificationRule", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<NotificationRuleCondition> notificationRuleConditions = new LinkedList<NotificationRuleCondition>();

    @OneToMany(mappedBy = "notificationRule", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<NotificationRuleSymptom> notificationRuleSymptoms = new LinkedList<NotificationRuleSymptom>();

    @OneToMany(mappedBy = "notificationRule", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<NotificationRuleRole> notificationRuleRoles = new LinkedList<NotificationRuleRole>();


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationRule that = (NotificationRule) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    public List<NotificationRuleCondition> getNotificationRuleConditions() {
        return notificationRuleConditions;
    }

    public void setNotificationRuleConditions(List<NotificationRuleCondition> notificationRuleConditions) {
        this.notificationRuleConditions = notificationRuleConditions;
    }

    public List<NotificationRuleSymptom> getNotificationRuleSymptoms() {
        return notificationRuleSymptoms;
    }

    public void setNotificationRuleSymptoms(List<NotificationRuleSymptom> notificationRuleSymptoms) {
        this.notificationRuleSymptoms = notificationRuleSymptoms;
        for (NotificationRuleSymptom notificationRuleSymptom : this.notificationRuleSymptoms) {
            notificationRuleSymptom.setNotificationRule(this);
        }
    }

    public void addNotificationRuleCondition(NotificationRuleCondition notificationRuleCondition) {
        if (notificationRuleCondition != null) {
            notificationRuleCondition.setNotificationRule(this);
            notificationRuleConditions.add(notificationRuleCondition);
        }
    }

    public void addNotificationRuleSymptom(NotificationRuleSymptom notificationRuleSymptom) {
        if (notificationRuleSymptom != null) {
            notificationRuleSymptom.setNotificationRule(this);
            notificationRuleSymptoms.add(notificationRuleSymptom);
        }
    }

    public void addNotificationRuleRole(NotificationRuleRole notificationRuleRole) {
        if (notificationRuleRole != null) {
            notificationRuleRole.setNotificationRule(this);
            notificationRuleRoles.add(notificationRuleRole);
        }
    }

    public List<NotificationRuleRole> getNotificationRuleRoles() {
        return notificationRuleRoles;
    }

    public void setNotificationRuleRoles(List<NotificationRuleRole> notificationRuleRoles) {
        this.notificationRuleRoles = notificationRuleRoles;
        for (NotificationRuleRole notificationRuleRole : this.notificationRuleRoles) {
            notificationRuleRole.setNotificationRule(this);
        }
    }

    public NotificationRule getCopy() {
        NotificationRule notificationRule = new NotificationRule();
        for (NotificationRuleSymptom notificationRuleSymptom : notificationRuleSymptoms) {
            notificationRule.addNotificationRuleSymptom(notificationRuleSymptom.getCopy());
        }
        for (NotificationRuleCondition notificationRuleCondition : notificationRuleConditions) {
            notificationRule.addNotificationRuleCondition(notificationRuleCondition.getCopy());
        }
        for (NotificationRuleRole notificationRuleRole : notificationRuleRoles) {
            notificationRule.addNotificationRuleRole(notificationRuleRole.getCopy());
        }
        return notificationRule;
    }
}