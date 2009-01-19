package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author Vinay Kumar
 * @crated Nov 6, 2008
 */
@Entity
@Table(name = "ctc_categories")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_ctc_categories_id")})
public class CtcCategory extends BasePersistable {

	@Column(name = "name")
	private String name;

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

    @JoinColumn(name = "version_id", referencedColumnName = "id")
    @ManyToOne
    private Ctc ctc;

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



    public Ctc getCtc() {
        return ctc;
    }

    public void setCtc(Ctc ctc) {
        this.ctc = ctc;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CtcCategory)) return false;

        CtcCategory that = (CtcCategory) o;

        if (ctc != null ? !ctc.equals(that.ctc) : that.ctc != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (ctc != null ? ctc.hashCode() : 0);
        return result;
    }
}
