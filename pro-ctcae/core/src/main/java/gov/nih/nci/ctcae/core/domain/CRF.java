package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.validation.annotation.NotEmpty;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrf;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "crfs")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crfs_id")})

public class CRF extends BaseVersionable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "status", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private CrfStatus status = CrfStatus.DRAFT;

	@Column(name = "crf_version", nullable = false)
	private String crfVersion;

	@Column(name = "effective_start_date", nullable = true)
	private Date effectiveStartDate;

	@Column(name = "effective_end_date", nullable = true)
	private Date effectiveEndDate;

	@Column(name = "next_version_id", nullable = true)
	private Integer nextVersionId;

    @Column(name = "parent_version_id", nullable =true)
    private Integer parentVersionId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "crf", fetch = FetchType.EAGER)
	private List<CrfItem> crfItems = new ArrayList<CrfItem>();

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "crf", fetch = FetchType.LAZY)
	private StudyCrf studyCrf;

	public StudyCrf getStudyCrf() {
		return studyCrf;
	}

	public void setStudyCrf(StudyCrf studyCrf) {
		this.studyCrf = studyCrf;
	}


	public CRF() {
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NotEmpty(message = "Missing Title")
	@UniqueTitleForCrf
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CrfStatus getStatus() {
		return status;
	}

	public void setStatus(CrfStatus status) {
		this.status = status;
	}

	public String getCrfVersion() {
		return crfVersion;
	}

	public void setCrfVersion(String crfVersion) {
		this.crfVersion = crfVersion;
	}

	public List<CrfItem> getCrfItemsSortedByDislayOrder() {
		Collections.sort(crfItems, new DisplayOrderComparator());
		return crfItems;
	}

	public List<CrfItem> getCrfItems() {
		return crfItems;
	}

	public void removeCrfItem(CrfItem crfItem) {
		if (crfItem != null) {
			crfItems.remove(crfItem);
		}
	}


	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final CRF crf = (CRF) o;

		if (crfVersion != null ? !crfVersion.equals(crf.crfVersion) : crf.crfVersion != null) return false;
		if (description != null ? !description.equals(crf.description) : crf.description != null) return false;
		if (status != crf.status) return false;
		if (title != null ? !title.equals(crf.title) : crf.title != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = title != null ? title.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (crfVersion != null ? crfVersion.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return title;
	}

	public boolean isReleased() {
		return getStatus().equals(CrfStatus.RELEASED);
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

    public Integer getNextVersionId() {
        return nextVersionId;
    }

    public void setNextVersionId(Integer nextVersionId) {
        this.nextVersionId = nextVersionId;
    }

    public Integer getParentVersionId() {
        return parentVersionId;
    }

    public void setParentVersionId(Integer parentVersionId) {
        this.parentVersionId = parentVersionId;
    }
    

    /**
	 * this is required to update the crf item properties on right side of the create form
	 *
	 * @param proCtcQuestion
	 * @param displayOrder
	 */
	public void addOrUpdateCrfItem(final ProCtcQuestion proCtcQuestion, final Integer displayOrder) {

		//check if it already exists
		for (CrfItem existingCrfItem : getCrfItemsSortedByDislayOrder()) {
			if (existingCrfItem.getProCtcQuestion() != null
				&& (existingCrfItem.getProCtcQuestion().equals(proCtcQuestion))) {
				//probably we are updating order only
				existingCrfItem.setDisplayOrder(displayOrder);
				return;
			}
		}
		CrfItem crfItem = new CrfItem();
		crfItem.setProCtcQuestion(proCtcQuestion);
		crfItem.setDisplayOrder(displayOrder);

		updateOrderNumber(crfItem);
		crfItem.setCrf(this);
		crfItems.add(crfItem);

	}

	/**
	 * used for adding a new crf items with empty properties..this is required to add questions from left side of the create form
	 *
	 * @param proCtcQuestion
	 */
	public CrfItem removeExistingAndAddNewCrfItem(final ProCtcQuestion proCtcQuestion) {
		CrfItem crfItem = new CrfItem();
		crfItem.setProCtcQuestion(proCtcQuestion);

		//check if it already exists
		CrfItem existingCrfItem = getCrfItemByQuestion(crfItem.getProCtcQuestion());
		if (existingCrfItem != null) {
			//we are updating order only  and removing properties
			existingCrfItem.setDisplayOrder(getCrfItemsSortedByDislayOrder().size());
			existingCrfItem.setCrfItemAllignment(null);
			existingCrfItem.setInstructions(null);
			existingCrfItem.setResponseRequired(Boolean.FALSE);
			return existingCrfItem;
		}

		updateOrderNumber(crfItem);
		crfItem.setCrf(this);
		crfItems.add(crfItem);

		return crfItem;


	}

	private void updateOrderNumber(final CrfItem crfItem) {
		if (crfItem.getDisplayOrder() == null || crfItem.getDisplayOrder() == 0) {
			crfItem.setDisplayOrder(getCrfItems().size() + 1);

		}
	}

	public CrfItem getCrfItemByQuestion(final ProCtcQuestion proCtcQuestion) {
		if (proCtcQuestion != null) {
			for (CrfItem existingCrfItem : getCrfItems()) {
				if (existingCrfItem.getProCtcQuestion() != null
					&& (existingCrfItem.getProCtcQuestion().equals(proCtcQuestion))) {
					return existingCrfItem;
				}
			}

		}
		return null;

	}

	public void addCrfItem(CrfItem crfItem) {
		if (crfItem != null) {
			crfItem.setCrf(this);
			crfItems.add(crfItem);
		}
	}


	public CRF getCopy() {

		CRF copiedCrf = new CRF();
		copiedCrf.setTitle("Copy of " + title + "_" + System.currentTimeMillis());
		copiedCrf.setDescription(description);
		copiedCrf.setStatus(CrfStatus.DRAFT);
    	copiedCrf.setCrfVersion("1.0");
		for (CrfItem crfItem : crfItems) {
			copiedCrf.addCrfItem(crfItem.getCopy());
		}

		return copiedCrf;
	}


	public List<CrfItem> removeExistingAndAddNewCrfItem(final ProCtcTerm proCtcTerm) {
		List<ProCtcQuestion> questions = new ArrayList<ProCtcQuestion>(proCtcTerm.getProCtcQuestions());
		List<CrfItem> addedCrfItems = new ArrayList<CrfItem>();
		for (int i = 0; i < questions.size(); i++) {
			ProCtcQuestion proCtcQuestion = questions.get(i);
			CrfItem crfItem = removeExistingAndAddNewCrfItem(proCtcQuestion);
			addedCrfItems.add(crfItem);
		}
		return addedCrfItems;
	}

}
