package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.validation.annotation.NotEmpty;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrf;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "CRFS")
public class CRF extends BaseVersionable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", nullable = false)
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

	public List<CrfItem> getCrfItems() {
		Collections.sort(crfItems, new DisplayOrderComparator());
		return crfItems;
	}

	public void removeCrfItem(CrfItem crfItem) {
		if (crfItem != null) {
			crfItems.remove(crfItem);
		}
	}

	public void removeCrfItems(Collection<CrfItem> crfItems) {
		for (CrfItem crfItem : crfItems) {
			removeCrfItem(crfItem);
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

	public void addOrUpdateCrfItem(final ProCtcQuestion proCtcQuestion, final int displayOrder) {
		CrfItem crfItem = new CrfItem();
		crfItem.setProCtcQuestion(proCtcQuestion);
		crfItem.setDisplayOrder(displayOrder);

		//check if it already exists
		for (CrfItem existingCrfItem : getCrfItems()) {
			if (existingCrfItem.getProCtcQuestion() != null
				&& (existingCrfItem.getProCtcQuestion().equals(crfItem.getProCtcQuestion()))) {
				//probably we are updating order only
				existingCrfItem.setDisplayOrder(crfItem.getDisplayOrder());
				return;
			}
		}
		if (crfItem.getDisplayOrder() == null || crfItem.getDisplayOrder() == 0) {
			crfItem.setDisplayOrder(getCrfItems().size() + 1);

		}
		crfItem.setCrf(this);
		crfItems.add(crfItem);

	}

	/**
	 * used for adding a new crf items with empty properties
	 *
	 * @param proCtcQuestion
	 * @param displayOrder
	 */
	public void removeExistingAndAddNewCrfItem(final ProCtcQuestion proCtcQuestion, final Integer displayOrder) {
		CrfItem crfItem = new CrfItem();
		crfItem.setProCtcQuestion(proCtcQuestion);
		crfItem.setDisplayOrder(displayOrder);

		//check if it already exists
		CrfItem existingCrfItem = getCrfItemByQuestion(crfItem.getProCtcQuestion());
		if (existingCrfItem != null) {
			//we are updating order only  and removing properties
			existingCrfItem.setDisplayOrder(crfItem.getDisplayOrder());
			existingCrfItem.setCrfItemAllignment(null);
			existingCrfItem.setInstructions(null);
			existingCrfItem.setResponseRequired(Boolean.FALSE);
			return;
		}

		if (crfItem.getDisplayOrder() == null || crfItem.getDisplayOrder() == 0) {
			crfItem.setDisplayOrder(getCrfItems().size() + 1);

		}
		crfItem.setCrf(this);
		crfItems.add(crfItem);


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



    public void removeExistingAndAddNewCrfItem(final ProCtcTerm proCtcTerm, final Integer displayOrder) {
		for (ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()) {
			removeExistingAndAddNewCrfItem(proCtcQuestion, displayOrder);
		}

	}
}
