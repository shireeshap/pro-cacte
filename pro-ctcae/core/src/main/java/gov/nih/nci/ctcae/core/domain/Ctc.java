package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//
/**
 * The Class Ctc.
 *
 * @author Mehul Gulati
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "CTC")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_ctc_id")})
public class Ctc extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The name.
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * The ctc categories.
     */
    @OneToMany(mappedBy = "ctc")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})

    private List<CtcCategory> ctcCategories = new ArrayList<CtcCategory>();

    /**
     * Instantiates a new ctc.
     */
    public Ctc() {
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
     * Gets the ctc categories.
     *
     * @return the ctc categories
     */
    public List<CtcCategory> getCtcCategories() {
        return ctcCategories;
    }

    /**
     * Sets the ctc categories.
     *
     * @param ctcCategories the new ctc categories
     */
    public void setCtcCategories(List<CtcCategory> ctcCategories) {
        this.ctcCategories = ctcCategories;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ctc)) return false;

        Ctc ctc = (Ctc) o;

        if (name != null ? !name.equals(ctc.name) : ctc.name != null) return false;

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }
}