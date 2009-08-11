package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class CrfPageItemDisplayRule.
 *
 * @author Vinay Kumar
 * @created Dec 17, 2008
 */

@Entity
@Table(name = "CRF_PAGE_ITEM_DISPLAY_RULES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "SEQ_CRF_PAGE_ITEM_DISPLAY_R_ID")})
public class CrfPageItemDisplayRule extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The pro ctc valid value.
     */
    @JoinColumn(name = "pro_ctc_valid_value_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcValidValue proCtcValidValue;


    /**
     * The crf page item.
     */
    @JoinColumn(name = "crf_item_id", referencedColumnName = "id")
    @ManyToOne
    private CrfPageItem crfPageItem;


    /**
     * Instantiates a new crf page item display rule.
     */
    public CrfPageItemDisplayRule() {
        super();
    }

    /**
     * Instantiates a new crf page item display rule.
     *
     * @param proCtcQuestionDisplayRule the pro ctc question display rule
     */
    public CrfPageItemDisplayRule(ProCtcQuestionDisplayRule proCtcQuestionDisplayRule) {
        super();
        this.proCtcValidValue = proCtcQuestionDisplayRule.getProCtcValidValue();


    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Persistable#getId()
     */
    public Integer getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Persistable#setId(java.lang.Integer)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the crf item.
     *
     * @return the crf item
     */
    public CrfPageItem getCrfItem() {
        return crfPageItem;
    }

    /**
     * Sets the crf item.
     *
     * @param crfPageItem the new crf item
     */
    public void setCrfItem(final CrfPageItem crfPageItem) {
        this.crfPageItem = crfPageItem;
    }

    /**
     * Gets the crf page item.
     *
     * @return the crf page item
     */
    public CrfPageItem getCrfPageItem() {
        return crfPageItem;
    }

    /**
     * Sets the crf page item.
     *
     * @param crfPageItem the new crf page item
     */
    public void setCrfPageItem(final CrfPageItem crfPageItem) {
        this.crfPageItem = crfPageItem;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = proCtcValidValue != null ? proCtcValidValue.hashCode() : 0;
        result = 31 * result + (crfPageItem != null ? crfPageItem.hashCode() : 0);
        return result;
    }

    /**
     * Gets the pro ctc valid value.
     *
     * @return the pro ctc valid value
     */
    public ProCtcValidValue getProCtcValidValue() {
        return proCtcValidValue;
    }

    /**
     * Sets the pro ctc valid value.
     *
     * @param proCtcValidValue the new pro ctc valid value
     */
    public void setProCtcValidValue(final ProCtcValidValue proCtcValidValue) {
        this.proCtcValidValue = proCtcValidValue;
    }

    /**
     * Gets the copy.
     *
     * @return the copy
     */
    public CrfPageItemDisplayRule copy() {
        CrfPageItemDisplayRule copiedCrfPageItemDisplayRule = new CrfPageItemDisplayRule();
        copiedCrfPageItemDisplayRule.setProCtcValidValue(getProCtcValidValue());
        return copiedCrfPageItemDisplayRule;

    }
}