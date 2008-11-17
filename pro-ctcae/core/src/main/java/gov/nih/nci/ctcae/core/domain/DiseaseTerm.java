package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;

/**
 * @author Mehul Gulati
 * Date: Nov 12, 2008
 */

@Entity
@Table(name = "disease_terms")
public class DiseaseTerm extends BaseVersionable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "term", nullable = true)
    private String term;

    @Column(name = "ctep_term", nullable = true)
    private String ctepTerm;

    @JoinColumn(name = "disease_category_id", referencedColumnName = "id")
    @ManyToOne
    private DiseaseCategory category;


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

    public String getCtepTerm() {
        return ctepTerm;
    }

    public void setCtepTerm(String ctepTerm) {
        this.ctepTerm = ctepTerm;
    }

    public DiseaseCategory getCategory() {
        return category;
    }

    public void setCategory(DiseaseCategory category) {
        this.category = category;
    }

    @Transient
    public String getFullName() {
        return term;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiseaseTerm)) return false;

        DiseaseTerm that = (DiseaseTerm) o;

        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (ctepTerm != null ? !ctepTerm.equals(that.ctepTerm) : that.ctepTerm != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (term != null ? !term.equals(that.term) : that.term != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (term != null ? term.hashCode() : 0);
        result = 31 * result + (ctepTerm != null ? ctepTerm.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
