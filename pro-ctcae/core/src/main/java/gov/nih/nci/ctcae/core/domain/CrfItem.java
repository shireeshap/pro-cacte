package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "CRF_ITEMS")
public class CrfItem extends BasePersistable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder=0;

    @JoinColumn(name = "crf_id", referencedColumnName = "id")
    @ManyToOne
    private CRF crf;

    @JoinColumn(name = "pro_ctc_question_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcQuestion proCtcQuestion;

    public CrfItem() {
    }

    public CrfItem(Integer id) {
        this.id = id;
    }

    public CrfItem(Integer id, Integer displayOrder) {
        this.id = id;
        this.displayOrder = displayOrder;
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

    public CRF getCRF() {
        return crf;
    }

    public void setCRF(CRF crf) {
        this.crf = crf;
    }

    public ProCtcQuestion getProCtcTerm() {
        return proCtcQuestion;
    }

    public void setProCtcTerm(ProCtcQuestion proCtcQuestion) {
        this.proCtcQuestion = proCtcQuestion;
    }

    @Override
	public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CrfItem crfItem = (CrfItem) o;

        if (displayOrder != crfItem.displayOrder)
            return false;
        if (crf != null ? !crf.equals(crfItem.crf) : crfItem.crf != null)
            return false;
        if (id != null ? !id.equals(crfItem.id) : crfItem.id != null)
            return false;
        if (proCtcQuestion != null ? !proCtcQuestion.equals(crfItem.proCtcQuestion)
                : crfItem.proCtcQuestion != null)
            return false;

        return true;
    }

    @Override
	public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + displayOrder;
        result = 31 * result + (crf != null ? crf.hashCode() : 0);
        result = 31 * result + (proCtcQuestion != null ? proCtcQuestion.hashCode() : 0);
        return result;
    }

//    @Override
//    public String toString() {
//        return "[DISPLAY ORDER: CRF : QUESTION] " + displayOrder + " : "
//                + crf.getTitle() + " : " + proCtcQuestion.getQuestionText();
//    }

}
