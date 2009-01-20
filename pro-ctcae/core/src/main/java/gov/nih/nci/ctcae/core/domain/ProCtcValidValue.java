package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "pro_ctc_valid_values")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_pro_ctc_valid_values_id")})
public class ProCtcValidValue extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "value", nullable = false)
    private String value;

    @JoinColumn(name = "pro_ctc_question_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcQuestion proCtcQuestion;

    public ProCtcValidValue() {
    }

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

    public ProCtcQuestion getProCtcQuestion() {
        return proCtcQuestion;
    }


    public void setProCtcQuestion(ProCtcQuestion proCtcQuestion) {
        this.proCtcQuestion = proCtcQuestion;
    }


    @Override
    public String toString() {
        return value + "";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProCtcValidValue)) return false;

        ProCtcValidValue that = (ProCtcValidValue) o;

        if (proCtcQuestion != null ? !proCtcQuestion.equals(that.proCtcQuestion) : that.proCtcQuestion != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (value != null ? value.hashCode() : 0);
        result = 31 * result + (proCtcQuestion != null ? proCtcQuestion.hashCode() : 0);
        return result;
    }
}
