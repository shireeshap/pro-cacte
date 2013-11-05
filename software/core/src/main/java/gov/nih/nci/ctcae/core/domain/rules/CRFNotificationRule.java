package gov.nih.nci.ctcae.core.domain.rules;

import gov.nih.nci.ctcae.core.domain.BasePersistable;
import gov.nih.nci.ctcae.core.domain.CRF;
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
@Table(name = "CRF_NOTIFICATION_RULES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_rules_id")})

public class CRFNotificationRule extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @JoinColumn(name = "crf_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private CRF crf;

    @JoinColumn(name = "notification_rule_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private NotificationRule notificationRule;


    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public CRF getCrf() {
        return crf;
    }

    public void setCrf(CRF crf) {
        this.crf = crf;
    }

    public NotificationRule getNotificationRule() {
        return notificationRule;
    }

    public void setNotificationRule(NotificationRule notificationRule) {
        this.notificationRule = notificationRule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CRFNotificationRule crfRule = (CRFNotificationRule) o;

        if (crf != null ? !crf.equals(crfRule.crf) : crfRule.crf != null) return false;
        if (displayOrder != null ? !displayOrder.equals(crfRule.displayOrder) : crfRule.displayOrder != null)
            return false;
        if (id != null ? !id.equals(crfRule.id) : crfRule.id != null) return false;
        if (notificationRule != null ? !notificationRule.equals(crfRule.notificationRule) : crfRule.notificationRule != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (displayOrder != null ? displayOrder.hashCode() : 0);
        result = 31 * result + (crf != null ? crf.hashCode() : 0);
        result = 31 * result + (notificationRule != null ? notificationRule.hashCode() : 0);
        return result;
    }

}