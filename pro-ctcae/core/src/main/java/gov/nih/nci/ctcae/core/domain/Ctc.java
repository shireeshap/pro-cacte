package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "ctc")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_ctc_id")})
public class Ctc extends BasePersistable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ctc")
    private List<CtcCategory> ctcCategories = new ArrayList<CtcCategory>();

    public Ctc() {
	}



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

    public List<CtcCategory> getCtcCategories() {
        return ctcCategories;
    }

    public void setCtcCategories(List<CtcCategory> ctcCategories) {
        this.ctcCategories = ctcCategories;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ctc)) return false;

        Ctc ctc = (Ctc) o;

        if (ctcCategories != null ? !ctcCategories.equals(ctc.ctcCategories) : ctc.ctcCategories != null) return false;
        if (name != null ? !name.equals(ctc.name) : ctc.name != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (ctcCategories != null ? ctcCategories.hashCode() : 0);
        return result;
    }
}