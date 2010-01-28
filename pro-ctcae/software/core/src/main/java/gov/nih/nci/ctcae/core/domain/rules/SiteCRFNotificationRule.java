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
@Table(name = "SITE_CRF_NOTIFICATION_RULES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_rules_id")})

public class SiteCRFNotificationRule extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @JoinColumn(name = "study_site_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private StudySite studySite;

    @JoinColumn(name = "crf_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private CRF crf;

    public CRF getCrf() {
        return crf;
    }

    public void setCrf(CRF crf) {
        this.crf = crf;
    }

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


    public NotificationRule getNotificationRule() {
        return notificationRule;
    }

    public void setNotificationRule(NotificationRule notificationRule) {
        this.notificationRule = notificationRule;
    }

    public StudySite getStudySite() {
        return studySite;
    }

    public void setStudySite(StudySite studySite) {
        this.studySite = studySite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteCRFNotificationRule that = (SiteCRFNotificationRule) o;

        if (crf != null ? !crf.equals(that.crf) : that.crf != null) return false;
        if (displayOrder != null ? !displayOrder.equals(that.displayOrder) : that.displayOrder != null) return false;
        if (notificationRule != null ? !notificationRule.equals(that.notificationRule) : that.notificationRule != null)
            return false;
        if (studySite != null ? !studySite.equals(that.studySite) : that.studySite != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = displayOrder != null ? displayOrder.hashCode() : 0;
        result = 31 * result + (studySite != null ? studySite.hashCode() : 0);
        result = 31 * result + (crf != null ? crf.hashCode() : 0);
        result = 31 * result + (notificationRule != null ? notificationRule.hashCode() : 0);
        return result;
    }
}