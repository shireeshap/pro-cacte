package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class CtcTerm.
 *
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "CTC_TERMS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "SEQ_CTC_TERMS_ID")})
public class CtcTerm extends BasePersistable {

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

    /**
     * The category.
     */
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne
    private CtcCategory category;

    /**
     * The select.
     */
    @Column(name = "select_ae")
    private String select;

    /**
     * The ctep term.
     */
    @Column(name = "ctep_term")
    private String ctepTerm;

    /**
     * The ctep code.
     */
    @Column(name = "ctep_code")
    private String ctepCode;


    /**
     * Instantiates a new ctc term.
     */
    public CtcTerm() {
        super();
    }

    /**
     * Instantiates a new ctc term.
     *
     * @param id the id
     */
    public CtcTerm(Integer id) {
        this.id = id;
    }

    /**
     * Instantiates a new ctc term.
     *
     * @param id   the id
     * @param term the term
     */
    public CtcTerm(Integer id, String term) {
        this.id = id;
        this.term = term;
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
     * Gets the select.
     *
     * @return the select
     */
    public String getSelect() {
        return select;
    }

    /**
     * Sets the select.
     *
     * @param select the new select
     */
    public void setSelect(String select) {
        this.select = select;
    }

    /**
     * Gets the ctep term.
     *
     * @return the ctep term
     */
    public String getCtepTerm() {
        return ctepTerm;
    }

    /**
     * Sets the ctep term.
     *
     * @param ctepTerm the new ctep term
     */
    public void setCtepTerm(String ctepTerm) {
        this.ctepTerm = ctepTerm;
    }

    /**
     * Gets the ctep code.
     *
     * @return the ctep code
     */
    public String getCtepCode() {
        return ctepCode;
    }

    /**
     * Sets the ctep code.
     *
     * @param ctepCode the new ctep code
     */
    public void setCtepCode(String ctepCode) {
        this.ctepCode = ctepCode;
    }


    /**
     * Gets the category.
     *
     * @return the category
     */
    public CtcCategory getCategory() {
        return category;
    }

    /**
     * Sets the category.
     *
     * @param category the new category
     */
    public void setCategory(CtcCategory category) {
        this.category = category;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return term;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
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