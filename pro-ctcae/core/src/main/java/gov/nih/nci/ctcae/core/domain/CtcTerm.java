package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "CTC_TERMS")

public class CtcTerm extends BasePersistable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "term", nullable = false)
    private String term;

    @Column(name = "select_ae")
    private String select;

    @Column(name = "ctep_term")
    private String ctepTerm;

    @Column(name = "ctep_code")
    private String ctepCode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ctcTerm")
    private Collection<ProCtcTerm> proCtcTerms = new ArrayList<ProCtcTerm>();

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

    public Collection<ProCtcTerm> getProCtcTerms() {
        return proCtcTerms;
    }


    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CtcTerm ctcTerm = (CtcTerm) o;

        if (ctepCode != null ? !ctepCode.equals(ctcTerm.ctepCode) : ctcTerm.ctepCode != null) return false;
        if (ctepTerm != null ? !ctepTerm.equals(ctcTerm.ctepTerm) : ctcTerm.ctepTerm != null) return false;
        if (id != null ? !id.equals(ctcTerm.id) : ctcTerm.id != null) return false;
        if (proCtcTerms != null ? !proCtcTerms.equals(ctcTerm.proCtcTerms) : ctcTerm.proCtcTerms != null)
            return false;
        if (select != null ? !select.equals(ctcTerm.select) : ctcTerm.select != null) return false;
        if (term != null ? !term.equals(ctcTerm.term) : ctcTerm.term != null) return false;

        return true;
    }

    @Override
	public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (term != null ? term.hashCode() : 0);
        result = 31 * result + (select != null ? select.hashCode() : 0);
        result = 31 * result + (ctepTerm != null ? ctepTerm.hashCode() : 0);
        result = 31 * result + (ctepCode != null ? ctepCode.hashCode() : 0);
        result = 31 * result + (proCtcTerms != null ? proCtcTerms.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return term;
    }
}
