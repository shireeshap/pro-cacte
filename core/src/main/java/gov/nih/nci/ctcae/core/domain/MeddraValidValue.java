package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author Mehul Gulati
 * Date: Jun 1, 2009
 */

@Entity
@Table(name = "MEDDRA_VALID_VALUES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_meddra_valid_values_id")})
public class MeddraValidValue extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "display_order", nullable = true)
    private Integer displayOrder;

    @JoinColumn(name = "meddra_question_id", referencedColumnName = "id")
    @ManyToOne
    private MeddraQuestion meddraQuestion;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public MeddraQuestion getMeddraQuestion() {
        return meddraQuestion;
    }

    public void setMeddraQuestion(MeddraQuestion meddraQuestion) {
        this.meddraQuestion = meddraQuestion;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeddraValidValue)) return false;

        MeddraValidValue that = (MeddraValidValue) o;

        if (displayOrder != null ? !displayOrder.equals(that.displayOrder) : that.displayOrder != null) return false;
        if (meddraQuestion != null ? !meddraQuestion.equals(that.meddraQuestion) : that.meddraQuestion != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (value != null ? value.hashCode() : 0);
        result = 31 * result + (displayOrder != null ? displayOrder.hashCode() : 0);
        result = 31 * result + (meddraQuestion != null ? meddraQuestion.hashCode() : 0);
        return result;
    }
}
