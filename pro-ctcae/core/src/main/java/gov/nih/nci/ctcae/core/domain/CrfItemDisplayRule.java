package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author Vinay Kumar
 * @created Dec 17, 2008
 */

@Entity
@Table(name = "crf_item_display_rules")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_item_display_rules_id")})
public class CrfItemDisplayRule extends BasePersistable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;


	@JoinColumn(name = "crf_item_id", referencedColumnName = "id")
	@ManyToOne
	private CrfItem crfItem;

	@Column(name = "required_object_class")
	private String requiredObjectClass;

	@Column(name = "required_object_id")
	private Integer requiredObjectId;

	@Transient
	private Persistable persistable;

	public CrfItemDisplayRule() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CrfItem getCrfItem() {
		return crfItem;
	}

	public void setCrfItem(final CrfItem crfItem) {
		this.crfItem = crfItem;
	}

	public String getRequiredObjectClass() {
		return requiredObjectClass;
	}

	private void setRequiredObjectClass(final String requiredObjectClass) {
		this.requiredObjectClass = requiredObjectClass;
	}

	public Integer getRequiredObjectId() {
		return requiredObjectId;
	}

	private void setRequiredObjectId(final Integer requiredObjectId) {
		this.requiredObjectId = requiredObjectId;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final CrfItemDisplayRule that = (CrfItemDisplayRule) o;

		if (crfItem != null ? !crfItem.equals(that.crfItem) : that.crfItem != null) return false;
		if (requiredObjectClass != null ? !requiredObjectClass.equals(that.requiredObjectClass) : that.requiredObjectClass != null)
			return false;
		if (requiredObjectId != null ? !requiredObjectId.equals(that.requiredObjectId) : that.requiredObjectId != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = crfItem != null ? crfItem.hashCode() : 0;
		result = 31 * result + (requiredObjectClass != null ? requiredObjectClass.hashCode() : 0);
		result = 31 * result + (requiredObjectId != null ? requiredObjectId.hashCode() : 0);
		return result;
	}

	private void setRequiredObjectClass(final Class requiredObjectClass) {
		setRequiredObjectClass(requiredObjectClass.getName());
	}

	public void setPersistable(final Persistable persistable) {
		if (persistable != null) {
			setRequiredObjectClass(persistable.getClass());
			setRequiredObjectId(persistable.getId());
		}
		this.persistable=persistable;

	}

	public Persistable getPersistable() {
		return persistable;
	}
}