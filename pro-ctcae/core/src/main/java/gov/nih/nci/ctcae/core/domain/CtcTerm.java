package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "ctc_terms")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_pro_ctc_terms_id")})
public class CtcTerm extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "term", nullable = false)
    private String term;

    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne
    private CtcCategory category;

    @Column(name = "select_ae")
    private String select;

    @Column(name = "ctep_term")
    private String ctepTerm;

    @Column(name = "ctep_code")
    private String ctepCode;


    public CtcTerm() {
    }

    public CtcTerm(Integer id) {
        this.id = id;
    }

    public CtcTerm(Integer id, String term) {
        this.id = id;
        this.term = term;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getCtepTerm() {
        return ctepTerm;
    }

    public void setCtepTerm(String ctepTerm) {
        this.ctepTerm = ctepTerm;
    }

    public String getCtepCode() {
        return ctepCode;
    }

    public void setCtepCode(String ctepCode) {
        this.ctepCode = ctepCode;
    }


    public CtcCategory getCategory() {
        return category;
    }

    public void setCategory(CtcCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return term;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CtcTerm)) return false;

        CtcTerm ctcTerm = (CtcTerm) o;

        if (category != null ? !category.equals(ctcTerm.category) : ctcTerm.category != null) return false;
        if (ctepCode != null ? !ctepCode.equals(ctcTerm.ctepCode) : ctcTerm.ctepCode != null) return false;
        if (ctepTerm != null ? !ctepTerm.equals(ctcTerm.ctepTerm) : ctcTerm.ctepTerm != null) return false;
        if (select != null ? !select.equals(ctcTerm.select) : ctcTerm.select != null) return false;
        if (term != null ? !term.equals(ctcTerm.term) : ctcTerm.term != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (term != null ? term.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (select != null ? select.hashCode() : 0);
        result = 31 * result + (ctepTerm != null ? ctepTerm.hashCode() : 0);
        result = 31 * result + (ctepCode != null ? ctepCode.hashCode() : 0);
        return result;
    }
}