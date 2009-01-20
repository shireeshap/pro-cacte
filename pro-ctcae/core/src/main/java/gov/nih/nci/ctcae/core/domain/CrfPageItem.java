package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "crf_page_items")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_page_items_id")})
public class CrfPageItem extends BasePersistable {

    public static final Integer INITIAL_ORDER = 1;

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @JoinColumn(name = "crf_page_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private CRFPage crfPage;

    @JoinColumn(name = "pro_ctc_question_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcQuestion proCtcQuestion;

    @Column(name = "response_required")
    private Boolean responseRequired = Boolean.FALSE;

    @Column(name = "instructions")
    private String instructions;

    public static CrfItemAllignment DEFAULT_ALLIGNMENT = CrfItemAllignment.HORIZONTAL;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "allignment")
    private CrfItemAllignment crfItemAllignment = DEFAULT_ALLIGNMENT;

    @OneToMany(mappedBy = "crfPageItem", fetch = FetchType.LAZY)
    @Cascade(value = {CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private Set<CrfPageItemDisplayRule> crfPageItemDisplayRules = new HashSet<CrfPageItemDisplayRule>();


    public CrfPageItem() {
    }


    public Boolean getResponseRequired() {
        return responseRequired;
    }

    public void setResponseRequired(final Boolean responseRequired) {
        this.responseRequired = responseRequired;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(final String instructions) {
        this.instructions = instructions;
    }

    public CrfItemAllignment getCrfItemAllignment() {
        return crfItemAllignment;
    }

    public void setCrfItemAllignment(final CrfItemAllignment crfItemAllignment) {
        this.crfItemAllignment = crfItemAllignment;
    }

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


    public CRFPage getCrfPage() {
        return crfPage;
    }

    public void setCrfPage(final CRFPage crfPage) {
        this.crfPage = crfPage;
    }

    public ProCtcQuestion getProCtcQuestion() {
        return proCtcQuestion;
    }

    public void setProCtcQuestion(ProCtcQuestion proCtcQuestion) {
        this.proCtcQuestion = proCtcQuestion;
    }

    public CrfPageItem getCopy() {
        CrfPageItem copiedCrfPageItem = new CrfPageItem();
        copiedCrfPageItem.setInstructions(instructions);
        copiedCrfPageItem.setDisplayOrder(displayOrder);
        copiedCrfPageItem.setResponseRequired(responseRequired);
        copiedCrfPageItem.setCrfItemAllignment(crfItemAllignment);
        copiedCrfPageItem.setProCtcQuestion(proCtcQuestion);
        for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItemDisplayRules) {
            copiedCrfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule.getCopy());
        }
        return copiedCrfPageItem;
    }

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

    //    @Override
//    public String toString() {
//        return "[DISPLAY ORDER: CRF : QUESTION] " + displayOrder + " : "
//                + crf.getTitle() + " : " + proCtcQuestion.getQuestionText();
//    }


    public Set<CrfPageItemDisplayRule> getCrfPageItemDisplayRules() {
        return crfPageItemDisplayRules;
    }


    public void removeCrfPageItemDisplayRulesByProCtcValidValueIds(final String objectsIdsToRemove) {
        Set<String> ids = org.springframework.util.StringUtils.commaDelimitedListToSet(objectsIdsToRemove);
        final Set<Integer> proCtcValidValues = new HashSet();
        for (String id : ids) {
            proCtcValidValues.add(Integer.valueOf(id));
        }
        removeCrfPageItemDisplayRulesByIds(proCtcValidValues);

    }

    public void removeCrfPageItemDisplayRulesByIds(final Set<Integer> proCtcValidValues) {
        List<CrfPageItemDisplayRule> crfPageItemDisplayRulesToRemove = new ArrayList<CrfPageItemDisplayRule>();
        for (Integer id : proCtcValidValues) {
            CrfPageItemDisplayRule crfPageItemDisplayRule = getCrfDisplayRuleById(id);
            crfPageItemDisplayRulesToRemove.add(crfPageItemDisplayRule);
        }

        for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItemDisplayRulesToRemove) {
            this.removeCrfPageItemDisplayRule(crfPageItemDisplayRule);
        }

    }

    private CrfPageItemDisplayRule getCrfDisplayRuleById(final Integer id) {
        for (CrfPageItemDisplayRule crfPageItemDisplayRule : getCrfPageItemDisplayRules()) {
            if (crfPageItemDisplayRule.getProCtcValidValue().getId().equals(id)) {
                return crfPageItemDisplayRule;
            }
        }
        return null;
    }

    private void removeCrfPageItemDisplayRule(final CrfPageItemDisplayRule crfPageItemDisplayRule) {

        getCrfPageItemDisplayRules().remove(crfPageItemDisplayRule);
    }

    public boolean addCrfPageItemDisplayRules(CrfPageItemDisplayRule crfPageItemDisplayRule) {
        if (crfPageItemDisplayRule != null) {
            crfPageItemDisplayRule.setCrfItem(this);
            boolean b = getCrfPageItemDisplayRules().add(crfPageItemDisplayRule);
            return b;

        }
        return false;
    }

    public boolean shouldDisplay(List<ProCtcValidValue> selectedProCtcValidValues) {
        return true;
    }

    public List<CrfPageItemDisplayRule> addCrfPageItemDisplayRules(final List<ProCtcValidValue> proCtcValidValues) {
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

    public void resetAllPropertiesToDefault() {
        this.setCrfItemAllignment(CrfPageItem.DEFAULT_ALLIGNMENT);
        this.setInstructions(null);
        this.setResponseRequired(Boolean.FALSE);
        this.displayOrder = 0;
        this.getCrfPageItemDisplayRules().clear();

    }
}
