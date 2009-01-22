package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "pro_ctc_terms")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_pro_ctc_terms_id")})
public class ProCtcTerm extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "term", nullable = false)
    private String term;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcTerm")
    private Collection<ProCtcQuestion> proCtcQuestions = new ArrayList<ProCtcQuestion>();

    @JoinColumn(name = "pro_ctc_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtc proCtc;

    @JoinColumn(name = "ctc_term_id", referencedColumnName = "id")
    @ManyToOne
    private CtcTerm ctcTerm;

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


    public Collection<ProCtcQuestion> getProCtcQuestions() {
        return proCtcQuestions;
    }

    public ProCtc getProCtc() {
        return proCtc;
    }

    public void setProCtc(ProCtc proCtc) {
        this.proCtc = proCtc;
    }


    public void addProCtcQuestion(ProCtcQuestion proCtcQuestion) {
        if (proCtcQuestion != null) {
            proCtcQuestion.setProCtcTerm(this);
            proCtcQuestions.add(proCtcQuestion);
            
        }
    }


    @Override
    public String toString() {
        return term;
    }

    public CtcTerm getCtcTerm() {
        return ctcTerm;
    }

    public void setCtcTerm(CtcTerm ctcTerm) {
        this.ctcTerm = ctcTerm;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProCtcTerm)) return false;

        ProCtcTerm that = (ProCtcTerm) o;

        if (ctcTerm != null ? !ctcTerm.equals(that.ctcTerm) : that.ctcTerm != null) return false;
        if (proCtc != null ? !proCtc.equals(that.proCtc) : that.proCtc != null) return false;
        if (term != null ? !term.equals(that.term) : that.term != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (term != null ? term.hashCode() : 0);
        result = 31 * result + (proCtc != null ? proCtc.hashCode() : 0);
        result = 31 * result + (ctcTerm != null ? ctcTerm.hashCode() : 0);
        return result;
    }
}
