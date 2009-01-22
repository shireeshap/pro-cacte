package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author Vinay Kumar
 * @created Dec 17, 2008
 */

@Entity
@Table(name = "crf_page_item_display_rules")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_item_display_rules_id")})
public class CrfPageItemDisplayRule extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "pro_ctc_valid_value_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcValidValue proCtcValidValue;


    @JoinColumn(name = "crf_item_id", referencedColumnName = "id")
    @ManyToOne
    private CrfPageItem crfPageItem;


    public CrfPageItemDisplayRule() {
    }

    public CrfPageItemDisplayRule(ProCtcQuestionDisplayRule proCtcQuestionDisplayRule) {
        super();
        this.proCtcValidValue = proCtcQuestionDisplayRule.getProCtcValidValue();


    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CrfPageItem getCrfItem() {
        return crfPageItem;
    }

    public void setCrfItem(final CrfPageItem crfPageItem) {
        this.crfPageItem = crfPageItem;
    }

    public CrfPageItem getCrfPageItem() {
        return crfPageItem;
    }

    public void setCrfPageItem(final CrfPageItem crfPageItem) {
        this.crfPageItem = crfPageItem;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final CrfPageItemDisplayRule that = (CrfPageItemDisplayRule) o;

        if (crfPageItem != null ? !crfPageItem.equals(that.crfPageItem) : that.crfPageItem != null) return false;
        if (proCtcValidValue != null ? !proCtcValidValue.equals(that.proCtcValidValue) : that.proCtcValidValue != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = proCtcValidValue != null ? proCtcValidValue.hashCode() : 0;
        result = 31 * result + (crfPageItem != null ? crfPageItem.hashCode() : 0);
        return result;
    }

    public ProCtcValidValue getProCtcValidValue() {
        return proCtcValidValue;
    }

    public void setProCtcValidValue(final ProCtcValidValue proCtcValidValue) {
        this.proCtcValidValue = proCtcValidValue;
    }

    public CrfPageItemDisplayRule getCopy() {
        CrfPageItemDisplayRule copiedCrfPageItemDisplayRule = new CrfPageItemDisplayRule();
        copiedCrfPageItemDisplayRule.setProCtcValidValue(getProCtcValidValue());
        return copiedCrfPageItemDisplayRule;

    }
}