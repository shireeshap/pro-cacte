package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

//
/**
 * The Class CrfPageItem.
 *
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "CRF_PAGE_ITEMS")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_page_items_id")})
public class CrfPageItem extends BasePersistable {

    /**
     * The Constant INITIAL_ORDER. This must be 0 only otherwise index at ui will not work
     */
    public static final Integer INITIAL_ORDER = 0;

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The display order.
     */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = INITIAL_ORDER;

    /**
     * The crf page.
     */
    @JoinColumn(name = "crf_page_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private CRFPage crfPage;

    /**
     * The pro ctc question.
     */
    @JoinColumn(name = "pro_ctc_question_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcQuestion proCtcQuestion;

    /**
     * The response required.
     */
    @Column(name = "response_required")
    private Boolean responseRequired = Boolean.TRUE;

    /**
     * The instructions.
     */
    @Column(name = "instructions")
    private String instructions;

    /**
     * The DEFAUL t_ allignment.
     */
    public static CrfItemAllignment DEFAULT_ALLIGNMENT = CrfItemAllignment.HORIZONTAL;

    /**
     * The crf item allignment.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "allignment")
    private CrfItemAllignment crfItemAllignment = DEFAULT_ALLIGNMENT;

    /**
     * The crf page item display rules.
     */
    @OneToMany(mappedBy = "crfPageItem", fetch = FetchType.LAZY)
    @Cascade(value = {CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private List<CrfPageItemDisplayRule> crfPageItemDisplayRules = new ArrayList<CrfPageItemDisplayRule>();


    /**
     * Instantiates a new crf page item.
     */
    public CrfPageItem() {
        super();
    }

    /**
     * Instantiates a new crf page item.
     *
     * @param proCtcQuestion the pro ctc question
     */
    public CrfPageItem(ProCtcQuestion proCtcQuestion) {
        super();
        this.proCtcQuestion = proCtcQuestion;
        for (ProCtcQuestionDisplayRule proCtcQuestionDisplayRule : proCtcQuestion.getProCtcQuestionDisplayRules()) {
            CrfPageItemDisplayRule crfPageItemDisplayRule = new CrfPageItemDisplayRule(proCtcQuestionDisplayRule);
            this.addCrfPageItemDisplayRules(crfPageItemDisplayRule);
        }

    }


    /**
     * Gets the response required.
     *
     * @return the response required
     */
    public Boolean getResponseRequired() {
        return responseRequired;
    }

    /**
     * Sets the response required.
     *
     * @param responseRequired the new response required
     */
    public void setResponseRequired(final Boolean responseRequired) {
        this.responseRequired = responseRequired;
    }

    /**
     * Gets the instructions.
     *
     * @return the instructions
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the instructions.
     *
     * @param instructions the new instructions
     */
    public void setInstructions(final String instructions) {
        this.instructions = instructions;
    }

    /**
     * Gets the crf item allignment.
     *
     * @return the crf item allignment
     */
    public CrfItemAllignment getCrfItemAllignment() {
        return crfItemAllignment;
    }

    /**
     * Sets the crf item allignment.
     *
     * @param crfItemAllignment the new crf item allignment
     */
    public void setCrfItemAllignment(final CrfItemAllignment crfItemAllignment) {
        this.crfItemAllignment = crfItemAllignment;
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
     * Gets the display order.
     *
     * @return the display order
     */
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    /**
     * Sets the display order.
     *
     * @param displayOrder the new display order
     */
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }


    /**
     * Gets the crf page.
     *
     * @return the crf page
     */
    public CRFPage getCrfPage() {
        return crfPage;
    }

    /**
     * Sets the crf page.
     *
     * @param crfPage the new crf page
     */
    public void setCrfPage(final CRFPage crfPage) {
        this.crfPage = crfPage;
    }

    /**
     * Gets the pro ctc question.
     *
     * @return the pro ctc question
     */
    public ProCtcQuestion getProCtcQuestion() {
        return proCtcQuestion;
    }

    /**
     * Sets the pro ctc question.
     *
     * @param proCtcQuestion the new pro ctc question
     */
    public void setProCtcQuestion(ProCtcQuestion proCtcQuestion) {
        this.proCtcQuestion = proCtcQuestion;
    }

    /**
     * Gets the copy.
     *
     * @return the copy
     */
    public CrfPageItem copy() {
        CrfPageItem copiedCrfPageItem = new CrfPageItem();
        copiedCrfPageItem.setInstructions(instructions);
        copiedCrfPageItem.setDisplayOrder(displayOrder);
        copiedCrfPageItem.setResponseRequired(responseRequired);
        copiedCrfPageItem.setCrfItemAllignment(crfItemAllignment);
        copiedCrfPageItem.setProCtcQuestion(proCtcQuestion);
        for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItemDisplayRules) {
            copiedCrfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule.copy());
        }
        return copiedCrfPageItem;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final CrfPageItem crfPageItem = (CrfPageItem) o;

        if (crfPage != null ? !crfPage.equals(crfPageItem.crfPage) : crfPageItem.crfPage != null) return false;
        if (crfItemAllignment != crfPageItem.crfItemAllignment) return false;
        if (displayOrder != null ? !displayOrder.equals(crfPageItem.displayOrder) : crfPageItem.displayOrder != null)
            return false;
        if (instructions != null ? !instructions.equals(crfPageItem.instructions) : crfPageItem.instructions != null)
            return false;
        if (proCtcQuestion != null ? !proCtcQuestion.equals(crfPageItem.proCtcQuestion) : crfPageItem.proCtcQuestion != null)
            return false;
        if (responseRequired != null ? !responseRequired.equals(crfPageItem.responseRequired) : crfPageItem.responseRequired != null)
            return false;

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = displayOrder != null ? displayOrder.hashCode() : 0;
        result = 31 * result + (crfPage != null ? crfPage.hashCode() : 0);
        result = 31 * result + (proCtcQuestion != null ? proCtcQuestion.hashCode() : 0);
        result = 31 * result + (responseRequired != null ? responseRequired.hashCode() : 0);
        result = 31 * result + (instructions != null ? instructions.hashCode() : 0);
        result = 31 * result + (crfItemAllignment != null ? crfItemAllignment.hashCode() : 0);
        return result;
    }


    /**
     * Gets the crf page item display rules.
     *
     * @return the crf page item display rules
     */
    public List<CrfPageItemDisplayRule> getCrfPageItemDisplayRules() {
        return crfPageItemDisplayRules;
    }


    /**
     * Removes the crf page item display rules by ids.
     *
     * @param proCtcValidValues the pro ctc valid values
     */
    public void removeCrfPageItemDisplayRulesByIds(final Set<Integer> proCtcValidValues) {
        List<CrfPageItemDisplayRule> crfPageItemDisplayRulesToRemove = new ArrayList<CrfPageItemDisplayRule>();
        for (Integer id : proCtcValidValues) {
            CrfPageItemDisplayRule crfPageItemDisplayRule = getCrfDisplayRuleByProCtcValidValueId(id);
            if (crfPageItemDisplayRule != null) {
                crfPageItemDisplayRulesToRemove.add(crfPageItemDisplayRule);
            }
        }

        for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItemDisplayRulesToRemove) {
            this.removeCrfPageItemDisplayRule(crfPageItemDisplayRule);
        }

    }

    /**
     * Gets the crf display rule by pro ctc valid value id.
     *
     * @param id the id
     * @return the crf display rule by pro ctc valid value id
     */
    private CrfPageItemDisplayRule getCrfDisplayRuleByProCtcValidValueId(final Integer id) {
        for (CrfPageItemDisplayRule crfPageItemDisplayRule : getCrfPageItemDisplayRules()) {
            if (crfPageItemDisplayRule.getProCtcValidValue().getId().equals(id)) {
                return crfPageItemDisplayRule;
            }
        }
        return null;
    }

    /**
     * Removes the crf page item display rule.
     *
     * @param crfPageItemDisplayRule the crf page item display rule
     */
    private void removeCrfPageItemDisplayRule(final CrfPageItemDisplayRule crfPageItemDisplayRule) {

        getCrfPageItemDisplayRules().remove(crfPageItemDisplayRule);
    }

    /**
     * Adds the crf page item display rules.
     *
     * @param crfPageItemDisplayRule the crf page item display rule
     * @return true, if successful
     */
    public boolean addCrfPageItemDisplayRules(CrfPageItemDisplayRule crfPageItemDisplayRule) {
        if (crfPageItemDisplayRule != null) {
            CrfPageItemDisplayRule anotherCrfPageItemDisplayRule = getCrfDisplayRuleByProCtcValidValueId(crfPageItemDisplayRule.getProCtcValidValue().getId());
            if (anotherCrfPageItemDisplayRule == null) {
                crfPageItemDisplayRule.setCrfItem(this);
                getCrfPageItemDisplayRules().add(crfPageItemDisplayRule);
                return true;
            }


        }
        return false;
    }


    /**
     * Adds the crf page item display rules.
     *
     * @param proCtcValidValues the pro ctc valid values
     * @return the list< crf page item display rule>
     */
    public List<CrfPageItemDisplayRule> addCrfPageItemDisplayRules(final List<ProCtcValidValue> proCtcValidValues) {
        getCrfPageItemDisplayRules().clear();
        final List<CrfPageItemDisplayRule> addedCrfPageItemDisplayRules = new ArrayList<CrfPageItemDisplayRule>();
        for (ProCtcValidValue proCtcValidValue : proCtcValidValues) {
            CrfPageItemDisplayRule crfPageItemDisplayRule = new CrfPageItemDisplayRule();
            crfPageItemDisplayRule.setProCtcValidValue(proCtcValidValue);

            boolean isAdded = addCrfPageItemDisplayRules(crfPageItemDisplayRule);
            if (isAdded) {
                addedCrfPageItemDisplayRules.add(crfPageItemDisplayRule);
            }
        }
        return addedCrfPageItemDisplayRules;
    }

    /**
     * Reset all properties to default.
     */
    public void resetAllPropertiesToDefault() {
        this.setCrfItemAllignment(CrfPageItem.DEFAULT_ALLIGNMENT);
        this.setInstructions(null);
        this.setResponseRequired(Boolean.FALSE);
        this.displayOrder = 0;
        this.getCrfPageItemDisplayRules().clear();


    }
}
