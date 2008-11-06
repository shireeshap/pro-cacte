package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;

/**
 * @author Vinay Kumar
 * @crated Nov 6, 2008
 */
@Entity
@Table(name = "CTC_CATEGORIES")
public class CtcCategory extends BasePersistable {

    @Column(name = "name")
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
