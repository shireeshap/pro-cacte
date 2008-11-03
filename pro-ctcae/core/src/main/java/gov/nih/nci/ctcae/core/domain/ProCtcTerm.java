package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "PRO_CTC_TERMS")

public class ProCtcTerm extends BasePersistable{

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcTerm")
    private Collection<ProCtcQuestion> proCtcQuestions = new ArrayList<ProCtcQuestion>();

    @JoinColumn(name = "pro_ctc_id", referencedColumnName = "id")
	@ManyToOne
	private ProCtc proCtc;

    public ProCtcTerm() {
    }

    public ProCtcTerm(Integer id) {
        this.id = id;
    }

    public ProCtcTerm(Integer id, String term) {
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

    public Collection<ProCtcQuestion> getProCtcTerms() {
        return proCtcQuestions;
    }


    public ProCtc getProCtc() {
        return proCtc;
    }

    public void setProCtc(ProCtc proCtc) {
        this.proCtc = proCtc;
    }


    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProCtcTerm proCtcTerm = (ProCtcTerm) o;

        if (ctepCode != null ? !ctepCode.equals(proCtcTerm.ctepCode) : proCtcTerm.ctepCode != null) return false;
        if (ctepTerm != null ? !ctepTerm.equals(proCtcTerm.ctepTerm) : proCtcTerm.ctepTerm != null) return false;
        if (id != null ? !id.equals(proCtcTerm.id) : proCtcTerm.id != null) return false;
        if (proCtcQuestions != null ? !proCtcQuestions.equals(proCtcTerm.proCtcQuestions) : proCtcTerm.proCtcQuestions != null)
            return false;
        if (select != null ? !select.equals(proCtcTerm.select) : proCtcTerm.select != null) return false;
        if (term != null ? !term.equals(proCtcTerm.term) : proCtcTerm.term != null) return false;

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
        result = 31 * result + (proCtcQuestions != null ? proCtcQuestions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return term;
    }
}
