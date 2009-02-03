package gov.nih.nci.ctcae.core.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.*;

//
/**
 * The Class CRFPage.
 *
 * @author Vinay Kumar
 * @created Dec 29, 2008
 */

@Entity
@Table(name = "CRF_PAGES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_pages_id")})

public class CRFPage extends BaseVersionable {

    /**
     * The Constant logger.
     */
    private static final Log logger = LogFactory.getLog(CRFPage.class);

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    /**
     * The description.
     */
    @Column(name = "description")
    private String description;

    /**
     * The instructions.
     */
    @Column(name = "instructions")
    private String instructions;

    /**
     * The page number.
     */
    @Column(name = "page_number", nullable = false)
    private Integer pageNumber = 0;

    /**
     * The crf page items.
     */
    @OneToMany(mappedBy = "crfPage", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<CrfPageItem> crfPageItems = new ArrayList<CrfPageItem>();


    /**
     * The crf.
     */
    @JoinColumn(name = "crf_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private CRF crf;


    /**
     * Instantiates a new cRF page.
     */
    public CRFPage() {
        super();
    }


    /**
     * Gets the crf.
     *
     * @return the crf
     */
    public CRF getCrf() {
        return crf;
    }

    /**
     * Sets the crf.
     *
     * @param crf the new crf
     */
    public void setCrf(final CRF crf) {
        this.crf = crf;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Persistable#getId()
     */
    public Integer getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Persistable#setId(java.lang.Integer)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the page number.
     *
     * @return the page number
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the page number.
     *
     * @param pageNumber the new page number
     */
    public void setPageNumber(final Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Gets the crf items sorted by dislay order.
     *
     * @return the crf items sorted by dislay order
     */
    public List<CrfPageItem> getCrfItemsSortedByDislayOrder() {

        List<CrfPageItem> sortedCrfPageItems = new ArrayList<CrfPageItem>(crfPageItems);

        Collections.sort(sortedCrfPageItems, new DisplayOrderComparator());
        return sortedCrfPageItems;
    }

    /**
     * Gets the crf page items.
     *
     * @return the crf page items
     */
    public List<CrfPageItem> getCrfPageItems() {
        return crfPageItems;
    }


    /**
     * Removes the crf page item.
     *
     * @param crfPageItem the crf page item
     */
    public void removeCrfPageItem(CrfPageItem crfPageItem) {
        if (crfPageItem != null) {
            crfPageItems.remove(crfPageItem);
            updateDisplayOrderOfCrfPageItems(CrfPageItem.INITIAL_ORDER);
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
     * this is required to update the crf item properties on right side of the create form.
     *
     * @param proCtcQuestion the pro ctc question
     * @param displayOrder   the display order
     */
    public void addOrUpdateCrfItem(final ProCtcQuestion proCtcQuestion, final Integer displayOrder) {

        //check if it already exists
        for (CrfPageItem existingCrfPageItem : getCrfItemsSortedByDislayOrder()) {
            if (existingCrfPageItem.getProCtcQuestion() != null && (existingCrfPageItem.getProCtcQuestion().equals(proCtcQuestion))) {
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
     * this is required to move crf page item from one crf page to another crf page.
     *
     * @param crfPageItem the crf page item
     */
    public void addOrUpdateCrfItem(final CrfPageItem crfPageItem) {
        if (crfPageItem != null) {
            //check if it already exists..if yes remove it
            for (CrfPageItem existingCrfPageItem : getCrfItemsSortedByDislayOrder()) {
                if (existingCrfPageItem.getProCtcQuestion() != null
                        && (existingCrfPageItem.getProCtcQuestion().equals(crfPageItem.getProCtcQuestion()))) {
                    removeCrfPageItem(crfPageItem);
                }
            }
        }

        updateOrderNumber(crfPageItem);
        crfPageItem.setCrfPage(this);
        crfPageItems.add(crfPageItem);
        updateDisplayOrderOfCrfPageItems(CrfPageItem.INITIAL_ORDER);

    }

    /**
     * used for adding a new crf items with empty properties..this is required to add questions from left side of the create form
     *
     * @param proCtcQuestion the pro ctc question
     * @return the crf page item
     */
    public CrfPageItem removeExistingAndAddNewCrfItem(final ProCtcQuestion proCtcQuestion) {
        if (proCtcQuestion != null) {

            CrfPageItem crfPageItem = new CrfPageItem();
            crfPageItem.setProCtcQuestion(proCtcQuestion);

            //check if it already exists
            CrfPageItem existingCrfPageItem = getCrfPageItemByQuestion(crfPageItem.getProCtcQuestion());
            if (existingCrfPageItem != null) {
                //we are updating order only  and removing properties
                existingCrfPageItem.resetAllPropertiesToDefault();
                existingCrfPageItem.setDisplayOrder(getCrfItemsSortedByDislayOrder().size());

                return existingCrfPageItem;
            }

            updateOrderNumber(crfPageItem);
            crfPageItem.setCrfPage(this);
            crfPageItems.add(crfPageItem);

            return crfPageItem;
        }
        logger.error("can not add crf page item because question is null");
        return null;

    }

    /**
     * used while reordering the crf page item between crf pages  or removing crf page item mannualy.
     *
     * @param proCtcQuestion the pro ctc question
     * @return the crf page item
     */
    public CrfPageItem removeExistingButDoNotAddNewCrfItem(final ProCtcQuestion proCtcQuestion) {
        CrfPageItem existingCrfPageItem = getCrfPageItemByQuestion(proCtcQuestion);
        removeCrfPageItem(existingCrfPageItem);
        return existingCrfPageItem;

    }

    /**
     * Update order number.
     *
     * @param crfPageItem the crf page item
     */
    private void updateOrderNumber(final CrfPageItem crfPageItem) {
        if (crfPageItem.getDisplayOrder() == null || crfPageItem.getDisplayOrder() == 0) {
            crfPageItem.setDisplayOrder(getCrfPageItems().size() + 1);

        }

    }

    /**
     * Gets the crf page item by question.
     *
     * @param questionId the question id
     * @return the crf page item by question
     */
    public CrfPageItem getCrfPageItemByQuestion(final Integer questionId) {
        if (questionId != null) {
            for (CrfPageItem existingCrfPageItem : getCrfPageItems()) {
                if (existingCrfPageItem.getProCtcQuestion() != null
                        && (existingCrfPageItem.getProCtcQuestion().getId().equals(questionId))) {
                    return existingCrfPageItem;
                }
            }

        }
        return null;

    }

    /**
     * Gets the crf page item by question.
     *
     * @param proCtcQuestion the pro ctc question
     * @return the crf page item by question
     */
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

    /**
     * Adds the crf item.
     *
     * @param crfPageItem the crf page item
     */
    public void addCrfItem(CrfPageItem crfPageItem) {
        if (crfPageItem != null) {
            crfPageItem.setCrfPage(this);
            crfPageItems.add(crfPageItem);
        }
    }


    /**
     * Gets the copy.
     *
     * @return the copy
     */
    public CRFPage getCopy() {

        CRFPage copiedCrfPage = new CRFPage();
        copiedCrfPage.setDescription(description);
        for (CrfPageItem crfPageItem : crfPageItems) {
            copiedCrfPage.addCrfItem(crfPageItem.getCopy());
        }

        return copiedCrfPage;
    }


    /**
     * Removes the existing and add new crf item.
     *
     * @param proCtcTerm the pro ctc term
     * @return the list< crf page item>
     */
    public List<CrfPageItem> removeExistingAndAddNewCrfItem(final ProCtcTerm proCtcTerm) {
        if (proCtcTerm != null) {
            List<ProCtcQuestion> questions = new ArrayList<ProCtcQuestion>(proCtcTerm.getProCtcQuestions());
            List<CrfPageItem> addedCrfPageItems = new ArrayList<CrfPageItem>();
            for (int i = 0; i < questions.size(); i++) {
                ProCtcQuestion proCtcQuestion = questions.get(i);
                CrfPageItem crfPageItem = removeExistingAndAddNewCrfItem(proCtcQuestion);
                addedCrfPageItems.add(crfPageItem);
            }
            return addedCrfPageItems;
        }
        logger.error("can not add proCtcTerm because pro ctc term is null");

        return null;
    }

    /**
     * Adds the pro ctc term.
     *
     * @param proCtcTerm the pro ctc term
     * @return the list< crf page item>
     */
    public List<CrfPageItem> addProCtcTerm(final ProCtcTerm proCtcTerm) {
        if (proCtcTerm != null) {
            List<ProCtcQuestion> questions = new ArrayList<ProCtcQuestion>(proCtcTerm.getProCtcQuestions());
            List<CrfPageItem> addedCrfPageItems = new ArrayList<CrfPageItem>();
            for (int i = 0; i < questions.size(); i++) {
                ProCtcQuestion proCtcQuestion = questions.get(i);
                CrfPageItem crfPageItem = addProCtcQuestion(proCtcQuestion);
                if (crfPageItem != null) {
                    addedCrfPageItems.add(crfPageItem);
                }
            }
            updateDisplayOrderOfCrfPageItems(CrfPageItem.INITIAL_ORDER);
            return addedCrfPageItems;
        }
        logger.error("can not add proCtcTerm because pro ctc term is null");

        return null;
    }

    /**
     * Update instructions.
     */
    public void updateInstructions() {
        if (!getCrfPageItems().isEmpty()) {
            ProCtcTerm proCtcTerm = getCrfPageItems().get(0).getProCtcQuestion().getProCtcTerm();
            setInstructions(String.format("Please think back %s:", getCrf().getRecallPeriod(), proCtcTerm.getTerm()));
        }
    }

    /**
     * Adds the pro ctc question.
     *
     * @param proCtcQuestion the pro ctc question
     * @return the crf page item
     */
    public CrfPageItem addProCtcQuestion(final ProCtcQuestion proCtcQuestion) {
        if (proCtcQuestion != null) {


            //check if it already exists
            CrfPageItem existingCrfPageItem = getCrfPageItemByQuestion(proCtcQuestion);

            if (existingCrfPageItem != null) {
                return null;
            }

            CrfPageItem crfPageItem = new CrfPageItem(proCtcQuestion);

            updateOrderNumber(crfPageItem);
            crfPageItem.setCrfPage(this);
            crfPageItems.add(crfPageItem);

            return crfPageItem;
        }
        logger.error("can not add crf page item because question is null");
        return null;

    }

    /**
     * Removes the extra crf items in crf page.
     *
     * @param questionsToKeep the questions to keep
     */
    public void removeExtraCrfItemsInCrfPage(final List<Integer> questionsToKeep) {
        Set<Integer> questionIdSet = new HashSet<Integer>(questionsToKeep);
        List<CrfPageItem> crfPageItemsToRemove = new ArrayList<CrfPageItem>();

        for (CrfPageItem crfPageItem : getCrfPageItems()) {
            if (!questionIdSet.contains(crfPageItem.getProCtcQuestion().getId())) {
                crfPageItemsToRemove.add(crfPageItem);
            }
        }


        for (CrfPageItem crfPageItem : crfPageItemsToRemove) {
            removeCrfPageItem(crfPageItem);
        }

    }

    /**
     * Update display order of crf page items.
     *
     * @param initialOrder the initial order
     */
    public void updateDisplayOrderOfCrfPageItems(Integer initialOrder) {
        List<CrfPageItem> itemsSortedByDislayOrder = getCrfItemsSortedByDislayOrder();
        for (int i = 0; i < itemsSortedByDislayOrder.size(); i++) {
            CrfPageItem crfPageItem = itemsSortedByDislayOrder.get(i);
            crfPageItem.setDisplayOrder(i + initialOrder);
        }

    }

    /**
     * Check if pro ctc term exists.
     *
     * @param proCtcTerm the pro ctc term
     * @return true, if successful
     */
    public boolean checkIfProCtcTermExists(ProCtcTerm proCtcTerm) {
        for (ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()) {
            CrfPageItem crfPageItem = getCrfPageItemByQuestion(proCtcQuestion);
            if (crfPageItem != null) {
                return true;
            }
        }
        return false;

    }

    /**
     * Gets the instructions.
     *
     * @return the instructions
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the instructions.
     *
     * @param instructions the new instructions
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}