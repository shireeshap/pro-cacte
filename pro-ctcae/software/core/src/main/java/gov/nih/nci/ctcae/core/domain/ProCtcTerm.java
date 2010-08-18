package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//

/**
 * The Class ProCtcTerm.
 *
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "PRO_CTC_TERMS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_pro_ctc_terms_id")})
public class ProCtcTerm extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The term.
     */
    @Column(name = "term", nullable = false)
    private String term;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "core", nullable = true)
    private Boolean core;


    /**
     * The pro ctc questions.
     */
    @OneToMany(mappedBy = "proCtcTerm")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<ProCtcQuestion> proCtcQuestions = new ArrayList<ProCtcQuestion>();

    /**
     * The pro ctc.
     */
    @JoinColumn(name = "pro_ctc_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtc proCtc;

    /**
     * The ctc term.
     */
    @JoinColumn(name = "ctc_term_id", referencedColumnName = "id")
    @ManyToOne
    private CtcTerm ctcTerm;

    /**
     * Instantiates a new pro ctc term.
     */
    public ProCtcTerm() {
        super();
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
     * Gets the term.
     *
     * @return the term
     */
    public String getTerm() {
        return term;
    }

    /**
     * Sets the term.
     *
     * @param term the new term
     */
    public void setTerm(String term) {
        this.term = term;
    }


    /**
     * Gets the pro ctc questions.
     *
     * @return the pro ctc questions
     */
    public List<ProCtcQuestion> getProCtcQuestions() {
        return proCtcQuestions;
    }

    /**
     * Gets the pro ctc.
     *
     * @return the pro ctc
     */
    public ProCtc getProCtc() {
        return proCtc;
    }

    /**
     * Sets the pro ctc.
     *
     * @param proCtc the new pro ctc
     */
    public void setProCtc(ProCtc proCtc) {
        this.proCtc = proCtc;
    }


    /**
     * Adds the pro ctc question.
     *
     * @param proCtcQuestion the pro ctc question
     */
    public void addProCtcQuestion(ProCtcQuestion proCtcQuestion) {
        if (proCtcQuestion != null) {
            proCtcQuestion.setProCtcTerm(this);
            proCtcQuestions.add(proCtcQuestion);

        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */

    @Override
    public String toString() {
        return term;
    }

    /**
     * Gets the ctc term.
     *
     * @return the ctc term
     */
    public CtcTerm getCtcTerm() {
        return ctcTerm;
    }

    /**
     * Sets the ctc term.
     *
     * @param ctcTerm the new ctc term
     */
    public void setCtcTerm(CtcTerm ctcTerm) {
        this.ctcTerm = ctcTerm;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProCtcTerm)) return false;

        ProCtcTerm that = (ProCtcTerm) o;

        if (ctcTerm != null ? !ctcTerm.equals(that.ctcTerm) : that.ctcTerm != null) return false;
        if (proCtc != null ? !proCtc.equals(that.proCtc) : that.proCtc != null) return false;
        if (term != null ? !term.equals(that.term) : that.term != null) return false;

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */

    public int hashCode() {
        int result;
        result = (term != null ? term.hashCode() : 0);
        result = 31 * result + (proCtc != null ? proCtc.hashCode() : 0);
        result = 31 * result + (ctcTerm != null ? ctcTerm.hashCode() : 0);
        return result;
    }

    public Boolean isCore() {
        return core;
    }

    public void setCore(Boolean core) {
        this.core = core;
    }
}
