package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;
import java.util.List;

/**
 * @author Mehul Gulati
 * Date: Nov 12, 2008
 */
@Entity
@Table(name = "disease_categories")
public class DiseaseCategory extends BaseVersionable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "names", nullable = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category", fetch = FetchType.LAZY)
    private List<DiseaseTerm> terms;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DiseaseTerm> getTerms() {
        return terms;
    }

    public void setTerms(List<DiseaseTerm> terms) {
        this.terms = terms;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
