package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.LinkedList;
import java.util.List;

//
/**
 * The Class CtcCategory.
 *
 * @author Vinay Kumar
 * @since Nov 6, 2008
 */
@Entity
@Table(name = "CTC_CATEGORIES")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_ctc_categories_id")})
public class CtcCategory extends BasePersistable {

    /**
     * The name.
     */
    @Column(name = "name")
    private String name;

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The ctc.
     */
    @JoinColumn(name = "version_id", referencedColumnName = "id")
    @ManyToOne
    private Ctc ctc;


    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<CategoryTermSet> categoryTermSets = new LinkedList();

//    private boolean userDefined;

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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }



    /**
     * Gets the ctc.
     *
     * @return the ctc
     */
    public Ctc getCtc() {
        return ctc;
    }

    /**
     * Sets the ctc.
     *
     * @param ctc the new ctc
     */
    public void setCtc(Ctc ctc) {
        this.ctc = ctc;
    }

    public List<CategoryTermSet> getCategoryTermSets() {
        return categoryTermSets;
    }

    public void setCategoryTermSets(List<CategoryTermSet> categoryTermSets) {
        this.categoryTermSets = categoryTermSets;
    }

    /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CtcCategory)) return false;

        CtcCategory that = (CtcCategory) o;

        if (ctc != null ? !ctc.equals(that.ctc) : that.ctc != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 31 * result + (ctc != null ? ctc.hashCode() : 0);
        return result;
    }
}
