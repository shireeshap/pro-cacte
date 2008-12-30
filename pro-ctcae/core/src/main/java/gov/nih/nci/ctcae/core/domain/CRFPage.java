package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.*;

/**
 * @author Vinay Kumar
 * @created Dec 29, 2008
 */

@Entity
@Table(name = "crf_pages")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_pages_id")})

public class CRFPage extends BaseVersionable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;


	@Column(name = "description")
	private String description;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "crfPage", fetch = FetchType.LAZY)
	private List<CrfPageItem> crfPageItems = new ArrayList<CrfPageItem>();


	@JoinColumn(name = "crf_id", referencedColumnName = "id", nullable = false)
	@ManyToOne
	private CRF crf;


	public CRFPage() {
	}


	public CRF getCrf() {
		return crf;
	}

	public void setCrf(final CRF crf) {
		this.crf = crf;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public List<CrfPageItem> getCrfItemsSortedByDislayOrder() {
		Collections.sort(crfPageItems, new DisplayOrderComparator());
		return crfPageItems;
	}

	public List<CrfPageItem> getCrfPageItems() {
		return crfPageItems;
	}


	public void removeCrfItem(CrfPageItem crfPageItem) {
		if (crfPageItem != null) {
			crfPageItems.remove(crfPageItem);
		}
	}


//	@Override
//	public boolean equals(final Object o) {
//		if (this == o) return true;
//		if (o == null || getClass() != o.getClass()) return false;
//
//		final CRFPage crf = (CRFPage) o;
//
//		if (description != null ? !description.equals(crf.description) : crf.description != null) return false;
//
//		return true;
//	}
//
//	@Override
//	public int hashCode() {
//		int result = description != null ? description.hashCode() : 0;
//		return result;
//	}


	/**
	 * this is required to update the crf item properties on right side of the create form
	 *
	 * @param proCtcQuestion
	 * @param displayOrder
	 */
	public void addOrUpdateCrfItem(final ProCtcQuestion proCtcQuestion, final Integer displayOrder) {

		//check if it already exists
		for (CrfPageItem existingCrfPageItem : getCrfItemsSortedByDislayOrder()) {
			if (existingCrfPageItem.getProCtcQuestion() != null
				&& (existingCrfPageItem.getProCtcQuestion().equals(proCtcQuestion))) {
				//probably we are updating order only
				existingCrfPageItem.setDisplayOrder(displayOrder);
				return;
			}
		}
		CrfPageItem crfPageItem = new CrfPageItem();
		crfPageItem.setProCtcQuestion(proCtcQuestion);
		crfPageItem.setDisplayOrder(displayOrder);

		updateOrderNumber(crfPageItem);
		crfPageItem.setCrfPage(this);
		crfPageItems.add(crfPageItem);

	}

	/**
	 * this is required to move crf page item from one crf page to another crf page
	 */
	public void addOrUpdateCrfItem(final CrfPageItem crfPageItem) {
		if (crfPageItem != null) {
			//check if it already exists..if yes remove it
			for (CrfPageItem existingCrfPageItem : getCrfItemsSortedByDislayOrder()) {
				if (existingCrfPageItem.getProCtcQuestion() != null
					&& (existingCrfPageItem.getProCtcQuestion().equals(crfPageItem.getProCtcQuestion()))) {
					removeCrfItem(crfPageItem);
				}
			}
		}

		updateOrderNumber(crfPageItem);
		crfPageItem.setCrfPage(this);
		crfPageItems.add(crfPageItem);

	}

	/**
	 * used for adding a new crf items with empty properties..this is required to add questions from left side of the create form
	 *
	 * @param proCtcQuestion
	 */
	public CrfPageItem removeExistingAndAddNewCrfItem(final ProCtcQuestion proCtcQuestion) {
		CrfPageItem crfPageItem = new CrfPageItem();
		crfPageItem.setProCtcQuestion(proCtcQuestion);

		//check if it already exists
		CrfPageItem existingCrfPageItem = getCrfPageItemByQuestion(crfPageItem.getProCtcQuestion());
		if (existingCrfPageItem != null) {
			//we are updating order only  and removing properties
			existingCrfPageItem.setDisplayOrder(getCrfItemsSortedByDislayOrder().size());
			existingCrfPageItem.setCrfItemAllignment(null);
			existingCrfPageItem.setInstructions(null);
			existingCrfPageItem.setResponseRequired(Boolean.FALSE);
			return existingCrfPageItem;
		}

		updateOrderNumber(crfPageItem);
		crfPageItem.setCrfPage(this);
		crfPageItems.add(crfPageItem);

		return crfPageItem;


	}

	/**
	 * used while reordering the crf page item between crf pages
	 *
	 * @param proCtcQuestion
	 */
	public CrfPageItem removeExistingButDoNotAddNewCrfItem(final ProCtcQuestion proCtcQuestion) {
		CrfPageItem existingCrfPageItem = getCrfPageItemByQuestion(proCtcQuestion);
		removeCrfItem(existingCrfPageItem);
		return existingCrfPageItem;

	}

	private void updateOrderNumber(final CrfPageItem crfPageItem) {
		if (crfPageItem.getDisplayOrder() == null || crfPageItem.getDisplayOrder() == 0) {
			crfPageItem.setDisplayOrder(getCrfPageItems().size() + 1);

		}
	}

	public CrfPageItem getCrfPageItemByQuestion(final ProCtcQuestion proCtcQuestion) {
		if (proCtcQuestion != null) {
			for (CrfPageItem existingCrfPageItem : getCrfPageItems()) {
				if (existingCrfPageItem.getProCtcQuestion() != null
					&& (existingCrfPageItem.getProCtcQuestion().equals(proCtcQuestion))) {
					return existingCrfPageItem;
				}
			}

		}
		return null;

	}

	public void addCrfItem(CrfPageItem crfPageItem) {
		if (crfPageItem != null) {
			crfPageItem.setCrfPage(this);
			crfPageItems.add(crfPageItem);
		}
	}


	public CRFPage getCopy() {

		CRFPage copiedCrf = new CRFPage();
		copiedCrf.setDescription(description);
		for (CrfPageItem crfPageItem : crfPageItems) {
			copiedCrf.addCrfItem(crfPageItem.getCopy());
		}

		return copiedCrf;
	}


	public List<CrfPageItem> removeExistingAndAddNewCrfItem(final ProCtcTerm proCtcTerm) {
		List<ProCtcQuestion> questions = new ArrayList<ProCtcQuestion>(proCtcTerm.getProCtcQuestions());
		List<CrfPageItem> addedCrfPageItems = new ArrayList<CrfPageItem>();
		for (int i = 0; i < questions.size(); i++) {
			ProCtcQuestion proCtcQuestion = questions.get(i);
			CrfPageItem crfPageItem = removeExistingAndAddNewCrfItem(proCtcQuestion);
			addedCrfPageItems.add(crfPageItem);
		}
		return addedCrfPageItems;
	}

	public void removeExtraCrfItemsInCrfPage(final List<Integer> questionsToKeep) {
		Set<Integer> questionIdSet = new HashSet<Integer>(questionsToKeep);
		List<CrfPageItem> crfPageItemsToRemove = new ArrayList<CrfPageItem>();

		for (CrfPageItem crfPageItem : getCrfPageItems()) {
			if (!questionIdSet.contains(crfPageItem.getProCtcQuestion().getId())) {
				crfPageItemsToRemove.add(crfPageItem);
			}
		}


		for (CrfPageItem crfPageItem : crfPageItemsToRemove) {
			removeCrfItem(crfPageItem);
		}

	}

	public void updateDisplayOrderOfCrfPageItems() {
		List<CrfPageItem> itemsSortedByDislayOrder = getCrfItemsSortedByDislayOrder();
		for (int i = 0; i < itemsSortedByDislayOrder.size(); i++) {
			CrfPageItem crfPageItem = itemsSortedByDislayOrder.get(i);
			crfPageItem.setDisplayOrder(i + CrfPageItem.INITIAL_ORDER);
		}

	}
}