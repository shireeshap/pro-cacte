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
@Table(name = "NOTIFICATION_RULE_CONDITIONS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_rules_id")})

public class NotificationRuleCondition extends BasePersistable {

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

    @Column(name = "operator", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationRuleOperator notificationRuleOperator = NotificationRuleOperator.GREATER_EQUAL;

    @Column(name = "question_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProCtcQuestionType proCtcQuestionType;

    @Column(name = "threshold", nullable = false)
    private Integer threshold;

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

    public NotificationRuleOperator getNotificationRuleOperator() {
        return notificationRuleOperator;
    }

    public void setNotificationRuleOperator(NotificationRuleOperator notificationRuleOperator) {
        this.notificationRuleOperator = notificationRuleOperator;
    }

    public ProCtcQuestionType getProCtcQuestionType() {
        return proCtcQuestionType;
    }

    public void setProCtcQuestionType(ProCtcQuestionType proCtcQuestionType) {
        this.proCtcQuestionType = proCtcQuestionType;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationRuleCondition that = (NotificationRuleCondition) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (notificationRule != null ? !notificationRule.equals(that.notificationRule) : that.notificationRule != null)
            return false;
        if (notificationRuleOperator != that.notificationRuleOperator) return false;
        if (proCtcQuestionType != that.proCtcQuestionType) return false;
        if (threshold != null ? !threshold.equals(that.threshold) : that.threshold != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (notificationRule != null ? notificationRule.hashCode() : 0);
        result = 31 * result + (notificationRuleOperator != null ? notificationRuleOperator.hashCode() : 0);
        result = 31 * result + (proCtcQuestionType != null ? proCtcQuestionType.hashCode() : 0);
        result = 31 * result + (threshold != null ? threshold.hashCode() : 0);
        return result;
    }

    public NotificationRuleCondition getCopy() {
        NotificationRuleCondition notificationRuleCondition = new NotificationRuleCondition();
        notificationRuleCondition.setProCtcQuestionType(proCtcQuestionType);
        notificationRuleCondition.setNotificationRuleOperator(notificationRuleOperator);
        notificationRuleCondition.setThreshold(threshold);
        return notificationRuleCondition;
    }
}