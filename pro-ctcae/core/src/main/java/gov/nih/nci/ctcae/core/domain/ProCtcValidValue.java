package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "PRO_CTC_VALID_VALUES")
public class ProCtcValidValue extends BasePersistable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "value", nullable = false)
    private String value;

    @JoinColumn(name = "pro_ctc_question_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcQuestion proCtcQuestion;

    public ProCtcValidValue() {
    }

    public ProCtcValidValue(Integer id) {
        this.id = id;
    }

    public ProCtcValidValue(String value) {
        this.value = value;
    }

    public ProCtcValidValue(Integer id, String value) {
        this.id = id;
        this.value = value;
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

        ProCtcValidValue that = (ProCtcValidValue) o;

        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;
        if (proCtcQuestion != null ? !proCtcQuestion.equals(that.proCtcQuestion)
                : that.proCtcQuestion != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (proCtcQuestion != null ? proCtcQuestion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return value;
    }

}
