package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author mehul
 *         Date: 4/18/12
 */
@Entity
@Table(name = "CATEGORY_TERM_SET")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_cts_id")})
public class CategoryTermSet extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CtcCategory category;

    @ManyToOne
    @JoinColumn(name = "ctcTerm_id", nullable = false)
    private CtcTerm ctcTerm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CtcCategory getCategory() {
        return category;
    }

    public void setCategory(CtcCategory category) {
        this.category = category;
    }

    public CtcTerm getCtcTerm() {
        return ctcTerm;
    }

    public void setCtcTerm(CtcTerm ctcTerm) {
        this.ctcTerm = ctcTerm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryTermSet that = (CategoryTermSet) o;

        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (ctcTerm != null ? !ctcTerm.equals(that.ctcTerm) : that.ctcTerm != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = category != null ? category.hashCode() : 0;
        result = 31 * result + (ctcTerm != null ? ctcTerm.hashCode() : 0);
        return result;
    }
}
