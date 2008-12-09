package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "pro_ctc_terms")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_pro_ctc_terms_id")})
public class ProCtcTerm extends BasePersistable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@Column(name = "term", nullable = false)
	private String term;

	@JoinColumn(name = "category_id", referencedColumnName = "id")
	@ManyToOne
	private CtcCategory category;

	@Column(name = "select_ae")
	private String select;

	@Column(name = "ctep_term")
	private String ctepTerm;

	@Column(name = "ctep_code")
	private String ctepCode;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcTerm")
	private Collection<ProCtcQuestion> proCtcQuestions = new ArrayList<ProCtcQuestion>();

	@JoinColumn(name = "pro_ctc_id", referencedColumnName = "id")
	@ManyToOne
	private ProCtc proCtc;

	public ProCtcTerm() {
	}

	public ProCtcTerm(Integer id) {
		this.id = id;
	}

	public ProCtcTerm(Integer id, String term) {
		this.id = id;
		this.term = term;
	}

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

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getCtepTerm() {
		return ctepTerm;
	}

	public void setCtepTerm(String ctepTerm) {
		this.ctepTerm = ctepTerm;
	}

	public String getCtepCode() {
		return ctepCode;
	}

	public void setCtepCode(String ctepCode) {
		this.ctepCode = ctepCode;
	}

	public Collection<ProCtcQuestion> getProCtcQuestions() {
		return proCtcQuestions;
	}

	public ProCtc getProCtc() {
		return proCtc;
	}

	public void setProCtc(ProCtc proCtc) {
		this.proCtc = proCtc;
	}

	public CtcCategory getCategory() {
		return category;
	}

	public void setCategory(CtcCategory category) {
		this.category = category;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final ProCtcTerm that = (ProCtcTerm) o;

		if (category != null ? !category.equals(that.category) : that.category != null) return false;
		if (ctepCode != null ? !ctepCode.equals(that.ctepCode) : that.ctepCode != null) return false;
		if (ctepTerm != null ? !ctepTerm.equals(that.ctepTerm) : that.ctepTerm != null) return false;
		if (proCtc != null ? !proCtc.equals(that.proCtc) : that.proCtc != null) return false;
		if (select != null ? !select.equals(that.select) : that.select != null) return false;
		if (term != null ? !term.equals(that.term) : that.term != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = term != null ? term.hashCode() : 0;
		result = 31 * result + (category != null ? category.hashCode() : 0);
		result = 31 * result + (select != null ? select.hashCode() : 0);
		result = 31 * result + (ctepTerm != null ? ctepTerm.hashCode() : 0);
		result = 31 * result + (ctepCode != null ? ctepCode.hashCode() : 0);
		result = 31 * result + (proCtc != null ? proCtc.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return term;
	}
}
